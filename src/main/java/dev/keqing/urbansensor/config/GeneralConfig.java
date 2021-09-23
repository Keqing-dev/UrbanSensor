package dev.keqing.urbansensor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "config")
@Component
public class GeneralConfig {

    private String domainName;
    private String uploadDir;
    private String avatarDir;
    private String fileDir;
    private int itemPerPage = 10;

    public int initPage(int page) {
        return page - 1;
    }

    public int limitPage(int limit) {
        return limit > 100000 ? itemPerPage : limit;
    }

    public Pageable pageable(int page, int limit) {
        return PageRequest.of(this.initPage(page), this.limitPage(limit));
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getAvatarDir() {
        return avatarDir;
    }

    public void setAvatarDir(String avatarDir) {
        this.avatarDir = avatarDir;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }
}
