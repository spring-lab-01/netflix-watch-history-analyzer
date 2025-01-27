package com.example.prj.netflix_analyzer.endpoint;

import com.example.prj.netflix_analyzer.service.AnalysisService;
import com.example.prj.netflix_analyzer.service.TempStorage;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardEndpoint {

    private final AnalysisService analysisService;

    public DashboardEndpoint(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping
    public ModelAndView getHome(ModelAndView modelAndView) {
        modelAndView.setViewName("home");
        if(!TempStorage.isEmpty()) {
            modelAndView.setViewName("analysis");
            modelAndView.getModel().put("watchedContentByProfile", analysisService.getWatchedContentByProfile(TempStorage.getViewingActivities()));
            modelAndView.getModel().put("watchedContentByDevice", analysisService.getWatchedContentByDevice(TempStorage.getViewingActivities()));
            modelAndView.getModel().put("watchData", analysisService.watchData(TempStorage.getViewingActivities()));
        }
        return modelAndView;
    }

    @GetMapping("/clear")
    public String clearData(ModelAndView modelAndView) {
        if(!CollectionUtils.isEmpty(TempStorage.getViewingActivities())) {
            TempStorage.clear();
        }
        return "redirect:/";
    }
}
