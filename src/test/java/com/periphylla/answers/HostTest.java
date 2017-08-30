package com.periphylla.answers;

import com.periphylla.jabber.ChatReceiver;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;


public class HostTest {

    @Test
    public void test_call_for_host() throws Exception {
        Host host = new Host();
        AtomicReference<String> answer = new AtomicReference<>();
        ChatReceiver.Callback callback = testCallback(answer);
        host.handle("host: x-1302-1", callback);
        assertThat(answer.get()).isEqualTo("User: peri.phylla is logged in to x-1302-1 with ip 1.2.3.4");
    }

    @Test
    public void test_call_for_not_exsisting() throws Exception {
        Host host = new Host();
        AtomicReference<String> answer = new AtomicReference<>();
        ChatReceiver.Callback callback = testCallback(answer);
        host.handle("host: not-ex-isting", callback);
        assertThat(answer.get()).isEqualTo("no data for host: not-ex-isting\nUsage Host: <user> - to show ip and user for host");
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
