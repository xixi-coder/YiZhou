/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/sys/ad/list";
    var columns = [
        {"data": "name"},
        {"data": "url"},

        {
            "data": "default_flag",
            'render': function (a) {
                if (a == 0) {
                    return '否';
                } else {
                    return '是'
                }
            }
        },
        {
            "data": "type",
            'render': function (a, b, c) {
                if (a == 1) {
                    return '启动页广告';
                } else if (a == 2) {
                    return '首页广告';
                } else if (a == 3) {
                    return '微信广告';
                } else {
                    return '4'
                }
            }
        },
        {
            "data": "app_type",
            'render': function (a, b, c) {
                if (a == 1) {
                    return '司机端';
                } else if (a == 2) {
                    return '会员端';
                }else if (a == 3) {
                    return '微信';
                }
            }
        },
        {"data": "sort"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];

    var table = dt.build($('#ad-list'), url, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/ad/del',
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
