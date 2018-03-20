package plugin.sqlInXml;

import com.google.common.collect.Maps;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by BOGONm on 16/8/8.
 */
public class SqlManager {
    private static Map<String, String> sqlMap = Maps.newHashMap();

    public static String sql(String groupNameAndsqlId) {
        return sqlMap.get(groupNameAndsqlId);
    }

    public static void clearSqlMap() {
        sqlMap.clear();
    }

    public static List<File> findSqlFile(String path) {
        List<File> sqlFiles = new ArrayList<File>();
        String tempName = null;
        // 判断目录是否存在
        String baseDirName = path;
        File baseDir = new File(baseDirName);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            System.err.println("search error：" + baseDirName + "is not a dir！");
        } else {
            String[] filelist = baseDir.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(baseDirName + File.separator
                        + filelist[i]);
                if (!readfile.isDirectory()) {
                    tempName = readfile.getName();
                    if (tempName.endsWith("-sql.xml")) {
                        sqlFiles.add(readfile);
                    }
                } else if (readfile.isDirectory()) {
                    sqlFiles.addAll(findSqlFile(baseDirName + File.separator
                            + filelist[i]));
                }
            }
        }
        return sqlFiles;
    }

    public static void parseSqlXml() {
        List<File> files = findSqlFile(SqlManager.class.getClassLoader().getResource("sqlxml").getPath());
        for (File xmlfile : files) {
            SqlGroup group = null;
            try {
                JAXBContext context = JAXBContext.newInstance(SqlGroup.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                group = (SqlGroup) unmarshaller.unmarshal(xmlfile);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
            String name = group.name;
            if (name == null || name.trim().equals("")) {
                name = xmlfile.getName();
            }
            for (SqlItem sqlItem : group.sqlItems) {
                sqlMap.put(name + "." + sqlItem.id, sqlItem.value);
            }
        }
    }
}
