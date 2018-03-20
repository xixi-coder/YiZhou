package jobs.count;

import base.Constant;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import models.count.DriverOrderStatistics;
import models.count.MemberOrderStatistics;
import models.count.TaskScheduler;
import models.order.Order;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * 订单统计
 */
public class OrderCountJob implements Job {
    Log log = LogFactory.get();
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("统计司机和用户完成订单数开始：时间：", DateUtil.now());
        countCompleteOrder();
        log.info("统计司机和用户完成订单数结束：时间：", DateUtil.now());
    }
    
    /**
     * 统计完成订单数
     */
    private void countCompleteOrder() {
        TaskScheduler task = TaskScheduler.dao.findById(Constant.Task.COUNT_COMPLETE_ORDER_ID);
        
        List<Order> list = Order.dao.findOrderByPayTime(task.getLastExecutionTime());
        
        if (list != null) {
            if (list.size() != 0) {
                for (Order order : list) {
                    MemberOrderStatistics member = MemberOrderStatistics.dao.findByMemberId(order.getMember());
                    DriverOrderStatistics driver = DriverOrderStatistics.dao.findByDriverId(order.getDriver());
                    
                    if (member == null) {
                        final MemberOrderStatistics member1 = new MemberOrderStatistics();
                        member1.setOrderNum(1);
                        member1.setMemberId(order.getMember());
                        member1.save();
                    } else {
                        member.setOrderNum((member == null ? 0 : member.getOrderNum()) + 1);
                        member.update();
                    }
                    if (driver == null) {
                        final DriverOrderStatistics driver1 = new DriverOrderStatistics();
                        driver1.setOrderNum(1);
                        driver1.setDriverId(order.getDriver());
                        driver1.save();
                    } else {
                        driver.setOrderNum((driver == null ? 0 : driver.getOrderNum()) + 1);
                        driver.update();
                    }
                }
                task.setLastExecutionTime(DateUtil.offsetSecond(list.get(list.size() - 1).getPayTime(), 1));
                task.update();
            }
        }
    }
    
    
}
