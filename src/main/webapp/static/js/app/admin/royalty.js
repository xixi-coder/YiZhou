/**
 * Created by BOGONj on 2016/9/12.
 */
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/sys/royalty/list",
        del: g.ctx + "/admin/sys/royalty/del"
    };
    var columns = [
        {"data": "name"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];
    var table = dt.build($('#royalty-list'), url.list, columns, function () {

        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: url.del,
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
    $('.search').on('click',function () {
        table.ajax.reload();
    })
});