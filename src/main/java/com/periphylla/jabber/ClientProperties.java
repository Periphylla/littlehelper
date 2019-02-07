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

public class ClientProperties {

    private String _username;
    private String _password;
    private String _host;
    private String _domain;
    private int _port;
    private boolean _untrusted;

    public ClientProperties() { }

    public ClientProperties(Properties p) {
        _username = p.getProperty("username");
        _password = p.getProperty("password");
        _host = p.getProperty("server");
        _domain = p.getProperty("domain");
        _port = Integer.parseInt(p.getProperty("port"));
        _untrusted = Boolean.parseBoolean(p.getProperty("untrusted", "true"));
    }

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

    public String getUsername() {
        return _username;
    }

    public void setUsername(String username) {
        _username = username;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public void setHost(String host) {
        _host = host;
    }

    public void setDomain(String domain) {
        _domain = domain;
    }

    public void setPort(int port) {
        _port = port;
    }

    public void setUntrusted(boolean untrusted) {
        _untrusted = untrusted;
    }

    @Override
    public String toString() {
        return "ClientProperties{" +
                "_username='" + _username + '\'' +
                ", _host='" + _host + '\'' +
                ", _domain='" + _domain + '\'' +
                ", _port=" + _port +
                ", _untrusted=" + _untrusted +
                '}';
    }
}
