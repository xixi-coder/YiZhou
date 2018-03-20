/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/sys/notice/list";
    var columns = [
        {"data": "title"},
        {
            "data": "recive_type",
            "render": function (a) {
                if (a == -1) {
                    return '全部(司机端和乘客端)';
                } else if (a == -2) {
                    return '全部客户(乘客端)';
                } else if (a == -3) {
                    return '全部司机(司机端)';
                } else if (a == -4) {
                    return '指定客户';
                } else if (a == -5) {
                    return '指定司机';
                } else {
                    return '公告';
                }
            }
        },
        {
            "data": "type",
            "render": function (a) {
                if (a == 1) {
                    return '公告';
                } else if (a == 2) {
                    return '通知';
                }else if (a == 3) {
                    return '出租车公告';
                }else if (a == 4) {
                    return '快车公告';
                }else if (a == 5) {
                    return '专车公告';
                }else if (a == 6) {
                    return '代驾公告';
                }else if (a == 7) {
                    return '顺风车公告';
                }else if (a == 8) {
                    return '专线公告';
                }
            }
        },
        {
            "data": "send_type",
            'render': function (a,b,c) {
                if(c.type==2){
                    if (a == 1) {
                        return '短信通知';
                    } else {
                        return '推送通知'
                    }
                }else{
                    return '自动获取'
                }

            }
        },
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];

    var table = dt.build($('#notice-list'), url, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/notice/del/' + id,
                    type: 'POST',
                    success: function (data) {
                        if (data.status == 'OK') {
                            layer.msg(data.msg);
                            table.ajax.reload();
                        } else {
                            layer.msg(data.msg);
                        }
                    }
                })
            });
        });
    });
    $(".search").on('click', function () {
        table.ajax.reload();
    });
})
