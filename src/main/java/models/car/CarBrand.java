package models.car;

import annotation.TableBind;
import base.Constant;
import base.models.BaseCarBrand;
import base.models.BaseCarModel;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.common.base.Strings;
import kits.StringsKit;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
@TableBind(tableName = "dele_car_brand")
public class CarBrand extends BaseCarBrand<CarBrand> {
    public static CarBrand dao = new CarBrand();

    /**
     * 查询汽车的品牌
     *
     * @param start
     * @param pageSize
     * @return
     */
    public List<CarBrand> findByPage(int start, int pageSize, String name) {
        if (!Strings.isNullOrEmpty(name)) {
            name = "%" + name + "%";
            return find(StringsKit.replaceSql(SqlManager.sql("carBrand.findByPage"), " AND (name LIKE ? OR description LIKE ?)"), name, name, start, pageSize);
        } else {
            return find(SqlManager.sql("carBrand.findByPage"), start, pageSize);
        }

    }

    public List<CarBrand> findAll() {
        return find(SqlManager.sql("carBrand.findAll"));
    }

    public int findByCount(String name, int id) {
        return findFirst(SqlManager.sql("carBrand.findByCount"), name, id).getNumber("c").intValue();
    }

    public CarBrand findByName(String name) {
        return findFirst(SqlManager.sql("carBrand.findByName"), name);
    }

    public void setFristPy(String name) {
        try {
            String fristPy = PinyinHelper.getShortPinyin(name);
            if (fristPy.length() > 1) {
                super.setFristPy(fristPy.substring(0, 1));
            }
        } catch (PinyinException e) {
            e.printStackTrace();
        }
    }
}
