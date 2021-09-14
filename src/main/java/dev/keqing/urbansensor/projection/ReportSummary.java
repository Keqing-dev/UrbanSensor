package dev.keqing.urbansensor.projection;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.keqing.urbansensor.utils.FileURLConverter;

import java.time.LocalDateTime;

public interface ReportSummary {
    String getId();
    String getLatitude();
    String getLongitude();
    String getAddress();
    LocalDateTime getTimestamp();
    String getCategories();
    @JsonSerialize(converter = FileURLConverter.class)
    String getFile();
    UserSummary getUser();

    interface UserSummary {
        String getId();
        String getName();
        String getLastName();
    }
}
