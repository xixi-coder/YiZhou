package models.car;

import annotation.TableBind;
import base.Constant;
import base.models.BaseCarModel;
import com.google.common.base.Strings;
import kits.StringsKit;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
@TableBind(tableName = "dele_car_model")
public class CarModel extends BaseCarModel<CarModel> {
    public static CarModel dao = new CarModel();

    /**
     * 通过品牌查询车辆型号
     *
     * @param brandId
     * @param start
     * @param pageSize
     * @return
     */
    public List<CarModel> findByBrandAndPage(int brandId, int start, int pageSize, String name) {
        if (!Strings.isNullOrEmpty(name)) {
            name = "%" + name + "%";
            return find(StringsKit.replaceSql(SqlManager.sql("carModel.findByBrandAndPage")," AND (name LIKE ? OR description LIKE ?)"), brandId, name, name, start, pageSize);
        } else {
            return find(SqlManager.sql("carModel.findByBrandAndPage"), brandId, start, pageSize);
        }
    }

    public List<CarModel> findAll() {
        return find(SqlManager.sql("carModel.findAll"));
    }

    public List<CarModel> findByBrand(int brand) {
        return find("select * from dele_car_model where brand = ?", brand);
    }


    public int findByCount(String name, int id) {
        return findFirst("select count(1) c from dele_car_model where name = ? and id != ? limit 1", name, id).getNumber("c").intValue();
    }

    public CarModel findByName(String name) {
        return findFirst("select * from dele_car_model where name = ? limit 1", name);
    }
}
