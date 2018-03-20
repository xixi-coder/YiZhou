package models.wechat;

import annotation.TableBind;
import base.models.BaseWechatMenu;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by admin on 2016/9/30.
 */
@TableBind(tableName = "dele_wechat_menu")
public class WechatMenu extends BaseWechatMenu<WechatMenu> {
    public static WechatMenu dao = new WechatMenu();

    /**
     * 通过父级查找
     *
     * @param parent
     * @return
     */
    public List<WechatMenu> findByParent(int parent) {
        return find(SqlManager.sql("wechatMenu.findByParent"), parent);
    }

    /**
     * 查询所有的自定义菜单
     *
     * @return
     */
    public List<WechatMenu> findAll() {
        return find(SqlManager.sql("wechatMenu.findAll"));
    }
}
