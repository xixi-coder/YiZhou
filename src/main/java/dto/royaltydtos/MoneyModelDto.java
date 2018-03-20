package dto.royaltydtos;

import java.util.List;

/**
 * Created by BOGONj on 2016/9/13.
 */
public class MoneyModelDto {
    private String startTime;
    private String endTime;
    private List<MoneyItemDto> item;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<MoneyItemDto> getItem() {
        return item;
    }

    public void setItem(List<MoneyItemDto> item) {
        this.item = item;
    }
}
