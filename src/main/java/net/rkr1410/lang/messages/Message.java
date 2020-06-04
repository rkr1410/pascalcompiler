package net.rkr1410.lang.messages;

import java.util.Arrays;

public class Message {
    private MessageType type;
    private Object body;

    public Message(MessageType type, Object body) {
        this.type = type;
        this.body = body;
    }

    public MessageType getType() {
        return type;
    }

    public Object getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", body=" + unpackBody() +
                '}';
    }

    private String unpackBody() {
        if (body != null) {
            if (body.getClass().isArray()) {
                return Arrays.toString((Object[]) body);
            } else {
                return body.toString();
            }
        } else {
            return null;
        }
    }
}
