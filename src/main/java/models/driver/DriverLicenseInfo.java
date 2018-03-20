package models.driver;

import annotation.TableBind;
import base.models.BaseDriverLicenseInfo;
import plugin.sqlInXml.SqlManager;

/**
 * Created by BOGONj on 2016/9/20.
 */
@TableBind(tableName = "dele_driver_license_info")
public class DriverLicenseInfo extends BaseDriverLicenseInfo<DriverLicenseInfo> {
    public static DriverLicenseInfo dao = new DriverLicenseInfo();

    public DriverLicenseInfo findByDriver(int driver) {
        return findFirst(SqlManager.sql("driverLicenseInfo.findByDriver"), driver);
    }
}
