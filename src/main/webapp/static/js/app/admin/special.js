
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/special/list?type" + $('#orderTypeItem').val(),
    };

    var columns = [
        {
            "data": "id",
        },
        {
            "data": "company_name"
        },
        {
            "data": "status",
            'render': function (display, cell, row) {
                if (display == 1) {
                    return '<span class="label label-info">该专线正在使用</span>';
                } else if (display == 0) {
                    return '<span class="label label-default">该专线已被禁用</span>';
                } else {
                    return '未知状态';
                }
            }
        },
        {"data": "reservationAddress"},
        {"data": "destination"},
        {"data": "reservationCity"},
        {"data": "destinationCity"},
        {"data": "distance"},
        {"data": "index_price"},
        {"data": "sharing_price"},
        {"data": "estimated_price"},
        {"data": "sharing_esyimated_price"},
        {"data": "need_time"},
        {"data": "tag"},
        {"data": "create_time"},
        {"data": "update_time",},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {
                    id: row.id,
                    status: row.status,
                    realName: row.real_name,
                    amount: row.amount,
                    inst_phone:row.inst_phone
                });
            }
        }

    ];

    var table = dt.build($('#special-list'), url.list, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/special/del',
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
        $('.update').on('click', function (a) {
            var id = $(a.target).data("id");
            var status = $(a.target).data("status");
            var title = '';
            if (status == 1) {
                title = "是否禁用该路线？";
            } else if (status == 0) {
                title = "是否启用该路线？";
            }
            layer.confirm(title, {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/special/linestatus/' + id,
                    type: 'POST',
                    success: function (data) {
                        layer.msg(data.msg);
                        table.ajax.reload();
                    }
                })
            });
        });
    });
    $(".search").on('click', function () {
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        table.ajax.reload();
        statistics.statisticsAll();
    });
});