package com.periphylla.jabber;

import org.jivesoftware.smack.chat2.Chat;

interface Answer {
    boolean handle(String message, Chat chat);
}
