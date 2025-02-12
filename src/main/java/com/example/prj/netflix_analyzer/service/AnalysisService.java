package com.example.prj.netflix_analyzer.service;


import com.example.prj.netflix_analyzer.model.ViewingActivity;
import com.example.prj.netflix_analyzer.model.WatchedSummary;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class AnalysisService {

    public List<WatchedSummary> getWatchedContentByProfile(List<ViewingActivity> viewingActivityList) {
        return summarizeByField(viewingActivityList, ViewingActivity::getProfile);
    }

    public List<WatchedSummary> getWatchedContentByDevice(List<ViewingActivity> viewingActivityList) {
        return summarizeByField(viewingActivityList, ViewingActivity::getDevice);
    }

    private static List<WatchedSummary> summarizeByField(List<ViewingActivity> viewingActivityList, Function<? super ViewingActivity, String> field) {
        return viewingActivityList.stream()
                .sorted(Comparator.comparing(ViewingActivity::getTitle))
                .collect(Collectors.groupingBy(field))
                .entrySet().stream().map(e -> {
                    String profile = e.getKey();
                    return e.getValue().stream()
                            .collect(Collectors.groupingBy(ViewingActivity::getYear))
                            .entrySet().stream().map(e1 -> {
                                String year = e1.getKey();
                                Integer watchedContent = e1.getValue().size();
                                Duration watchedDuration = Duration.ofSeconds(e1.getValue().stream().map(ViewingActivity::getDuration).map(Duration::getSeconds).reduce(Long::sum).orElse(0L));
                                return new WatchedSummary(profile, watchedContent, watchedDuration, DurationFormatUtils.formatDurationWords(watchedDuration.toMillis(), true, true), year);
                            }).toList();
                }).flatMap(List::stream)
                .sorted(Comparator.comparing(WatchedSummary::getProfile).thenComparing(WatchedSummary::getYear))
                .collect(Collectors.toList());
    }

    public List<ViewingActivity> getViewingActivity(List<String> lines) {
                try {
                    List<String> splitContent = Arrays.asList("episode", "limited series", "season");
                    return IntStream.range(1, lines.size())
                            .mapToObj(index -> processViewedContentLine(lines.get(index)))
                            .filter(v -> !StringUtils.hasLength(v.getVideoType()))
                            .peek(v -> {
                                if (splitContent.stream().anyMatch(x -> v.getTitle().toLowerCase().contains(x))) {
                                    String title = v.getTitle().split(":")[0];
                                    v.setTitle(title);
                                }
                            })
                            .distinct()
                            .sorted(Comparator.comparing(ViewingActivity::getTitle))
                            .collect(Collectors.toList());
                }catch (Exception e) {
                    throw new RuntimeException("File seems to be corrupted or issue in processing");
                }
    }

    private ViewingActivity processViewedContentLine(String line) {
        String[] record = line.split(",");
        Duration duration = convertStringToDuration(record[2]);
        String year;
        try {
            year = getYearFromDateTimeFormat(record[1]);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String device = record[6];
        String country = record[7];

        return new ViewingActivity(record[0], record[1], duration, record[4], record[5], year, device, country);
    }

    private static String getYearFromDateTimeFormat(String dateString) throws ParseException {
        SimpleDateFormat sdf ;
        if(dateString.contains("/")){
            sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        }
        else {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        Date date = sdf.parse(dateString);
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return df.format(date);
    }


    private static Duration convertStringToDuration(String durationString) {
        String[] arr = durationString.split(":");
        return Duration.ofSeconds(Long.parseLong(arr[0])*60*60 + Long.parseLong(arr[1])*60 + Long.parseLong(arr[2]));
    }

    public List<ViewingActivity> getWatchedContentByPredicate(List<ViewingActivity> viewingActivities, Predicate<ViewingActivity> predicate) {
        return viewingActivities.stream().filter(predicate).collect(Collectors.toList());
    }

    public static Predicate<ViewingActivity> predicateProfileAndYear(String profile, String year) {
        return v -> v.getProfile().trim().equalsIgnoreCase(profile)
                && v.getYear().trim().equalsIgnoreCase(year);
    }

    public static Predicate<ViewingActivity> predicateDeviceAndYear(String device, String year) {
        return v -> v.getDevice().trim().equalsIgnoreCase(device)
                && v.getYear().trim().equalsIgnoreCase(year);
    }

    public Map<String, Integer> watchData(List<ViewingActivity> viewingActivities) {
        return summarizeByField(viewingActivities, ViewingActivity::getProfile).stream()
                .collect(Collectors.toMap(WatchedSummary::getProfile, WatchedSummary::getTotal,
                        (oldValue, newValue) -> newValue, TreeMap::new));
    }
}
