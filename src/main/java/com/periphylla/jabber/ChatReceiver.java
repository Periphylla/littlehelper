package com.periphylla.jabber;

import com.periphylla.answers.*;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ChatReceiver {

    private final ChatManager _chatManager;
    private boolean _running = true;
    private final List<Answer> _answers = new ArrayList();
    private Instant _timeOfLastMessage = Instant.now();

    public ChatReceiver(XMPPTCPConnection connection) {
        _chatManager = ChatManager.getInstanceFor(connection);
    }

    public void init() {
        _chatManager.addIncomingListener((entityBareJid, message, chat) -> {
            String body = message.getBody();
            System.out.println("Received message: " + body + " from " + chat.getXmppAddressOfChatPartner());
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
        });
        add(new Cat());
        add(new Dog());
        add(new Stats(_answers));
        add(new User());
        add(new Ip());
        add(new Host());
        add(new DefaultAnswer(_answers));
    }

    private void add(Answer answer) {
        _answers.add(answer);
    }

    public boolean isRunning() {
        return _running;
    }

    public Instant getTimeOfLastMessage() {
        return _timeOfLastMessage;
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
