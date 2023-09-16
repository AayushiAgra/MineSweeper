package com.example.minesweeper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class ResultActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        TextView textView = (TextView) findViewById(R.id.seconds);
        textView.setText("Used " + time + " seconds.");
        textView.setTextColor(Color.GRAY);
        String result = intent.getStringExtra("result");
        textView = (TextView) findViewById(R.id.result);


        textView.setText("You " + result + ".");
        textView.setTextColor(Color.GRAY);
        TextView msgt = (TextView) findViewById(R.id.message);
        String msg = "";
        if (result.equals("won")){
            msg = "Good Job!";
        }
        else{
            msg = "Try Again!";
        }


        msgt.setText(msg);
        msgt.setTextColor(Color.GRAY);

    }

    public void backToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
