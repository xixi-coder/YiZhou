package dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by BOGONj on 2016/9/1.
 */
public class CalculationDto {
    private BigDecimal baseAmount;//起步费用
    private BigDecimal minAmount;//起步费用
    private BigDecimal distanceAmount;//里程费用
    private BigDecimal timeAmount;//超时费用
    private BigDecimal waitAmount;//等待费用
    private BigDecimal addAmount;//加价费用
    private int pdFlag; //是否拼车
    private BigDecimal discount;
    private int typeId;


    private BigDecimal zxLinePrice;//专线线路费用
    private BigDecimal totalAmount;//总费用

    public CalculationDto() {
    }


    public BigDecimal getDiscount() {
        return this.discount;
    }
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    
    public BigDecimal getMinAmount() {
        return this.minAmount;
    }
    
    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }
    
    public int getPdFlag() {
        return this.pdFlag;
    }
    
    public void setPdFlag(int pdFlag) {
        this.pdFlag = pdFlag;
    }
    
    public BigDecimal getAddAmount() {
        return addAmount;
    }
    
    public void setAddAmount(BigDecimal addAmount) {
        this.addAmount = addAmount;
    }
    
    public BigDecimal getWaitAmount() {
        return waitAmount;
    }

    public void setWaitAmount(BigDecimal waitAmount) {
        this.waitAmount = waitAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getBaseAmount() {
        return baseAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDistanceAmount() {
        return distanceAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public void setDistanceAmount(BigDecimal distanceAmount) {
        this.distanceAmount = distanceAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTimeAmount() {
        return timeAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTimeAmount(BigDecimal timeAmount) {
        this.timeAmount = timeAmount.setScale(2, RoundingMode.HALF_UP);
    }


    public BigDecimal getZxLinePrice() {
        return zxLinePrice;
    }

    public void setZxLinePrice(BigDecimal zxLinePrice) {
        this.zxLinePrice = zxLinePrice;
    }
}
