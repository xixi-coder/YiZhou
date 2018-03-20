/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/sys/alarm/list";
    var columns = [
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        },
        {"data": "alarm_name"},
        {"data": "back_phone"},
        {"data": "company_name"},
        {
            "data": "alarm_message",
            "render": function (a) {
                if (a == null) {
                    return ""
                } else if (a.length > 25) {
                    return a.substr(0, 25) + "......"
                } else {
                    return a
                }
            }
        },
        {"data": "create_time"},
        {
            "data": "status",
            'render': function (a) {
                if (a == 0) {
                    return '未处理';
                } else if (a == 1) {
                    return '处理中'
                } else if (a == 2) {
                    return '已处理'
                } else {
                    return '未知状态'
                }
            }
        },
        {
            "data": "dispose_message",
            "render": function (a) {
                if (a == null) {
                    return ""
                } else if (a.length > 25) {
                    return a.substr(0, 25) + "......"
                } else {
                    return a
                }
            }
        },
        {"data": "user_name"}
    ];

    var table = dt.build($('#ad-list'), url, columns, function () {
    });

    $(".search").on('click', function () {
        table.ajax.reload();
    });
})
