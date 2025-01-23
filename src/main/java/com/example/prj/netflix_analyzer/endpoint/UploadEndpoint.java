package com.example.prj.netflix_analyzer.endpoint;

import com.example.prj.netflix_analyzer.service.TempStorage;
import com.example.prj.netflix_analyzer.model.ViewingActivity;
import com.example.prj.netflix_analyzer.model.WatchedSummary;
import com.example.prj.netflix_analyzer.service.AnalysisService;
import com.example.prj.netflix_analyzer.service.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
public class UploadEndpoint {

    private final AnalysisService analysisService;
    private final UploadService fileUploadService;


    public UploadEndpoint(AnalysisService fileUploadService, UploadService fileUploadService1) {
        this.analysisService = fileUploadService;
        this.fileUploadService = fileUploadService1;
    }

    @PostMapping("/upload")
    public String uploadFile(Model model, @RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            if(file.isEmpty()) {
                message = "File is empty";
            } else if (!Objects.requireNonNull(file.getOriginalFilename()).equalsIgnoreCase("ViewingActivity.csv")) {
                message = "Please upload ViewingActivity.csv";
            } else {
                List<String> lines = fileUploadService.upload(file);
                List<ViewingActivity> viewingActivityList = analysisService.getViewingActivity(lines);
                TempStorage.viewingActivities = viewingActivityList;
                List<WatchedSummary> getWatchedContentByProfile = CollectionUtils.isEmpty(viewingActivityList) ? Collections.emptyList() : analysisService.getWatchedContentByProfile(viewingActivityList);
                model.addAttribute("watchedContentByProfile", getWatchedContentByProfile);
                List<WatchedSummary> watchedContentByDevice = CollectionUtils.isEmpty(viewingActivityList) ? Collections.emptyList() : analysisService.getWatchedContentByDevice(viewingActivityList);
                model.addAttribute("watchedContentByDevice", watchedContentByDevice);
            }
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
        }
        finally {
            model.addAttribute("message", message);
        }
        return "analysis";
    }

}


