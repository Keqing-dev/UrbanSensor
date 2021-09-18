package dev.keqing.urbansensor.response;

public abstract class DataResponse<T> {
    private boolean status;
    private T data;

    public DataResponse(boolean status, T data) {
        this.status = status;
        this.data = data;
    }

    public DataResponse() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
