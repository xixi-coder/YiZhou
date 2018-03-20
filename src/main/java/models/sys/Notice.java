package models.sys;

import annotation.TableBind;
import base.models.BaseNotice;
import plugin.sqlInXml.SqlManager;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/11/4.
 */
@TableBind(tableName = "dele_notice")
public class Notice extends BaseNotice<Notice> {
    public static Notice dao = new Notice();

    /**
     * 通过公司查询
     *
     * @param id
     * @param now
     * @param reciverType
     * @return
     */
    public List<Notice> findByCompanyAndDate(Integer id,int reciverType, Date now) {
        return find(SqlManager.sql("notice.findByCompanyAndDate"), id, 1, reciverType,-1, now);
    }

    /**
     * 通过公司和类型查询
     *
     * @param id
     * @param now
     * @param reciverType
     * @return
     */
    public List<Notice> findByCompanyAndDate(Integer id,int TypeId,int reciverType, Date now) {
        return find(SqlManager.sql("notice.findByCompanyAndDate"), id, TypeId, reciverType,-1, now);
    }

    /**
     * 通过开始和结束日期查询通知和短信
     *
     * @param now
     * @return
     */
    public List<Notice> findNoticeAndSmsByDate(Date now) {
        return find(SqlManager.sql("notice.findNoticeAndSmsByDate"), 2, now);
    }

    public List<Notice> findMessageByCompany(Integer id, Date start, Date end) {
        return find(SqlManager.sql("notice.findMessageByCompany"), id, 2, 2, start,end);
    }
}
