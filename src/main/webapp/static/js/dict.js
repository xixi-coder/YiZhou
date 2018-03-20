/**
 * Created by admin on 2016/10/17.
 */
var dict = {
    orderStatus: function (val) {
        switch (val + '') {//订单的状态
            case '1':
                return '订单创建';
            case '2':
                return '司机接单';
            case '3':
                return '订单开始';
            case '4':
                return '到达终点';
            case '5':
                return '订单已支付';
            case '6':
                return '订单取消';
            case '9':
                return '订单完成';
            default:
                return '';
        }
    },
    orderAction: function (val) {//订单的操作行为
        switch (val + '') {
            case '1':
                return '订单创建';
            case '2':
                return '司机接单';
            case '3':
                return '订单开始';
            case '4':
                return '到达终点';
            case '5':
                return '订单支付';
            case '6':
                return '订单取消';
            case '7':
                return '司机到达目的地';
            default:
                return '';
        }
    },
    payStatus: function (val) {//支付的状态
        switch (val + '') {
            case '5':
                return '订单已支付';
            case '6':
                return '未支付';
            default:
                return '';
        }
    },
    serviceType:function(val){//服务类型
        switch (val + '') {
            case '1':
                return '专车';
            case '2':
                return '代驾';
            case '3':
                return '出租车';
            case '4':
                return '快车';
            default:
                return '';
        }
    },
    memberLevel:function(val){//服务类型
        switch (val + '') {
            case '1':
                return '个人客户';
            case '2':
                return '企业客户';
            default:
                return '';
        }
    },
    capitalOperationType:function(val){//操作会员账户的类型
        switch (val+''){
            case '1':
                return '代驾提成';
            case '2':
                return '专车提成';
            case '3':
                return '快车提成';
            case '4':
                return '出租车提成';
            case '11':
                return '后台充值';
            case '12':
                return 'APP充值';
            case '13':
                return '支付宝充值';
            case '14':
                return '微信充值';
            case '21':
                return '代收费用';
            default:
                return '' ;
        }
    },
    orderFormType:function(val){
        switch (val + '') {//订单的来源类型
            case '1':
                return 'app注册';
            case '2':
                return '微信预约';
            case '3':
                return '电话预约';
            case '4':
                return '服务人员补单';
            default:
                return '';
        }
    },
    loginStatus:function(val){//司机和客户登录状态
        switch (val + '') {
            case '1':
                return '已经登陆';
            case '2':
                return '未登录';
            case '3':
                return '接单中';
            case '4':
                return '接单属于拼单';
            case '5':
                return '拼单已经满员不可再接拼单';
            default:
                return '';
        }
    },
    driverCarAduitStatus:function(val){//司机车辆审核状态
        switch (val + '') {
            case '1':
                return '审核通过';
            case '0':
                return '审核中';
            case '3':
                return '审核不通过';
            default:
                return '';
        }
    }
}