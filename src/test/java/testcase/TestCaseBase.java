package testcase;

import annotation.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import org.junit.After;
import org.junit.BeforeClass;
import plugin.Scan;
import plugin.SqlInXmlPlugin;

import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2016/10/24.
 */
public class TestCaseBase {
    private static final List<String> basePackage = Lists.newArrayList(); //model包
    protected static C3p0Plugin dp;
    protected static ActiveRecordPlugin activeRecord;

    /**
     * 数据连接地址
     */
    private static final String URL = "jdbc:mysql://121.40.142.141:3306/dele_special_car_dev?useUnicode=true&characterEncoding=utf-8";

    /**
     * 数据库账号
     */
    private static final String USERNAME = "root";

    /**
     * 数据库密码
     */
    private static final String PASSWORD = "root";

    /**
     * 数据库驱动
     */
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    protected static void componentSscan(List<String> modelPackage) {
        modelPackage.add("models");
    }

    /**
     * 数据库类型（如mysql，oracle）
     */
    private static final String DATABASE_TYPE = "mysql";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        dp = new C3p0Plugin(URL, USERNAME, PASSWORD, DRIVER, 100, 2, 20, 10, 20);
        dp.getDataSource();
        dp.start();
        activeRecord = new ActiveRecordPlugin(dp);
        activeRecord.setDialect(new MysqlDialect());
        componentSscan(basePackage);
        Scan driven = new Scan();
        for (String pake : basePackage) {
            Set<Class<?>> clazzs = driven.getClasses(pake);
            for (Class<?> clazz : clazzs) {
                if (Model.class.isAssignableFrom(clazz)) {
                    TableBind model = clazz.getAnnotation(TableBind.class);
                    if (null != model) {
                        activeRecord.addMapping(model.tableName(), model.pks(), (Class<? extends Model<?>>) clazz);
                    }
                }
            }
        }
        new SqlInXmlPlugin().start();
        activeRecord.start();
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        activeRecord.stop();
        dp.stop();
    }
}
