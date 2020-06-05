package net.rkr1410.lang.frontend;

import net.rkr1410.lang.messages.MessageProducer;

public abstract class ErrorHandler<T extends ErrorCode> implements MessageProducer {
    //todo Token should probably be generic, too
    public abstract void flag(Token token, T errorCode);
    public abstract int getErrorCount();
    public abstract void abortTranslation(T code);
}
