
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/zxLine/list/" + $('#TypeItem').val(),
    };

    var columns = [
        {
            "data": "id",
        },
        {
            "data": "name"
        },
        {"data": "start_address_detail"},
        {"data": "end_address_detail"},
        {"data": "distance"},
        {"data": "price"},
        {"data": "sharing_price"},
        {"data": "price_special"},
        {"data": "sharing_price_special"},
        {"data": "create_time"},
        {"data": "update_time",},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {
                    id: row.id
                });
            }
        }

    ];

    var table = dt.build($('#special-list'), url.list, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/zxLine/del',
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
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        table.ajax.reload();
        statistics.statisticsAll();
    });
});