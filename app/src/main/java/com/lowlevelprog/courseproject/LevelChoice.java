package com.lowlevelprog.courseproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LevelChoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelchoice);
        Button standard = findViewById(R.id.but_standard);
        standard.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toStart;
                        toStart = new Intent(LevelChoice.this, Standard.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(toStart);
                    }
                }
        );
        Button hard = findViewById(R.id.but_hard);
        hard.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toHard;
                        toHard = new Intent(LevelChoice.this, Hard.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(toHard);
                    }
                }
        );
    }
}
