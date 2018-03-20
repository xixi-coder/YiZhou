/**
 * Created by admin on 2016/12/19.
 */
/**
 * Created by BOGONm on 16/8/11.
 */
var url = {
    list: g.ctx + "/admin/member/driver/withdrawals/list",
};
var table;
$(document).ready(function () {
    var columns = [
        {
            "data": "amount"
        },
        {
            "data": "status",
            'render': function (a, b, c) {
                if (a == 0) {
                    return '<span class="label label-default">待审核</span>';
                } else if (a == 1) {
                    return '<span class="label label-success">审核通过</span>';
                } else if (a == 2) {
                    return '<span class="label label-danger">审核未通过</span>';
                }
            }
        },
        {"data": "create_time"},
        {
            "data": "dreal_name",
            'render': function (a, b, c) {
                if (a == null) {
                    return c.mreal_name;
                } else {
                    return a;
                }
            }
        },
        {
            "data": "dphone",
            'render': function (a, b, c) {
                if (a == null) {
                    return c.mphone;
                } else {
                    return a;
                }
            }
        },
        {"data": "bank_no"},
        {"data": "bank_name"},
        {"data": "bname"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id, status: row.status});
            }
        }
    ];

    table = dt.build($('#withdrawals-list'), url.list, columns, function () {
        $(".audit").on('click', function (a) {
            var id = $(a.target).data("id");
            var index = layer.open({
                type: 0,
                title: "审核",
                content: tpl.bind($('#withdrawals_tpl'), {id: id}),
                btn: ['审核通过', '审核不通'], //可以无限个按钮
                btn2: function (index, layero) {
                    $("#auditStatus").val(2);
                    $('#withdrawals_form').trigger('submit');
                },
                btn1: function (index, layero) {
                    $("#auditStatus").val(1);
                    $('#withdrawals_form').trigger('submit');
                }, success: function (layero, index) {
                    $('#withdrawals_form').validator({
                        valid: function (form) {
                            $.ajax({
                                type: 'post',
                                url: g.ctx + '/admin/member/driver/withdrawals/audit',
                                data: $(form).serialize(),
                                success: function (data) {
                                    layer.msg(data.msg);
                                    table.ajax.reload();
                                }
                            });
                        }
                    });
                }
            });
        });
    });
    $('.search').on('click', function () {
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        table.ajax.reload();
    });
});