/**
 * Created by Administrator on 2016/10/11.
 */
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/count/companyrebate/list"
    };
    var columns = [
        {"data": "name"},
        {"data": "amount"},
        {
            "data": "no",
            'render': function (a, b, c) {
                a = '【' + a + '】';
                return order.getOrderNo(a);
            }
        },
        {"data": "rebate_time"},
        {"data": "user_name"}
    ];
    var table = dt.build($('#companyRebate-list'), url.list, columns, function () {

    });
    $('.search').on('click', function () {
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        table.ajax.reload();
    })
});
