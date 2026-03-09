package medisphere.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification {
    private final String notificationId;
    private final String userId;
    private final String title;
    private final String message;
    private final String channel; // "EMAIL" | "SYSTEM"
    private boolean read;
    private final LocalDateTime createdAt;

    public Notification(String id, String userId, String title, String message, String channel) {
        this.notificationId = id;
        this.userId  = userId;
        this.title   = title;
        this.message = message;
        this.channel = channel;
        this.read    = false;
        this.createdAt = LocalDateTime.now();
    }

    public String getNotificationId() { return notificationId; }
    public String getUserId()         { return userId; }
    public String getTitle()          { return title; }
    public String getMessage()        { return message; }
    public String getChannel()        { return channel; }
    public boolean isRead()           { return read; }
    public void setRead(boolean read) { this.read = read; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
    public String getFormattedTime()  {
        return createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));
    }
}
