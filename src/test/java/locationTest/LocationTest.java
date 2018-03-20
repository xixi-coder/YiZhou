package locationTest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.math.BigDecimal;

public class LocationTest {
    
    @Test
    public void location() {
        String str = "[{\"latitude\":31.74644287109375,\"longitude\":31.74644287109375}]";
        
        JSONArray array = JSONObject.parseArray(str);
    
        HistoricalLocationTest.traceLatLng traceLatLng = new HistoricalLocationTest.traceLatLng();
        for (int i = 0; i < array.size(); i++) {
            traceLatLng = array.getObject(i, HistoricalLocationTest.traceLatLng.class);
            
        }
        
        System.out.println(traceLatLng.getLatitude());
    }
    
    @Test
    public  void  tttt(){
        BigDecimal num1 = new BigDecimal("-55");
        BigDecimal num2 = new BigDecimal("-50");
        System.out.println(!(num2.compareTo(num1)<1));
    }
}
