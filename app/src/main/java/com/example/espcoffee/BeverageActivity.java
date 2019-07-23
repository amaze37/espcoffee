package com.example.espcoffee;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.espcoffee.tools.CustomProgressBar;

public class BeverageActivity extends MainActivity {
    private String url;

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
        CustomProgressBar progress = findViewById(R.id.progressBar);
        progress.setTextView(findViewById(R.id.percent));
        ObjectAnimator anim = ObjectAnimator.ofInt(progress, "progress", 0, 100);
        anim.setDuration(10000);
        anim.start();
    }


    public void startDispensing(View view) {
        sendGetRequest(url);
    }

    public void onBackPressed(View view) { super.onBackPressed(); }
}
