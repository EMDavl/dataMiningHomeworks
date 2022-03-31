package ru.itis;

import java.net.URI;

public class DomainInfo {

    private URI link;
    private String referer;


    public DomainInfo(URI link, String referer) {
        this.link = link;
        this.referer = referer;
    }

    public URI getLink() {
        return link;
    }

    public String getReferer() {
        return referer;
    }

    @Override
    public String toString() {
        return "link: " + link + ", referer: " + referer;
    }
}
