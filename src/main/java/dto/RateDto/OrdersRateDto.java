package dto.RateDto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/2.
 */
public class OrdersRateDto implements Serializable{

    private static final long serialVersionUID = 3704314375936667776L;
    private  double zc;
    private  double dj;
    private  double czc;
    private  double kc;
    private  double sfc;
    private  double zx;
    private  double ALLs;
    private int count;

    public double getZc() {
        return zc;
    }

    public void setZc(double zc) {
        this.zc = zc;
    }

    public double getDj() {
        return dj;
    }

    public void setDj(double dj) {
        this.dj = dj;
    }

    public double getCzc() {
        return czc;
    }

    public void setCzc(double czc) {
        this.czc = czc;
    }

    public double getKc() {
        return kc;
    }

    public void setKc(double kc) {
        this.kc = kc;
    }

    public double getSfc() {
        return sfc;
    }

    public void setSfc(double sfc) {
        this.sfc = sfc;
    }

    public double getZx() {
        return zx;
    }

    public void setZx(double zx) {
        this.zx = zx;
    }

    public double getALLs() {
        return ALLs;
    }

    public void setALLs(double ALLs) {
        this.ALLs = ALLs;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
