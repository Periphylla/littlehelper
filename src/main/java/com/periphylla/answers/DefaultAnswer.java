package com.periphylla.answers;

import com.periphylla.jabber.Answer;
import com.periphylla.jabber.ChatReceiver;

import java.util.ArrayList;
import java.util.List;

public class DefaultAnswer extends Answer {
    private final List<Answer> _allAnswers = new ArrayList<>();

    public DefaultAnswer(List<Answer> allAnswers) {
        _allAnswers.addAll(allAnswers); // make a copy or you find yourself in the list
    }

    @Override
    public boolean handle(String message, ChatReceiver.Callback chat) {
        StringBuilder sb = new StringBuilder("Usage: ");
        for (Answer answer : _allAnswers) {
            sb.append("\n").append(answer.usage());
        }
        chat.callback(sb.toString());
        return false;
    }

    @Override
    public String usage() {
        return "";
    }

    @Override
    public String toString() {
        return "Usages";
    }
}
