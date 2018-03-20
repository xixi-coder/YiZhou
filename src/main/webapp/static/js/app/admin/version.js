/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/sys/version/list";
    var columns = [
        {"data": "version_no"},
        {"data": "version"},
        {
            "data": "os_type",
            'render': function (a) {
                if (a == 2) {
                    return 'iOS';
                } else {
                    return 'ANDROID';
                }
            }
        },
        {
            "data": "type",
            'render': function (a, b, c) {
                if (a == 1) {
                    return '司机端';
                } else {
                    return '客户端';
                }
            }
        },
        {"data": "update_time"},
        {"data": "create_time"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];

    var table = dt.build($('#version-list'), url, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/version/del',
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
    $('.search').on('click', function () {
        table.ajax.reload();
    })
})
