package es.demokt.kotlindemoproject.utils.message;

import android.support.v7.app.AppCompatActivity;

import es.demokt.kotlindemoproject.utils.message.impl.NotificationMessage;
import es.demokt.kotlindemoproject.utils.message.impl.SnackbarMessage;
import es.demokt.kotlindemoproject.utils.message.impl.ToastMessage;
 public class MessageFactory {

   public  final AppCompatActivity activity;

    public MessageFactory(AppCompatActivity activity) {
        this.activity = activity;
    }

    public Message getMessage(MessageParams messageParams) {
        if (Message.Types.TOAST == messageParams.getType()) {
            return new ToastMessage(activity, messageParams);
        } else if (Message.Types.SNACKBAR == messageParams.getType()) {
            return new SnackbarMessage(activity, messageParams);
        } else if (Message.Types.NOTIFICATION == messageParams.getType()) {
            return new NotificationMessage(activity, messageParams);
        } else if (Message.Types.ACTION_SHEET == messageParams.getType()) {
            throw new NoSuchMethodError("Action Sheet is not supported");
        }
        return null;
    }

    public Message getDefaultErrorMessage(String message) {
        MessageParams messageParams = new MessageParams();
        messageParams.message(message).type(Message.Types.SNACKBAR).messageType(Message.MessageTypes.ERROR);
        return new SnackbarMessage(activity, messageParams);
    }

    public Message getDefaultMessage(String message) {
        MessageParams messageParams = new MessageParams();
        messageParams.message(message).type(Message.Types.SNACKBAR).messageType(Message.MessageTypes.SUCCESS);
        return new SnackbarMessage(activity, messageParams);
    }
}
