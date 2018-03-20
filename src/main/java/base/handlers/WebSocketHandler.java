package base.handlers;

import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin on 2016/10/11.
 */
public class WebSocketHandler extends Handler {
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        //对于websocket 不交予 jfinal 处理
        if (target.indexOf("socket") == -1) {
            nextHandler.handle(target, request, response, isHandled);
        }
    }
}
