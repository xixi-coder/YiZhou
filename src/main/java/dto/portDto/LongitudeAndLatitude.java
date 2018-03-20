package dto.portDto;


import java.text.DecimalFormat;

/**
 * GPS 经纬度
 * Created by Administrator on 2017/7/17.
 */
public class LongitudeAndLatitude {

    DecimalFormat dcmFmt = new DecimalFormat("0");

    private Double longitude;
    private Double latitude;
    private Double longitudeEnd;
    private Double latitudeEnd;
    private Double speed;
    private Double orientation;
    private Long recoviceTime;

    private Long startTime;
    private Long endTime;

    public Double getLongitude() {
        return Double.valueOf(dcmFmt.format(longitude));
    }

    public void setLongitude(Double longitude) {
        this.longitude =  Double.valueOf(dcmFmt.format(longitude));
    }

    public Double getLatitude() {
        return Double.valueOf(dcmFmt.format(latitude));
    }

    public void setLatitude(Double latitude) {
        this.latitude =  Double.valueOf(dcmFmt.format(latitude));;
    }

    public Long getRecoviceTime() {
        return recoviceTime;
    }

    public void setRecoviceTime(Long recoviceTime) {
        this.recoviceTime = recoviceTime;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getOrientation() {
        return orientation;
    }

    public void setOrientation(Double orientation) {
        this.orientation = orientation;
    }

    public Double getLongitudeEnd() {
        return longitudeEnd;
    }

    public void setLongitudeEnd(Double longitudeEnd) {
        this.longitudeEnd = longitudeEnd;
    }

    public Double getLatitudeEnd() {
        return latitudeEnd;
    }

    public void setLatitudeEnd(Double latitudeEnd) {
        this.latitudeEnd = latitudeEnd;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
