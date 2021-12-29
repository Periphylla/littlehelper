package com.periphylla.jabber;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Startup {
    private static final Logger LOGGER = Logger.getLogger(Startup.class.getName());
    public static void main(String[] args) {
        ClientProperties clientProperties = readProps();
        JabberClient client = new JabberClient(clientProperties);
        client.run();
        LOGGER.info("Jabber client " + clientProperties.getUsername() + " finished");
    }

    private static ClientProperties readProps() {
        LOGGER.info("Starting ...");
        ClientProperties clientProperties = null;

        File file = new File("client.properties");
        if (!file.exists()) {
            LOGGER.info("no client.properties !");
            System.exit(1);
        }
        LOGGER.info("reading properties ...");
        try (InputStream is = new FileInputStream(file)) {
            Properties p = new Properties();
            p.load(is);
            clientProperties = new ClientProperties(p);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not open client.properties !", e);
            System.exit(1);
        }
        LOGGER.info("Try to establish jabber connection");
        return clientProperties;
    }
}
