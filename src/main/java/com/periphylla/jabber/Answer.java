package com.periphylla.jabber;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Answer {
    private AtomicInteger _count = new AtomicInteger();
    public abstract boolean handle(String message, ChatReceiver.Callback chat);
    public abstract String usage();

    public boolean incomingMessage(String message, ChatReceiver.Callback chat) {
        boolean handled = handle(message, chat);
        if (handled) {
            _count.incrementAndGet();
        }
        return handled;
    }

    public int stats() {
        return _count.get();
    }
}
