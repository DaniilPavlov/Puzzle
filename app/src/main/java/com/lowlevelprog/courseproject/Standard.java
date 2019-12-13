package com.lowlevelprog.courseproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Standard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        Button s1 = findViewById(R.id.but_s1);
        s1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toS1;
                        toS1 = new Intent(Standard.this, Puzzle.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        toS1.putExtra("strName", "1");
                        startActivity(toS1);
                    }
                }
        );
        Button s2 = findViewById(R.id.but_s2);
        s2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toS2;
                        toS2 = new Intent(Standard.this, Puzzle.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        toS2.putExtra("strName", "2");
                        startActivity(toS2);
                    }
                }
        );
        Button s3 = findViewById(R.id.but_s3);
        s3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toS3;
                        toS3 = new Intent(Standard.this, Puzzle.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        toS3.putExtra("strName", "3");
                        startActivity(toS3);
                    }
                }
        );
    }
}