package ru.itis;

import ru.itis.parser.YoutubeVideosParser;

import java.util.*;
import java.util.stream.Collectors;

import static ru.itis.DescriptionUtils.getLinksFromDescriptions;

public class CommandLineRunner {


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Введите ID канала: ");

        String channelId = scan.next();

        System.out.println("Parsing started, please wait.");
        Map<String, Long> linksWithoutDuplicates = getLinksWithoutDuplicates(channelId);

        Map<DomainInfo, Long> domainInfos = getDomainInfos(linksWithoutDuplicates);
        Map<HostInfo, Double> finalStatistic = getFinalStatistic(domainInfos);

        System.out.println("Parsing ended. Results");

        for (Map.Entry<HostInfo, Double> e : finalStatistic.entrySet()) {
            System.out.println(e.getKey() + " \n" + e.getValue());
        }
    }

    private static Map<HostInfo, Double> getFinalStatistic(Map<DomainInfo, Long> domainInfos) {
        Map<HostInfo, Double> res = new HashMap<>();
        Map<String, List<DomainInfo>> tempMap = new HashMap<>();
        Long linksAmount = (long) domainInfos.size();

        for (Map.Entry<DomainInfo, Long> e : domainInfos.entrySet()) {
            String host = e.getKey().getLink().getHost();

            if(tempMap.containsKey(host)) {
                tempMap.get(host).add(e.getKey());
            } else {
                List<DomainInfo> d = new ArrayList<>();
                d.add(e.getKey());
                tempMap.put(host, d);
            }
        }

        for (Map.Entry<String, List<DomainInfo>> e : tempMap.entrySet()){
            Double percentOutOfAllLinks = getPercentage(linksAmount, e.getValue().size());
            res.put(new HostInfo(e.getKey(), e.getValue()), percentOutOfAllLinks);
        }

        return res;
    }

    private static Double getPercentage(Long linksAmount, int size) {
        return ((int) ((((double) size) / linksAmount) * 10000)) / 100D;
    }

    private static Map<String, Long> getLinksWithoutDuplicates(String channelId) {
        List<String> descriptions = getDescriptionsFromChannel(channelId);
        List<String> linksFromDescriptions = getLinksFromDescriptions(descriptions);

        return linksFromDescriptions.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

    }

    private static Map<DomainInfo, Long> getDomainInfos(Map<String, Long> linksWithoutDuplicates) {
        Map<DomainInfo, Long> res = new HashMap<>();
        DomainInfoService service = new DomainInfoService();

        for (Map.Entry<String, Long> e : linksWithoutDuplicates.entrySet()) {
            DomainInfo info = service.getDomainInfo(e.getKey());
            if (info.getReferer().equals("EXCEPTION")) continue;
            res.put(info, e.getValue());
        }

        return res;
    }

    private static List<String> getDescriptionsFromChannel(String channelId) {
        return new YoutubeVideosParser().parseChanelAndGetOutgoingLinks(channelId);
    }

}
