package controllers.api;

import annotation.Controller;
import base.Constant;
import base.controller.BaseApiController;
import models.sys.Area;
import models.sys.ZxLine;
import services.CalZxLineService;
import utils.DateUtil;

import java.math.BigDecimal;
import java.util.List;

@Controller("/api/zxLine")
public class ZxLineController extends BaseApiController {

    /**
     * @api {get} /api/zxLine/getzxLine  （会员接口）判断当前地区可用专线
     * @apiGroup Z_zxLineController
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {number} serviceType   服务类型（6:城际专线 56：接机 57：送机）
     * @apiParam {String} cityCode  城市code
     * @apiSuccessExample Success-Response:
     * HTTP/1.1
     * {
     * "status", "OK",
     * "data", data,
     * "msg", message,
     * }
     * @apiSuccess {number} id 线路ID
     * @apiSuccess {string} startAddress 展示起点名称
     * @apiSuccess {string} endAddress 展示终点名称
     * @apiSuccess {string} startCode 起点CityCode
     * @apiSuccess {String} endCode 终点CityCode
     * @apiSuccess {String} endCityName 终点城市名称
     */
    public void getzxLine() {
        String adCode = getPara("cityCode");
        int serviceType = getParaToInt("serviceType");
        int serviceTypeItem;
        if (serviceType == Constant.ServiceType.ZhuanXian) {
            serviceTypeItem = Constant.ServiceItemType.KuaChengZhuanXian;
        }else {
            serviceTypeItem = serviceType;
        }
        List<ZxLine> lines = ZxLine.dao.findZxLineList(serviceTypeItem, adCode);

        if (lines.size() > 0){
            Area area = Area.dao.findByAdCode(adCode);
            List<ZxLine> line1 = ZxLine.dao.findZxLineList(serviceTypeItem,area.getParent());
            if (line1.size() > 0){
                lines.addAll(line1);
            }
        }
        if (lines.size() <= 0){
            Area area = Area.dao.findByAdCode(adCode);
            lines = ZxLine.dao.findZxLineList(serviceTypeItem,area.getParent());//最多到市，不能以省为起点
        }
        renderAjaxSuccess(lines, "获取专线列表成功！");
    }

    /**
     * @api {get} /api/zxLine/getzxLinePrice  （会员接口）获取预估价格
     * @apiGroup Z_zxLineController
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {number} zxLineId   专线线路ID
     * @apiParam {number} pdFlag 是否拼车1、拼车 0、包车
     * @apiParam {number} people 拼车人数
     * @apiParam {number} distance 距离
     * @apiParam {String} setOutTime 预约时间
     * @apiParam {String} cityCode 城市Code
     * @apiSuccessExample Success-Response:
     * HTTP/1.1
     * {
     * "status", "OK",
     * "data", data,
     * "msg", message,
     * }
     * @apiSuccess {number} baseAmount 专线超出基础公里数之外的 起步费用
     * @apiSuccess {number} minAmount 专线超出基础公里数之外的 最低消费
     * @apiSuccess {number} distanceAmount 专线超出基础公里数之外的 里程费用
     * @apiSuccess {number} timeAmount 专线超出基础公里数之外的 超时费用
     * @apiSuccess {number} waitAmount 专线超出基础公里数之外的 等待费用
     * @apiSuccess {number} pdFlag 专线超出基础公里数之外的 是否拼车
     * @apiSuccess {number} discount 专线超出基础公里数之外的 折扣
     * @apiSuccess {number} typeId 专线类型
     * @apiSuccess {number} zxLinePrice 专线线路费用
     * @apiSuccess {number} totalAmount 总费用
     */
    public void getzxLinePrice() {
        int id = getParaToInt("zxLineId");
        int sharingFlag = getParaToInt("pdFlag", 0);//1、拼车 0、包车
        int peopleNumber = getParaToInt("people", 1);
        String cityCode = getPara("cityCode");
        Long strSetOutTime = getParaToLong("setOutTime", System.currentTimeMillis());
        String setOutTime = DateUtil.stampToDate(strSetOutTime);
        BigDecimal distance = new BigDecimal(getPara("distance"));
        ZxLine zxLine = ZxLine.dao.findById(id);
        renderAjaxSuccess(CalZxLineService.getInstance().calculationDtoSetUp1(zxLine,setOutTime, sharingFlag,peopleNumber,cityCode,distance));
    }
}
