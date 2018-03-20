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
    if(orderType>1){
        $($('.tabs-select')[orderType - 2]).addClass("active ");
    }else{
        $($('.tabs-select')[orderType - 1]).addClass("active ");
    }
    var columns = [
        {"data": "cname"},

        {
            "data": "audit_status",
            'render': function (a) {
                if(a==null) {
                    return  '<span class="label label-danger">未审</span>'
                }else if(a==1) {
                    return  '<span class="label label-success">已审通过</span>'
                }else if(a==0) {
                    return  '<span class="label label-danger">已审未通过</span>'
                }
            }
        },
        {"data": "NO"},
        {"data": "amount"},
        {"data": "create_time"},
        {"data": "remark",
            'render':function(a,b,c){
                return order.getOrderNo(a);
            }
        },
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id,audit_status:row.audit_status});
            }
        }
    ];
    var table = dt.build($('#order-list'), url.list, columns, function () {
        $('.audit').on('click',function (a) {
            var id = $(a.target).attr("id");
            layer.open({
                title:'财务审核',
                content:tpl.bind($('#amount_tpl'),{id:id}),
                btn:['审核通过','审核不通过'],
                yes:function (index,layelo) {
                    $('#amount_form').validator({
                        valid: function (form) {
                            $.ajax({
                                type: 'post',
                                url: g.ctx + '/admin/finance/audit/1-'+id,
                                data: $(form).serialize(),
                                success: function (data) {
                                    if (data.status == 'OK') {
                                        layer.msg(data.msg);
                                        window.location.href = g.ctx + '/admin/finance';
                                    } else {
                                        layer.alert(data.msg);
                                        window.location.href=g.ctx+'/admin/finance';
                                    }
                                }
                            });
                        }
                    });
                    $('#amount_form').trigger('submit');

                },
                btn2:function (index,layelo) {
                    $('#amount_form').validator({
                        valid: function (form) {
                            $.ajax({
                                type: 'post',
                                url: g.ctx + '/admin/finance/audit/0-'+id,
                                data: $(form).serialize(),
                                success: function (data) {
                                    if (data.status == 'OK') {
                                        layer.msg(data.msg);
                                        window.location.href = g.ctx + '/admin/finance';
                                    } else {
                                        layer.alert(data.msg);
                                        window.location.href=g.ctx+'/admin/finance';
                                    }
                                }
                            });
                        }
                    });
                    $('#amount_form').trigger('submit');
                },
            })
        })
        
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
    $(".search").on('click',function(){
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        table.ajax.reload();
        statistics.statisticsAll();
    });
});