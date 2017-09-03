package com.periphylla.answers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.periphylla.jabber.Answer;
import com.periphylla.jabber.ChatReceiver;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class Dog extends Answer {
    @Override
    public boolean handle(String message, ChatReceiver.Callback chat) {
        if (message.contains("dog")) {
            try {
                Map result = new ObjectMapper().readValue(new URL("https://dog.ceo/api/breeds/image/random"), Map.class);
                String url = (String) result.get("message");
                chat.callback(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return message.equals("dog");   // only break operation if they just asked for dog
        }
        return false;
    }

    @Override
    public String usage() {
        return "dog - for dog links";
    }

    @Override
    public String toString() {
        return "Dog";
    }
}
