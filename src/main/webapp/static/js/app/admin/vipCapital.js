/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/member/list/vipCapitalList/" + id;
    var columns = [
        {"data": "id"},
        {"data": "phone"},
        {"data": "use_amount"},
        {"data": "log_content"},
        {"data": "create_time"}
    ];

    dt.build($('#vipcapital'), url, columns, function () {
    });
})
