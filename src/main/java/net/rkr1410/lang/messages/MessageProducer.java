package net.rkr1410.lang.messages;

public interface MessageProducer {

    default void addMessageReceiver(MessageReceiver receiver) {
        MessageHelper messageHelper = getMessageHelper();
        if (messageHelper != null) {
            messageHelper.addMessageReceiver(receiver);
        }
    }

    default void sendMessage(Message message) {
        MessageHelper messageHelper = getMessageHelper();
        if (messageHelper != null) {
            messageHelper.sendMessage(message);
        }
    }

    default MessageHelper getMessageHelper() {
        return null;
    }
}
