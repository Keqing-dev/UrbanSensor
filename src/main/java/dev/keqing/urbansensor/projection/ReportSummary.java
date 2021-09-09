package dev.keqing.urbansensor.projection;

public interface ReportSummary {
    String getId();
    String getLatitude();
    String getLongitude();
    String getAddress();
    String getTimestamp();
    String getCategories();
    String getFile();

    interface UserSummary {
        String getId();
        String getName();
        String getLastName();
    }
}
