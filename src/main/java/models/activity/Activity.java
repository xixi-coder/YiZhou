package models.activity;

import annotation.TableBind;
import base.Constant;
import base.datatable.DataTablePage;
import base.models.BaseActivity;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
@TableBind(tableName = "dele_activity")
public class Activity extends BaseActivity<Activity> {
    public static Activity dao = new Activity();

    /**
     * 通过触发时间查询对象的活动
     *
     * @param register
     * @param registerTime
     * @return
     */
    public List<Activity> findByEvent(int register, DateTime registerTime, int companId) {
        return find(SqlManager.sql("activity.findByEvent"), register, companId, registerTime.toDate());
    }

    public Integer getMinOrderCount() {
        return super.getMinOrderCount() == null ? 1 : super.getMinOrderCount();
    }
}
