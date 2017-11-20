package com.periphylla.jabber;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatReceiver {

    private static final Logger LOGGER = Logger.getLogger(ChatReceiver.class);
    private final ChatManager _chatManager;
    private boolean _running = true;
    private final List<Answer> _answers;
    private Instant _timeOfLastMessage;
    private IncomingChatMessageListener _listener;
    private final AtomicInteger _activeCount = new AtomicInteger();

    public ChatReceiver(XMPPTCPConnection connection, List<Answer> answers) {
        _chatManager = ChatManager.getInstanceFor(connection);
        _answers = answers;

    }

    public void init() {
        _listener = (entityBareJid, message, chat) -> {
            _activeCount.incrementAndGet();
            String body = message.getBody();
            LOGGER.info("Received message: " + body + " from " + chat.getXmppAddressOfChatPartner());
            if (body.equals("stop")) {
                _running = false;
            } else {
                _timeOfLastMessage = Instant.now();
                Callback callback = new Callback(chat);
                for (Answer answer : _answers) {
                    if (answer.incomingMessage(body, callback)) {
                        break;
                    }
                }
            }
            _activeCount.decrementAndGet();
        };
        _chatManager.addIncomingListener(_listener);
    }

    public boolean isRunning() {
        return _running;
    }

    public Instant getTimeOfLastMessage() {
        return _timeOfLastMessage;
    }

    public void destroy() {
        _chatManager.removeListener(_listener);
    }

    public void await() {
        while (_activeCount.get() > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }

    public static class Callback {
        private final Chat _chat;

        public Callback(Chat chat) {
            _chat = chat;
        }

        public void callback(String message) {
            try {
                _chat.send(message);
            } catch (Exception e) {
                throw new IllegalStateException("Caught exception while answering " + _chat, e);
            }
        }
    }
}
