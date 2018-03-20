package plugin;

import com.jfinal.plugin.IPlugin;
import plugin.sqlInXml.SqlManager;

/**
 * Created by BOGONm on 16/8/8.
 */
public class SqlInXmlPlugin implements IPlugin {
    @Override
    public boolean start() {
        try {
            SqlManager.parseSqlXml();
        } catch (Exception e) {
            new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean stop() {
        SqlManager.clearSqlMap();
        return true;
    }

}
