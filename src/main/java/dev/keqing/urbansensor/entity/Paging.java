package dev.keqing.urbansensor.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.keqing.urbansensor.config.GeneralConfig;

public class Paging {

    private final String url = GeneralConfig.INSTANCE.getUrl();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String previous;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String next;

    public Paging(String previous, String next) {
        this.previous = previous;
        this.next = next;
    }

    public Paging() {
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = url + previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = url + next;
    }
}
