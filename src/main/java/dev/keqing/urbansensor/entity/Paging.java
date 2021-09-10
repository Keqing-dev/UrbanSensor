package dev.keqing.urbansensor.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.keqing.urbansensor.config.GeneralConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class Paging {

    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private Paging paging;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String previous;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String next;

    public Paging toPagination(Page reports, int page, String type) {
        Paging paginated = null;

        if (reports.hasPrevious() || reports.hasNext())
            paginated = paging;

        if (reports.hasNext() && paginated != null)
            paginated.setNext(type + "?page=" + (page + 1));

        if (reports.hasPrevious() && paginated != null)
            paginated.setPrevious(type + "?page=" + (page - 1));

        return paginated;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = generalConfig.getDomainName() + previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = generalConfig.getDomainName() + next;
    }

}
