package com.lowlevelprog.courseproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.Random;

public class Puzzle extends AppCompatActivity {

    int a = 0;
    private int counter, firstClick, secondClick, rememberData;
    public static int width;
    boolean check = false;
    private static Random random = new Random();

    private int[] imageArray = {R.drawable.one, R.drawable.two,
            R.drawable.three, R.drawable.four,
            R.drawable.five, R.drawable.six,
            R.drawable.seven, R.drawable.eight, R.drawable.nine};
    private int[] newImageArray = {R.drawable.one, R.drawable.two,
            R.drawable.three, R.drawable.four,
            R.drawable.five, R.drawable.six,
            R.drawable.seven, R.drawable.eight, R.drawable.nine};

    public static void  swap(int[] array, int firstInd, int secondInd) {
        int temporary = array[firstInd];
        array[firstInd] = array[secondInd];
        array[secondInd] = temporary;
    }

    public static int[] shake(int[] array) {
        int counter = array.length - 1;
        while (counter > 0) {
            int index = random.nextInt(counter + 1);
            swap(array, index, counter);
            --counter;
        }
        return array;
    }

    int[] randomImageArray = shake(imageArray);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        DisplayMetrics dmetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dmetrics);
        width = dmetrics.widthPixels / 3;

        final GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new Adapter(this, randomImageArray));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                counter++;
                if (counter == 1) {
                    firstClick = position;
                    rememberData = randomImageArray[position];
                } else if (counter == 2) {
                    secondClick = position;
                    randomImageArray[firstClick] = randomImageArray[secondClick];
                    randomImageArray[secondClick] = rememberData;
                    gridView.invalidateViews();
                    counter = 0;
                }

                if (randomImageArray[0] == newImageArray[0] &&
                        randomImageArray[1] == newImageArray[1] &&
                        randomImageArray[2] == newImageArray[2] &&
                        randomImageArray[3] == newImageArray[3] &&
                        randomImageArray[4] == newImageArray[4] &&
                        randomImageArray[5] == newImageArray[5] &&
                        randomImageArray[6] == newImageArray[6] &&
                        randomImageArray[7] == newImageArray[7] &&
                        randomImageArray[8] == newImageArray[8]) {
                    check = true;
                }

                if (check) {

                }
            }
        });
    }

    public void change(View v) {
        v.setBackgroundResource(imageArray[a]);
        a++;
        a = a % 9;
    }
}
