package com.tourpal.data.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;
import java.util.Map;

// Log of each tour
public class TourLog {

    // Each walked point stores both a coordinate and the time it was recorded.
    public static class WalkedPoint {
        public GeoPoint point;
        public Timestamp recordedAt;
    }

    // Register notes, images and geolocations for each user log
    public static class Annotation {
        public String logId;
        public String notes;
        public List<Map<String, GeoPoint>> images;
    }

    private Timestamp createdAt;
    private Timestamp endedAt;
    private String guideId;
    private String notes;
    private String tourPlanId;
    private String userId;
    private List<Annotation> logs;
    private List<WalkedPoint> walkedPath;
}

