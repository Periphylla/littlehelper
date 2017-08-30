package com.periphylla.answers;

import com.periphylla.jabber.ChatReceiver;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;


public class UserTest {

    @Test
    public void test_parse_csv() throws Exception {
        User user = new User();
        AtomicReference<String> answer = new AtomicReference<>();
        ChatReceiver.Callback callback = testCallback(answer);
        user.handle("user: peri.phylla", callback);
        assertThat(answer.get()).isEqualTo("User: peri.phylla is logged in to x-1302-1 with ip 1.2.3.4");
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
