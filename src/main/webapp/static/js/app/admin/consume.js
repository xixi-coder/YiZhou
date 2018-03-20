/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var type = $('#type').val();
    $($('.tabs-select')[type - 1]).addClass("active");
    var url = g.ctx + "/admin/count/consume/list?type=" + type;
    var columns = [
        {"data": "real_name"},
        {"data": "user_name"},
        {"data": "name"},
        {"data": "count"},
        {"data": "sum"}
    ];

    var table = dt.build($('#consume-list'), url, columns, function () {

    });
    $("#search_button").on('click', function () {
        var start = $("#startTime").val();
        var end = $("#endTime").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        var level = $("#level").val();
        if (level != 0) {
            $("#sLevel").val(level);
        }
        table.ajax.reload();
        statistics.statisticsAll();
    });
})
