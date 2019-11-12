package gq.dynitios.cheatbuster.http;

import com.google.gson.Gson;
import gq.dynitios.cheatbuster.recorder.FinishedRecording;

import java.io.IOException;

public class Logstash {
    private HttpClient httpClient;

    public Logstash() {
        httpClient = new HttpClient();
    }

    public void sendToLogstash(FinishedRecording recording) {
        Gson gson = new Gson();
        String json = gson.toJson(recording);
        try {
            httpClient.putJson("http://192.168.116.128:1337/", json); // todo: make url configurable
        } catch (IOException e) {
            // todo: warn admins of invalid connection url or logstash downtime
            e.printStackTrace();
        }
    }
}
