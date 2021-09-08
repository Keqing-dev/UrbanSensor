package dev.keqing.urbansensor.config;

import org.springframework.beans.factory.annotation.Value;

public enum GeneralConfig {
    INSTANCE;

    @Value("${domain.name}")
    private String url = "https://urban.keqing.dev/";

    private int itemPerPage = 10;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }
}
