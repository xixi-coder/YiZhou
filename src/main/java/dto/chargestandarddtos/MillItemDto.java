package dto.chargestandarddtos;

import java.io.Serializable;

/**
 * Created by BOGONj on 2016/9/19.
 */
public class MillItemDto implements Serializable {
    private String end;
    private String money;
    private String start;

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}
