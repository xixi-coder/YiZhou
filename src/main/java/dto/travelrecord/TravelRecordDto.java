package dto.travelrecord;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Administrator on 2017/2/20.
 */
public class TravelRecordDto implements Serializable {
    private BigDecimal baseAmount;//起步费用
    private BigDecimal distanceAmount;//里程费用
    private BigDecimal timeAmount;//超时费用
    private BigDecimal waitAmount;//等待费用
    private BigDecimal addAmount;//加价费用

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

    private int typeId;
    private BigDecimal totalAmount;//总费用
    private int pdFlag;

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

    public int getPdFlag() {
        return this.pdFlag;
    }

    public void setPdFlag(int pdFlag) {
        this.pdFlag = pdFlag;
    }
}