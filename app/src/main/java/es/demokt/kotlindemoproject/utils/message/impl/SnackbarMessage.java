package es.demokt.kotlindemoproject.utils.message.impl;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.ViewGroup;
import android.widget.TextView;

import es.demokt.kotlindemoproject.R;
import es.demokt.kotlindemoproject.utils.message.Message;
import es.demokt.kotlindemoproject.utils.message.MessageParams;

public class SnackbarMessage implements Message {

    private MessageParams messageParams;
    private Activity mActivity;
    private ButtonListener mButtonListener;
    private Snackbar snackbar;

    /**
     * SnackbarMessage empty constructor
     */
    public SnackbarMessage() {
    }

    /**
     * SnackbarMessage constructor
     *
     * @param activity Activity
     * @param showMessageParams ShowInfoParams
     */
    public SnackbarMessage(Activity activity, MessageParams showMessageParams) {
        mActivity = activity;
        messageParams = showMessageParams;
    }

    @Override public Message init() {
        //final Button button = messageParams.getButtons().get(0);

        final ViewGroup view = (ViewGroup) ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0);

        snackbar = Snackbar.make(view, messageParams.getMessage(), Snackbar.LENGTH_LONG);

        setStyles();

/*
        snackbar.setAction(messageParams.getMessage(), new View.OnClickListener() {
            @Overr ide
            public void onClick(View view) {
                if (mButtonListener != null) {
                    //mButtonListener.onButtonClick(button);
                }
            }
        });*/
        return this;
    }

    private void setStyles() {
        switch (messageParams.getMessageType()) {
            case ERROR:
                snackbar.getView().setBackgroundResource(R.color.message_error);
                break;
            case SUCCESS:
            case WARNING:
            case INFO:
                break;
        }

        TextView messageView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        if (messageView != null) {
            messageView.setTextColor(mActivity.getResources().getColor(android.R.color.white));
        }
    }

    @Override public void show() {
        snackbar.show();
    }

    @Override public void setButtonListener(ButtonListener buttonListener) {
        mButtonListener = buttonListener;
    }

    @Override public void cancel() {
        snackbar.dismiss();
    }
}
