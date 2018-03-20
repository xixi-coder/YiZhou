package dto.pushorder;

import java.io.Serializable;

/**
 * Created by admin on 2016/10/14.
 */
public class DispatchSetting implements Serializable {
    private int timeOut;
    private int startDistance;
    private int endDistance;

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getStartDistance() {
        return startDistance;
    }

    public void setStartDistance(int startDistance) {
        this.startDistance = startDistance;
    }

    public int getEndDistance() {
        return endDistance;
    }

    public void setEndDistance(int endDistance) {
        this.endDistance = endDistance;
    }
}
