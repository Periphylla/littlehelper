package com.periphylla.jabber;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Mood {
    private final List<Presence> _inactiveMoods = new ArrayList<>();
    private final Presence _available = new Presence(Presence.Type.available, "I am glad to work for you", 42, Presence.Mode.available);

    private final XMPPTCPConnection _connection;
    private Instant _lastMoodChange = Instant.now();
    private Presence _currentMood = _available;

    public Mood(XMPPTCPConnection connection) {
        _connection = connection;
        _inactiveMoods.add(new Presence(Presence.Type.available, "I am bored, nobody talks to me", 42, Presence.Mode.available));
        _inactiveMoods.add(new Presence(Presence.Type.available, "I am very bored, nobody talks to me", 42, Presence.Mode.available));
        _inactiveMoods.add(new Presence(Presence.Type.probe, "I don't know what i am", 42, Presence.Mode.xa));
        _inactiveMoods.add(new Presence(Presence.Type.unavailable, "Gone fishing ...", 42, Presence.Mode.away));
        _inactiveMoods.add(new Presence(Presence.Type.available, "I in love to my creators", 42, Presence.Mode.available));
    }

    public void mood(ChatReceiver chatReceiver) {
        Optional<Presence> nextMood = nextMood(chatReceiver);
        nextMood.ifPresent(mood -> {
            if (!_currentMood.equals(mood)) {
                try {
                    _connection.sendStanza(_available);
                    _currentMood = mood;
                    _lastMoodChange = Instant.now();
                    System.out.println("Setting mood to: " + mood.getStatus());
                } catch (Exception e) {
                    System.out.println("Could not set mood. " + e.getMessage());
                }
            }
        });
    }

    private Optional<Presence> nextMood(ChatReceiver chatReceiver) {
        Presence mood = null;
        Instant now = Instant.now();
        if (now.minusSeconds(10).isBefore(chatReceiver.getTimeOfLastMessage()) && !_available.equals(_currentMood)) {
            mood = _available;
        } else if (now.minus(5, ChronoUnit.MINUTES).isAfter(_lastMoodChange) && now.minus(3, ChronoUnit.MINUTES).isAfter(chatReceiver.getTimeOfLastMessage())) {
            mood = _inactiveMoods.get(new Random().nextInt(_inactiveMoods.size()));
        }
        return Optional.ofNullable(mood);
    }
}
