package com.periphylla.jabber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.Context;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jivesoftware.smack.chat2.Chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Map;

public class Cat implements Answer {
    @Override
    public boolean handle(String message, Chat chat) {
        if (message.contains("cat")) {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet("http://random.cat/meow");
            try {
                HttpResponse response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String json;
                try (InputStream content = entity.getContent()) {
                    json = IOUtils.toString(content);
                }
                new ObjectMapper().readValue(new URL("http://random.cat/meow"), Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
