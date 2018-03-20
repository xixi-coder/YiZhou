/**
 * Created by Administrator on 2016/9/22.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/activity/list/list";
    var columns = [
        {"data": "companyName"},
        {"data": "name"},
        {
            "data": "event",
            'render': function (display, cell, row) {
                if (display == 1) {
                    return '注册';
                } else if (display == 2) {
                    return '首次下单';
                } else if (display == 3) {
                    return '下单';
                } else if (display == 4) {
                    return '打开APP活动';
                }
            }
        },
        // {"data": "rebate"},
        {
            "data": "coupon_title",
            'render': function (display, cell, row) {
                if (row.type == 1) {
                    return '奖励现金:' + row.rebate + '元';
                } else if (row.type == 2) {
                    return '奖励优惠券:【' + row.coupon_title + "】";
                }
            }
        },
        {"data": "create_time"},
        {"data": "start_time"},
        {"data": "end_time"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];

    var table = dt.build($('#activity'), url, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/activity/list/del',
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
    })
})
