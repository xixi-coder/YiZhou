/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var url = g.ctx + "/admin/member/callback/list";
    var columns = [
        {"data": "user_name"},
        {"data": "app_type",'render':function (a) {
            if(a==1) {
                return '司机端'
            }else {
                return '客户端'
            }
        }},
        {"data": "create_time"},
        {"data": "content"},

    ];

    var table = dt.build($('#callback'), url, columns, function () {

    });
    $(".search").on('click',function(){
        var start = $("#start").val();
        var end = $("#end").val();
        if (start != '' && end != '') {
            $('#timeSearch').val('D_' + start + ',' + end);
        }
        table.ajax.reload();
    });
})
