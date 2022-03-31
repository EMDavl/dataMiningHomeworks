package ru.itis;

import okhttp3.*;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

public class DomainInfoService {

    private static final Logger LOG = Logger.getLogger("DomainInfoService");
    private static final Queue<String> lastSixHosts = new LinkedList<>();

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .build();

    private static final String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/99.0.4844.84 Safari/537.36";


    public DomainInfo getDomainInfo(String url) {
        URI requestUri = URI.create(url);
        try {
            Thread.sleep(400);
            checkIfInLastHosts(requestUri.getHost());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Request r = createRequest(url);
        Call call = client.newCall(r);

        try {
            Response resp = call.execute();
            DomainInfo info = new DomainInfo(resp.request().url().uri(), requestUri.getHost());
            resp.close();
            return info;

        } catch (IOException e) {
            LOG.info("EXCEPTION: Error while sending request. URI: " + r.url().uri());
            return new DomainInfo(r.url().uri(), "EXCEPTION");
        }
    }

    private void checkIfInLastHosts(String host) throws InterruptedException {
        boolean res = lastSixHosts.contains(host);
        if (lastSixHosts.size() == 6){
            lastSixHosts.poll();
        }
        lastSixHosts.offer(host);
        if (res) {
            Thread.sleep(3000);
        }
    }

    private Request createRequest(String url){
        return new Request.Builder()
                .get()
                .url(url)
                .addHeader("user-agent", USER_AGENT_VALUE)
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .build();
    }

}
