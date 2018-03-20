/**
 * Created by admin on 2016/9/23.
 */
/**
 * Created by BOGONm on 16/8/11.
 */
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/finance/list/" + $('#orderType').val()
    };
    var orderType = Number($('#orderType').val());
    if (orderType > 1) {
        $($('.tabs-select')[orderType - 2]).addClass("active ");
    } else {
        $($('.tabs-select')[orderType - 1]).addClass("active ");
    }
    var columns = [
        {"data": "real_name"},
        {
            "data": "operation_type",
            'render': function (a) {
                if (a == 11) {
                    return '后台充值'
                } else if (a == 12) {
                    return 'APP充值'
                }else if (a == 13) {
                    return '支付宝充值'
                }else if (a == 14) {
                    return '微信充值'
                }
            }
        },
        {"data": "phone"},
        {"data": "amount"},
        {"data": "create_time"},
        {
            "data": "remark",
            'render': function (a, b, c) {
                console.log(a);
                order.getOrderNo(a);
                return a;
            }
        }
    ];
    var table = dt.build($('#capital-list'), url.list, columns, function () {

    }, true);
    $('.search').on('click', function () {
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        table.ajax.reload();
        statistics.statisticsAll();
    })
});