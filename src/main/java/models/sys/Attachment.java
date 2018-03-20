package models.sys;

import annotation.TableBind;
import base.models.BaseAttachment;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/8/23.
 */
@TableBind(tableName = "dele_attachment")
public class Attachment extends BaseAttachment<Attachment> {
    public static Attachment dao = new Attachment();

}
