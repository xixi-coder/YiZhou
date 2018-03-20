package utils;

import org.junit.Test;

public class DistanceDome {
    
    public class LatLng {
        public Double latitude;
        public Double longitude;
        
        public LatLng(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
    
    /**
     * 计算两点之间距离
     *
     * @param start
     * @param end
     * @return 米
     */
    public double getDistance(LatLng start, LatLng end) {
        double lat1 = (Math.PI / 180) * start.latitude;
        double lat2 = (Math.PI / 180) * end.latitude;
        
        double lon1 = (Math.PI / 180) * start.longitude;
        double lon2 = (Math.PI / 180) * end.longitude;
        
        //地球半径
        double R = 6371;
        
        //两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        
        return d * 1000;
    }
    
    @Test
    public void test() {
        LatLng start = new LatLng(39.95676, 116.401394);
        LatLng end = new LatLng(39.93014, 116.409574);
        System.out.println(getDistance(start, end));
    }
    
}
