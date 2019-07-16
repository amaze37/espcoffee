package com.example.espcoffee;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.espcoffee.http.OkHttpCompleteFeature;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void Socket1Power(View view) {
        String request = "";
        try {
            request = sendGetRequest("http://192.168.1.150:80/socket1power");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Socket2Power(View view) {
        String request = "";
        try {
            request = sendGetRequest("http://192.168.1.150:80/socket2power");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Socket3Power(View view) {
        String request = "";
        try {
            request = sendGetRequest("http://192.168.1.150:80/socket3power");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Socket4Power(View view) {
        String request = "";
        try {
            request = sendGetRequest("http://192.168.1.150:80/socket4power");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Socket5Power(View view) {
        String request = "";
        try {
            request = sendGetRequest("http://192.168.1.150:80/socket5power");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Socket6Power(View view) {
        String request = "";
        try {
            request = sendGetRequest("http://192.168.1.150:80/socket6power");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///Для остальных кнопок аналогично

    private String sendGetRequest(String url) throws IOException {
        GetRequestHelper requestSender = new GetRequestHelper(url);
        requestSender.execute();
        try {
            requestSender.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return requestSender.getCompletableHttpBody();
    }

    private class GetRequestHelper extends AsyncTask {
        private final OkHttpClient client = new OkHttpClient();
        private String url;
        private TextView errorView = findViewById(R.id.errorView);
        private String responseBody;

        GetRequestHelper(String url) {
            this.url = url;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Object doInBackground(Object[] objects) {
            errorView.setVisibility(View.INVISIBLE);
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
                e.printStackTrace();
            }
            return new Object();
        }

        private void showErrorMessage(String message) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setText(message);
            errorView.invalidate();
        }

        String getCompletableHttpBody() {
            return responseBody;
        }
    }
}

