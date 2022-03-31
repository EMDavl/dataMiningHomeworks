package ru.itis;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HostInfo {
    private List<DomainInfo> domainLinks;
    private final String hostName;

    public HostInfo(String hostName) {
        domainLinks = new ArrayList<>();
        this.hostName = hostName;
    }

    public HostInfo(String hostName, List<DomainInfo> domainLinks) {
        this.domainLinks = domainLinks;
        this.hostName = hostName;
    }

    public void addDomainInfo(DomainInfo domainInfo){
        domainLinks.add(domainInfo);
    }

    public String getHostName(){
        return hostName;
    }

    public List<DomainInfo> getDomainLinks() {
        return domainLinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HostInfo hostInfo = (HostInfo) o;

        return Objects.equals(hostName, hostInfo.hostName);
    }

    @Override
    public int hashCode() {
        return hostName != null ? hostName.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder("Host: " + hostName);

        for (DomainInfo d : domainLinks) {
            toString.append("\n\t").append(d);
        }


        return toString.toString();
    }
}
