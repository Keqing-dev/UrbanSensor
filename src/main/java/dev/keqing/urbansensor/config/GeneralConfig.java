package dev.keqing.urbansensor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public enum GeneralConfig {
    INSTANCE;

    @Value("${domain.name}")
    private String url = "https://urban.keqing.dev/";

    private int itemPerPage = 10;


    public int initPage(int page){
        return page - 1 < 0 ? 0: page + 1;
    }

    public int limitPage(int limit){
        return limit > 100 ? itemPerPage : limit;
    }

    public Pageable pageable(int page, int limit){
        return PageRequest.of(this.initPage(page), this.limitPage(limit));
    }

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
