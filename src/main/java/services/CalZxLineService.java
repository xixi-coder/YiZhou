package services;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import base.Constant;
import dto.CalculationDto;
import kits.TimeKit;
import models.sys.ChargeStandard;
import models.sys.ChargeStandardItem;
import models.sys.ChargeStandardMileage;
import models.sys.ServiceTypeItem;
import models.sys.ZxLine;

public class CalZxLineService {
    private Logger logger = LoggerFactory.getLogger(CalZxLineService.class);

    private static BigDecimal pdRebate = BigDecimal.valueOf(8.5).divide(BigDecimal.TEN);//拼车打折
    private static BigDecimal pd9Rebate = BigDecimal.valueOf(9).divide(BigDecimal.TEN);//拼车打折

    /**
     * 计算费用
     *
     * @param chargeStandard
     * @param setOutTime
     * @return
     */

    public BigDecimal calculationWaitAmount(ChargeStandard chargeStandard, BigDecimal times, DateTime setOutTime) {
        ChargeStandardItem chargeStandardItem = ChargeStandardItem.dao.findByChargeStandardAndTime(chargeStandard.getId(), setOutTime);
        BigDecimal amount = BigDecimal.ZERO;
        if (chargeStandard == null || chargeStandardItem == null) {
            logger.error("没有相关时间段的收费标准,在时间上做相应调整");
            chargeStandardItem = ChargeStandardItem.dao.findByChargeStandardAndTime(chargeStandard.getId(), setOutTime.plusMinutes(-1));
        }
        BigDecimal baseMintues = chargeStandardItem.getMianfeidenghoufenzhong();
        if (times.compareTo(baseMintues) <= 0) {
            amount = BigDecimal.ZERO;
        } else {
            if (chargeStandardItem.getDenghoumeiduoshaofengzhong().compareTo(BigDecimal.ZERO) != 0) {
                amount = amount.add(chargeStandardItem.getDenghouchaoguolijijiashoujine());//超过立即加收
                BigDecimal moreThan = times.subtract(baseMintues);//超过的分钟数
                BigDecimal[] than = moreThan.divideAndRemainder(chargeStandardItem.getDenghoumeiduoshaofengzhong());//计算超过了几个计费分钟，还剩几个
                amount = amount.add(chargeStandardItem.getDenghouchaoguojiashoujine().multiply(than[0]));//超过实际的金额
                if (than[1].compareTo(chargeStandardItem.getDenghoudiyuduoshaofenzhong()) > 0) {
                    amount = amount.add(chargeStandardItem.getDenghouchaoguojiashoujine());
                }
            }
        }
        return amount;
    }

    public CalculationDto calculation(ChargeStandard chargeStandard, BigDecimal distance, DateTime setOutTime, BigDecimal price) {
        CalculationDto calculationDto = new CalculationDto();
        ChargeStandardItem chargeStandardItem = ChargeStandardItem.dao.findByChargeStandardAndTime(chargeStandard.getId(), setOutTime);
        if (chargeStandard == null || chargeStandardItem == null) {
            logger.error("没有相关时间段的收费标准,在时间上做相应调整");
            chargeStandardItem = ChargeStandardItem.dao.findByChargeStandardAndTime(chargeStandard.getId(), setOutTime.plusMinutes(-1));
        }
        BigDecimal baseMil = chargeStandardItem.getChaoguogonglishu();//基本公里数
        BigDecimal baseAmount = chargeStandardItem.getBaseAmount();//起步价格
        BigDecimal minAmount = chargeStandardItem.getMinAmount();//最低价格
        BigDecimal chaoGongLiJinE = BigDecimal.ZERO;//超过公里数金额
        BigDecimal chaoShiJinE = BigDecimal.ZERO;//超过实际金额
        BigDecimal milAmount = BigDecimal.ZERO;
        BigDecimal startDistance = BigDecimal.ZERO;//起始公里数

        //基本公里数内价格信息
        List<ChargeStandardMileage> chargeStandardMileageList;
        chargeStandardMileageList = ChargeStandardMileage.dao.findByChargeStandardItemAndMileage(chargeStandardItem.getId(), distance.setScale(0, BigDecimal.ROUND_UP));

        //判断有没有起始公里数
        if (chargeStandardMileageList.size() > 0) {
            if (chargeStandardMileageList.get(0).getJiajiajine().compareTo(BigDecimal.ZERO) == 0) {
                startDistance = chargeStandardMileageList.get(0).getEnd();
            }
        }

        //超过起始公里数但不超过基本公里数
        if (distance.compareTo(baseMil) < 0 && distance.compareTo(startDistance) > 0) {
            for (int i = 0; i < chargeStandardMileageList.size(); i++) {
                //获取设置的每条单价的公里数
                BigDecimal distance2 = chargeStandardMileageList.get(i).getEnd().subtract(chargeStandardMileageList.get(i).getStart());
                //获取最后一公里的公里数
                if (distance.compareTo(distance2) > 0) {
                    distance = distance.subtract(distance2);
                }
                //累加价格
                if (i == chargeStandardMileageList.size() - 1) {
                    milAmount = milAmount.add(chargeStandardMileageList.get(i).getJiajiajine().divide(distance2, 2, BigDecimal.ROUND_HALF_UP).multiply(distance));
                } else {
                    milAmount = milAmount.add(chargeStandardMileageList.get(i).getJiajiajine());
                }
            }
            chaoGongLiJinE = chaoGongLiJinE.add(milAmount);
        }

        //超过基本公里数
        if (distance.compareTo(baseMil) >= 0) {
            BigDecimal moreThan = distance.subtract(baseMil);
            //基本公里数内金额
            for (ChargeStandardMileage chargeStandardMileage : chargeStandardMileageList) {
                milAmount = milAmount.add(chargeStandardMileage.getJiajiajine());
            }
            chaoGongLiJinE = chaoGongLiJinE.add(milAmount);
            //超过基本公里数金额
            if (chargeStandardItem.getMeiduoshaogongli().compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal[] than = moreThan.divideAndRemainder(chargeStandardItem.getMeiduoshaogongli());
                chaoGongLiJinE = chaoGongLiJinE.add(chargeStandardItem.getGonglijiashoujine().multiply(than[0]));//超过公里数金额
                if (than[1].compareTo(chargeStandardItem.getBuzugonglishu()) > 0) {
                    chaoGongLiJinE = chaoGongLiJinE.add(chargeStandardItem.getGonglijiashoujine());
                }
            }
        }

        BigDecimal baseMintues = chargeStandardItem.getQibufenzhong();

        calculationDto.setBaseAmount(baseAmount);
        calculationDto.setMinAmount(minAmount);
        calculationDto.setDistanceAmount(chaoGongLiJinE);
        calculationDto.setTimeAmount(chaoShiJinE);
        calculationDto.setZxLinePrice(price);//专线基础价费用
        if (baseAmount.add(chaoGongLiJinE.add(chaoShiJinE)).compareTo(chargeStandardItem.getMinAmount()) < 0) {//不满最低消费
            calculationDto.setTotalAmount(chargeStandardItem.getMinAmount().add(price));
        } else {
            calculationDto.setTotalAmount(baseAmount.add(chaoGongLiJinE.add(chaoShiJinE)).add(price));
        }
        return calculationDto;
    }

    public CalculationDto calculation(ChargeStandard chargeStandard, BigDecimal distance, BigDecimal times, DateTime setOutTime, boolean pdFlag) {
        CalculationDto calculationDto = calculation(chargeStandard, distance, setOutTime, BigDecimal.ZERO);
        if (calculationDto == null) {
            return null;
        }
        if (pdFlag) {
            if (calculationDto.getTotalAmount().compareTo(BigDecimal.TEN) >= 0) {
                calculationDto.setBaseAmount(calculationDto.getBaseAmount().multiply(pdRebate));
                calculationDto.setTimeAmount(calculationDto.getTimeAmount().multiply(pdRebate));
                calculationDto.setDistanceAmount(calculationDto.getDistanceAmount().multiply(pdRebate));
                calculationDto.setTotalAmount(calculationDto.getTotalAmount().multiply(pdRebate));
                calculationDto.setTypeId(1);
                calculationDto.setPdFlag(Constant.OrderPdFlag.PD);
            } else {
                calculationDto.setBaseAmount(calculationDto.getBaseAmount().multiply(pd9Rebate));
                calculationDto.setTimeAmount(calculationDto.getTimeAmount().multiply(pd9Rebate));
                calculationDto.setDistanceAmount(calculationDto.getDistanceAmount().multiply(pd9Rebate));
                calculationDto.setTotalAmount(calculationDto.getTotalAmount().multiply(pd9Rebate));
                calculationDto.setTypeId(1);
                calculationDto.setPdFlag(Constant.OrderPdFlag.PD);
            }
        }
        return calculationDto;
    }

    public CalculationDto calculationDtoSetUp2(ZxLine zxLine, DateTime setOutTime, boolean sharingFlag, int peopleNumber, BigDecimal distance) {
        BigDecimal zxDistance = BigDecimal.valueOf(zxLine.getDistance());
        BigDecimal zxDistance1 = BigDecimal.ZERO;
        if (zxDistance.compareTo(distance) < 0) {
            zxDistance1 = distance.subtract(zxDistance);
        }
        DateTime time1 = setOutTime;
        DateTime time2, time3;
        time1 = DateTime.parse(time1.toString(DateTimeFormat.forPattern(TimeKit.HHMMSS)), DateTimeFormat.forPattern(TimeKit.HHMMSS));
        time2 = DateTime.parse(new DateTime(zxLine.getSetoutTime1()).toString(DateTimeFormat.forPattern(TimeKit.HHMMSS)), DateTimeFormat.forPattern(TimeKit.HHMMSS));
        time3 = DateTime.parse(new DateTime(zxLine.getSetoutTime2()).toString(DateTimeFormat.forPattern(TimeKit.HHMMSS)), DateTimeFormat.forPattern(TimeKit.HHMMSS));
        BigDecimal price;
        if (time1.isAfter(time2) && time1.isBefore(time3)) {
            if (sharingFlag) {
                BigDecimal p = zxLine.getSharingPriceSpecial();
                price = BigDecimal.valueOf(Double.valueOf(peopleNumber)).multiply(p);
            } else {
                price = zxLine.getSharingPrice();
            }
        } else {
            if (sharingFlag) {
                BigDecimal p = zxLine.getSharingPrice();
                price = BigDecimal.valueOf(Double.valueOf(peopleNumber)).multiply(zxLine.getSharingPrice());
            } else {
                price = zxLine.getPrice();
            }
        }
        ServiceTypeItem serviceTypeItem = ServiceTypeItem.dao.findById(zxLine.getType());
        List<CalculationDto> calculationDtos = Lists.newArrayList();
        CalculationDto calculationDto = new CalculationDto();
        if (zxDistance1.compareTo(BigDecimal.ZERO) > 0) {
            ChargeStandard chargeStandard = ChargeStandard.dao.findByCompanyAndType(zxLine.getCompanyId(), serviceTypeItem.getId());
            if (chargeStandard == null) {
                logger.error("专线查询了默认的收费标准,所属公司为{}", zxLine.getCompanyId());
                chargeStandard = ChargeStandard.dao.findDefaultByServiceType(zxLine.getType());
            }

            if (time1.isAfterNow()) {//判断预约时间是否可用  根据预约时间计费
                time1 = DateTime.now();
            }
            calculationDto = CalZxLineService.getInstance().calculation(chargeStandard, distance, time1, price);

            calculationDto.setTypeId(serviceTypeItem.getId());
        } else {
            calculationDto.setZxLinePrice(price);
            calculationDto.setDistanceAmount(BigDecimal.ZERO);
            calculationDto.setTimeAmount(BigDecimal.ZERO);
            calculationDto.setWaitAmount(BigDecimal.ZERO);
            calculationDto.setAddAmount(BigDecimal.ZERO);
            calculationDto.setPdFlag(sharingFlag == true ? 1:0);
            calculationDto.setTypeId(zxLine.getType());
            calculationDto.setTotalAmount(price);
            calculationDto.setDiscount(BigDecimal.ZERO);
        }
        return calculationDto;
    }

    public List<CalculationDto> calculationDtoSetUp1(ZxLine zxLine, String setOutTime, int sharingFlag, int peopleNumber, String cityCode, BigDecimal distance) {
        BigDecimal zxDistance = BigDecimal.valueOf(zxLine.getDistance());
        BigDecimal zxDistance1 = BigDecimal.ZERO;
        if (zxDistance.compareTo(distance) < 0) {
            zxDistance1 = distance.subtract(zxDistance);
        }
        DateTime time1 = new DateTime(DateTime.now());
        if (!Strings.isNullOrEmpty(setOutTime)) {
            time1 = DateTime.parse(setOutTime, DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMMSS));
        }
        DateTime time2, time3;
        time1 = DateTime.parse(time1.toString(DateTimeFormat.forPattern(TimeKit.HHMMSS)), DateTimeFormat.forPattern(TimeKit.HHMMSS));
        time2 = DateTime.parse(new DateTime(zxLine.getSetoutTime1()).toString(DateTimeFormat.forPattern(TimeKit.HHMMSS)), DateTimeFormat.forPattern(TimeKit.HHMMSS));
        time3 = DateTime.parse(new DateTime(zxLine.getSetoutTime2()).toString(DateTimeFormat.forPattern(TimeKit.HHMMSS)), DateTimeFormat.forPattern(TimeKit.HHMMSS));
        BigDecimal pricePd;
        BigDecimal priceNpd;
        if (time1.isAfter(time2) && time1.isBefore(time3)) {
            BigDecimal p = zxLine.getSharingPriceSpecial();
            pricePd = BigDecimal.valueOf(Double.valueOf(peopleNumber)).multiply(p);
//            priceNpd = zxLine.getSharingPrice();
            priceNpd = zxLine.getPriceSpecial();
        } else {
            BigDecimal p = zxLine.getSharingPrice();
            pricePd = BigDecimal.valueOf(Double.valueOf(peopleNumber)).multiply(zxLine.getSharingPrice());
            priceNpd = zxLine.getPrice();
        }
        ServiceTypeItem serviceTypeItem1 = ServiceTypeItem.dao.findById(zxLine.getType());
        List<ServiceTypeItem> serviceTypeItems = ServiceTypeItem.dao.findByType(serviceTypeItem1.getType());
        List<CalculationDto> calculationDtos = Lists.newArrayList();
        CalculationDto calculationDtoPd = new CalculationDto();
        CalculationDto calculationDtoNpd = new CalculationDto();
        if (zxDistance1.compareTo(BigDecimal.ZERO) > 0) {
            for (ServiceTypeItem serviceTypeItem : serviceTypeItems) {
                ChargeStandard chargeStandard = ChargeStandard.dao.findByCompanyAndType(zxLine.getCompanyId(), serviceTypeItem.getId());
                if (chargeStandard == null) {
                    logger.error("专线查询了默认的收费标准,所属公司为{}", zxLine.getCompanyId());
                    chargeStandard = ChargeStandard.dao.findDefaultByServiceType(zxLine.getType());
                }

                if (!Strings.isNullOrEmpty(setOutTime)) {
                    if (time1.isAfterNow()) {//判断预约时间是否可用  根据预约时间计费
                        time1 = DateTime.now();
                    }
                    calculationDtoPd = CalZxLineService.getInstance().calculation(chargeStandard, zxDistance1, time1, pricePd);
                    calculationDtoNpd = CalZxLineService.getInstance().calculation(chargeStandard, zxDistance1, time1, priceNpd);

                } else {
                    time1 = DateTime.now();
                    calculationDtoPd = CalZxLineService.getInstance().calculation(chargeStandard, zxDistance1, time1, pricePd);
                    calculationDtoNpd = CalZxLineService.getInstance().calculation(chargeStandard, zxDistance1, time1, priceNpd);
                }
                calculationDtoPd.setPdFlag(Constant.OrderPdFlag.PD);
                calculationDtoPd.setTypeId(serviceTypeItem.getId());
                calculationDtoNpd.setPdFlag(Constant.OrderPdFlag.BPD);
                calculationDtoNpd.setTypeId(serviceTypeItem.getId());
                calculationDtos.add(calculationDtoPd);
                calculationDtos.add(calculationDtoNpd);
            }
        } else {
            calculationDtoNpd.setZxLinePrice(priceNpd);
            calculationDtoNpd.setDistanceAmount(BigDecimal.ZERO);
            calculationDtoNpd.setTimeAmount(BigDecimal.ZERO);
            calculationDtoNpd.setBaseAmount(BigDecimal.ZERO);
            calculationDtoNpd.setWaitAmount(BigDecimal.ZERO);
            calculationDtoNpd.setAddAmount(BigDecimal.ZERO);
            calculationDtoNpd.setPdFlag(Constant.OrderPdFlag.BPD);
            calculationDtoNpd.setTypeId(zxLine.getType());
            calculationDtoNpd.setTotalAmount(priceNpd);
            calculationDtoNpd.setDiscount(BigDecimal.ZERO);
            calculationDtoPd.setZxLinePrice(pricePd);
            calculationDtoPd.setDistanceAmount(BigDecimal.ZERO);
            calculationDtoPd.setTimeAmount(BigDecimal.ZERO);
            calculationDtoPd.setBaseAmount(BigDecimal.ZERO);
            calculationDtoPd.setWaitAmount(BigDecimal.ZERO);
            calculationDtoPd.setAddAmount(BigDecimal.ZERO);
            calculationDtoPd.setPdFlag(Constant.OrderPdFlag.PD);
            calculationDtoPd.setTypeId(zxLine.getType());
            calculationDtoPd.setTotalAmount(pricePd);
            calculationDtoPd.setDiscount(BigDecimal.ZERO);
            calculationDtos.add(calculationDtoPd);
            calculationDtos.add(calculationDtoNpd);
        }
        return calculationDtos;
    }


    private CalZxLineService() {
    }

    private static class CalServiceHolder {
        static CalZxLineService instance = new CalZxLineService();
    }

    public static CalZxLineService getInstance() {
        return CalServiceHolder.instance;
    }
}