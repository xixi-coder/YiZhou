/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/member/list/gradelist/" + id;
    var columns = [
        {"data": "aname"},
        {"data": "ename"},
        {"data": "score"},
        {"data": "content"},
        {"data": "create_time"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];

    dt.build($('#grade'), url, columns, function () {

    });
})
