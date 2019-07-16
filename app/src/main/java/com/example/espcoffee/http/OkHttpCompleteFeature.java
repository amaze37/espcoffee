package com.example.espcoffee.http;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@RequiresApi(api = Build.VERSION_CODES.N)
public class OkHttpCompleteFeature implements Callback {
    private final CompletableFuture<Response> future = new CompletableFuture<>();

    public void onFailure(Call call, IOException e) {
        future.completeExceptionally(e);
    }

    public void onResponse(Call call, Response response) throws IOException {
        future.complete(response);
    }

    public CompletableFuture<Response> getFuture() {
        return future;
    }
}
