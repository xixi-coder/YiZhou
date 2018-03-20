/**
 * Created by BOGONm on 16/8/11.
 */

var detailed ;


$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/order/invoice/list",
        invoice: g.ctx + "/admin/order/invoice/add",
        detailed: g.ctx + "/admin/order/invoice/findlistByid"
    };

    var columns = [
        {
            "data": "invoice_header"
        },
        {
            "data": "content"
        }, {
            "data": "mphone"
        },{
            "data": "amount"
        }
        , {
            "data": "addressee"
        }, {
            "data": "addressee_phone"
        }, {
            "data": "post_code"
        }, {
            "data": "post_address"
        }, {
            "data": "status",
            'render': function (display, cell, row) {
                if (display == 0) {
                    return '<span class="label label-default">待开票</span>';
                } else if (display ==1) {
                    return '<span class="label label-success">已开票</span>';
                } else {
                    return '未知状态';
                }
            }
        }, {
            "data": "company"
        }, {
            "data": "create_time"
        },

        {"data": "invoice_no"},
        {"data": "invoice_time"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {
                    id: row.id,
                    status:row.status
                });
            }
        }
    ];

    var table = dt.build($('#invoice-list'), url.list, columns, function () {
        $('.addInvoice').on('click', function (a) {
            var id = $(a.target).data("id");
            layer.open({
                type: 0,
                title: '开票',
                content: tpl.bind($('#invoice_no'), {id: id}),
                yes: function (index, layero) {
                    $('#addinvoice_form').trigger('submit');
                }, success: function (layero, index) {
                    $('#addinvoice_form').validator({
                        valid: function (form) {
                            $.ajax({
                                type: 'post',
                                url: url.invoice+'/'+id,
                                data: $(form).serialize(),
                                beforeSend: function () {
                                    layer.closeAll();
                                },
                                success: function (data) {
                                    if (data.status == 'OK') {
                                        layer.msg(data.msg);
                                        table.ajax.reload();
                                    } else {
                                        layer.tips(data.msg);
                                    }
                                }
                            });
                        }
                    });
                }
            })
        });

        /* 查看明细 */

        $('.findInvoice').on('click', function (a) {
            var id = $(a.target).data("id");
            $.ajax({
                cache: true,
                type: 'GET',
                url: url.detailed+'/'+id,
                data:{"id":id},
                error: function(data) {
                    alert("Connection error");
                },
                success: function(data) {
                    console.log(data);


                    detailed =data;
                    layer.open({
                        type: 0,
                        title: '明细',
                        area: '850px',
                        fixed: false, //不固定
                        maxmin: true,
                        content: tpl.bind($('#invoice_no2')),
                        end: function(){
                            history.go(0);
                        }
                        //content:''

                        });

                }
            });
        });
        });

    $(".search").on('click', function () {
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        table.ajax.reload();
        statistics.statisticsAll();
    });
});

/*
获取当前日期  yyyy-mm-dd
 */
function getNowFormatDate() {
    var date = new Date();

    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + '年' + month + '月' + strDate + '日';
    return currentdate;
}

