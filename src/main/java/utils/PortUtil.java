package utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/11/2.
 */
public class PortUtil {

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            // logger.info("~~~~~~~~first condition:x-forwarded-for");
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            //logger.info("~~~~~~~~second condition:WL-Proxy-Client-IP");
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            //logger.info("~~~~~~~~third condition:getremoteaddr");
            ipAddress = request.getRemoteAddr();
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        System.out.println("IP地址为：" + ipAddress);
        return ipAddress;
    }

    public static String getPort(HttpServletRequest request) {
        String port = request.getLocalPort() + "";
        System.out.println("端口号为：" + port);
        return port;
    }

}
