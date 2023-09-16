package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;


import android.os.Bundle;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private ArrayList<ArrayList<TextView>> cell_tvs;
    private ArrayList<ArrayList<Character>> items;
    private HashSet<Integer> bombs = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = new ArrayList<>();
        while (bombs.size() != 4){
            bombs.add((int)(Math.random() * 120));
        }
        for (int i = 0; i < 12; i++){
            ArrayList<Character> newr = new ArrayList<>();
            items.add(newr);
            for (int j = 0; j < 10; j++){
                if (bombs.contains(i*10 + j)){
                    items.get(i).add('b'); //bomb
                }
                else{
                    items.get(i).add('0'); //empty
                }
            }
        }
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1},{1, -1}, {1, 0}, {1, 1}};
        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 10; j++){
                if (items.get(i).get(j) != 'b'){
                    for (int k = 0; k < 8; k++){
                        if (i + directions[k][0] >= 0 && i + directions[k][0] < 12){
                            if (j + directions[k][1] >= 0 && j + directions[k][1] < 10){
                                if (items.get(i + directions[k][0]).get(j + directions[k][1]) == 'b'){
                                    items.get(i).set(j, (char)(items.get(i).get(j) + 1));
                                }
                            }
                        }
                    }
                }
            }
        }
        cell_tvs = new ArrayList<>();
        androidx.gridlayout.widget.GridLayout minegrid = (androidx.gridlayout.widget.GridLayout) findViewById(R.id.minegrid);
        for (int i = 0; i < 12; i++){
            ArrayList<TextView> newr = new ArrayList<>();
            cell_tvs.add(newr);
            for (int j = 0; j < 10; j++){
                TextView newtv = new TextView(this);
                newtv.setHeight( dpToPixel(32) );
                newtv.setWidth( dpToPixel(32) );
                newtv.setTextSize( dpToPixel(32) );
                newtv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                newtv.setTextColor(Color.GREEN);
                newtv.setBackgroundColor(Color.GREEN);
                //newtv.setOnClickListener(this::onClickGrid);
                newtv.setText("");
                androidx.gridlayout.widget.GridLayout.LayoutParams lp = new androidx.gridlayout.widget.GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(1), dpToPixel(1), dpToPixel(1), dpToPixel(1));
                lp.rowSpec = androidx.gridlayout.widget.GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                minegrid.addView(newtv, lp);

                cell_tvs.get(i).add(newtv);
            }
        }
    }
}