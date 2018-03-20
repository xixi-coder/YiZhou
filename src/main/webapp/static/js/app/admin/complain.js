/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/member/list/complainList/" + id;
    var columns = [
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        },
        {
            "data": "content",
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
        {
            "data": "reply_content",
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
        {"data": "name"},
        {"data": "create_time"}
    ];

    dt.build($('#capital'), url, columns, function () {
    });
})
