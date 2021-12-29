package com.periphylla.jabber;

import com.periphylla.answers.*;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JabberClient implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(JabberClient.class.getName());
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

    public void initNonBlocking() {
        _moods = new Moods();
        initAnswers();
        init();
    }

    private void init() {
        moodSwing();
    }

    protected void doWork() {
        while (_chatReceiver.isRunning()) {
            sleep(1000);
            moodSwing();
        }
    }

    public boolean isRunning() {
        return _chatReceiver.isRunning();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            LOGGER.info("interrupted...");
            System.exit(1);
        }
    }

    protected void moodSwing() {
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

    protected void initAnswers() {
        _answers.add(new Cat());
        _answers.add(new Dog());
        _answers.add(new Stats(_answers));
        _answers.add(new User());
        _answers.add(new Ip());
        _answers.add(new Host());
        _answers.add(_moods.moodSwing());
        _answers.add(new DefaultAnswer(_answers));
    }

    protected void addAnswer(Answer answer, int position) {
        if (position > 0) {
            _answers.add(position, answer);
        } else {
            _answers.add(_answers.size() - 2, answer);  // pre last position
        }
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
                LOGGER.log(Level.WARNING, "jabber client not running :-(");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Couldnt connect: ", e);
        }
        return connection;
    }

    @Override
    public void close() {
        if (_connection != null) {
            _connection.disconnect();
        }
    }
}
