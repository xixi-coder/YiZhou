/**
 * Created by Administrator on 2016/9/9.
 */

$(document).ready(function () {
    var url = g.ctx + "/admin/count/yeji/orderlist?username=" + username + '&type=' + type;
    var columns = [
        {"data": "pay_time"},
        {
            "data": "no",
            'render': function (a) {
                a = "【" + a + "】";
                return order.getOrderNo(a);
            }
        },
        {"data": "amount"},
        {"data": "damount"}
    ];

    var table = dt.build($('#order-list'), url, columns, function () {
    });

    $(".search").on('click', function () {
        var start = $("#start").val();
        var end = $("#end").val();
        $('#timeSearch').val('D_' + start + ',' + end);
        table.ajax.reload();
    });
})
