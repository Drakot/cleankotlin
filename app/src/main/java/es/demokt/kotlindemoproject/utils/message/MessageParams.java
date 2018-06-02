package es.demokt.kotlindemoproject.utils.message;

/**
 * @author by AlbertoLuengo on 12/5/16.
 */
public class MessageParams {
    //TODO add button feature
    private String title;
    private String message;

    private Message.MessageTypes messageType;
    private Message.Types type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message.MessageTypes getMessageType() {
        return messageType;
    }

    public void setMessageType(Message.MessageTypes messageType) {
        this.messageType = messageType;
    }

    public Message.Types getType() {
        return type;
    }

    public void setType(Message.Types type) {
        this.type = type;
    }

    public MessageParams title(String title) {
        setTitle(title);
        return this;
    }

    public MessageParams message(String message) {
        setMessage(message);
        return this;
    }

    public MessageParams type(Message.Types type) {
        setType(type);
        return this;
    }

    public MessageParams messageType(Message.MessageTypes messageType) {
        setMessageType(messageType);
        return this;
    }
}
