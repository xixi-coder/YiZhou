/**
 * Created by Administrator on 2016/10/11.
 */
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/member/driver/grade/list"
    };
    var columns = [
        {"data": "no",
            'render': function (a) {
            a = '【'+a+'】';
            return order.getOrderNo(a);
        }},
        {"data": "mname"},
        {"data": "dname"},
        {"data": "score"},
        {"data": "create_time"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];
    var table = dt.build($('#grade'), url.list, columns, function () {

    });
    $('.search').on('click', function () {
        $('#grade1').val($("#score"));
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        table.ajax.reload();
    })
});
