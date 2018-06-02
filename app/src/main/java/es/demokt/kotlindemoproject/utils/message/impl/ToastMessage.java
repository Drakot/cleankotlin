package es.demokt.kotlindemoproject.utils.message.impl;

import android.app.Activity;
import android.widget.Toast;

import es.demokt.kotlindemoproject.utils.message.Message;
import es.demokt.kotlindemoproject.utils.message.MessageParams;

public class ToastMessage implements Message {
    private MessageParams messageParams;
    private Activity mActivity;
    private Toast toast;

    /**
     * ToastMessage constructor
     *
     * @param activity       Activity
     * @param showMessageParams ShowInfoParams
     */
    public ToastMessage(Activity activity, MessageParams showMessageParams) {
        mActivity = activity;
        messageParams = showMessageParams;
    }

    @Override
    public Message init() {
        toast = Toast.makeText(mActivity, messageParams.getMessage(), Toast.LENGTH_LONG);
        return this;
    }

    @Override
    public void show() {
        toast.show();
    }


    @Override
    public void setButtonListener(Message.ButtonListener buttonListener) {
        throw new NoSuchMethodError("This method is not supported");
    }

    @Override
    public void cancel() {
        toast.cancel();
    }
}
