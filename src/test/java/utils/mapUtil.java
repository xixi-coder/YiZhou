package utils;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.xiaoleilu.hutool.util.NetUtil;
import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Administrator on 2017/7/14.
 */
public class mapUtil {

    private boolean t(Long onlineTime) {
        long t = System.currentTimeMillis() - onlineTime;
        if (t > (15 * 60 * 1000)) {
            return true;
        }
        return false;
    }
    
    @Test
    public void test()throws Exception {
        System.out.println(getLocalIP());
    }
    
    public  boolean isWindowOS() {
        boolean isWindowOS = false;
        String osName = System.getProperty("os.name");
        if(osName.toLowerCase().indexOf("windows") > -1) {
            isWindowOS = true;
        }
        return isWindowOS;
    }
    
    public  InetAddress getInetAddress() {
        InetAddress inetAddress = null;
        try {
            //如果是windows操作系统
            if (isWindowOS()) {
                inetAddress = InetAddress.getLocalHost();
            } else {
                boolean bFindIP = false;
                //定义一个内容都是NetworkInterface的枚举对象
                Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>)
                NetworkInterface.getNetworkInterfaces();
                //如果枚举对象里面还有内容(NetworkInterface)
                while (netInterfaces.hasMoreElements()) {
                    if (bFindIP) {
                        break;
                    }
                    //获取下一个内容(NetworkInterface)
                    NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                    //----------特定情况，可以考虑用ni.getName判断
                    //遍历所有IP
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    while (ips.hasMoreElements()) {
                        inetAddress = (InetAddress) ips.nextElement();
                        if (inetAddress.isSiteLocalAddress()         //属于本地地址
                        && !inetAddress.isLoopbackAddress()  //不是回环地址
                        && inetAddress.getHostAddress().indexOf(":") == -1) {   //地址里面没有:号
                            bFindIP = true;     //找到了地址
                            break;              //退出循环
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inetAddress;
    }
    
    public  String getLocalIP() {
        return getInetAddress().getHostAddress();
    }

}
