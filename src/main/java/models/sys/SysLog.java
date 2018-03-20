package models.sys;

import annotation.TableBind;
import base.datatable.DataTablePage;
import base.models.BaseSysLog;

/**
 * Created by admin on 2016/10/7.
 */
@TableBind(tableName = "dele_sys_log")
public class SysLog extends BaseSysLog<SysLog> {
    public static SysLog dao = new SysLog();

}
