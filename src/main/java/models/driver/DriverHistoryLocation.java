package models.driver;

import annotation.TableBind;
import base.models.BaseDriverHistoryLocation;
import dto.portDto.LongitudeAndLatitude;
import plugin.sqlInXml.SqlManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by BOGONj on 2016/9/4.
 */
@TableBind(tableName = "dele_driver_history_location")
public class DriverHistoryLocation extends BaseDriverHistoryLocation<DriverHistoryLocation> {
    public static DriverHistoryLocation dao = new DriverHistoryLocation();

    /**
     * 通过司机id查询某段时间的位置
     *
     * @param driver
     * @param start
     * @param end
     * @return
     */
    public List<DriverHistoryLocation> findByDriverAndTime(Integer driver, Date start, Date end) {
        return find(SqlManager.sql("driverHistoryLocation.findByDriverAndTime"), driver, start, end);
    }

    public List<DriverHistoryLocation> findByDriverAndTime1(Integer driver, Date start, Date end) {
        return find(SqlManager.sql("driverHistoryLocation.findByDriverAndTime1"), driver, start, end);
    }

    /**
     * 运营证接口  获取位置的经纬度
     * @param driver
     * @param time
     * @return
     */
    public   LongitudeAndLatitude findByDriverAndTime(Integer driver,String time){

        DriverHistoryLocation  dep =findFirst("SELECT longitude,latitude FROM `dele_driver_history_location`  WHERE driver ="+driver+" AND recovice_time BETWEEN '"+time+"' AND '"+dateAddHour(time,1)+"'  LIMIT 1 ");
        if(dep==null ){
            return  null;
        }
        LongitudeAndLatitude longitudeAndLatitude = new LongitudeAndLatitude();
        if(dep !=null ) {
            longitudeAndLatitude.setLongitude(dep.getLongitude() == null ? 0 : dep.getLongitude() / 1E-6);
            longitudeAndLatitude.setLatitude(dep.getLatitude() == null ? 0 : dep.getLatitude() / 1E-6);
        }

        return longitudeAndLatitude;
    }


    /**
     * 运营证接口  获取位置的经纬度
     * @param driver
     * @param time
     * @return
     */
    public   List<LongitudeAndLatitude> findByDriverAndTimeToList(Integer driver,String time,String time2){
        List<LongitudeAndLatitude> list = new ArrayList<LongitudeAndLatitude>();
        List<DriverHistoryLocation>  dep =find("SELECT longitude,latitude,recovice_time,speed,orientation FROM  `dele_driver_history_location`  WHERE driver=" + driver + " AND recovice_time  BETWEEN  '" + time + "' AND '" + time2 + "' ");
        if(dep==null ){
            return  null;
        }
        for(DriverHistoryLocation location:dep){
            LongitudeAndLatitude longitudeAndLatitude = new LongitudeAndLatitude();
            if(dep !=null ) {
                longitudeAndLatitude.setLongitude(location.getLongitude() == null ? 0 : location.getLongitude() / 1E-6);
                longitudeAndLatitude.setLatitude(location.getLatitude() == null ? 0 : location.getLatitude() / 1E-6);
                longitudeAndLatitude.setRecoviceTime(location.getRecoviceTime().getTime()/1000);//取秒值
                longitudeAndLatitude.setSpeed(location.getSpeed());
                longitudeAndLatitude.setOrientation(location.getOrientation());
            }
            list.add(longitudeAndLatitude);
        }
        return list;
    }


    public  List<DriverHistoryLocation>  findDataAddA48(String orderId){
        return find(SqlManager.sql("driverHistoryLocation.findDataAddA48"),orderId, orderId, orderId);
    }

    /**
     * 时间加小时
     * @param datestr
     * @param h
     * @return
     */
    private   String dateAddHour(String datestr,int h ){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(datestr+":00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar ca=Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, h);
        return  sdf.format(ca.getTime());


    }



}
