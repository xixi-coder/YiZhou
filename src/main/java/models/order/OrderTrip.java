package models.order;

import utils.DateUtil;
import annotation.TableBind;
import base.models.BaseTrip;
import dto.portDto.LongitudeAndLatitude;

/**
 * Created by BOGONj on 2016/9/7.
 */
@TableBind(tableName = "dele_trip", pks = "order_id")
public class OrderTrip extends BaseTrip<OrderTrip> {
    public static OrderTrip dao = new OrderTrip();

    public LongitudeAndLatitude findByDriverAndTime(Integer orderId){

        OrderTrip dep =findFirst(" SELECT  *  FROM  dele_trip WHERE  order_id=  "+orderId);
        if(dep==null ){
            return  null;
        }
        LongitudeAndLatitude longitudeAndLatitude = new LongitudeAndLatitude();
        if(dep !=null ) {
            longitudeAndLatitude.setLongitude(dep.getStartLongitude() == null ? 0 : dep.getStartLongitude() / 1E-6);
            longitudeAndLatitude.setLatitude(dep.getStartLatitude() == null ? 0 : dep.getStartLatitude() / 1E-6);
            longitudeAndLatitude.setLongitudeEnd(dep.getEndLongitude() == null ? 0 : dep.getEndLongitude() / 1E-6);
            longitudeAndLatitude.setLatitudeEnd(dep.getEndLatitude() == null ? 0 : dep.getEndLatitude() / 1E-6);
            longitudeAndLatitude.setStartTime(dep.getStartTime()==null ?null:DateUtil.dateToLong(dep.getStartTime()));
            longitudeAndLatitude.setEndTime(dep.getEndTime()==null?null:DateUtil.dateToLong(dep.getEndTime()));
        }

        return longitudeAndLatitude;
    }



}
