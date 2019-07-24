package com.example.espcoffee.http;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.espcoffee.ErrorDialogFragment;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestHelper extends AsyncTask<Integer, String, Integer> {
    private final String LOG_TAG = "AsyncRequest";
    private Activity callingActivity;
    private Bundle bundle;
    private final OkHttpClient client;
    private String url;
    private String responseBody;

    public RequestHelper(Activity activity, String url) {
        client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
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
        } catch (InterruptedException e) {
            publishProgress(String.format("ERROR: forbidden request %s", e.getMessage()));
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        } finally {
            OkHttpCompleteFeature.isComplete.set(true);
        }
        return 0;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (values.length > 0) {
            showErrorMessage(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Integer values) {
        super.onPostExecute(values);
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