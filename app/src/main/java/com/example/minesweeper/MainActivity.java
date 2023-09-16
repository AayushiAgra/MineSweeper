package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;


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
    private HashSet<Integer> visited = new HashSet<>();
    boolean currmode;
    boolean haslost;
    String result;
    private int seconds = 0;
    private boolean running = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currmode = true;
        haslost = false;
        result = "";
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
                newtv.setTextSize( 16 );
                newtv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                newtv.setTextColor(Color.GREEN);
                newtv.setBackgroundColor(Color.GREEN);
                newtv.setOnClickListener(this::onClickGrid);
                newtv.setText(Integer.toString(i*10 + j + 5));
                androidx.gridlayout.widget.GridLayout.LayoutParams lp = new androidx.gridlayout.widget.GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(1), dpToPixel(1), dpToPixel(1), dpToPixel(1));
                lp.rowSpec = androidx.gridlayout.widget.GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                minegrid.addView(newtv, lp);

                cell_tvs.get(i).add(newtv);
            }
        }
        runTimer();
    }

    private void onClickGrid(View view){
        if (haslost){
            sendResult();
        }
        if (visited.size() == 116){
            result = "won";
            sendResult();

        }
        TextView tv = (TextView) view;
        if (tv.getText().equals(getResources().getString(R.string.mine)) || tv.getText().equals("")){
            return;
        }
        if (tv.getText().equals(getResources().getString(R.string.flag))){
            if (currmode){
                return;
            }
            else{
                for (int i = 0; i < 12; i++){
                    for (int j = 0; j < 10; j++){
                        if (cell_tvs.get(i).get(j).equals(tv)){
                            TextView x = (TextView)findViewById(R.id.textViewFlagNum);
                            x.setText(Integer.toString(Integer.valueOf((String)x.getText()) + 1));
                            cell_tvs.get(i).get(j).setText(Integer.toString(i*10 + j + 5));
                        }
                    }
                }
                return;
            }
        }
        int curr = Integer.valueOf((String)tv.getText()) - 5;
        if (curr < 0){
            return;
        }
        if (!currmode){
            tv.setText(getResources().getString(R.string.flag));
            TextView x = (TextView)findViewById(R.id.textViewFlagNum);
            x.setText(Integer.toString(Integer.valueOf((String)x.getText()) - 1));
            return;
        }
        if (items.get(curr/10).get(curr%10) == 'b'){
            for (int i = 0; i < 12; i++){
                for (int j = 0; j < 10; j++){
                    if (items.get(i).get(j) == 'b'){
                        TextView bomb = cell_tvs.get(i).get(j);
                        bomb.setTextColor(Color.BLACK);
                        bomb.setBackgroundColor(Color.LTGRAY);
                        bomb.setText(getResources().getString(R.string.mine));

                    }
                }
            }
            haslost = true;
            running = false;
            result = "lost";
            return;
        }
        Queue<Integer> bfsq = new LinkedList<>();
        bfsq.add(curr);
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1},{1, -1}, {1, 0}, {1, 1}};
        while (!bfsq.isEmpty()){
            int x = bfsq.poll();
            int i = x/10;
            int j = x % 10;
            if (visited.contains(x)){
                continue; //already found
            }
            visited.add(x);
            TextView bomb = cell_tvs.get(i).get(j);
            if (bomb.getText().equals(getResources().getString(R.string.flag))){
                continue;
            }
            if (items.get(i).get(j) == '0'){
                bomb = cell_tvs.get(i).get(j);
                bomb.setBackgroundColor(Color.LTGRAY);
                bomb.setText("");
                for (int k = 0; k < 8; k++){
                    if (i + directions[k][0] >= 0 && i + directions[k][0] < 12){
                        if (j + directions[k][1] >= 0 && j + directions[k][1] < 10){
                            int toadd = (i + directions[k][0]) * 10 + j + directions[k][1];
                            bfsq.add(toadd);
                        }
                    }
                }
            }
            else if (items.get(i).get(j) == 'b'){
                continue;
            }
            else{
                bomb = cell_tvs.get(i).get(j);
                bomb.setBackgroundColor(Color.LTGRAY);
                bomb.setTextColor(Color.BLACK);
                bomb.setText(Character.toString(items.get(i).get(j)));
            }
            if (visited.size() == 116){
                running = false;
                for (int a = 0; a < 12; a++){
                    for (int b = 0; b < 10; b++){
                        if (items.get(a).get(b) == 'b'){
                            bomb = cell_tvs.get(a).get(b);
                            bomb.setTextColor(Color.BLACK);
                            bomb.setBackgroundColor(Color.LTGRAY);
                            bomb.setText(getResources().getString(R.string.mine));

                        }
                    }
                }
            }
        }
    }
    public void switchMode(View view){
        TextView tv = (TextView) view;
        if (currmode) {
            tv.setText(getResources().getString(R.string.flag));
            currmode = false;
        }
        else {
            tv.setText(getResources().getString(R.string.pick));
            currmode = true;
        }
    }
    private void runTimer() {
        final TextView timeView = (TextView)findViewById(R.id.textViewClockTime);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                timeView.setText(String.valueOf(seconds));
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }

        });
    }

    public void sendResult() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("time", String.valueOf(seconds));
        intent.putExtra("result", result);

        startActivity(intent);
    }
}