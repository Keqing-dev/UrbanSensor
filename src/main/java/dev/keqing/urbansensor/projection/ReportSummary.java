package dev.keqing.urbansensor.projection;

import java.time.LocalDateTime;

public interface ReportSummary {
    String getId();
    String getLatitude();
    String getLongitude();
    String getAddress();
    LocalDateTime getTimestamp();
    String getCategories();
    String getFile();
    UserSummary getUser();

    interface UserSummary {
        String getId();
        String getName();
        String getLastName();
    }
}
