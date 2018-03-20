package config;


import base.Constant;
import base.config.JFinalConfig;
import base.handlers.WebSocketHandler;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.core.Const;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.FreeMarkerRender;

import java.util.List;
import java.util.Properties;

/**
 * Created by BOGONm on 16/8/8.
 */
public class Config extends JFinalConfig {

    protected void controlSscan(List<String> controlPackage) {
        controlPackage.add("controllers");
    }

    protected void componentSscan(List<String> modelPackage) {
        modelPackage.add("models");
    }

    protected IDataSourceProvider setDataSource(boolean showSql) {
        C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password"), getProperty("driverClass"), getPropertyToInt("maxPoolSize", 100), getPropertyToInt("minPoolSize", 2), getPropertyToInt("initialPoolSize", 20), getPropertyToInt("maxIdleTime", 10), getPropertyToInt("acquireIncrement", 20));
        return c3p0Plugin;
    }

    public void configConstant(Constants me) {
        Constant.properties = loadPropertyFile("config.properties");
        me.setDevMode(getPropertyToBoolean("devMode", true));
        me.setFreeMarkerViewExtension(".ftl");
        me.setError401View("/views/errors/401.html");
        me.setError403View("/views/errors/403.html");
        me.setError404View("/views/errors/404.html");
        me.setError500View("/views/errors/500.html");
        me.setBaseViewPath("views");
        me.setMaxPostSize(100 * Const.DEFAULT_MAX_POST_SIZE);
    }

    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    public void configHandler(Handlers handlers) {
        handlers.add(new WebSocketHandler());
        handlers.add(new ContextPathHandler("ctx"));
    }

    public void afterJFinalStart() {
        Properties p = loadPropertyFile("freemarker.properties");
        //由于我们用到freemarker，所以在此进行freemarker配置文件的装载
        FreeMarkerRender.setProperties(p);

    }
}
