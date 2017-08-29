package com.periphylla.jabber;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.Properties;

public class Client {

    Client(Properties p) {
        _username = p.getProperty("username");
        _password = p.getProperty("password");
        _host = p.getProperty("server");
        _port = Integer.parseInt(p.getProperty("port"));
    }

    private final String _username;
    private final String _password;
    private final String _host;
    private final int _port;

    public String getUsername() {
        return _username;
    }

    public String getPassword() {
        return _password;
    }

    XMPPTCPConnectionConfiguration toConnectionConfiguration() throws XmppStringprepException {

        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setXmppDomain("localhost");
        builder.setHost(_host);
        builder.setPort(_port);
        builder.setUsernameAndPassword(_username, _password);
        builder.setCompressionEnabled(false);
        builder.setHostnameVerifier((s, sslSession) -> true);
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        XMPPTCPConnectionConfiguration conf = builder.build();
        return conf;
    }
}
