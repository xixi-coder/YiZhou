package models.portModels;


import utils.DateUtil;
import utils.Random;
import utils.StringUtil;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA38;
import dto.portDto.LongitudeAndLatitude;
import models.driver.DriverHistoryLocation;
import models.driver.DriverInfo;
import models.driver.DriverOnlineDetail;

import java.util.List;

/**
 * 车辆上线接口
 */
@TableBind(tableName = "A38")
public class A38 extends BaseA38<A38> {
    public static final A38 dao = new A38();

    public List<A38> findCompany() {
        return find("Select * from A38");
    }

    /**
     * 添加数据
     */
    public void add() {
        List<DriverOnlineDetail> details = DriverOnlineDetail.dao.findDataByA38AndA40(1);

        for (DriverOnlineDetail detail : details) {
            try {
                A38 a38 = new A38();
                DriverInfo driverInfo = DriverInfo.dao.findDataById(detail.getDriver());
                if (driverInfo.getCertificateNo() != null) {
                    LongitudeAndLatitude dep = DriverHistoryLocation.dao.findByDriverAndTime(detail.getDriver(), DateUtil.dateFmt(detail.getOperatTime(), "yyyy-MM-dd HH")); // 位置
                    if (dep != null) {
                        a38.setLicenseId(driverInfo.getCertificateNo());//驾驶证
                        a38.setVehicleNo(detail.get("VehicleNo").toString()); //车牌
                        a38.setLoginTime(DateUtil.dateToLong(detail.getOperatTime()));//上线时间
                        a38.setLongitude(new Long(Constant.DCMFMT.format(dep.getLongitude())));//上线经度
                        a38.setLatitude(new Long(Constant.DCMFMT.format(dep.getLatitude())));//上线纬度
                        a38.setEncrypt(0);//坐标加密标准
                        a38.save();
                        System.out.println("--------------------->>>>>>>>>>>>>>>>>>>ok");
                    }

                }
            } catch (Exception e) {
                System.out.println("------------------->>>>>>车辆上线接口插入失败！" + detail.getDriver());
            }

        }


    }


    public void addTestData() {
        List<A38> list = findCompany();
        List<A22> driverInfo = A22.dao.findDriverInfo();
        int ss = 10;
        int count = 0;
        for (int i = 0; i < 700; i++) {
            if(i==driverInfo.size()){
                i=0;
            }
            A38 a38 = new A38();
            a38.setLicenseId(driverInfo.get(i).getLicenseId());//驾驶证
            a38.setVehicleNo(driverInfo.get(i).get("VehicleNo").toString()); //车牌
            a38.setLoginTime(list.get(0).getLoginTime());//上线时间
            a38.setLongitude(list.get(0).getLongitude() + ss);//上线经度
            a38.setLatitude(list.get(0).getLatitude() + ss);//上线纬度
            a38.setEncrypt(0);//坐标加密标准
            a38.save();
            System.out.println("--------------------->>>>>>>>>>>>>>>>>>>ok");
            ss += 2;
            count++;
            if(count ==700){
                return;
            }
        }

    }


    public void updateTest() {
        List<A38> a38s = findCompany();

        for (A38 a38:a38s){

            a38.setLoginTime(new Long("201707" + Random.getRandNum3(1, 1)+Random.getRandNum3(9, 1)+"09"+ Random.getRandNum3(5, 4)));
            a38.update();
            System.out.println("+++++++++++++123");

        }

    }



    public void Test() {

        List<A38> list = findCompany();

        for (A38 a46 : list) {



            boolean b=    DateUtil.isValidDate(StringUtil.stringToDate(a46.getLoginTime().toString()));

            if(!b){

                System.out.println(a46.getLicenseId() + "            ++++++  ");
            }





        }

    }
}


