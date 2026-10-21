package com.example.security;

public final class KafkaTopics {
    public static final String USER_REGISTERED = "user.registered";
    public static final String ROUTINE_JOINED = "routine.joined";
    public static final String EVENT_JOINED = "event.joined";
    public static final String COACH_ANNOUNCEMENT = "coach.announcement";
    public static final String NOTIFICATION_FANOUT = "notification.fanout";
    public static final String CATALOG_CHANGED = "catalog.changed";

    private KafkaTopics() {
    }
}
