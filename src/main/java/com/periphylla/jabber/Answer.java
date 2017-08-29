package com.periphylla.jabber;

public interface Answer {
    boolean handle(String message, ChatReceiver.Callback chat);
}
