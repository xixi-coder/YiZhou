
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/company/accountlog/list"
    };
    statistics.statisticsAll();
    var columns = [

        {"data": "companyName"},
        {"data": "remark"},
        {"data": "amount"},
        {"data": "create_time"}
    ];
    var table = dt.build($('#accountlog-list'), url.list, columns, function () {
    });
    $(".search").on('click',function(){
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        table.ajax.reload();
        statistics.statisticsAll();
    });
});