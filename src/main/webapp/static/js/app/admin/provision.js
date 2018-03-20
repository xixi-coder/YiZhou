/**
 * Created by BOGONm on 16/8/11.
 */
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/sys/provision/list"
    };
    var columns = [
        {
            "data": "client_type",
            'render': function (display, cell, row) {
                if (display == 1) {
                    return '司机端';
                } else {
                    return '客户端';
                }
            }
        },
        {
            "data": "provision",
            "render":function(display){
                if(display==1){
                    return '软件使用协议及隐私政策';
                }else if(display==2){
                    return '代驾服务协议';
                }else if(display==3){
                    return '司机服务合作协议';
                }else if(display==4){
                    return '关于我们';
                }else if(display==5){
                    return '出租车用户协议';
                }else if(display==6){
                    return '专车使用条款';
                }else if(display==7){
                    return '顺风车协议条款';
                }else if(display==8){
                    return '专线协议条款';
                }else if(display==9){
                    return '快车协议条款';
                }
            }
        },
        {
            "data": "update_time"
        },
        {
            "width": '100px',
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id, status: row.status});
            }
        }
    ];

    var table = dt.build($('#provision-list'), url.list, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/provision/del',
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
        $('.cancel').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否确定销单？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/order/cancel',
                    data: {orderId: id},
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

    });
    $(".search").on('click', function () {
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        table.ajax.reload();
    });
});