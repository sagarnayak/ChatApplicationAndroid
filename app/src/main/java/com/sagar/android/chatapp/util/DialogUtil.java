package com.sagar.android.chatapp.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.sagar.android.chatapp.R;

@SuppressWarnings("unused")
public class DialogUtil {

    private static Dialog customDialog;

    public interface DialogWithOneButtonCallBack {
        void dialogCancelled();

        void buttonClicked();
    }

    public interface DialogWithMessageCallBack {
        void dialogCancelled();

        void buttonClicked();
    }

    public static void showDialogWithOneButton(
            Context context,
            String message,
            boolean cancellable,
            final DialogWithOneButtonCallBack dialogWithOneButtonCallBack
    ) {
        if (
                customDialog != null &&
                        customDialog.isShowing()
        )
            customDialog.dismiss();
        customDialog = new Dialog(context);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.dialog_with_single_button);
        //noinspection ConstantConditions
        customDialog.getWindow().setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView textViewMessage = customDialog.findViewById(R.id.text_view_message);
        AppCompatButton buttonAction = customDialog.findViewById(R.id.button_action);
        textViewMessage.setText(message);
        customDialog.setCancelable(cancellable);
        buttonAction.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogWithOneButtonCallBack.buttonClicked();
                        customDialog.dismiss();
                    }
                }
        );
        customDialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogWithOneButtonCallBack.dialogCancelled();
                    }
                }
        );
        customDialog.show();
    }

    public static void showDialogWithMessage(
            Context context,
            String message
    ) {
        if (
                customDialog != null &&
                        customDialog.isShowing()
        )
            customDialog.dismiss();
        customDialog = new Dialog(context);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.dialog_with_single_button);
        //noinspection ConstantConditions
        customDialog.getWindow().setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView textViewMessage = customDialog.findViewById(R.id.text_view_message);
        AppCompatButton buttonAction = customDialog.findViewById(R.id.button_action);
        textViewMessage.setText(message);
        buttonAction.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customDialog.dismiss();
                    }
                }
        );
        customDialog.setCancelable(true);
        customDialog.show();
    }

    public static void showDialogWithMessage(
            Context context,
            String message,
            boolean cancellable,
            final DialogWithMessageCallBack callBack
    ) {
        if (
                customDialog != null &&
                        customDialog.isShowing()
        )
            customDialog.dismiss();
        customDialog = new Dialog(context);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.dialog_with_single_button);
        //noinspection ConstantConditions
        customDialog.getWindow().setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView textViewMessage = customDialog.findViewById(R.id.text_view_message);
        AppCompatButton buttonAction = customDialog.findViewById(R.id.button_action);
        textViewMessage.setText(message);
        customDialog.setCancelable(cancellable);
        buttonAction.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callBack.buttonClicked();
                        customDialog.dismiss();
                    }
                }
        );
        customDialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        callBack.dialogCancelled();
                    }
                }
        );
        customDialog.show();
    }
}
