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
        // {"data": "alipay_account"},
        {"data": "dphone"},
        {
            "data": "total", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }
        },
        {"data": "amount"}
    ];
    var table = dt.build($('#order-list'), url.list, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/order/del',
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
    }, true);
    $(".search").on('click', function () {
        statistics.statisticsAll();
        table.ajax.reload();
    });
});