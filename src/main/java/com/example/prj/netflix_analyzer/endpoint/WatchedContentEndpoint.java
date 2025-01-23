package com.example.prj.netflix_analyzer.endpoint;

import com.example.prj.netflix_analyzer.service.AnalysisService;
import com.example.prj.netflix_analyzer.service.TempStorage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WatchedContentEndpoint {

    private final AnalysisService analysisService;

    public WatchedContentEndpoint(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/watched-content-profile/{profile}/{year}")
    public String getWatchedContent(@PathVariable("profile") String profile, @PathVariable("year") String year, Model model) {
        String message = "";
        try {
            model.addAttribute("contentType","Profile");
            model.addAttribute("content", analysisService.getWatchedContentByPredicate(TempStorage.viewingActivities, AnalysisService.predicateProfileAndYear(profile, year)));
            model.addAttribute("profile", profile);
            model.addAttribute("year", year);
            model.addAttribute("message", message);
        } catch (Exception e) {
            message = "Could not get Watched Content. Error: " + e.getMessage();
            model.addAttribute("message", message);
        }
        return "watched-content";
    }

    @GetMapping("/watched-content-device/{device}/{year}")
    public String getWatchedContentByDevice(@PathVariable("device") String device, @PathVariable("year") String year, Model model) {
        String message = "";
        try {
            model.addAttribute("contentType","Device");
            model.addAttribute("content", analysisService.getWatchedContentByPredicate(TempStorage.viewingActivities, AnalysisService.predicateDeviceAndYear(device, year)));
            model.addAttribute("profile", device);
            model.addAttribute("year", year);
            model.addAttribute("message", message);
        } catch (Exception e) {
            message = "Could not get Watched Content. Error: " + e.getMessage();
            model.addAttribute("message", message);
        }
        return "watched-content";
    }


}
