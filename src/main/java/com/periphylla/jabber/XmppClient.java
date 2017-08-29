package com.periphylla.jabber;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class XmppClient extends XMPPTCPConnection {

    public XmppClient(XMPPTCPConnectionConfiguration config) {
        super(config);
    }
}
