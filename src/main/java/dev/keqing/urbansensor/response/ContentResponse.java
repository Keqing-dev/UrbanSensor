package dev.keqing.urbansensor.response;

import dev.keqing.urbansensor.entity.Paging;

public abstract class ContentResponse<T> {
    private boolean status;
    private T content;
    private Paging paging;

    public ContentResponse(boolean status, T content, Paging paging) {
        this.status = status;
        this.content = content;
        this.paging = paging;
    }

    public ContentResponse(boolean status, T content) {
        this.status = status;
        this.content = content;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
