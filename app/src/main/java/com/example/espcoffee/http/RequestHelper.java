package com.example.espcoffee.http;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.espcoffee.ErrorDialogFragment;
import com.example.espcoffee.R;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestHelper extends AsyncTask<Integer, String, Integer> {
    private final int SUCCESS = 0;
    private final int CONNECTION_ERROR = 1;
    private final int INTERRUPTED_ERROR = 2;
    private final int UNKNOWN_ERROR = 3;
    private final String LOG_TAG = "AsyncRequest";
    private final OkHttpClient client = new OkHttpClient();
    private Activity callingActivity;
    private Bundle bundle;
    private String url;
    private String responseBody;

    public RequestHelper(Activity activity, String url) {
        bundle = new Bundle();
        this.callingActivity = activity;
        this.url = url;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Integer doInBackground(Integer... values) {
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
            publishProgress(String.format("Error: fail request %s", e.getMessage()));
            return CONNECTION_ERROR;
        } catch (InterruptedException e) {
            publishProgress(String.format("ERROR: forbidden request %s", e.getMessage()));
            return INTERRUPTED_ERROR;
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            return UNKNOWN_ERROR;
        } finally {
            OkHttpCompleteFeature.isComplete.set(true);
        }
        return SUCCESS;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (values.length > 0) {
            showErrorMessage(values[0]);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onPostExecute(Integer values) {
        super.onPostExecute(values);
        switch (values) {
            case SUCCESS:
                if (!callingActivity.isFinishing()) {
                    callingActivity
                            .findViewById(R.id.connectionStatusImage)
                            .setBackgroundResource(R.drawable.wifi_white);
                    callingActivity
                            .findViewById(R.id.connectionStatusText)
                            .setBackgroundResource(R.string.nav_header_wifi_connected);
                }
                break;
            case CONNECTION_ERROR:
                if (!callingActivity.isFinishing()) {
                    callingActivity
                            .findViewById(R.id.connectionStatusImage)
                            .setBackgroundResource(R.drawable.no_wifi_white);
                    callingActivity
                            .findViewById(R.id.connectionStatusText)
                            .setBackgroundResource(R.string.nav_header_wifi_no_connect);
                }
                break;
        }
    }

    public String getCompletableHttpBody() {
        return responseBody;
    }

    private void showErrorMessage(String message) {
        if (!callingActivity.isFinishing()) {
            ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment(callingActivity);
            bundle.putString("message", message);
            errorDialogFragment.onCreateDialog(bundle).show();
            errorDialogFragment.onDestroy();
        }
    }
}