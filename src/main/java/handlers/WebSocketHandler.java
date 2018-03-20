package handlers;

import com.jfinal.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by BOGONm on 16/8/8.
 */
public class WebSocketHandler extends Handler {
    Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    public void handle(String target, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, boolean[] isHandled) {
        //对于websocket 不交予 jfinal 处理
        if (target.indexOf("/socket") == -1) {
            nextHandler.handle(target, httpServletRequest, httpServletResponse, isHandled);
        }
        logger.info("访问的websocket地址是{}", target);
    }
}
