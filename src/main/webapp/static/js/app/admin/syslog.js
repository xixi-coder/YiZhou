/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/sys/syslog/list";
    var columns = [
        {"data": "url"},
        {
            "data": "params",
            "render": function (a) {
                if (a == null) {
                    return ""
                } else if (a.length > 10) {
                    return a.substr(0, 10) + "......"
                } else {
                    return a
                }
            }
        },
        {"data": "create_time"},
        {
            "data": "remark",
            "render": function (a) {
                if (a.length > 10) {
                    return a.substr(0, 10) + "......"
                } else {
                    return a
                }
            }
        },
        {"data": "ip_address"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];

    dt.build($('#syslog-list'), url, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/syslog/del',
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
    });
})
