package ru.itis.parser;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class YoutubeVideosParser {

    // =================== Defining constants =======
    private static final Long MAX_RESULTS = 20L;
    private static final String SEARCH_ORDER = "date";
    private static final List<String> SEARCH_TYPE = List.of("video");
    private static final List<String> REQUEST_PART = List.of("snippet");
    private final String API_KEY = "AIzaSyAXU2VNpvCaQYctxZSMP14DZ4vlj-hVeFI";
    private static final String APPLICATION_NAME = "dataMiningCrawler";
    // =============================================

    // ===================== Local Variables ================
    private final YouTube youTubeService;
    private String chanelId;
    // ======================================================

    private static final Logger LOG = Logger.getLogger("YoutubeVideosParser");


    public YoutubeVideosParser() {
        NetHttpTransport httpTransport = null;

        try {
            httpTransport = getHttpTransport();
        } catch (GeneralSecurityException | IOException e) {
            LOG.info("EXCEPTION: Error while creating http transport");
            throw new IllegalStateException(e.getMessage());
        }

        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        youTubeService = new YouTube.Builder(httpTransport, jsonFactory, null)
                .setApplicationName(APPLICATION_NAME)
                .setYouTubeRequestInitializer(new YouTubeRequestInitializer(API_KEY))
                .build();
    }

    public List<String> parseChanelAndGetOutgoingLinks(String chanelId) {

        List<String> descriptions = new ArrayList<>();
        this.chanelId = chanelId;

        List<List<String>> videosId = getIdList();
        executeRequests(descriptions, videosId);

        return descriptions;

    }

    private List<List<String>> getIdList() {
        List<List<String>> result = new ArrayList<>();
        try {
            YouTube.Search.List requestTemplate = getRequestTemplate();
            SearchListResponse execute = requestTemplate.execute();
            do {

                List<String> idList = execute.getItems()
                        .stream()
                        .map(e -> e.getId().getVideoId())
                        .collect(Collectors.toList());

                result.add(idList);

                if (execute.getNextPageToken() != null) {
                    execute = requestTemplate.setPageToken(execute.getNextPageToken()).execute();
                }

            } while (execute.getNextPageToken() != null);
        } catch (IOException e) {
            LOG.info("EXCEPTION: Error while executing request. " + e.getMessage());
            throw new IllegalStateException(e.getMessage());
        }

        return result;
    }

    private void executeRequests(List<String> descriptions, List<List<String>> videosId) {
        VideoListResponse videos = null;

        try {
            for (List<String> videoId: videosId) {
                videos = youTubeService.videos()
                        .list(List.of("snippet"))
                        .setId(videoId)
                        .execute();

                descriptions.addAll(videos.getItems()
                        .stream()
                        .map(e -> e.getSnippet().getDescription())
                        .collect(Collectors.toList()));
            }

        } catch (IOException e) {
            LOG.info("EXCEPTION: Error while getting videos by id. " + e.getMessage());
            throw new IllegalStateException(e.getMessage());
        }
    }

    private YouTube.Search.List getRequestTemplate() throws IOException {

        String oneYearLimit = LocalDateTime.now()
                .minusYears(1)
                .format(DateTimeFormatter.ISO_DATE_TIME) + "Z";

        return youTubeService.search()
                .list(REQUEST_PART)
                .setChannelId(chanelId)
                .setMaxResults(MAX_RESULTS)
                .setOrder(SEARCH_ORDER)
                .setPublishedAfter(oneYearLimit)
                .setType(SEARCH_TYPE);
    }

    private NetHttpTransport getHttpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }
}
