package com.periphylla.answers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.periphylla.jabber.Answer;
import com.periphylla.jabber.ChatReceiver;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class Cat implements Answer {
    @Override
    public boolean handle(String message, ChatReceiver.Callback chat) {
        if (message.contains("cat")) {
            try {
                Map result = new ObjectMapper().readValue(new URL("http://random.cat/meow"), Map.class);
                String url = (String) result.get("file");
                chat.callback(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
