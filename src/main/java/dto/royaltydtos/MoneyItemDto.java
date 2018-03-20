package dto.royaltydtos;

import java.io.Serializable;

/**
 * Created by BOGONj on 2016/9/13.
 */
public class MoneyItemDto implements Serializable {
    private String eachMoney;
    private String endMoney;
    private String startMoney;
    private String tiMoney;
    private String type;

    public String getEachMoney() {
        return eachMoney;
    }

    public void setEachMoney(String eachMoney) {
        this.eachMoney = eachMoney;
    }

    public String getEndMoney() {
        return endMoney;
    }

    public void setEndMoney(String endMoney) {
        this.endMoney = endMoney;
    }

    public String getStartMoney() {
        return startMoney;
    }

    public void setStartMoney(String startMoney) {
        this.startMoney = startMoney;
    }

    public String getTiMoney() {
        return tiMoney;
    }

    public void setTiMoney(String tiMoney) {
        this.tiMoney = tiMoney;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
