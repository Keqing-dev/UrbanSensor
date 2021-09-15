package dev.keqing.urbansensor.entity;


import dev.keqing.urbansensor.config.GeneralConfig;
import dev.keqing.urbansensor.utils.FileType;
import org.springframework.beans.factory.annotation.Autowired;


public class Thumbnails {

    String xs;
    String sm;
    String md;
    String lg;
    String xl;
    String xxl;

    int xsW = 40;
    int smW = 60;
    int mdW = 170;
    int lgW = 180;
    int xlW = 380;
    int xxlW = 1300;


    public Thumbnails(String filename, FileType fileType) {
        if (filename == null) return;
        if (filename.contains("https://")) {
            this.xs = filename;
            this.sm = filename;
            this.md = filename;
            this.lg = filename;
            this.xl = filename;
            this.xxl = filename;
        } else {
            this.xs = "https://urban.keqing.dev/" + "uploads/thumbnail/" + fileType + "/" + filename + "?width=" + xsW;
            this.sm = "https://urban.keqing.dev/" + "uploads/thumbnail/" + fileType + "/" + filename + "?width=" + smW;
            this.md = "https://urban.keqing.dev/" + "uploads/thumbnail/" + fileType + "/" + filename + "?width=" + mdW;
            this.lg = "https://urban.keqing.dev/" + "uploads/thumbnail/" + fileType + "/" + filename + "?width=" + lgW;
            this.xl = "https://urban.keqing.dev/" + "uploads/thumbnail/" + fileType + "/" + filename + "?width=" + xlW;
            this.xxl = "https://urban.keqing.dev/" + "uploads/thumbnail/" + fileType + "/" + filename + "?width=" + xxlW;
        }
    }

    public String getXs() {
        return xs;
    }

    public void setXs(String xs) {
        this.xs = xs;
    }

    public String getSm() {
        return sm;
    }

    public void setSm(String sm) {
        this.sm = sm;
    }

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }

    public String getLg() {
        return lg;
    }

    public void setLg(String lg) {
        this.lg = lg;
    }

    public String getXxl() {
        return xxl;
    }

    public void setXxl(String xxl) {
        this.xxl = xxl;
    }

    public String getXl() {
        return xl;
    }

    public void setXl(String xl) {
        this.xl = xl;
    }
}
