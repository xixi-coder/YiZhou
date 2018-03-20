package services;

/**
 * Created by admin on 2016/12/21.
 */
public class CancelOrderService {
    private CancelOrderService() {
    }

    private static class CancelOrderServiceHolder {
        static CancelOrderService instance = new CancelOrderService();
    }

    public static CancelOrderService getInstance() {
        return CancelOrderServiceHolder.instance;
    }

    public static void cancel(){
//        List<Order> orders = Order.dao.findBy
    }
}
