package base.config;

import annotation.Controller;
import annotation.TableBind;
import base.Constant;
import com.google.common.collect.Lists;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import net.dreamlu.event.EventPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.QuartzPlugin;
import plugin.Scan;
import plugin.SqlInXmlPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by BOGONm on 16/8/8.
 */
public abstract class JFinalConfig extends com.jfinal.config.JFinalConfig {

    final Logger logger = LoggerFactory.getLogger(JFinalConfig.class);

    private static final List<String> controlPackage = Lists.newArrayList();//控制器包

    private static final List<String> basePackage = Lists.newArrayList(); //model包

    //private static final List<String> interceptorPackage = Lists.newArrayList();

    /**
     * Scan control
     *
     * @param controlPackage 存储需要扫描的control包
     */
    protected abstract void controlSscan(List<String> controlPackage);

    /**
     * Scan basePackage
     *
     * @param modelPackage 存储需要扫描的model包
     */
    protected abstract void componentSscan(List<String> modelPackage);

    /**
     * Scan interceptor
     *
     * @param interceptorPackage 存储需要扫描的拦截器
     */
    //protected abstract void interceptorSscan(List<String> interceptorPackage);

    /**
     * 设置数据源
     *
     * @param showSql 是否显示SQL， true为现实，默认为false
     * @return IDataSourceProvider
     */
    protected abstract IDataSourceProvider setDataSource(boolean showSql);

    /**
     * controller
     *
     * @param me
     */
    @Override
    public void configRoute(Routes me) {
        controlSscan(controlPackage);//获取需要扫描的包
        //扫描器
        Scan driven = new Scan();
        for (String pake : controlPackage) {
            Set<Class<?>> clazzs = driven.getClasses(pake);
            for (Class<?> clazz : clazzs) {
                logger.debug("Controller:{}", clazz.getName());
                if (com.jfinal.core.Controller.class.isAssignableFrom(clazz)) {
                    Controller con = clazz.getAnnotation(Controller.class);
                    if (null != con) {
                        me.add(con.value(), (Class<? extends com.jfinal.core.Controller>) clazz);
                    }
                }
            }
        }
    }

    /**
     * model
     *
     * @param me
     */
    @Override
    public void configPlugin(Plugins me) {
        componentSscan(basePackage);
        IDataSourceProvider iDataSourceProvider = setDataSource(false);
        try {
            me.add((IPlugin) iDataSourceProvider);
        } catch (Exception e) {
            logger.error("{} is not IPlugin type", IPlugin.class.getName());
            throw new RuntimeException("is not IPlugin type");
        }
        ActiveRecordPlugin arp = new ActiveRecordPlugin(iDataSourceProvider);
        arp.setShowSql(Boolean.parseBoolean(getProperty("showSql", "false")));
        addActiveRecord(arp); // 加入附加的活动记录
        Scan driven = new Scan();
        for (String pake : basePackage) {
            Set<Class<?>> clazzs = driven.getClasses(pake);
            for (Class<?> clazz : clazzs) {
                logger.debug("models:{}", clazz.getName());
                if (com.jfinal.plugin.activerecord.Model.class.isAssignableFrom(clazz)) {
                    TableBind model = clazz.getAnnotation(TableBind.class);
                    if (null != model) {
                        arp.addMapping(model.tableName(), model.pks(), (Class<? extends Model<?>>) clazz);
                    }
                }
            }
        }
        me.add(arp);
        me.add(new SqlInXmlPlugin());
        me.add(new EhCachePlugin(this.getClass().getClassLoader().getResource("ehcache.xml").getPath()));
        me.add(new RedisPlugin(Constant.LOGINMEMBER_CACHE, getProperty("redis.host", "127.0.0.1"), getPropertyToInt("redis.port", 6379), 5000, getProperty("redis.pass", "HGsjdd2017Redis")));
        me.add(new RedisPlugin(Constant.USECAR_ID_CACHE, getProperty("redis.host", "127.0.0.1"), getPropertyToInt("redis.port", 6379), 5000, getProperty("redis.pass", "HGsjdd2017Redis")));
        me.add(new RedisPlugin(Constant.WS_ID, getProperty("redis.host", "127.0.0.1"), getPropertyToInt("redis.port", 6379), 5000, getProperty("redis.pass", "HGsjdd2017Redis")));
        me.add(new RedisPlugin(Constant.MEMBER_ORDER_CACHE, getProperty("redis.host", "127.0.0.1"), getPropertyToInt("redis.port", 6379), 5000, getProperty("redis.pass", "HGsjdd2017Redis")));
        me.add(new RedisPlugin(Constant.DRIVER_ORDER_CACHE, getProperty("redis.host", "127.0.0.1"), getPropertyToInt("redis.port", 6379), 5000, getProperty("redis.pass", "HGsjdd2017Redis")));
        me.add(new RedisPlugin(Constant.DRIVER_STATUS_CACHE, getProperty("redis.host", "127.0.0.1"), getPropertyToInt("redis.port", 6379), 5000, getProperty("redis.pass", "HGsjdd2017Redis")));
        me.add(new QuartzPlugin());
        // 初始化插件
        EventPlugin eventPlugin = new EventPlugin();
        eventPlugin.async();
        eventPlugin.scanPackage("jobs");
        me.add(eventPlugin);
        String a = "欧阳、太史、端木、上官、司马、东方、独孤、南宫、万俟、闻人、夏侯、诸葛、尉迟、公羊、赫连、澹台、皇甫、宗政、濮阳、公冶、太叔、申屠、公孙、慕容、仲孙、钟离、长孙、宇文、司徒、鲜于、司空、闾丘、子车、亓官、司寇、巫马、公西、颛孙、壤驷、公良、漆雕、乐正、宰父、谷梁、拓跋、夹谷、轩辕、令狐、段干、百里、呼延、东郭、南门、羊舌、微生、公户、公玉、公仪、梁丘、公仲、公上、公门、公山、公坚、左丘、公伯、西门、公祖、第五、公乘、贯丘、公皙、南荣、东里、东宫、仲长、子书、子桑、即墨、达奚、褚师、吴铭";
        String[] fr = a.split("、");
        Constant.fristName.addAll(Arrays.asList(fr));
    }


    /**
     * 这里进行附加的活动记录的添加，
     *
     * @param arp 活动记录插件
     */
    protected void addActiveRecord(ActiveRecordPlugin arp) {
    }
}
