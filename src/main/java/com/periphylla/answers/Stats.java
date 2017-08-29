package com.periphylla.answers;

import com.periphylla.jabber.Answer;
import com.periphylla.jabber.ChatReceiver;

import java.util.List;

public class Stats extends Answer {

    private final List<Answer> _allAnswers;

    public Stats(List<Answer> allAnswers) {
        _allAnswers = allAnswers;
    }

    @Override
    public boolean handle(String message, ChatReceiver.Callback chat) {
        if (!message.equals("stats")) {
            return false;
        }
        StringBuilder sb = new StringBuilder("Stats: ");
        for (Answer answer : _allAnswers) {
            sb.append("\n").append(answer).append(": ").append(answer.stats());
        }
        chat.callback(sb.toString());
        return true;
    }

    @Override
    public String usage() {
        return "stats - for stats";
    }

    @Override
    public int stats() {
        return 0;
    }

    @Override
    public String toString() {
        return "Stats";
    }
}
