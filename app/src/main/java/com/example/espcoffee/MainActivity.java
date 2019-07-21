package com.example.espcoffee;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void Socket1Power(View view) {
        sendGetRequest("http://192.168.1.150:80/socket1power");
    }

    public void Socket2Power(View view) {
        sendGetRequest("http://192.168.1.150:80/socket2power");
    }

    public void Socket3Power(View view) {
        sendGetRequest("http://192.168.1.150:80/socket3power");
    }

    public void Socket4Power(View view) {
        sendGetRequest("http://192.168.1.150:80/socket4power");
    }

    public void Socket5Power(View view) {
        sendGetRequest("http://192.168.1.150:80/socket5power");
    }

    public void Socket6Power(View view) {
        sendGetRequest("http://192.168.1.150:80/socket6power");
    }

    private String sendGetRequest(String url) {
        RequestHelper requestHelper = new RequestHelper(url);
        requestHelper.execute();
        return requestHelper.getCompletableHttpBody();
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

