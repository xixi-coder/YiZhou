/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/member/list/capitallist/" + id;
    var columns = [
        {
            "data": "operation_type",
            'render': function (a) {
                if (a == 1) {
                    return '代驾提成'
                } else if (a == 2) {
                    return '专车提成'
                } else if (a == 3) {
                    return '出租车提成'
                } else if (a == 4) {
                    return '快车提成'
                } else if (a == 11) {
                    return '后台充值'
                } else if (a == 12) {
                    return 'APP充值'
                } else if (a == 13) {
                    return '支付宝充值'
                } else if (a == 14) {
                    return '微信充值'
                } else if (a == 21) {
                    return '代驾代收费用'
                } else if (a == 22) {
                    return '专车代收费用'
                } else if (a == 23) {
                    return '出租代收费用'
                } else if (a == 24) {
                    return '快车代收费用'
                } else if (a == 31) {
                    return '推荐司机奖励'
                } else if (a == 32) {
                    return '推荐会员奖励'
                } else if (a == 41) {
                    return '参加活动奖励'
                } else if (a == 61) {
                    return '后台奖励'
                } else if (a == 71) {
                    return '保险费'
                } else if (a == 81) {
                    return '提现'
                } else {
                    return '未知类型'
                }
            }
        },
        {"data": "amount"},
        {"data": "create_time"},
        {
            "data": "remark", 'render': function (a) {
            return order.getOrderNo(a);
        }
        },
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];

    dt.build($('#capital'), url, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/list/capitaldel',
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
})
