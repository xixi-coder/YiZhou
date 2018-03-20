
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/schedule/order/list?type" + $('#orderType').val(),
    };

    var columns = [
        {
            "width": '100px',
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id, status: row.status,auto_dispatch_order:row.auto_dispatch_order});
            }
        },
        {
            "data": "no",
            'render': function (display, cell, row) {
                return '<a href="' + g.ctx + '/admin/order/lockup/' + row.id + '">' + display + '</a>';
            }
        },
        {
            "data": "company_name"
        },
        {
            "data": "status",
            'render': function (display, cell, row) {
                if (display == 1) {
                    return '<span class="label label-default">未派单</span>';
                } else if (display == 2 || display == 8) {
                    return '<span class="label label-info">已接单</span>';
                } else if (display == 3) {
                    return '<span class="label label-primary">执行中</span>';
                } else if (display == 4) {
                    return '<span class="label" style="background-color: #66CCFF">到达终点</span>';
                } else if (display == 6) {
                    return '<span class="label label-danger">已销单</span>';
                } else if (display == 9) {
                    return '<span class="label label-success">已完成</span>';
                } else if (display == 7) {
                    return '<span class="label" style="background-color: #CC9933">开始等待</span>';
                } else if (display == 5) {
                    return '<span class="label label-success">已支付</span>';
                } else {
                    return '未知状态';
                }
            }
        },
        {"data": "setouttime"},
        {"data": "reservation_address"},
        {"data": "mrealName"},
        {"data": "mPhone"},
        {"data": "destination"},
        {"data": "drealName"},
        {"data": "dPhone"},
        {
            "data": "from_type",
            "render": function (display) {
                    if (display == 1) {
                        return '<p class="text-warning">app下单</p>';
                    } else if (display == 2) {
                        return '<p class="text-info">微信预约</p>';
                    } else if (display == 3) {
                        return '<p class="text-primary">电话预约</p>';
                    } else if (display == 4) {
                        return '<p class="text-primary">服务人员补单</p>';
                    }
            }
        },
        {"data": "pay_time"},
        {"data": "real_distance"}
    ];

    var table = dt.build($('#order-list'), url.list, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/order/del',
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
        $('.add').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('确定追加该订单？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/order/add',
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
        $('.cancel').on('click', function (a) {
            var sruid = $(a.target).attr("stu-id");
            if (sruid == 4 && g.user != 1) {
                layer.msg("当前状态不可销单");
                return;
            }
            var id = $(a.target).attr("data-id");
            layer.confirm('是否确定销单？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/order/cancel',
                    data: {orderId: id},
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

    $(".search").on('click', function () {
        var a = $('#pay2').val();
        if (a == 6) {
            $('#pay1').val(4);
        }
        if (a == 5) {
            $('#pay1').val("");
        }
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        table.ajax.reload();
        statistics.statisticsAll();
    });
});