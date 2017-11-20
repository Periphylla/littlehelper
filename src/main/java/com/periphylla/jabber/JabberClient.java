package com.periphylla.jabber;

import com.periphylla.answers.*;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.ArrayList;
import java.util.List;

public class JabberClient {
    private static final Logger LOGGER = Logger.getLogger(JabberClient.class);
    private final ClientProperties _clientProperties;
    private final List<Answer> _answers = new ArrayList();
    private Moods _moods;
    private XMPPTCPConnection _connection;
    private ChatReceiver _chatReceiver;

    public JabberClient(ClientProperties clientProperties) {
        _clientProperties = clientProperties;
    }

    public void run() {
        _moods = new Moods();
        initAnswers();
        try {
            init();
            doWork();
        } finally {
            if (_connection != null) {
                _connection.disconnect();
            }
        }
    }

    private void init() {
        moodSwing();
    }

    private void doWork() {
        while (_chatReceiver.isRunning()) {
            sleep(1000);
            moodSwing();
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            LOGGER.info("interrupted...");
            System.exit(1);
        }
    }

    private void moodSwing() {
        _moods.nextMood(_chatReceiver).ifPresent(mood -> {
            if (_connection != null && _connection.isAuthenticated()) {
                _chatReceiver.await();
                _chatReceiver.destroy();
                _connection.disconnect();
            }
            XMPPTCPConnection connection = connect(_clientProperties, mood);
            ChatReceiver chatReceiver = new ChatReceiver(connection, _answers);
            chatReceiver.init();
            _connection = connection;
            _chatReceiver = chatReceiver;
            sleep(5000);
        });
    }

    private void initAnswers() {
        _answers.add(new Cat());
        _answers.add(new Dog());
        _answers.add(new Stats(_answers));
        _answers.add(new User());
        _answers.add(new Ip());
        _answers.add(new Host());
        _answers.add(_moods.moodSwing());
        _answers.add(new DefaultAnswer(_answers));
    }

    private XMPPTCPConnection connect(ClientProperties clientProperties, Presence mood) {
        XMPPTCPConnection connection;
        try {
            connection = new XmppClient(clientProperties.toConnectionConfiguration());
            connection.connect();
            connection.login();
            connection.sendStanza(mood);
            LOGGER.info("Setting mood to: " + mood.getStatus());
            if (connection.isAuthenticated()) {
                LOGGER.info("jabber client running...");
            } else {
                LOGGER.warn("jabber client not running :-(");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Couldnt connect: ", e);
        }
        return connection;
    }
}
