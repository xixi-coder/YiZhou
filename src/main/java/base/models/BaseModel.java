package base.models;

import base.datatable.DataTablePage;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by BOGONm on 16/8/11.
 */
public class BaseModel<M extends Model> extends Model<M> implements Serializable {
    public DataTablePage dataTable(int pageStart, int pageSize, String select, String where, int draw) {
        M a = findFirst("select count(1) c " + where);
        int count = a == null ? 0 : a.getNumber("c").intValue();//一共多少条数据
        where = where + " LIMIT ?,?";
        List<M> data = find(select + where, pageStart, pageSize);
        List<Object> tmp = Lists.newArrayList();
        tmp.addAll(data);
        DataTablePage dataTablePage = new DataTablePage(tmp, draw, count, count);
        return dataTablePage;
    }

    public DataTablePage dataTable(int pageStart, int pageSize, String select, String where, int draw, Map<String, String[]> params) {
        Map<String, String> searchP = Maps.newHashMap();
        for (String s : params.keySet()) {
            if (s.startsWith("s-")) {//取到所有条件
                if (!Strings.isNullOrEmpty(params.get(s)[0])) {
                    searchP.put(s, params.get(s)[0]);
                }
            }
        }
        M a = findFirst("select count(1) c " + where);
        int count = a == null ? 0 : a.getNumber("c").intValue();//一共多少条数据
        where = where + " LIMIT ?,?";
        List<M> data = find(select + where, pageStart, pageSize);
        List<Object> tmp = Lists.newArrayList();
        tmp.addAll(data);
        DataTablePage dataTablePage = new DataTablePage(tmp, draw, count, count);
        return dataTablePage;
    }

    public DataTablePage dataTable(int pageStart, int pageSize, String select, String where, int draw, List<Object> params) {
        M a = findFirst("select count(1) c " + where, params.toArray());
        int count = a == null ? 0 : a.getNumber("c").intValue();//一共多少条数据
        where = where + " LIMIT ?,?";
        params.add(pageStart);
        params.add(pageSize);
        List<M> data = find(select + where, params.toArray());
        List<Object> tmp = Lists.newArrayList();
        tmp.addAll(data);
        DataTablePage dataTablePage = new DataTablePage(tmp, draw, count, count);
        return dataTablePage;
    }
}
