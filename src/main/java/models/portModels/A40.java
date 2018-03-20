package models.portModels;


import utils.DateUtil;
import utils.Random;
import utils.StringUtil;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA40;
import dto.portDto.LongitudeAndLatitude;
import models.driver.DriverHistoryLocation;
import models.driver.DriverInfo;
import models.driver.DriverOnlineDetail;

import java.util.List;

/**
 * 下线
 */
@TableBind(tableName = "A40")
public class A40 extends BaseA40<A40> {
    public static final A40 dao = new A40();

    public List<A40> findCompany() {
        return find("Select * from A40");
    }

    /**
     * 添加数据
     */
    public void add() {
        List<DriverOnlineDetail> details = DriverOnlineDetail.dao.findDataByA38AndA40(0);

        for (DriverOnlineDetail detail : details) {
            try {
                A40 a40 = new A40();
                DriverInfo driverInfo = DriverInfo.dao.findDataById(detail.getDriver());
                if (driverInfo.getCertificateNo() != null) {
                    LongitudeAndLatitude dep = DriverHistoryLocation.dao.findByDriverAndTime(detail.getDriver(), DateUtil.dateFmt(detail.getOperatTime(), "yyyy-MM-dd HH")); // 位置
                    if (dep != null) {
                        a40.setLicenseId(driverInfo.getCertificateNo());//驾驶证
                        a40.setVehicleNo(detail.get("VehicleNo").toString()); //车牌
                        a40.setLogoutTime(DateUtil.dateToLong(detail.getOperatTime()));//下线时间
                        a40.setLongitude(new Long(Constant.DCMFMT.format(dep.getLongitude())));//上线经度
                        a40.setLatitude(new Long(Constant.DCMFMT.format(dep.getLatitude())));//上线纬度
                        a40.setEncrypt(0);//坐标加密标准
                        a40.save();
                        System.out.println("--------------------->>>>>>>>>>>>>>>>>>>ok");
                    }

                }
            } catch (Exception e) {
                System.out.println("------------------->>>>>>车辆下线接口插入失败！" + detail.getDriver());
            }

        }


    }


    public void addTestData() {
        List<A40> list = findCompany();
        List<A22> driverInfo = A22.dao.findDriverInfo();
        int ss = 10;
        int count=0;
        for (int i = 0; i < 700; i++) {
            if (i == driverInfo.size()) {
                i = 0;
            }
            A40 a40 = new A40();
            a40.setLicenseId(driverInfo.get(i).getLicenseId());//驾驶证
            a40.setVehicleNo(driverInfo.get(i).get("VehicleNo").toString()); //车牌
            a40.setLogoutTime(list.get(0).getLogoutTime());//下线时间
            a40.setLongitude(list.get(0).getLongitude() + ss);//上线经度
            a40.setLatitude(list.get(0).getLatitude() + ss);//上线纬度
            a40.setEncrypt(0);//坐标加密标准
             a40.save();
            System.out.println("--------------------->>>>>>>>>>>>>>>>>>>ok");
            ss += 22;
            count++;
            if(count==700){

                return;
            }
        }


    }

    public void updateTest() {
        List<A40> a40s = findCompany();

        for (A40 a40:a40s){

            a40.setLogoutTime(new Long("201707" + Random.getRandNum3(1, 1)+Random.getRandNum3(9, 1)+"03"+ Random.getRandNum3(5, 4)));
            a40.update();
            System.out.println("+++++++++++++123");

        }

    }


    public void Test() {

        List<A40> list = findCompany();

        for (A40 a46 : list) {



            boolean b=    DateUtil.isValidDate(StringUtil.stringToDate(a46.getLogoutTime().toString()));

            if(!b){

                System.out.println(a46.getLicenseId() + "            ++++++  ");
            }





        }

    }

}



