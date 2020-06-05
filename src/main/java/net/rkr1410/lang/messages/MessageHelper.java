package net.rkr1410.lang.messages;

import java.util.ArrayList;
import java.util.List;

public class MessageHelper implements MessageProducer {

    private List<MessageReceiver> receivers;

    public MessageHelper() {
        receivers = new ArrayList<>();
    }

    @Override
    public void addMessageReceiver(MessageReceiver receiver) {
        receivers.add(receiver);
    }

    @Override
    public void sendMessage(Message message) {
        receivers.forEach(r -> r.messageReceived(message));
    }
}
