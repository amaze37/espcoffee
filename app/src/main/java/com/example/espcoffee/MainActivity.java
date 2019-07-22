package com.example.espcoffee;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.espcoffee.http.OkHttpCompleteFeature;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Bundle mainActivityBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        mainActivityBundle = new Bundle();
        setContentView(R.layout.activity_main);
        /*      для запуска анимации
        final ImageView animImageView = (ImageView) findViewById(R.id.imageView3); id элемента
        animImageView.setBackgroundResource(R.drawable.coffee_bean);
        animImageView.post(((AnimationDrawable) animImageView.getBackground())::start);
*/
    }

    @Deprecated
    public void socket1Power(View view) {
        sendGetRequest("http://192.168.1.150:80/socket1power");
    }

    public void socket2Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.cappuccino, R.string.infoCoffee, R.string.cappucino, "http://192.168.1.150:80/socket2power");
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public void socket3Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.espresso, R.string.infoCoffee, R.string.espresso, "http://192.168.1.150:80/socket3power");
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public void socket4Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.espresso2x, R.string.infoCoffee, R.string.espresso2x, "http://192.168.1.150:80/socket4power");
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public void socket5Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.americano, R.string.infoCoffee, R.string.americano, "http://192.168.1.150:80/socket5power");
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public void socket6Power(View view) {
        Intent intent = new Intent(MainActivity.this, BeverageActivity.class);
        createBundle(R.drawable.americano2x, R.string.infoCoffee, R.string.americano2x, "http://192.168.1.150:80/socket6power");
        intent.putExtra(Intent.EXTRA_RESTRICTIONS_BUNDLE, mainActivityBundle);
        startActivity(intent);
    }

    public String sendGetRequest(String url) {
        RequestHelper requestHelper = new RequestHelper(url);
        requestHelper.execute();
        return requestHelper.getCompletableHttpBody();
    }

    private void createBundle(int coffeeImageId, int textId, int titleId, String url) {
        mainActivityBundle.putInt("coffeeImageId", coffeeImageId);
        mainActivityBundle.putInt("textId", textId);
        mainActivityBundle.putInt("titleId", titleId);
        mainActivityBundle.putString("url", url);
    }

    private void showErrorMessage(String message) {
        runOnUiThread(() -> {
            ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment(this);
            mainActivityBundle.putString("message", message);
            errorDialogFragment.onCreateDialog(mainActivityBundle).show();
            errorDialogFragment.onDestroy();
        });
    }

    private class RequestHelper extends AsyncTask<Void, Void, Void> {
        private final OkHttpClient client = new OkHttpClient();
        private String url;
        private String responseBody;

        RequestHelper(String url) {
            this.url = url;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            if (!OkHttpCompleteFeature.isComplete.get()) {
                return null;
            }
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            OkHttpCompleteFeature callback = new OkHttpCompleteFeature();
            client.newCall(request).enqueue(callback);
            CompletableFuture<Response> future = callback.getFuture().thenApply(response -> response);
            try {
                responseBody = future.get().body().string();
            } catch (ExecutionException e) {
                showErrorMessage(String.format("Error: fail request %s", e.getMessage()));
            } catch (InterruptedException e) {
                showErrorMessage(String.format("ERROR: forbidden request %s", e.getMessage()));
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            } finally {
                OkHttpCompleteFeature.isComplete.set(true);
            }
            return null;
        }
        String getCompletableHttpBody() {
            return responseBody;
        }
    }
}

