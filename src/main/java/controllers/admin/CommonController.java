package controllers.admin;

import annotation.Controller;
import base.controller.BaseController;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import dto.royaltydtos.MoneyModelDto;
import models.car.CarModel;
import models.sys.Area;
import models.sys.User;

import java.util.List;

/**
 * Created by BOGONm on 16/8/13.
 */
@Controller("/admin/common")
public class CommonController extends BaseController {
    public void portrait() {
        String username = getPara("username");
        User user = User.dao.findByUserName(username);
        if (user != null && user.getHeadPortrait() != null)
            renderAjaxSuccess(user.getHeadPortrait());
        else
            renderAjaxFailure("");
    }

    /**
     * 获取省市县
     */
    public void area() {
        String parent = getPara("parent");
        String level = getPara("level");
        List<Area> areaList = Lists.newArrayList();
        if (Strings.isNullOrEmpty(parent) && Strings.isNullOrEmpty(level)) {
            areaList = Area.dao.findByLevelAndParent("province", 0 + "");
        } else {
            areaList = Area.dao.findByLevelAndParent(level, parent);
        }
        renderAjaxSuccess(areaList);
    }

    public void car() {
        int brand = getParaToInt("brand", 0);
        List<CarModel> carModels = CarModel.dao.findByBrand(brand);
        renderAjaxSuccess(carModels);
    }

    public static void main(String[] args) {
        MoneyModelDto moneyModelDto = new MoneyModelDto();
        moneyModelDto.setStartTime("123");
        MoneyModelDto b = moneyModelDto;
        moneyModelDto.setStartTime("3333");
        System.out.printf(moneyModelDto.getStartTime() + ":" + b.getStartTime());
    }


}
