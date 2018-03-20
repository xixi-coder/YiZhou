package dto.activity;

import models.activity.Coupon;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by admin on 2016/11/28.
 */
public class AwardDto implements Serializable{
    private int type;
    private BigDecimal rebate;
    private Coupon coupon;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
