package models.order;

import annotation.TableBind;
import base.Constant;
import base.datatable.DataTablePage;
import base.models.BaseOrderDetail;
import com.google.common.collect.Lists;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by admin on 2016/10/11.
 */
@TableBind(tableName = "dele_order_detail")
public class OrderDetail extends BaseOrderDetail<OrderDetail> {
    public static OrderDetail dao = new OrderDetail();

//    /**
//     * 列表查询
//     *
//     * @param isSuperAdmin
//     * @param company
//     * @param start
//     * @param pageSize
//     * @param draw
//     * @return
//     */
//    public DataTablePage findByList(boolean isSuperAdmin, int company, int start, int pageSize, int draw) {
//        if (isSuperAdmin) {
//            return dataTable(start, pageSize, SqlManager.sql("orderDetail.column"), SqlManager.sql("orderDetail.where"), draw);
//        } else {
//            List<Object> params = Lists.newArrayList();
//            String whereSql = SqlManager.sql("orderDetail.where");
//            whereSql = whereSql.replace(Constant.SqlStrings.WHERE, " AND dod.company = ?");
//            params.add(company);
//            return dataTable(start, pageSize, SqlManager.sql("orderDetail.column"), whereSql, draw, params);
//        }
//    }
//
//    public DataTablePage findByListForDriver(boolean superAdmin, int companyId, int start, int pageSize, int draw) {
//        if (superAdmin) {
//            return dataTable(start, pageSize, SqlManager.sql("orderDetail.columnDriver"), SqlManager.sql("orderDetail.whereDriver"), draw);
//        } else {
//            List<Object> params = Lists.newArrayList();
//            String whereSql = SqlManager.sql("orderDetail.whereDriver");
//            whereSql = whereSql.replace(Constant.SqlStrings.WHERE, " AND ddi.company = ?");
//            params.add(companyId);
//            return dataTable(start, pageSize, SqlManager.sql("orderDetail.columnDriver"), whereSql, draw, params);
//        }
//    }
}
