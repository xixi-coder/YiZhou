/**
 * Created by Administrator on 2016/9/28.
 */
$(document).ready(function () {
    var table;
    var url = g.ctx + "/admin/activity/coupon/membercoupon/" + id1;
    var columns = [
        {"data": "title"},
        {"data": "amount"},
        {"data": "coupon_source"},
        {"data": "no"},
        {"data": "password"},
        {"data": "start_time"},
        {"data": "end_time"},
        {"data": "real_name"},
        {"data": "phone"},
        {"data": "gain_time"},
        {"data": "status",
            "render": function (a) {
                if (a == 0){
                    return '<span class="label" style="background-color: #66CCFF">未使用</span>';
                }
                if (a == 1){
                    return '<span class="label" style="background-color: #699934">已使用</span>';
                }else {
                    return '<span class="label" style="background-color: #5e5c38">已冻结</span>';
                }

            }
        },
        {"data": "use_time"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {did: row.id});
            }
        }
    ];

    table = dt.build($('#coupon-list'), url, columns, function () {
        $("#createcoupon").on('click', function (a) {
            var id = $(a.target).attr("data-id");
            var index = layer.open({
                type: 0,
                content: tpl.bind($('#coupon_tpl'), {id: id}),
                yes: function (index, layero) {
                    $('#coupon_form').trigger('submit');
                }, success: function (layero, index) {
                    $('#coupon_form').validator({
                        valid: function (form) {
                            $.ajax({
                                type: 'post',
                                url: g.ctx + '/admin/activity/coupon/createcoupon',
                                data: $(form).serialize(),
                                success: function (data) {
                                    if (data.status == 'OK') {
                                        layer.msg(data.msg);
                                        table.ajax.reload();
                                    } else {
                                        layer.msg(data.msg);
                                    }
                                },
                                beforeSend: function () {
                                    $(".layui-layer-btn0").off();
                                }
                            });
                        }
                    });
                }
            });
        });

        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/activity/coupon/coupondel',
                    data: {id: id},
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
    });
})