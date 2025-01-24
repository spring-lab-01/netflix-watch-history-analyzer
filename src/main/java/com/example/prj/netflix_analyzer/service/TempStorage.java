package com.example.prj.netflix_analyzer.service;

import com.example.prj.netflix_analyzer.model.ViewingActivity;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class TempStorage {
    private static List<ViewingActivity> viewingActivities;

    public static void setViewingActivities(List<ViewingActivity> viewingActivities){
        TempStorage.viewingActivities = viewingActivities;
    }

    public static List<ViewingActivity> getViewingActivities(){
        return viewingActivities;
    }

    public static boolean isEmpty(){
        return CollectionUtils.isEmpty(viewingActivities);
    }

    public static void clear(){
        viewingActivities.clear();
    }

}
