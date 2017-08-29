package com.periphylla.jabber;

import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.ArrayList;
import java.util.List;

public class ChatReceiver {

    private final ChatManager _chatManager;
    private boolean _running = true;
    private List<Answer> _answers = new ArrayList();

    public ChatReceiver(XMPPTCPConnection connection) {
        _chatManager = ChatManager.getInstanceFor(connection);
    }

    public void init() {
        _chatManager.addIncomingListener((entityBareJid, message, chat) -> {
            String body = message.getBody();
            System.out.println("Received message: " + body);
            if (body.equals("stop")) {
                _running = false;
            } else {
                for (Answer answer : _answers) {
                    if (answer.handle(body, chat)) {
                        break;
                    }
                }
            }
        });
        add(new Cat());
    }

    public void add(Answer answer) {
        _answers.add(answer);
    }

    public boolean isRunning() {
        return _running;
    }
}
