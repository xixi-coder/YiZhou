package models.sys;


import annotation.TableBind;
import base.Constant;
import base.models.BaseZxLine;
import com.jfinal.plugin.activerecord.Db;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
@TableBind(tableName = "dele_zx_line")
public class ZxLine extends BaseZxLine<ZxLine> {
	public static final ZxLine dao = new ZxLine();

	public int updateflag(int id){
		 return Db.update(SqlManager.sql("zxLine.updateflag"), 1, id);
	}

	public List<ZxLine> findZxLineList(int type,String adCode){
		return find(SqlManager.sql("zxLine.findZxLineList"),type,adCode);
	}

	public List<ZxLine> findByStartCityCode(int type,String adCode){
		return find(SqlManager.sql("zxLine.findByStartCityCode"),type,adCode);
	}
}
