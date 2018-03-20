package services;

import base.Constant;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import dto.CalculationDto;
import kits.TimeKit;
import models.company.Company;
import models.member.MemberInfo;
import models.sys.ChargeStandard;
import models.sys.ChargeStandardItem;
import models.sys.ChargeStandardMileage;
import models.sys.ServiceTypeItem;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class CalService {
    private Logger logger = LoggerFactory.getLogger(CalService.class);

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

    public CalculationDto calculation(ChargeStandard chargeStandard, BigDecimal distance, BigDecimal times, DateTime setOutTime) {
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

        BigDecimal needMintues = times;
        BigDecimal baseMintues = chargeStandardItem.getQibufenzhong();
        if (needMintues.compareTo(baseMintues) > 0) {
            BigDecimal moreThan = needMintues.subtract(baseMintues);
            if (chargeStandardItem.getMeiduoshaofengzhong().compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal[] than = moreThan.divideAndRemainder(chargeStandardItem.getMeiduoshaofengzhong());
                chaoShiJinE = chaoShiJinE.add(chargeStandardItem.getFengzhongjiashoujine().multiply(than[0]));//超过实际的金额
                if (than[1].compareTo(chargeStandardItem.getBuzufengzhong()) > 0) {
                    chaoShiJinE = chaoShiJinE.add(chargeStandardItem.getFengzhongjiashoujine());
                }
            }
        }
        calculationDto.setBaseAmount(baseAmount);
        calculationDto.setMinAmount(minAmount);
        calculationDto.setDistanceAmount(chaoGongLiJinE);
        calculationDto.setTimeAmount(chaoShiJinE);
        if (baseAmount.add(chaoGongLiJinE.add(chaoShiJinE)).compareTo(chargeStandardItem.getMinAmount()) < 0) {//不满最低消费
            calculationDto.setTotalAmount(chargeStandardItem.getMinAmount());
        } else {
            calculationDto.setTotalAmount(baseAmount.add(chaoGongLiJinE.add(chaoShiJinE)));
        }
        return calculationDto;
    }

    public CalculationDto calculation(ChargeStandard chargeStandard, BigDecimal distance, BigDecimal times, DateTime setOutTime, boolean pdFlag) {
        CalculationDto calculationDto = calculation(chargeStandard, distance, times, setOutTime);
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

    public BigDecimal calculationWaitSetUp1(int serviceType, int companyId, MemberInfo memberInfo, DateTime setOutTime, BigDecimal times) {
        ChargeStandard chargeStandard = ChargeStandard.dao.findByCompanyAndType(companyId, serviceType);
        if (chargeStandard == null) {
            chargeStandard = ChargeStandard.dao.findDefaultByServiceType(serviceType);
        }
        if (memberInfo != null && memberInfo.getChargeStandard() != 0) {//判断会员是否设置独立的收费标准
            chargeStandard = ChargeStandard.dao.findById(memberInfo.getChargeStandard());
        }
        return CalService.getInstance().calculationWaitAmount(chargeStandard, times, setOutTime);
    }

    public List<CalculationDto> calculationDtoSetUp1(int type, Company company, MemberInfo memberInfo, String strSetOutTime, BigDecimal distance, BigDecimal times) {
        List<ServiceTypeItem> serviceTypeItems = ServiceTypeItem.dao.findByType(type);
        List<CalculationDto> calculationDtos = Lists.newArrayList();
        for (ServiceTypeItem serviceTypeItem : serviceTypeItems) {
            ChargeStandard chargeStandard = ChargeStandard.dao.findByCompanyAndType(company.getId(), serviceTypeItem.getId());
            if (chargeStandard == null) {
                chargeStandard = ChargeStandard.dao.findDefaultByServiceType(type);
            }
            if (memberInfo != null && memberInfo.getChargeStandard() != null && memberInfo.getChargeStandard() != 0) {//判断会员是否设置独立的收费标准
                chargeStandard = ChargeStandard.dao.findById(memberInfo.getChargeStandard());
            }
            CalculationDto calculationDto;

            if (!Strings.isNullOrEmpty(strSetOutTime)) {
                DateTime setOutTime = DateTime.parse(strSetOutTime, DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMMSS));
                //判断预约时间是否可用  根据预约时间计费
                if (setOutTime.isAfterNow()) {
                    setOutTime = DateTime.now();
                }
                //todo 计算预计价格
                calculationDto = CalService.getInstance().calculation(chargeStandard, distance, times, setOutTime);
            } else {
                DateTime setOutTime = DateTime.now();
                calculationDto = CalService.getInstance().calculation(chargeStandard, distance, times, setOutTime);
            }
            if (type == Constant.ServiceType.KuaiChe) {
                calculationDto.setTypeId(0);
            } else {
                calculationDto.setTypeId(serviceTypeItem.getId());
            }
            calculationDtos.add(calculationDto);
        }
        if (type == Constant.ServiceType.KuaiChe) {
            //// TODO: 2016/10/11 获取系统配置来计算拼车价格
            boolean carPoolFlag = true;
            CalculationDto calculationDto = calculationDtos.get(0);
            CalculationDto tmp = new CalculationDto();
            if (carPoolFlag) {
                if (calculationDto.getTotalAmount().compareTo(calculationDto.getMinAmount()) <= 0) {
                    tmp.setBaseAmount(calculationDto.getBaseAmount());
                    tmp.setTimeAmount(calculationDto.getTimeAmount());
                    tmp.setDistanceAmount(calculationDto.getDistanceAmount());
                    tmp.setTotalAmount(calculationDto.getMinAmount());
                    tmp.setTypeId(1);
                    tmp.setPdFlag(Constant.OrderPdFlag.BPD);
                } else if (calculationDto.getTotalAmount().compareTo(BigDecimal.TEN) >= 0) {
                    tmp.setBaseAmount(calculationDto.getBaseAmount().multiply(pdRebate));
                    tmp.setTimeAmount(calculationDto.getTimeAmount().multiply(pdRebate));
                    tmp.setDistanceAmount(calculationDto.getDistanceAmount().multiply(pdRebate));
                    tmp.setTotalAmount(calculationDto.getTotalAmount().multiply(pdRebate));
                    tmp.setTypeId(1);
                    tmp.setPdFlag(Constant.OrderPdFlag.PD);
                } else {
                    if (calculationDto.getTotalAmount().multiply(pd9Rebate).compareTo(calculationDto.getMinAmount()) <= 0) {
                        tmp.setBaseAmount(calculationDto.getBaseAmount());
                        tmp.setTimeAmount(calculationDto.getTimeAmount());
                        tmp.setDistanceAmount(calculationDto.getDistanceAmount());
                        tmp.setTotalAmount(calculationDto.getMinAmount());
                        tmp.setPdFlag(Constant.OrderPdFlag.PD);
                    } else {
                        tmp.setBaseAmount(calculationDto.getBaseAmount().multiply(pd9Rebate));
                        tmp.setTimeAmount(calculationDto.getTimeAmount().multiply(pd9Rebate));
                        tmp.setDistanceAmount(calculationDto.getDistanceAmount().multiply(pd9Rebate));
                        tmp.setTotalAmount(calculationDto.getTotalAmount().multiply(pd9Rebate));
                        tmp.setPdFlag(Constant.OrderPdFlag.PD);
                    }
                    tmp.setTypeId(1);
                }
                calculationDtos.add(tmp);
            }
        }
        return calculationDtos;
    }

    public List<CalculationDto> calculationDtoSetUp2(Company company, MemberInfo memberInfo, BigDecimal distance, int people) {
        List<ServiceTypeItem> serviceTypeItems = ServiceTypeItem.dao.findByType(Constant.ServiceType.ShunFengChe);
        List<CalculationDto> calculationDtos = Lists.newArrayList();
        for (ServiceTypeItem serviceTypeItem : serviceTypeItems) {
            ChargeStandard chargeStandard = ChargeStandard.dao.findByCompanyAndType(company.getId(), serviceTypeItem.getId());
            if (chargeStandard == null) {
                chargeStandard = chargeStandard.dao.findById(serviceTypeItem.getChargeStandard());
            }
            if (memberInfo != null && memberInfo.getChargeStandard() != null && memberInfo.getChargeStandard() != 0) {
                chargeStandard = ChargeStandard.dao.findById(memberInfo.getChargeStandard());
            }
            CalculationDto calculationDto;
            calculationDto = CalService.getInstance().calculation(chargeStandard, distance, BigDecimal.ZERO, DateTime.now());
            if (serviceTypeItem.getId() != Constant.ServiceItemType.DaiHuo) {
                BigDecimal discount = discount(people, distance, chargeStandard).divide(BigDecimal.TEN);
                calculationDto.setDiscount(discount);
            }
            calculationDto.setTypeId(serviceTypeItem.getId());
            if (serviceTypeItem.getId() != Constant.ServiceItemType.DaiHuo) {
                calculationDto.setPdFlag(0);
            }
            calculationDtos.add(calculationDto);
        }
        List<CalculationDto> tmps = Lists.newArrayList();
        for (CalculationDto calculationDto : calculationDtos) {
            if (calculationDto.getTypeId() != Constant.ServiceItemType.DaiHuo) {
                CalculationDto tmp = new CalculationDto();
                tmp.setBaseAmount(calculationDto.getBaseAmount().multiply(calculationDto.getDiscount()));
                tmp.setTimeAmount(calculationDto.getTimeAmount().multiply(calculationDto.getDiscount()));
                tmp.setDistanceAmount(calculationDto.getDistanceAmount().multiply(calculationDto.getDiscount()));
                tmp.setTotalAmount(calculationDto.getTotalAmount().multiply(calculationDto.getDiscount()));
                tmp.setTypeId(calculationDto.getTypeId());
                tmp.setPdFlag(1);
                calculationDto.setPdFlag(Constant.OrderPdFlag.BPD);
                tmps.add(tmp);
            }
        }
        tmps.addAll(calculationDtos);
        return tmps;
    }

    /**
     * 计算折扣
     *
     * @param people
     * @param distance
     * @param chargeStandard
     * @return
     */
    public BigDecimal discount(int people, BigDecimal distance, ChargeStandard chargeStandard) {
        ChargeStandardItem chargeStandardItem = ChargeStandardItem.dao.findByChargeStandardAndTime(chargeStandard.getId(), DateTime.now());
        BigDecimal a = chargeStandardItem.getOneDiscount1().multiply(new BigDecimal(chargeStandardItem.getOneDistance1()));
        BigDecimal b = chargeStandardItem.getOneDiscount2().multiply(new BigDecimal(chargeStandardItem.getOneDistance2() - chargeStandardItem.getOneDistance1()));
        BigDecimal c = chargeStandardItem.getOneDiscount3().multiply(new BigDecimal(chargeStandardItem.getOneDistance3() - chargeStandardItem.getOneDistance2()));
        BigDecimal d = chargeStandardItem.getOneDiscount4().multiply(new BigDecimal(chargeStandardItem.getOneDistance4() - chargeStandardItem.getOneDistance3()));
        BigDecimal e;
        if (distance.compareTo(new BigDecimal(500)) > 0) {
            e = chargeStandardItem.getOneDiscount5().multiply(new BigDecimal(chargeStandardItem.getOneDistance5()).subtract(distance));
        } else if (distance.compareTo(new BigDecimal(30)) > 0) {
            e = chargeStandardItem.getOneDiscount4().multiply(new BigDecimal(chargeStandardItem.getOneDistance4()).subtract(distance));
        } else if (distance.compareTo(new BigDecimal(10)) > 0) {
            e = chargeStandardItem.getOneDiscount3().multiply(new BigDecimal(chargeStandardItem.getOneDistance3()).subtract(distance));
        } else if (distance.compareTo(new BigDecimal(2)) > 0) {
            e = chargeStandardItem.getOneDiscount2().multiply(new BigDecimal(chargeStandardItem.getOneDistance2()).subtract(distance));
        } else {
            e = chargeStandardItem.getOneDiscount1().multiply(distance);
        }
        if (people == 1) {
            if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance1())) <= 0) {
                return chargeStandardItem.getOneDiscount1();
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance2())) <= 0) {
                return chargeStandardItem.getOneDiscount2();
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance3())) <= 0) {
                return chargeStandardItem.getOneDiscount3();
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance4())) <= 0) {
                return chargeStandardItem.getOneDiscount4();
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance5())) > 0) {
                return chargeStandardItem.getOneDiscount5();
            } else {
                return chargeStandardItem.getOneDiscount5();
            }
        } else if (people == 2) {
            if (distance.compareTo(new BigDecimal(chargeStandardItem.getTwoDistance1())) <= 0) {
                return chargeStandardItem.getTwoDiscount1();
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getTwoDistance2())) <= 0) {
                return chargeStandardItem.getTwoDiscount2();
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getTwoDistance3())) <= 0) {
                return chargeStandardItem.getTwoDiscount3();
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getTwoDistance4())) <= 0) {
                return chargeStandardItem.getTwoDiscount4();
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getTwoDistance5())) > 0) {
                return chargeStandardItem.getTwoDiscount5();
            } else {
                return chargeStandardItem.getTwoDiscount5();
            }
        } else if (people == 3) {
            if (distance.compareTo(new BigDecimal(chargeStandardItem.getThreeDistance1())) <= 0) {
                return chargeStandardItem.getThreeDiscount1();
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getThreeDistance2())) <= 0) {
                return chargeStandardItem.getThreeDiscount2();
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getThreeDistance3())) <= 0) {
                return chargeStandardItem.getTwoDiscount3();
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getThreeDistance4())) <= 0) {
                return chargeStandardItem.getThreeDiscount4();
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getThreeDistance5())) > 0) {
                return chargeStandardItem.getThreeDiscount5();
            } else {
                return chargeStandardItem.getThreeDiscount5();
            }
        } else {
            return new BigDecimal(10);
        }
    }

    /**
     * 计算折后价格
     *
     * @param people
     * @param distance
     * @param chargeStandard
     * @return
     */
    public BigDecimal discountAmount(int people, BigDecimal distance, ChargeStandard chargeStandard) {
        CalculationDto calculationDto = CalService.getInstance().calculation(chargeStandard, distance, BigDecimal.ZERO, DateTime.now());
        ChargeStandardItem chargeStandardItem = ChargeStandardItem.dao.findByChargeStandardAndTime(chargeStandard.getId(), DateTime.now());
        BigDecimal a1 = chargeStandardItem.getOneDiscount1().multiply(chargeStandardItem.getBaseAmount());
        BigDecimal b1 = chargeStandardItem.getOneDiscount2().multiply(new BigDecimal(chargeStandardItem.getOneDistance2() - chargeStandardItem.getOneDistance1()));
        BigDecimal c1 = chargeStandardItem.getOneDiscount3().multiply(new BigDecimal(chargeStandardItem.getOneDistance3() - chargeStandardItem.getOneDistance2()));
        BigDecimal d1 = chargeStandardItem.getOneDiscount4().multiply(new BigDecimal(chargeStandardItem.getOneDistance4() - chargeStandardItem.getOneDistance3()));
        BigDecimal e1;
        if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance5())) > 0) {
            e1 = chargeStandardItem.getOneDiscount5().multiply(distance.subtract(new BigDecimal(chargeStandardItem.getOneDistance4()))).multiply(chargeStandardItem.getGonglijiashoujine());
        } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance3())) > 0) {
            e1 = chargeStandardItem.getOneDiscount4().multiply(distance.subtract(new BigDecimal(chargeStandardItem.getOneDistance3()))).multiply(chargeStandardItem.getGonglijiashoujine());
        } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance2())) > 0) {
            e1 = chargeStandardItem.getOneDiscount3().multiply(distance.subtract(new BigDecimal(chargeStandardItem.getOneDistance2()))).multiply(chargeStandardItem.getGonglijiashoujine());
        } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance1())) > 0) {
            e1 = chargeStandardItem.getOneDiscount2().multiply(distance.subtract(new BigDecimal(chargeStandardItem.getOneDistance1()))).multiply(chargeStandardItem.getGonglijiashoujine());
        } else {
            e1 = chargeStandardItem.getOneDiscount1().multiply(calculationDto.getBaseAmount());
        }
        BigDecimal a2 = chargeStandardItem.getTwoDiscount1().multiply(chargeStandardItem.getBaseAmount());
        BigDecimal b2 = chargeStandardItem.getTwoDiscount2().multiply(new BigDecimal(chargeStandardItem.getTwoDistance2() - chargeStandardItem.getTwoDistance1())).multiply(chargeStandardItem.getGonglijiashoujine());
        BigDecimal c2 = chargeStandardItem.getTwoDiscount3().multiply(new BigDecimal(chargeStandardItem.getTwoDistance3() - chargeStandardItem.getTwoDistance2())).multiply(chargeStandardItem.getGonglijiashoujine());
        BigDecimal d2 = chargeStandardItem.getTwoDiscount4().multiply(new BigDecimal(chargeStandardItem.getTwoDistance4() - chargeStandardItem.getTwoDistance3())).multiply(chargeStandardItem.getGonglijiashoujine());
        BigDecimal e2;
        if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance5())) > 0) {
            e2 = chargeStandardItem.getTwoDiscount5().multiply(distance.subtract(new BigDecimal(chargeStandardItem.getTwoDistance4()))).multiply(chargeStandardItem.getGonglijiashoujine());
        } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance3())) > 0) {
            e2 = chargeStandardItem.getTwoDiscount4().multiply(distance.subtract(new BigDecimal(chargeStandardItem.getTwoDistance3()))).multiply(chargeStandardItem.getGonglijiashoujine());
        } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance2())) > 0) {
            e2 = chargeStandardItem.getTwoDiscount3().multiply(distance.subtract(new BigDecimal(chargeStandardItem.getTwoDistance2()))).multiply(chargeStandardItem.getGonglijiashoujine());
        } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance1())) > 0) {
            e2 = chargeStandardItem.getTwoDiscount2().multiply(distance.subtract(new BigDecimal(chargeStandardItem.getTwoDistance1()))).multiply(chargeStandardItem.getGonglijiashoujine());
        } else {
            e2 = chargeStandardItem.getTwoDiscount1().multiply(calculationDto.getBaseAmount());
        }
        BigDecimal a3 = chargeStandardItem.getThreeDiscount1().multiply(chargeStandardItem.getBaseAmount());
        BigDecimal b3 = chargeStandardItem.getThreeDiscount2().multiply(new BigDecimal(chargeStandardItem.getThreeDistance2() - chargeStandardItem.getThreeDistance1())).multiply(chargeStandardItem.getGonglijiashoujine());
        BigDecimal c3 = chargeStandardItem.getThreeDiscount3().multiply(new BigDecimal(chargeStandardItem.getThreeDistance3() - chargeStandardItem.getThreeDistance2())).multiply(chargeStandardItem.getGonglijiashoujine());
        BigDecimal d3 = chargeStandardItem.getThreeDiscount4().multiply(new BigDecimal(chargeStandardItem.getThreeDistance4() - chargeStandardItem.getThreeDistance3())).multiply(chargeStandardItem.getGonglijiashoujine());
        BigDecimal e3;
        if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance5())) > 0) {
            e3 = chargeStandardItem.getThreeDiscount5().multiply(distance.subtract(new BigDecimal(chargeStandardItem.getThreeDistance4()))).multiply(chargeStandardItem.getGonglijiashoujine());
        } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance3())) > 0) {
            e3 = chargeStandardItem.getThreeDiscount4().multiply(distance.subtract(new BigDecimal(chargeStandardItem.getThreeDistance3()))).multiply(chargeStandardItem.getGonglijiashoujine());
        } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance2())) > 0) {
            e3 = chargeStandardItem.getThreeDiscount3().multiply(distance.subtract(new BigDecimal(chargeStandardItem.getThreeDistance2()))).multiply(chargeStandardItem.getGonglijiashoujine());
        } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance1())) > 0) {
            e3 = chargeStandardItem.getThreeDiscount2().multiply(distance.subtract(new BigDecimal(chargeStandardItem.getThreeDistance1()))).multiply(chargeStandardItem.getGonglijiashoujine());
        } else {
            e3 = chargeStandardItem.getThreeDiscount1().multiply(calculationDto.getBaseAmount());
        }
        BigDecimal amount;
        if (people == 1) {
            if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance5())) > 0) {
                amount = e1.add(d1).add(c1).add(b1).add(a1);
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance3())) >= 0) {
                amount = e1.add(c1).add(b1).add(a1);
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance2())) >= 0) {
                amount = e1.add(b1).add(a1);
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance1())) >= 0) {
                amount = e1.add(a1);
            } else {
                amount = e1;
            }
        } else if (people == 2) {
            if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance5())) > 0) {
                amount = e2.add(d1).add(c1).add(b1).add(a1);
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance3())) >= 0) {
                amount = e2.add(c1).add(b1).add(a1);
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance2())) >= 0) {
                amount = e2.add(b1).add(a1);
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance1())) >= 0) {
                amount = e2.add(a1);
            } else {
                amount = e2;
            }
        } else if (people == 3) {
            if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance5())) > 0) {
                amount = e3.add(d1).add(c1).add(b1).add(a1);
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance3())) >= 0) {
                amount = e3.add(c1).add(b1).add(a1);
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance2())) >= 0) {
                amount = e3.add(b1).add(a1);
            } else if (distance.compareTo(new BigDecimal(chargeStandardItem.getOneDistance1())) >= 0) {
                amount = e3.add(a1);
            } else {
                amount = e3;
            }
        } else {
            amount = calculationDto.getTotalAmount().multiply(BigDecimal.TEN);
        }
        return amount.divide(BigDecimal.TEN);

    }

    public BigDecimal noSuccess(BigDecimal distance, int people, int company, MemberInfo memberInfo) {
        ChargeStandard chargeStandard = ChargeStandard.dao.findByCompanyAndType(company, Constant.ServiceItemType.ShiNei);
        if (chargeStandard == null) {
            chargeStandard = chargeStandard.dao.findById(ServiceTypeItem.dao.findById(Constant.ServiceItemType.ShiNei).getChargeStandard());
        }
        if (memberInfo != null && memberInfo.getChargeStandard() != null && memberInfo.getChargeStandard() != 0) {
            chargeStandard = ChargeStandard.dao.findById(memberInfo.getChargeStandard());
        }
        BigDecimal discount = discount(people, distance, chargeStandard);
        CalculationDto calculationDto = CalService.getInstance().calculation(chargeStandard, distance, BigDecimal.ZERO, DateTime.now());
        if (distance.compareTo(new BigDecimal(30)) <= 0) {
            return calculationDto.getTotalAmount();
        } else {
            BigDecimal totalAmount = CalService.getInstance().calculation(chargeStandard, distance, BigDecimal.ZERO, DateTime.now()).getTotalAmount();
            BigDecimal amount1 = CalService.getInstance().calculation(chargeStandard, new BigDecimal(30), BigDecimal.ZERO, DateTime.now()).getTotalAmount();
            BigDecimal amount2 = (amount1.subtract(totalAmount)).multiply(discount);
            return amount1.add(amount2);
        }
    }

    public CalculationDto calculationDtoByServiceItem(int type, int serviceType, Company company, BigDecimal distance, BigDecimal times, DateTime setOutTime, MemberInfo memberInfo, boolean pdFlag) {
        ChargeStandard chargeStandard = ChargeStandard.dao.findByCompanyAndType(company.getId(), type);
        if (chargeStandard == null) {
            chargeStandard = ChargeStandard.dao.findDefaultByServiceType(serviceType);
        }
        if (memberInfo != null && memberInfo.getChargeStandard() != 0) {//判断会员是否设置独立的收费标准
            chargeStandard = ChargeStandard.dao.findById(memberInfo.getChargeStandard());
        }
        return CalService.getInstance().calculation(chargeStandard, distance, times, setOutTime, pdFlag);
    }

    private CalService() {
    }

    private static class CalServiceHolder {
        static CalService instance = new CalService();
    }

    public static CalService getInstance() {
        return CalServiceHolder.instance;
    }
}