package com.example.myapplication.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.HashSet;

public class DialogManager {

    private static HashSet<CharSequence> currentlyShowingMessages = new HashSet<>();

    @Nullable
    public static AlertDialog showAlertDialogBuilder(@NonNull AlertDialog.Builder alert)
    {
        try {
            return alert.show();
        }
        catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static AlertDialog showAlertDialog(@NonNull Context context,
                                              @Nullable CharSequence title,
                                              @NonNull CharSequence message)
    {
        return showAlertDialog(context, title, message, "ok",null,null,null);
    }


    @Nullable
    public static AlertDialog showAlertDialog(@NonNull Context context,
                                              @Nullable CharSequence title,
                                              @NonNull final CharSequence message,
                                              @Nullable String buttonTitle)
    {
        return showAlertDialog(context, title, message, buttonTitle, null, null, null);
    }

    @Nullable
    public static AlertDialog showAlertDialog(@NonNull Context context,
                                              @Nullable CharSequence title,
                                              @NonNull final CharSequence message,
                                              @Nullable String buttonTitle,
                                              @Nullable final DialogInterface.OnClickListener buttonListener){
        return showAlertDialog(context,title,message,buttonTitle,buttonListener,null,null);
    }

    @Nullable
    public static AlertDialog showAlertDialog(@NonNull Context context,
                                              @Nullable CharSequence title,
                                              @NonNull final CharSequence message,
                                              @Nullable String positiveButtonTitle,
                                              @Nullable final DialogInterface.OnClickListener positiveButtonListener,
                                              @Nullable String negativeButtonTitle,
                                              @Nullable final DialogInterface.OnClickListener negativeButtonListener)
    {
        if (context != null && message!=null && !currentlyShowingMessages.contains(message)){

            AlertDialog.Builder b =new AlertDialog.Builder(context);

            if(title!=null)
                b.setTitle(title);

            b.setMessage(message);
            b.setCancelable(false);

            if(positiveButtonTitle!=null)
                b.setPositiveButton(positiveButtonTitle, positiveButtonListener);

            if(negativeButtonTitle!=null)
                b.setNegativeButton(negativeButtonTitle, negativeButtonListener);

//            b.setIcon(android.R.drawable.ic_dialog_alert);

            currentlyShowingMessages.add(message);

            AlertDialog dialog = showAlertDialogBuilder(b);

            if(dialog != null) {
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        currentlyShowingMessages.remove(message);
                    }
                });
            }

            return dialog;
        }
        return null;
    }


    private static Dialog customProgressDialog = null;



}

