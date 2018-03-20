package models.portModels;


import utils.DateUtil;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA16;
import models.car.CarInfo;

import java.util.Date;
import java.util.List;

/**
 * Generated by JFinal.
 */
@TableBind(tableName = "A16")
public class A16 extends BaseA16<A16> {
	public static final A16 dao = new A16();
	public List<A16> findCompany(){
		List list = find("SELECT * FROM a16");
		return  list;
	}

	public A16 findCar(String VehicleNo){
		return   findFirst("SELECT * FROM a16 where VehicleNo = '"+VehicleNo+"' ");

	}

	public void setInfos(){
		List<CarInfo> carInfos = CarInfo.dao.findCarInfos();
		for (int i = 0;i<carInfos.size();i++){
			CarInfo carInfo = carInfos.get(i);
			A16 a16 = new A16();
			try {
				a16.setVehicleNo((String) carInfo.get("VehicleNo"));
				a16.setPlateColor((String) carInfo.get("PlateColor"));
//				int seats = carInfo.get("Seats",5);
				a16.setSeats(5);
				a16.setBrand((String) carInfo.get("Brand"));
				a16.setModel((String) carInfo.get("Model"));
				a16.setVehicleType((String) carInfo.get("VehicleType"));
				a16.setOwnerName((String) carInfo.get("OwnerName"));
				a16.setVehicleColor((String) carInfo.get("VehicleColor"));
				a16.setEngineId((String) carInfo.get("EngineId"));
				a16.setVIN((String) carInfo.get("VIN"));
				a16.setCertifyDateA(DateUtil.dateToLongYMD((Date) carInfo.get("CertifyDateA")));
				a16.setFuelType((String) carInfo.get("FuelType"));
				a16.setEngineDisplace((String) carInfo.get("EngineDisplace"));
				//a16.setPhotoId((String) carInfo.get("PhotoId"));
				a16.setCertificate((String) carInfo.get("Certificate"));
				a16.setTransAgency((String) carInfo.get("TransAgency"));
				a16.setTransArea((String) carInfo.get("TransArea"));
				a16.setTransDateStart(DateUtil.dateToLongYMD((Date) carInfo.get("TransDateStart")));
				a16.setTransDateStop(DateUtil.dateToLongYMD((Date) carInfo.get("TransDateStop")));
				a16.setCertifyDateB(DateUtil.dateToLongYMD((Date) carInfo.get("CertifyDateB")));
				int g = carInfo.get("FixState");
				switch (g){
					case 1 : a16.setFixState("1");
						break;

					case 2 : a16.setFixState("2");
						break;

					case 0 : a16.setFixState("0");
						break;

					default : a16.setFixState("1");
						break;
				}

				int g1 = carInfo.get("CheckState");
				switch (g1){
					case 1 : a16.setCheckState("1");
						break;

					case 2 : a16.setCheckState("2");
						break;

					case 0 : a16.setCheckState("0");
						break;

					default:a16.setCheckState("1");
						break;
				}
				int t = carInfo.get("NextFixDate","20180531");
				a16.setNextFixDate((long) t);
				a16.setFeePrintId((String) carInfo.get("FeePrintId"));
				a16.setGPSBrand((String) carInfo.get("GPSBrand"));
				a16.setGPSModel((String) carInfo.get("GPSModel"));
				a16.setGPSIMEI((String) carInfo.get("GPSIMEI"));
				a16.setGPSInstallDate(DateUtil.dateToLongYMD((Date) carInfo.get("GPSinstallDate")));
				a16.setRegisterDate(DateUtil.dateToLongYMD((Date) carInfo.get("RegisterDate")));
				a16.setCommercialType((Integer) carInfo.get("CommercialType"));
				a16.setFareType("1");
				a16.setState(0);
				a16.setFlag(Constant.Flag.ADD);
				a16.setUpdateTime(DateUtil.UpdateTime());
				a16.save();
			}catch (Exception e){
				System.out.println("车辆所有人:"+carInfo.get("OwnerName")+" 插入表A16失败");
			}
		}
	}
}
