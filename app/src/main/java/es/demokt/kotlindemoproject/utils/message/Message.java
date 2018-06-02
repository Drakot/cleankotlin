package es.demokt.kotlindemoproject.utils.message;


import android.widget.Button;

public interface Message {
    enum MessageTypes {
        SUCCESS,
        INFO,
        WARNING,
        ERROR
    }

    enum Types {
        ACTION_SHEET,
        TOAST,
        NOTIFICATION,
        SNACKBAR
    }

    enum Duration {
        SHORT,
        LONG,
        INFINITE
    }


    Message init();

    void show();

    void cancel();

    void setButtonListener(ButtonListener buttonListener);


    /**
     * ButtonListener interface
     */
    interface ButtonListener {
        /**
         * onButtonClick
         *
         * @param button Button
         */
        void onButtonClick(Button button);
    }
}
