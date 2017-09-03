package com.periphylla.jabber;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Startup {
    public static void main(String args[]) {
        ClientProperties clientProperties = readProps();
        JabberClient client = new JabberClient(clientProperties);
        client.run();
        System.out.println("Jabber client " + clientProperties.getUsername() + " finished");
    }

    private static ClientProperties readProps() {
        System.out.println("Starting ...");
        ClientProperties clientProperties = null;

        File file = new File("client.properties");
        if (!file.exists()) {
            System.out.println("no client.properties !");
            System.exit(1);
        }
        System.out.println("reading properties ...");
        try (InputStream is = new FileInputStream(file)) {
            Properties p = new Properties();
            p.load(is);
            clientProperties = new ClientProperties(p);
        } catch (IOException ignored) {
            System.out.println("Could not open client.properties !");
            System.exit(1);
        }
        System.out.println("Try to establish jabber connection");
        return clientProperties;
    }
}
