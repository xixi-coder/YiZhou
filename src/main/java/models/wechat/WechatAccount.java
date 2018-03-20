package models.wechat;

import annotation.TableBind;
import base.models.BaseWechatAccount;
import plugin.sqlInXml.SqlManager;

/**
 * Created by admin on 2016/9/30.
 */
@TableBind(tableName = "dele_wechat_account")
public class WechatAccount extends BaseWechatAccount<WechatAccount> {
    public static WechatAccount dao = new WechatAccount();

    /**
     * 查询微信公众号信息
     *
     * @return
     */
    public WechatAccount findFrist() {
        return findFirst(SqlManager.sql("wechatAccount.findFrist"));
    }
}
