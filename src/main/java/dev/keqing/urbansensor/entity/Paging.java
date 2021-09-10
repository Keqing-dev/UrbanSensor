package dev.keqing.urbansensor.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.keqing.urbansensor.config.GeneralConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

public class Paging {

    @Autowired
    private GeneralConfig generalConfig;

    private final String url = generalConfig.getDomainName();

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


    public static Paging toPagination(Page reports, int page, String type) {
        Paging paging = null;
        if (reports.hasPrevious() || reports.hasNext())
            paging = new Paging();

        if (reports.hasNext() && paging != null)
            paging.setNext(type + "?page=" + (page + 1));

        if (reports.hasPrevious() && paging != null)
            paging.setPrevious(type + "?page=" + (page - 1));

        return paging;
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
