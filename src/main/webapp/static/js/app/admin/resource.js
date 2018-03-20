/**
 * Created by BOGONm on 16/8/11.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/sys/resource/list";
    var columns = [
        {
            "data": "name"
        },
        {"data": "show_name"},
        {
            "data": "code"
        },
        {"data": "path"},
        {
            "data": "ico",
            'render': function (a, b, c) {
                return '<i class="' + a + '">';
            }
        },
        {
            "data": "status",
            'render': function (a, b, c) {
                if (a == 1) {
                    return '启用';
                } else {
                    return '禁用';
                }
            }
        },
        {
            "data": "pname",
            'render': function (a, b, c) {
                if (a) {
                    return a;
                } else {
                    return '顶级资源'
                }
            }
        },
        {
            "render": function (display, cell, row) {
                return tpl.bind($('#action_tpl'), {id: row.id})
            }
        }
    ];
    var table = dt.build($('#resource-list'), url, columns, function () {
        $(".del").on('click', function (a) {
            var id = $(a.delegateTarget).data('id');
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/resource/del',
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

});