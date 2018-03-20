package dto.location;

import java.io.Serializable;
import java.util.List;

public class HistoricalLocation implements Serializable {
    
    private static final long serialVersionUID = 2623767388982552505L;
    private Integer loginId;
    private List<traceLatLng> traceLatLngList;
    
    public static class traceLatLng {
        private Double latitude;
        private Double longitude;
        
        public Double getLatitude() {
            return latitude;
        }
        
        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }
        
        public Double getLongitude() {
            return longitude;
        }
        
        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }
    
    public Integer getLoginId() {
        return loginId;
    }
    
    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }
    
    public List<traceLatLng> getTraceLatLngList() {
        return traceLatLngList;
    }
    
    public void setTraceLatLngList(List<traceLatLng> traceLatLngList) {
        this.traceLatLngList = traceLatLngList;
    }
    
}
