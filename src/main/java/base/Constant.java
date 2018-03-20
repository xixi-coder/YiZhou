package base;

import com.google.common.collect.Lists;
import com.jfinal.plugin.redis.Redis;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;

/**
 * Created by BOGONm on 16/8/10.
 */
public class Constant {
    public static Properties properties;

    public static int SHUNFENGCHE_SHENHE = 1;

    //短信模板
    public final static String COMPANY = "司机点点";

    public static class downUrl {
        //域名访问的下载地址
        public final static String DOWNURL = "http://" + Constant.properties.getProperty("app.host") + "\\";
    }

    public final static String MENU_ECACHE = "resources_ecache";

    public final static String TOKEN = "token";

    public final static String SECRET = "secret";

    public final static String APPTYPE = "appType";

    public final static int iOS = 2;//iOS

    public final static int ANDROID = 1;//ANDROID

    public final static int DRIVER = 1;//司机端

    public final static int MEMBER = 2;//会员端

    public final static String CARSTR = "CAR-";//redis里面的司机车辆前缀

    public final static String LOGINMEMBER_CACHE = "login_member_cache";//redis的用户登陆信息缓存名称
    public final static String USECAR_ID_CACHE = "use_car_cache";//redis的使用车辆缓存名称
    public final static String WS_ID = "websocket_cache";//redis的使用车辆缓存名称

    public final static String MEMBER_ORDER_CACHE = "member_order_cache";//redis的用户订单信息缓存名称
    public final static String DRIVER_ORDER_CACHE = "driver_order_cache";//redis的用户订单信息缓存名称
    public final static String DRIVER_STATUS_CACHE = "driver_status_cache";//redis的司机状态

    public static class Cache {
        public final static Jedis JEDIS = Redis.use(Constant.DRIVER_STATUS_CACHE).getJedis();
    }

    public final static int LOGIN_TIMEOUT = 60 * 60 * 12 * 7; //登陆信息保存时间

    public final static int DRIVER_ONLINE = 60 * 60 * 12; //司机上线保存时间

    public final static int ORDER_UNDO_TIMEOUT = 5 * 60 * 1000; //订单超时时间  5分钟 单位ms

    //public final static int CAR_TIMEOUT = 60 * 4; //车辆信息保存时间

    public final static List<String> fristName = Lists.newArrayList();

    //数据结构里的CompanyId为公司的简称拼音，如：didi、shouqi等
    public final static String COMPANYID = "sjdd";

    // 数据结构里的Source 字段暂写0
    public final static String Source = "0";

    public static class UserType {
        public final static int NORMAL = 1;//普通后台用户
        public final static int UNION = 2;//联盟的用户
    }

    //验证码,密码等验证次数
    public static class Security {
        public final static int SECURITY_COUNT = 0;//记录次数
        public final static String SECURITY = "SECURITY";//记录获取验证码次数标识缓存
        //客户端 SECURITY_MEMBER_
        public final static String SECURITY_MEMBER_REGISTER = "SECURITY_MEMBER_REGISTER";//用户注册短信验证次数
        public final static String SECURITY_MEMBER_PASS = "SECURITY_MEMBER_PASS";//用户输入密码标识缓存
        public final static String SECURITY_MEMBER_RESETPDPASS = "SECURITY_MEMBER_RESETPDPASS";//用户重置密码标识缓存
        public final static String SECURITY_MEMBER_UPDATEPASS = "SECURITY_MEMBER_UPDATEPASS";//用户修改密码标识缓存
        //司机端 SECURITY_DRIVER_
        public final static String SECURITY_DRIVER_REGISTER = "SECURITY_DRIVER_REGISTER";//司机注册短信验证次数
        public final static String SECURITY_DRIVER_PASS = "SECURITY_DRIVER_PASS";//司机输入密码标识缓存
        public final static String SECURITY_DRIVER_RESETPDPASS = "SECURITY_DRIVER_RESETPDPASS";//司机重置密码标识缓存
        public final static String SECURITY_DRIVER_UPDATEPASS = "SECURITY_MEMBER_UPDATEPASS";//司机修改密码标识缓存
    }

    public static class Sms {
        public final static String SMS_CACHE = "sms_code_cache";//短信验证码缓存
        public final static int SMS_TYPE_REGISTER = 1;//注册短信模板
        public final static int SMS_TYPE_FORGETPW = 2;//忘记密码短信模板
        public final static int SMS_TYPE_CHANGEPH = 3;//更换手机号短信模板
        public final static int SMS_TYPE_DRIVERJIEDAN = 4;//司机接单
        public final static int SMS_TYPE_DRIVERDAODA = 5;//司机到达预约地
        public final static int SMS_TYPE_DRIVERCOMPLETEORDER = 6;//司机订单完成
        public final static int SMS_TYPE_DRIVERNEWORDER = 7;//司机有新订单
        public final static int SMS_TYPE_DRIVERRECHANGE = 8;//司机充值
        public final static int SMS_TYPE_DRIVERREGISTER = 9;//注册
        public final static int SMS_TYPE_ORDERCANCEL = 10;//订单取消
        public final static int SMS_TYPE_ACTIVITY = 11;//参与活动短信提醒
        public final static int SMS_TYPE_AMOUNTNOTENOUGH = 12;//司机余额短信提醒
        public final static int SMS_TYPE_BAOXIAN = 13;//保险单号短信
        public final static int SMS_TYPE_YUYUETOSIJI = 14;//预约订单短信提醒司机
        public final static int SMS_TYPE_YUYUETOHUIYUAN = 15;//预约订单短信提醒乘客
        public final static int SMS_TYPE_YUYUETOSIJI5 = 16;//预约订单要被销单前5分钟提醒司机
        public final static int SMS_TYPE_PAYED = 17;//乘客支付订单
        public final static int SMS_TYPE_Driver = 18;//推送订单司机
        public final static int SMS_TYPE_WEIXIN = 19;//微信订单
        public final static int SMS_TYPE_AUDIT_OK = 20;//审核通过
        public final static int SMS_TYPE_AUDIT_ERROR = 21;//审核不通过
    }

    public final static int MEMBER_TYPE_NORMAL = 2;//普通会员

    public final static int MEMBER_TYPE_DRIVER = 1;//专车司机注册

    public final static int MEMBER_TYPE_DAIJIA = 3;//代驾司机注册

    public final static String ADMIN_ACTION = "action";//后台操作类型

    public final static int ADMIN_ACTION_EDIT = 1;//后台操作类型

    public final static int ADMIN_ACTION_ADD = 0;//后台操作类型

    public final static BigDecimal DEFAULTSPEED = BigDecimal.valueOf(40);//默认速度40km/h

    public final static String EXCEL = "excel"; //excel

    /**
     * 菜单
     */
    public static String MENU = "menu";

    public static String BUTTON = "button";

    public static class OrderStatus {
        public final static int CREATE = 1;//订单创建
        public final static int ACCEPT = 2;//司机接单
        public final static int START = 3;//订单开始
        public final static int PAYED = 5;//订单已支付
        public final static int CANCEL = 6;//订单取消
        public final static int END = 4;//到达终点
        public final static int DRIVERWAIT = 7;//司机开始等待
        public final static int SFCACCEPT = 8;//顺风车司机接单
    }

    public static class MemberInfoStatus {
        public final static int OK = 1;//正常
        public final static int BLACK = 2;//黑名单用户
    }

    public static class TraverStatus {
        public final static int CREATE = 1;//创建行程
        public final static int ACCEPT = 2;//执行中行程
        public final static int ACTION = 6; //出发的行程
        public final static int END = 3;//结束行程
        public final static int CANCEL = 4;//取消行程
    }

    public static class ScheduleStatus {
        public final static int finish = 1;//已完成代办事项
        public final static int wait = 0;//未完成
    }

    public static class DispatchOrder {
        public final static int auto = 0;//自动派单
        public final static int hand = 1;//手动派单
    }

    /**
     * 专线全局变量
     */
    public static class LineStatus {
        public final static int ENABLE = 1;//路线可用
        public final static int DISABLE = 0;//路线不可用
    }

    public static class LineStatusByDriver {
        public final static int HASPASSAGES = 1;//司机已接到乘客，该司机其它线路不可用
        public final static int NOPASSAGES = 0;//司机未接到乘客，该司机所有线路均可接单
    }

    public static class OrderAction {
        public final static int CREATE = 1;//订单创建
        public final static int ACCEPT = 2;//司机接单
        public final static int START = 3;//订单开始
        public final static int END = 4;//到达终点
        public final static int PAYED = 5;//订单支付
        public final static int CANCEL = 6;//订单取消
        public final static int DRIVERARRIVE = 7;//司机到达目的地
        public final static int EDITEND = 8;//司机修改终点
    }

    public static class PayStatus {
        public final static int PAYED = 5;//订单已支付
        public final static int NOPAY = 6;//未支付
    }

    public static class RoyaltyType {
        public final static int EasyType = 1;//简单模式
        public final static int MoneyType = 2;//金额区间模式
    }

    public static class RoyaltyGetTime {
        public final static int AppointmentType = 1;//预约时间
        public final static int StartDj = 2;//开始代驾时间
        public final static int Settlement = 3;//结算时间
    }

    public static class FromType {
        public final static int APPTYPE = 1;//app注册
        public final static int WECHATTYPE = 2;//微信预约
        public final static int TELTYPE = 3;//电话预约
        public final static int BUDAN = 4;//服务人员补单
        public final static int TUIJIAN = 5;//推荐
        public final static int WECHATH5TYPE = 6;//微信H5
    }

    public static class DataAuditStatus {
        public final static int CREATE = 0;//创建
        public final static int AUDITOK = 1;//审核通过
        public final static int AUDITFAIL = 2;//审核不通过
    }

    public static class SqlStrings {
        public final static String WHERE = "-- where";
    }

    public static class DriverStatus {
        public final static int ShengHeTongGuo = 1;//审核通过
        public final static int FROZEN = 3;//冻结
    }

    public static class LoginStatus {
        public final static int LOGINED = 1;//已经登陆

        public final static int LOGOUTED = 0;//未登录

        public final static int RECIVEDORDERPD = 4;//接单属于拼单

        public final static int RECIVEDORDER = 3;//上线

        public final static int RECIVEDORDERPDNO = 5;//拼单已经满员不可再接拼单

        public final static int BUSY = 6;//表示已有订单，不是拼单
    }

    public static class DriverCarStatus {
        public final static int ShengHeTongGuo = 1;//审核通过
        public final static int ShengHeZhong = 0;//审核中
        public final static int ShengHeBuTongGuo = 3;//审核不通过
    }

    public static class AdType {
        public final static int Index = 1;
        public final static int Home = 2;
    }

    public static class ServiceType {
        //登陆了 但没有上线
        public final static int NoService = -1;
        public final static int AllService = 0;//全部服务类型
        public final static int ZhuanChe = 1;//专车
        public final static int DaiJia = 2;//代驾
        public final static int Taxi = 3;//出租车
        public final static int KuaiChe = 4;//快车
        public final static int ShunFengChe = 5;//顺风车
        public final static int ZhuanXian = 6;//城际专线
        public final static int HangKongZhuanXian = 7;//航空专线
    }

    public static class ServiceItemType {
        public final static int ShuShi = 43;
        public final static int HaoHua = 37;
        public final static int ShangWu = 45;
        public final static int DaiJia = 40;
        //包司机
        public final static int BSJ = 41;
        public final static int Taxi = 42;
        public final static int KuaiChe = 44;
        //市内
        public final static int ShiNei = 49;
        //跨城
        public final static int KuaCheng = 50;
        //带货
        public final static int DaiHuo = 51;
        //接机专线
        @Deprecated
        public final static int JieJiZhuanXian = 48;
        //送机专线
        @Deprecated
        public final static int SongJiZhuanXian = 46;
        @Deprecated
        public final static int ShiNeiZhuanXian = 52;
        /**
         * 航空专线 接机
         */
        public final static int HangKongZhuanXianJieJI = 56;

        /**
         * 航空专线 送机
         */
        public final static int HangKongZhuanXianSongJi = 57;
        /**
         * 城际专线
         */
        public final static int KuaChengZhuanXian = 47;

        /**
         * 航空专线 上线专用
         */
        public final static int HangKongZhuanXianShangXian = 58;
        /**
         * 城际专线 上线专用
         */
        public final static int KuaChengZhuanXianShangXian = 59;
    }

    public static class Distribution {
        public final static int memberStyle = 1;
        public final static int driverStyle = 2;
    }

    public static class OnlineOperatType {
        public final static int online = 1;
        public final static int downline = 0;
    }

    public static class CapitalOperationType {//会员账户操作类型
        public final static int TCOFDJ = 1;//代驾提成
        public final static int TCOFZC = 2;//专车提成
        public final static int TCOFKC = 3;//快车提成
        public final static int TCOFCZ = 4;//出租车提成
        public final static int TCOFSF = 5;//顺风车提成
        @Deprecated
        public final static int TCOFZX = 6;//专线提成
        public final static int TCOFHKZX = 7;//航空专线提成

        public final static int CZOFHT = 11;//后台充值
        public final static int CZOFAPP = 12;//APP充值
        public final static int CZOFali = 13;//支付宝充值
        public final static int CZOFaliVIP = 131;//支付宝充值VIP
        public final static int CZOFweixin = 14;//微信充值
        public final static int CZOFweixinVIP = 141;//微信充值VIP

        public final static int DJDAISHOU = 21;//代驾代收费用
        public final static int ZCDAISHOU = 22;//专车代收费用
        public final static int CZDAISHOU = 23;//出租代收费用
        public final static int KCDAISHOU = 24;//快车代收费用
        public final static int SHUNFENGCHE = 25;//顺风车代收费用
        public final static int ZHUANXIAN = 26;//专线代收费用
        public final static int HANGKONGZHUANXIAN = 27;//专线代收费用

        public final static int TUIJIANSIJIJIANGLI = 31;//推荐司机奖励
        public final static int TUIJIANGHUIYUANJIANGLI = 32;//推荐会员奖励

        public final static int ACTIVITYJIANGLI = 41;//参加活动奖励

        public final static int CASHSHOURU = 51;//现金收入

        public final static int REWARD = 61;//后台奖励的金额

        public final static int BAOXIAN = 71;//保险费

        public final static int TX = 81;//提现
    }

    public static class MemberLevel {
        public final static int PERSONAL = 1;//个人客户
        public final static int COMPANY = 2;//企业客户
    }

    public static class JpushType {
        //ws推送
        public final static int PushOrderToDriver = 1;//下单推送订单给司机
        public final static int PushToMember = 2;//司机接单推送给会员
        public final static int PushMemberPay = 3;//推送提示会员支付订单
        public final static int PushDriverCancel = 4;//推送司机提示订单被销单
        public final static int PushActivity = 5;//参加活动
        public final static int PushNotice = 6;//推送通知
        public final static int PushOrderToShunFengCheDriver = 7;
        public final static int PushToShunFengCheCustomer = 8;
        public final static int PushToCustomerConfig = 9;
        public final static int PushToCustomerStart = 10;
        public final static int PushToZhuanXian = 12;
        public final static int PushDriverSetOffToMember = 22;//司机出发推送给会员
        public final static int PushDriverWaitToMember = 23;//司机等待推送给会员
    }

    public static class AliPayOrderType {
        public final static String NomorlOrder = "SJDD";//普通的订单
        public final static String CZFRIVER = "SJCZ";//司机充值
        public final static String CZCUSTOMER = "CUCZ";//会员充值
    }

    public static class PayType {
        public final static int Alipay = 1; //支付宝支付
        public final static int Collection = 2;//代收类型
        public final static int YE = 3;//余额支付
        public final static int WECHAT = 4;//微信支付
        public final static int WECHATTHEPUBLIC = 5;//微信公众号支付
        public final static int LITEAPP = 6;//微信小程序支付
    }

    public static class CompanyOperteType {
        public final static int TCD = 1;//司机提成成
        public final static int BDD = 2;//司机报单
        public final static int BXF = 3;//保险费
        public final static int HDJEZC = 4;//活动金额支出
        public final static int HDJEZR = 5;//活动金额支入
    }

    public static class CompanyAccountActivity {
        public final static int awardMember = 1;//将优惠券赠送给会员
        public final static int createcoupon = 2;//生成优惠券
        public final static int awardDriver = 3;//后台奖励给司机
        public final static int rewardMCal = 4;//推荐奖励会员
        public final static int rewardDCal = 5;//推荐奖励司机
    }

    public static class CouponFromType {
        public final static int Demo = 1;//来源1
    }

    public static class CouponType {
        public final static int Normal = 1;//普通券
        public final static int Discount = 2;//折扣券
    }

    public static class CouponStatus {
        public final static int USED = 1; //已使用
        public final static int DISABLE = 2;//冻结
        public final static int USEFUL = 0;//可用的
    }

    public static class ActivityEvent {
        public final static int REGISTER = 1;//注册时间
        public final static int FRISTORDER = 2;//首次下单
        public final static int TIMEORDER = 3;//时间段下单
        public final static int OPENAPP = 4;//打开app
    }

    public static class ActivityType {//活动的返利类型
        public final static int yue = 1;//余额
        public final static int coupon = 2;//优惠券

    }

    public static class CapitalStatus {
        public final static int OK = 1;//成功的
        public final static int ERROR = 0;//失败
    }

    public static class CompanyAcitivyType {
        public final static int SANJIFENXIAO = 1;//三级分销奖励的金额
        public final static int ACTIVITYCOUPON = 2;//活动赠送的优惠券
        public final static int DINGXIANGCOUPON = 3;//定向赠送
        public final static int CODECOUPON = 4;//兑换码领取
        public final static int HUODONGJIANGLI = 5;//活动奖励余额
        public final static int SIJIJIANGLI = 6;//司机奖励金额
    }

    //网约车接口操作标识
    public static class Flag {
        public final static int ADD = 1;//新增
        public final static int UPDATE = 2;//更新
        public final static int DELETE = 3;//删除
    }

    //网约车接口地图类型
    public static class MapType {
        public final static int BAIDU = 1;//百度地图
        public final static int GAODE = 2;//高德地图
        public final static int OTHER = 3;//其他
    }

    public final static DecimalFormat DCMFMT = new DecimalFormat("0");
    public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("######0.00");

    //1是司机端 2是客户端
    public static class PushType {
        public final static int SJ = 1;
        public final static int CH = 2;
        //个推
        public final static int GT = 1;
        //极光
        public final static int JG = 2;
    }

    public static class LoginType {
        public final static int SMS = 1;
        public final static int PWD = 2;
    }

    public static class OrderPdFlag {
        //拼单
        public final static int PD = 1;
        //不拼单
        public final static int BPD = 0;
    }

    public static class VipActivityFlag {
        //送矿泉水
        public final static int ACTIVITY_KQC = 1;
    }

    public static class BSJ {
        //包司机最低用时标准4个小时
        public final static BigDecimal MINIMUM_STANDARDS_TIME = new BigDecimal(240.0);
        //一天的
        public final static BigDecimal ONE_DAY_TIME = new BigDecimal(1440.0);
        //包司机最低距离标准300公里
        public final static BigDecimal MINIMUM_STANDARDS_DISTANCE = new BigDecimal(300.0);
        //一天的600公里
        public final static BigDecimal ONE_DAY_TIME_DISTANCE = new BigDecimal(600.0);
    }

    //任务调度
    public static class Task {
        //完成订单统计任务id
        public final static int COUNT_COMPLETE_ORDER_ID = 1000;
    }

    //服务器内网
    public static String MASTER_1_IP = "10.174.119.209";
    public static String MASTER_2_IP = "10.47.81.136";
}
