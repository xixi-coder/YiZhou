package services;

/**
 * Created by BOGONj on 2016/9/6.
 */
public class OrderLogService {
    private OrderLogService() {
    }

    private static class OrderLogServiceHolder {
        static OrderLogService intance = new OrderLogService();
    }

    public OrderLogService getIntance() {
        return OrderLogServiceHolder.intance;
    }
}
