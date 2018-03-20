$(document).ready(function () {
    var url = g.ctx + "/admin/sys/smslog/list";
    var columns = [
        {"data": "tmp"},
        {"data": "phone"},
        {"data": "status"},
        {"data": "send_time"},
        {"data": "content"},
        {
            "render": function (display, cell, row) {
                return tpl.bind($("#action_tpl"), {id: row.id});
            }
        }
    ];

    var table = dt.build($('#smslog-list'), url, columns, function () {
    });

    $('.search').on('click',function () {
        var start = $("#start").val();
        var end = $("#end").val();
        if(start!=''&&end!=''){
            $('#timeSearch').val('DT_' + start + ',' + end);
        }
        table.ajax.reload();
    })

})
