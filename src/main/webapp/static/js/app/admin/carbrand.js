/**
 * Created by Administrator on 2016/9/20.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/member/driver/brandlist";
    var columns = [
        {"data": "name"},
        {"data": "py_name"},
        {"data": "description"},
        {"data": "create_time"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];

    var table = dt.build($('#carbrand-list'), url, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/member/driver/delbrand',
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
    $(".search").on('click', function () {
        table.ajax.reload();
    });
})