package com.periphylla.jabber;

import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jxmpp.stringprep.XmppStringprepException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Properties;

public class Client {

    Client(Properties p) {
        _username = p.getProperty("username");
        _password = p.getProperty("password");
        _host = p.getProperty("server");
        _domain = p.getProperty("domain");
        _port = Integer.parseInt(p.getProperty("port"));
        _untrusted = Boolean.parseBoolean(p.getProperty("untrusted", "true"));
    }

    private final String _username;
    private final String _password;
    private final String _host;
    private final String _domain;
    private final int _port;
    private final boolean _untrusted;

    XMPPTCPConnectionConfiguration toConnectionConfiguration() throws XmppStringprepException, NoSuchAlgorithmException, KeyManagementException {

        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setXmppDomain(_domain);
        builder.setHost(_host);
        builder.setPort(_port);
        builder.setUsernameAndPassword(_username, _password);
        builder.setCompressionEnabled(false);
        if (_untrusted) {
            useUntrustedConnection(builder);
        }

        XMPPTCPConnectionConfiguration conf = builder.build();
        return conf;
    }

    private void useUntrustedConnection(XMPPTCPConnectionConfiguration.Builder builder) throws NoSuchAlgorithmException, KeyManagementException {
        builder.setHostnameVerifier((s, sslSession) -> true);
        SSLContext sslctx = SSLContext.getInstance("TLS");
        TrustManager manager = new TLSUtils.AcceptAllTrustManager();
        TrustManager[] trustManagers = {manager};
        sslctx.init(null, trustManagers, new SecureRandom());
        builder.setCustomSSLContext(sslctx);
    }
}
