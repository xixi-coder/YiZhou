package dto.websocket;

/**
 * Created by admin on 2016/11/5.
 */
public class WSContants {
    public static final int ping = 1;//握手
    public static final int pong = 2;//握手
    public static final int order =3;//订单消息
    public static final int notice=4;//普通推送消息
    public static final int activity=5;//参加活动推送消息
}
