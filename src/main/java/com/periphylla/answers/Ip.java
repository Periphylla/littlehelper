package com.periphylla.answers;

import com.periphylla.jabber.Answer;
import com.periphylla.jabber.ChatReceiver;
import com.periphylla.util.UserData;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ip extends Answer {

    private static final Pattern IP_PATTERN = Pattern.compile("ip\\: ([a-zA-Z0-9]+\\.[a-zA-z0-9]+\\.[a-zA-z0-9]+\\.[a-zA-z0-9]+)", Pattern.CASE_INSENSITIVE);
    private Map<String, UserData> _data;

    @Override
    public boolean handle(String message, ChatReceiver.Callback chat) {
        if (_data == null) {
            init();
        }
        Matcher matcher = IP_PATTERN.matcher(message);
        if (matcher.find()) {
            String foundIp = matcher.group(1);
            UserData data = _data.get(foundIp);
            if (data == null) {
                chat.callback("no data for ip: " + foundIp + "\nUsage " + usage());
            } else {
                chat.callback(data.toString());
            }
            return true;
        }
        return false;
    }

    private void init() {
        Map<String, UserData> data = new HashMap<>();
        File file = new File("user_host_ip.csv");
        if (file.exists()) {
            try {
                List<String> lines = FileUtils.readLines(file);
                for (String line : lines) {
                    UserData userData = new UserData(line);
                    data.put(userData.getIp(), userData);
                }
            } catch (IOException e) {
                System.out.println("Could not read " + file + ". " + e.getMessage());
            }
        }
        _data = data;
    }

    @Override
    public String usage() {
        return "Ip: <ip> - to show user and hostname for ip";
    }

    @Override
    public String toString() {
        return "Ip";
    }
}
