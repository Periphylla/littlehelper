package com.periphylla.jabber;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Startup {
    public static void main(String args[]) {
        Client client = readProps();
        XMPPTCPConnection connection = connect(client);
        try {
            ChatReceiver chatReceiver = new ChatReceiver(connection);
            chatReceiver.init();
            Mood mood = new Mood(connection);
            if (connection.isAuthenticated()) {
                run(chatReceiver, mood);
            }
        } finally {
            connection.disconnect();
        }
        System.out.println("Jabber finished");
    }

    private static XMPPTCPConnection connect(Client client) {
        XMPPTCPConnection connection;
        try {
            connection = new XmppClient(client.toConnectionConfiguration());
            connection.connect();
            connection.login();
            Presence p = new Presence(Presence.Type.available, "I am glad to work for you", 42, Presence.Mode.available);
            connection.sendStanza(p);
            if (connection.isAuthenticated()) {
                System.out.println("jabber client running...");
            } else {
                System.out.println("jabber client not running :-(");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Couldnt connect: ", e);
        }
        return connection;
    }

    private static void run(ChatReceiver chatReceiver, Mood mood) {
        long counter = 0;
        while (chatReceiver.isRunning()) {
            try {
                mood.mood(chatReceiver);
                Thread.sleep(1000);
                if (++counter % 30 == 0) {
                    System.out.print(".");
                }
                if (counter % 3000 == 0) {
                    System.out.println();
                }
            } catch (InterruptedException ignored) {
                System.out.println("interrupted...");
                System.exit(1);
            }
        }
    }

    private static Client readProps() {
        System.out.println("Starting ...");
        Client client = null;

        File file = new File("client.properties");
        if (!file.exists()) {
            System.out.println("no client.properties !");
            System.exit(1);
        }
        System.out.println("reading properties ...");
        try (InputStream is = new FileInputStream(file)) {
            Properties p = new Properties();
            p.load(is);
            client = new Client(p);
        } catch (IOException ignored) {
            System.out.println("Could not open client.properties !");
            System.exit(1);
        }
        System.out.println("Try to establish jabber connection");
        return client;
    }
}
