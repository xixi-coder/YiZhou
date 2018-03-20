package dto.chargestandarddtos;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BOGONj on 2016/9/19.
 */
public class ChargestandardDto implements Serializable {
    private String name;//名称
    private String desc;//描述
    private List<ChargestandardItemDto> cdItem;

    private int company;

    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<ChargestandardItemDto> getCdItem() {
        return cdItem;
    }

    public void setCdItem(List<ChargestandardItemDto> cdItem) {
        this.cdItem = cdItem;
    }
}
