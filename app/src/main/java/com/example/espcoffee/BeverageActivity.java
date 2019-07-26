package com.example.espcoffee;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.espcoffee.tools.CustomProgressBar;

public class BeverageActivity extends MainActivity {
    private String url;
    private Integer percent;
    private Boolean isStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beverage_dispensing);
        Bundle bundle = getIntent().getBundleExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE);
        if (bundle.containsKey("nameId")) {
            int titleId = bundle.getInt("nameId");
            TextView textView = findViewById(R.id.coffeeName);
            textView.setText(titleId);
        }
        if (bundle.containsKey("coffeeImageId")) {
            int coffeeImage = bundle.getInt("coffeeImageId");
            ImageView coffeeImageView = findViewById(R.id.beverageCoffeeImage);
            coffeeImageView.setImageResource(coffeeImage);

        }
        if (bundle.containsKey("url")) {
            url = bundle.getString("url");
        }
        if (bundle.containsKey("textId")) {
            int textId = bundle.getInt("textId");
            TextView textView = findViewById(R.id.beverageCoffeeText);
            textView.setText(textId);
        }
        if (bundle.containsKey("percent")) {
            percent = bundle.getInt("percent");
        }


    }

    public void startDispensing(View view) {
        ImageView coffeeImageView = findViewById(R.id.beverageCoffeeImage);
        TextView percent1 = findViewById(R.id.percent);
        CustomProgressBar progress = findViewById(R.id.progressBar);
        Button beverageStart = findViewById(R.id.beverageStart);
        ObjectAnimator anim = ObjectAnimator.ofInt(progress, "progress", 0, 100);
        anim.setDuration(percent);

        if (!isStarted) {
            sendGetRequest(url);
            coffeeImageView.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
            percent1.setVisibility(View.VISIBLE);
            progress.setTextView(findViewById(R.id.percent));
            anim.start();
            beverageStart.setText(R.string.stop);
            beverageStart.setBackgroundColor(getResources().getColor(R.color.Red));
            isStarted = true;
        } else {
            sendGetRequest(url);
            coffeeImageView.setVisibility(View.VISIBLE);
            progress.setVisibility(View.INVISIBLE);
            percent1.setVisibility(View.INVISIBLE);
            progress.setTextView(findViewById(R.id.percent));
            anim.cancel();
            beverageStart.setText(R.string.start);
            beverageStart.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            isStarted = false;
        }
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
    }
}
