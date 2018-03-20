/**
 * Created by Administrator on 2016/9/7.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/sys/smslog/item";
    var columns = [
        {"data": "name"},
        {"data": "content"},
        {"data": "company"},
        {"data": "create_time"},
        {"data": "creater"},
        {"data": "type"},
    ];

    dt.build($('#smsTmp-list'), url, columns, function () {

    });
})