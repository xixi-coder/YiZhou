/**
 * Created by admin on 2016/9/23.
 */
/**
 * Created by BOGONm on 16/8/11.
 */
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/order/listByStatus?statusType=" + $('#statusType').val(),
        /* excelList: g.ctx + "/admin/order/excelList?length=1000000&type=" + $('#statusType').val(),*/
        union: g.ctx + "/admin/order/unionuser"
    };

    var columns = [
        {
            "width": '100px',
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {
                    id: row.id,
                    status: row.status,
                    no: row.no,
                    serviceType: row.service_type
                });
            }
        },
        {
            "data": "no",
            'render': function (display, cell, row) {
                return '<a href="' + g.ctx + '/admin/order/lockup/' + row.id + '">' + display + '</a>';
            }
        },
        {
            "data": "company_name"
        }, {
            "data": "type",
            'render': function (display, cell, row) {

                switch (display) {
                    case 43:
                        return '<span class="label label-default" style="background-color: #67b168">舒适型</span>';
                    case 37:
                        return '<span class="label label-default" style="background-color: #F39C12">豪华型</span>';
                    case 45:
                        return '<span class="label label-default" style="background-color: #f57a82">商务型</span>';
                    case 40:
                        return '<span class="label label-default" style="background-color: #eb9316">代驾</span>';
                    case 41:
                        return '<span class="label label-default" style="background-color: #F0675A">包司机</span>';
                    case 42:
                        return '<span class="label label-default" style="background-color: #B2B2B2">出租车</span>';
                    case 44:
                        return '<span class="label label-default" style="background-color: #31BF2F">快车</span>';
                    case 49:
                        return '<span class="label label-default" style="background-color: #0A487C">市内顺风车</span>';
                    case 50:
                        return '<span class="label label-default" style="background-color: #8a6d3b">跨城顺风车</span>';
                    case 48:
                        return '<span class="label label-default" style="background-color: #ebccd1">接机专线</span>';
                    case 46:
                        return '<span class="label label-default" style="background-color: #761c19">送机专线</span>';
                    case 52:
                        return '<span class="label label-default" style="background-color: #5BA9C2">市内专线</span>';
                    case 56:
                        return '<span class="label label-default" style="background-color: #4a525f">航空专线 接机</span>';
                    case 57:
                        return '<span class="label label-default" style="background-color: #D92623">航空专线 送机</span>';
                    case 47:
                        return '<span class="label label-default" style="background-color: #2a6496">城际专线</span>';
                    default:
                        return '<span class="label label-default" style="background-color: #1b1e1e">未知</span>';
                }

            }
        },
        {
            "data": "status",
            'render': function (display, cell, row) {
                if (display == 1) {
                    return '<span class="label label-default">未派单</span>';
                } else if (display == 2 || display == 8) {
                    return '<span class="label label-info">已接单</span>';
                } else if (display == 3) {
                    return '<span class="label label-primary">执行中</span>';
                } else if (display == 4) {
                    return '<span class="label" style="background-color: #66CCFF">到达终点</span>';
                } else if (display == 6) {
                    return '<span class="label label-danger">已销单</span>';
                } else if (display == 9) {
                    return '<span class="label label-success">已完成</span>';
                } else if (display == 7) {
                    return '<span class="label" style="background-color: #CC9933">开始等待</span>';
                } else if (display == 5) {
                    return '<span class="label label-success">已支付</span>';
                } else {
                    return '未知状态';
                }
            }
        },
        {"data": "setouttime"},
        {"data": "reservation_address"},
        {"data": "mrealName"},
        {"data": "mPhone"},
        {"data": "destination"},
        {"data": "drealName"},
        {"data": "dPhone"},
        {
            "data": "from_type",
            "render": function (display) {
                if (display == 1) {
                    return '<p class="text-warning">app下单</p>';
                } else if (display == 2) {
                    return '<p class="text-info">微信预约</p>';
                } else if (display == 3) {
                    return '<p class="text-primary">电话预约</p>';
                } else if (display == 4) {
                    return '<p class="text-primary">服务人员补单</p>';
                } else if (display == 6) {
                    return '<p class="text-primary">微信H5下单</p>';
                }

            }
        },
        {"data": "pay_time"},
        {"data": "real_distance"},
        {"data": "real_pay"}
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
        $('.add').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('确定追加该订单？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/order/add',
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
            var sruid = $(a.target).attr("stu-id");
            if (sruid == 4 && g.user != 1) {
                layer.msg("当前状态不可销单");
                return;
            }
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
        var a = $('#pay2').val();
        if (a == 6) {
            $('#pay1').val(4);
        }
        if (a == 5) {
            $('#pay1').val("");
        }
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        table.ajax.reload();
        statistics.statisticsAll();
    });

    $(".excel").on('click', function () {
        var a = $('#pay2').val();
        if (a == 6) {
            $('#pay1').val(4);
        }
        if (a == 5) {
            $('#pay1').val("");
        }
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        $.ajax({
            url: url.excelList,
            data: $("#searchForm_").serialize(),
            type: 'post',
            beforeSend: function () {
                $("#tijiao").prop("disabled", "true");
            },
            success: function () {
                layer.msg("导出中...");
                $("#tijiao").prop("disabled", "");
                location.href = g.ctx + "/admin/order/excel";
            }

        })
        statistics.statisticsAll();
    });


});

//Excel
var tableToExcel = (function () {
    var uri = 'data:application/vnd.ms-excel;base64,',
        template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
        base64 = function (s) {
            return window.btoa(unescape(encodeURIComponent(s)))
        },
        // 下面这段函数作用是：将template中的变量替换为页面内容ctx获取到的值
        format = function (s, c) {
            return s.replace(/{(\w+)}/g,
                function (m, p) {
                    return c[p];
                }
            )
        };
    return function (table, name) {
        if (!table.nodeType) {
            table = document.getElementById(table);
            //table.rows[0].cells[0].remove();
            for (var i = 0; i < table.rows.length; i++) {
                table.rows[i].cells[1].remove();
            }
        }
        // 获取表单的名字和表单查询的内容
        alert(table.innerHTML);
        var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML};
        // format()函数：通过格式操作使任意类型的数据转换成一个字符串
        // base64()：进行编码
        window.location.href = uri + base64(format(template, ctx))
    }
})()


function find_push_list(orderId) {
    // var id = $(a.target).attr("data-id");
    $.ajax({
        url: g.ctx + '/admin/order/findPushList',
        data: {orderId: orderId},
        type: 'GET',
        success: function (data) {
            if (data.status == "ERROR") {
                layer.alert('暂无数据！')
                return;
            }
            if (data.status == 'OK') {
                var last = data.data;
                var html = "";
                if (last.length == 0) {
                    layer.alert('暂无数据！')
                    return;
                }
                layer.open({
                    type: 1,
                    title: "人员列表",
                    skin: 'layui-layer-rim', //加上边框
                    area: ['600px', '500px'], //宽高
                    shade: 0.6,//遮罩透明度
                    maxmin: true, //允许全屏最小化
                    anim: 1,
                    content: $("#HiddenList")
                    //content: [data.member, '#HiddenList']
                });
                console.log(data.data);

                for (var i = 0; i < last.length; i++) {
                    var str = '';
                    if (last[i].status == 2) {
                        str = '司机拒接';
                    } else if (last[i].status == 0) {
                        str = '司机未响应';
                    } else if (last[i].status == 1) {
                        str = '司机已接单';
                    } else {
                        var str = '未知';
                    }
                    html += '<tr id="row' + last[i].id + '"><th>' + last[i].name + '</th><th>' + last[i].phone + '</th><th>' + str + '</th></tr>'
                }
                $("#HiddenList tbody").html(html);
            } else {
            }
        }
    });
}

