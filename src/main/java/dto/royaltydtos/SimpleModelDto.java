package dto.royaltydtos;

import java.io.Serializable;

/**
 * Created by BOGONj on 2016/9/13.
 */
public class SimpleModelDto implements Serializable {
    private String startTime;
    private String endTime;
    private String biLiShaoYuYuan;
    private String biLiTiCheng;
    private String biLiTiYuan;
    private String guShaoYuYuan;
    private String guTiCheng;
    private String guTiYuan;
    private String type;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBiLiShaoYuYuan() {
        return biLiShaoYuYuan;
    }

    public void setBiLiShaoYuYuan(String biLiShaoYuYuan) {
        this.biLiShaoYuYuan = biLiShaoYuYuan;
    }

    public String getBiLiTiCheng() {
        return biLiTiCheng;
    }

    public void setBiLiTiCheng(String biLiTiCheng) {
        this.biLiTiCheng = biLiTiCheng;
    }

    public String getBiLiTiYuan() {
        return biLiTiYuan;
    }

    public void setBiLiTiYuan(String biLiTiYuan) {
        this.biLiTiYuan = biLiTiYuan;
    }

    public String getGuShaoYuYuan() {
        return guShaoYuYuan;
    }

    public void setGuShaoYuYuan(String guShaoYuYuan) {
        this.guShaoYuYuan = guShaoYuYuan;
    }

    public String getGuTiCheng() {
        return guTiCheng;
    }

    public void setGuTiCheng(String guTiCheng) {
        this.guTiCheng = guTiCheng;
    }

    public String getGuTiYuan() {
        return guTiYuan;
    }

    public void setGuTiYuan(String guTiYuan) {
        this.guTiYuan = guTiYuan;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
