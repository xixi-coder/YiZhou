/**
 * Created by BOGONm on 16/8/11.
 */
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/sys/servicetype/listforcompany"
    };
    var columns = [
        {"data": "serviceName"},
        {"data": "typeName"},
        {"data": "royaltyStandardName"},
        {"data": "chargeStandardName"},
        {"data": "companyName"},
        {"data": "last_update_time"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];
   var table =  dt.build($('#servicetype_company-list'), url.list, columns, function () {
        $('.delete').on('click', function (a) {
            var id = $(a.target).attr("data-id");
            layer.confirm('是否删除？', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    url: g.ctx + '/admin/sys/servicetype/companyservicedel',
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