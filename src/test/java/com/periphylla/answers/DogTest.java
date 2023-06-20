package com.periphylla.answers;

import com.periphylla.jabber.ChatReceiver;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class DogTest {
    @Test
    public void simple_call_dog() {
        Dog dog = new Dog();
        AtomicReference<String> answer = new AtomicReference<>();
        ChatReceiver.Callback callback = testCallback(answer);
        dog.handle("dog", callback);
        assertThat(answer.get()).startsWith("https://images.dog.ceo/");
    }


    private ChatReceiver.Callback testCallback(AtomicReference<String> answer) {
        return new ChatReceiver.Callback(null) {
            @Override
            public void callback(String message) {
                answer.set(message);
            }
        };
    }
}