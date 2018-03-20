
$(document).ready(function () {
    var url = g.ctx + "/admin/sys/appLog/list";
    var columns = [
        {"data": "id"},
        {"data": "app_type",
            'render': function (a) {
                if (a == 1) {
                    return '司机端';
                } else {
                    return '客户端';
                }
            }
        },
        {"data": "device_type",
            'render': function (a) {
                if (a == 1) {
                    return '安卓';
                } else {
                    return 'iOS';
                }
            }},
        {"data": "version_code"},
        {"data": "log_path"},
        {"data": "create_time"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {path: row.log_path});
            }
        }
    ];
    var table = dt.build($('#applog-list'), url, columns, function () {
    });

    $('.search').on('click',function () {
        var start = $("#start").val();
        var end = $("#end").val();
        var type = $("#type").val();
        var type2 = $("#type2").val();
        if(start!=''&&end!=''){
            $('#timeSearch').val('DT_' + start + ',' + end +','+ type+','+ type2);
        }
        table.ajax.reload();
    })

})
