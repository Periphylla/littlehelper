package com.periphylla.jabber;

import org.apache.commons.io.IOUtils;
import org.jivesoftware.smack.packet.Presence;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Moods {
    private final List<Presence> _inactiveMoods = new ArrayList<>();
    private final Presence _available = new Presence(Presence.Type.available, "I am glad to work for you", 42, Presence.Mode.available);

    private Instant _lastMoodChange = Instant.now();
    private Instant _lastSeenMessage = Instant.now();
    private Presence _currentMood = _available;
    private MoodSwing _moodSwing = new MoodSwing();

    public Moods() {
        _inactiveMoods.add(new Presence(Presence.Type.available, "I am bored, nobody talks to me", 43, Presence.Mode.available));
        _inactiveMoods.add(new Presence(Presence.Type.available, "I am very bored, nobody talks to me", 44, Presence.Mode.available));
        _inactiveMoods.add(new Presence(Presence.Type.probe, "I don't know what i am", 45, Presence.Mode.xa));
        _inactiveMoods.add(new Presence(Presence.Type.unavailable, "Gone fishing ...", 46, Presence.Mode.away));
        _inactiveMoods.add(new Presence(Presence.Type.available, "I in love to my creators", 47, Presence.Mode.available));
    }

    public Optional<Presence> nextMood(ChatReceiver chatReceiver) {
        if (chatReceiver == null) {
            return Optional.of(_currentMood);
        }
        Instant timeOfLastMessage = chatReceiver.getTimeOfLastMessage();
        if (timeOfLastMessage != null) {
            _lastSeenMessage = timeOfLastMessage;
        }
        Presence mood = null;
        Instant now = Instant.now();
        if (_moodSwing._swingInit.get()) {
            mood = randomMood();
            _moodSwing._swingInit.set(false);
        } else {
            if (now.minusSeconds(2).isBefore(_lastSeenMessage) && !_available.equals(_currentMood)) {
                mood = _available;
            } else if (now.minus(5, ChronoUnit.MINUTES).isAfter(_lastMoodChange) && now.minus(30, ChronoUnit.MINUTES).isAfter(_lastSeenMessage)) {
                mood = randomMood();
            }
        }
        if (mood != null) {
            _currentMood = mood;
            _lastMoodChange = Instant.now();
        }
        return Optional.ofNullable(mood);
    }

    private Presence randomMood() {
        Presence mood;
        int moodIndex = new Random().nextInt(100);
        if (moodIndex < _inactiveMoods.size()) {
            mood = _inactiveMoods.get(moodIndex);
        } else {
            try {
                Process fortune = Runtime.getRuntime().exec("/usr/games/fortune -s");
                String wiseMessage = IOUtils.toString(fortune.getInputStream());
                mood = new Presence(Presence.Type.available, wiseMessage, 42 + _inactiveMoods.size(), Presence.Mode.available);
                _inactiveMoods.add(mood);
            } catch (IOException e) {
                System.out.println("Coud not generate new mood " + e.getMessage());
                mood = _available;
            }
        }
        return mood;
    }

    private class MoodSwing extends Answer {

        private final AtomicBoolean _swingInit = new AtomicBoolean();

        @Override
        public boolean handle(String message, ChatReceiver.Callback chat) {
            if (message.equals("swing")) {
                _swingInit.set(true);
                return true;
            }
            return false;
        }

        @Override
        public String usage() {
            return "";
        }

        @Override
        public String toString() {
            return "MoodSwing";
        }
    }

    public Answer moodSwing() {
        return _moodSwing;
    }
}
