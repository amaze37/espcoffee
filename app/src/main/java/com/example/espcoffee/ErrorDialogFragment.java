package com.example.espcoffee;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ErrorDialogFragment extends DialogFragment {
    Activity showingActivity;

    public ErrorDialogFragment(Activity showingActivity) {
        this.showingActivity = showingActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String message = "unknown exception";
        if (savedInstanceState != null && savedInstanceState.containsKey("message")) {
            message = savedInstanceState.getString("message");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(showingActivity);
        builder.setMessage(message)
                .setPositiveButton("ok", (dialog, id) -> dialog.dismiss());
        return builder.create();
    }
}
