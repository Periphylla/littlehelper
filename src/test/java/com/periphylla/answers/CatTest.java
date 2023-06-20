package com.periphylla.answers;

import com.periphylla.jabber.ChatReceiver;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;


public class CatTest {
    @Ignore @Test
    public void simple_call_cat() {
        Cat cat = new Cat();
        AtomicReference<String> answer = new AtomicReference<>();
        ChatReceiver.Callback callback = testCallback(answer);
        cat.handle("cat", callback);
        assertThat(answer.get()).startsWith("http://random.cat");
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