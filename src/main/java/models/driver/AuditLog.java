package models.driver;

import annotation.TableBind;
import base.models.BaseAuditLog;

/**
 * Created by Administrator on 2016/10/6.
 */
@TableBind(tableName = "dele_audit_log")
public class AuditLog extends BaseAuditLog<AuditLog> {
    public static AuditLog dao = new AuditLog();

    public AuditLog findByLicense(int license) {
        return findFirst("select * from dele_audit_log where license = ?", license);
    }

}
