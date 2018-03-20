/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/sys/smstmp/list";
    var columns = [
        {
            "data": "type", "render": function (a) {
            if (a == 1) {
                return '注册短信模板'
            } else if (a == 2) {
                return '忘记密码短信模板'
            } else if (a == 3) {
                return '更换手机号短信模板'
            } else if (a == 4) {
                return '司机接单短信'
            } else if (a == 5) {
                return '司机到达预约地'
            } else if (a == 6) {
                return '司机订单完成'
            } else if (a == 7) {
                return '司机有新订单'
            } else if (a == 8) {
                return '司机充值'
            } else if (a == 9) {
                return '注册'
            } else if (a == 10) {
                return '取消短信模板'
            } else if (a == 11) {
                return '活动奖励提醒短信模板'
            } else if (a == 12) {
                return '司机余额短信提醒模板'
            } else if (a == 13) {
                return '保险单号短信模板'
            } else if (a == 14) {
                return '预约订单短信提醒司机模板'
            } else if (a == 15) {
                return '预约订单短信提醒乘客模板'
            } else if (a == 16) {
                return '预约订单要被销单前5分钟提醒司机模板'
            } else if (a == 18) {
                return '推送订单司机短信模板'
            } else if (a == 19) {
                return '微信下单短信模板'
            }else if (a == 20) {
                return '司机审核通过模板'
            }else if (a == 21) {
                return '司机审核不通过模板'
            } else {
                return '未知类型'
            }
        }
        }, {"data": "cname"},
        {"data": "create_time"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];

    var table = dt.build($('#smstmp-list'), url, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/smstmp/del',
                    data: {id: id},
                    type: 'POST',
                    success: function (data) {
                        if (data.status == 'OK') {
                            layer.msg(data.msg);
                            window.location.reload();
                        } else {
                            layer.msg(data.msg);
                        }
                    }
                })
            });
        });
    });
    $('.search').on('click', function () {
        table.ajax.reload();
    });
});
