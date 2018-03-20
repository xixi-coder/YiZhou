package controllers.api;

import annotation.Controller;
import base.Constant;
import base.controller.BaseApiController;
import com.google.common.base.Strings;
import com.jfinal.core.ActionKey;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import models.company.Company;
import models.member.MemberInfo;
import services.CalService;
import utils.DateUtil;

import java.math.BigDecimal;

@Controller("/api/BSJ/order")
public class BSJController extends BaseApiController {
    private static Log logger = LogFactory.get();

    /**
     * @api {post} /api/BSJ/order/calamount  获取包司机预估价格
     * @apiGroup D_OrderController
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {String} type  服务项目的大项类型
     * @apiParam {String} cityCode     城市code
     * @apiParam {String} setOutTime   预约时间
     * @apiParam {String} time   大约用时
     * @apiParam {String} distance  距离
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "code": 200,
     * "data": {},
     * "message": "success",
     * "isSuccess": true
     * }
     */
    @ActionKey("/api/BSJ/order/calamount")
    public void calAmount() {
        MemberInfo memberInfo = null;
        if (getLoginMember() != null) {
            memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
        }
        String cityCode = getPara("cityCode");
        Long strSetOutTime = getParaToLong("setOutTime", System.currentTimeMillis());
        String setOutTime = null;
        if (t(strSetOutTime)) {
            setOutTime = DateUtil.stampToDate(strSetOutTime);
        }
        BigDecimal distance = new BigDecimal(getPara("distance"));
        Company company;
        if (Strings.isNullOrEmpty(cityCode)) {
            company = Company.dao.findByCompanyId(memberInfo.getCompany());
        } else {
            company = Company.dao.findByCity(cityCode);
        }
        if (company == null) {
            renderAjaxFailure("该地区无法使用！");
            return;
        }
        BigDecimal times = new BigDecimal(getPara("time") == null ? "0" : getPara("time"));
        //时间和距离 不到最低标准 就用4小时的来算
        if (times.compareTo(Constant.BSJ.MINIMUM_STANDARDS_TIME) == -1 || distance.compareTo(Constant.BSJ.MINIMUM_STANDARDS_DISTANCE) == -1) {
            times = Constant.BSJ.MINIMUM_STANDARDS_TIME;
            distance = Constant.BSJ.MINIMUM_STANDARDS_DISTANCE;
        } else if (times.compareTo(Constant.BSJ.MINIMUM_STANDARDS_TIME) == 1 || distance.compareTo(Constant.BSJ.MINIMUM_STANDARDS_DISTANCE) == 1) {
            //时间和距离 超过最低标准 就用一天的来算
            times = Constant.BSJ.ONE_DAY_TIME;
            distance = Constant.BSJ.ONE_DAY_TIME_DISTANCE;
        }
        renderAjaxSuccess(CalService.getInstance().calculationDtoSetUp1(Constant.ServiceType.DaiJia, company, memberInfo, setOutTime, distance, times));
    }
}
