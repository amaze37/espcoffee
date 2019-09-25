package com.example.espcoffee;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.espcoffee.tools.CustomProgressBar;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.espcoffee.http.NetworkConstants.SERVER_IP;
import static com.example.espcoffee.http.NetworkConstants.SERVER_PORT;

public class BeverageActivity extends MainActivity {
    private static final Logger LOGGER = Logger.getLogger(BeverageActivity.class.getName());
    private String url;
    private Integer percent;
    private Boolean isStarted = false;
    private ServiceStatusChecker serviceStatusChecker;

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
        CustomProgressBar progress = findViewById(R.id.progressBar);
        ObjectAnimator anim = ObjectAnimator.ofInt(progress, "progress", 0, 100);
        anim.setDuration(percent);
        serviceStatusChecker = new ServiceStatusChecker();

        if (Boolean.FALSE.equals(isStarted)) {
            serviceStatusChecker.execute();
        } else {
            serviceStatusChecker.cancel(false);
            isStarted = false;
        }
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
        //serviceStatusChecker.cancel(false);
    }

    private void showConnectionError() {
        ViewGroup viewGroup = findViewById(R.id.mainContentLayout);
        Crouton.makeText(this, getResources().getText(R.string.error_no_connection_coffee), Style.ALERT, viewGroup)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }

    private class ServiceStatusChecker extends AsyncTask<Void, Void, Boolean> {
        private SocketAddress socketAddress;
        private ObjectAnimator anim;

        @Override
        protected void onPreExecute() {
            socketAddress = new InetSocketAddress(SERVER_IP, SERVER_PORT);
            anim = ObjectAnimator.ofInt(findViewById(R.id.progressBar), "progress", 0, 100);
            anim.setDuration(percent);

            findViewById(R.id.beverageCoffeeImage).setVisibility(View.INVISIBLE);
            findViewById(R.id.percent).setVisibility(View.VISIBLE);

            Button beverage = findViewById(R.id.beverageStart);
            beverage.setText(R.string.stop);
            beverage.setBackgroundColor(getResources().getColor(R.color.Red));

            CustomProgressBar customProgressBar = findViewById(R.id.progressBar);
            customProgressBar.setVisibility(View.VISIBLE);
            customProgressBar.setTextView(findViewById(R.id.percent));
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try (Socket socket = new Socket()) {
                socket.connect(socketAddress, 5000);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccessConnect) {
            if (Boolean.TRUE.equals(isSuccessConnect)) {
                sendGetRequest(url);
                anim.start();
                isStarted = true;
            } else {
                showConnectionError();
                updateUI();
            }

        }

        @Override
        protected void onCancelled() {
            updateUI();
            isStarted = false;
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            sendGetRequest(url);
            updateUI();
            if (aBoolean != null) {
                isStarted = aBoolean;
            } else {
                isStarted = false;
            }
        }

        private void updateUI() {
            if (anim != null && Boolean.TRUE.equals(anim.isStarted())) {
                anim.cancel();
            }
            Button beverage = findViewById(R.id.beverageStart);
            beverage.setText(R.string.start);
            beverage.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            findViewById(R.id.beverageCoffeeImage).setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            findViewById(R.id.percent).setVisibility(View.INVISIBLE);
        }
    }
}
