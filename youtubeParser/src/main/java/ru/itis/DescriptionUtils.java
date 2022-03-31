package ru.itis;

import java.util.Collection;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DescriptionUtils {
    private static final Pattern urlPattern = Pattern.compile(
            "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    public static List<String> getLinksFromDescriptions(List<String> descriptions){
        return descriptions.stream()
                .map(DescriptionUtils::getLinksFromDescription)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static List<String> getLinksFromDescription(String description){
        Matcher m = urlPattern.matcher(description);
        Stream<MatchResult> results = m.results();
        return results.map(MatchResult::group).collect(Collectors.toList());
    }
}
