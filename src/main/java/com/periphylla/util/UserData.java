package com.periphylla.util;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private final String _name;
    private final String _ip;
    private final String _host;

    public UserData(String data) {
        String[] parts = data.split(",");
        List<String> userData = new ArrayList<>();
        _ip = parts[0].replaceAll("\"", "");
        _host = parts[1].replaceAll("\"", "");
        _name = parts[2].replaceAll("\"", "");
    }

    public String getName() {
        return _name;
    }

    public String getIp() {
        return _ip;
    }

    public String getHost() {
        return _host;
    }

    @Override
    public String toString() {
        return "User: " + _name + " is logged in to " + _host + " with ip " + _ip;
    }
}
