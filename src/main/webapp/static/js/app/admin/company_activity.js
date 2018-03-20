/**
 * Created by admin on 2016/12/28.
 */
$(document).ready(function () {
    var url = {
        list: g.ctx + "/admin/company/activitylog/list"
    };
    statistics.statisticsAll();
    var columns = [

        {"data": "type",'render':function(a){
            if(a==1){
                return '<span class="label" style="background-color: #66CCFF">三级分销奖励</span>';
            }else if(a==2){
                return '<span class="label" style="background-color: #c7edcc">活动赠送的优惠券</span>';
            }else if(a==3){
                return '<span class="label" style="background-color: #a3c2a1">定向赠送</span>';
            }else if(a==4){
                return '<span class="label" style="background-color: #699934">兑换码领取</span>';
            }else if(a==5){
                return '<span class="label" style="background-color: #68ffab">活动奖励余额</span>';
            }else if(a==6){
                return '<span class="label" style="background-color: #ff82ee">司机奖励金额</span>';
            }else{
                return '<span class="label" style="background-color: #2dffb9">未知</span>';
            }
        }},
        {"data": "amount"},
        {"data": "couponType",'render':function(a){
            if(a==1){
                return '<span class="label" style="background-color: #66CCFF">普通券</span>';
            }else if(a==2){
                return '<span class="label" style="background-color: #699934">折扣券</span>';
            }else{
                return '<span class="label" style="background-color: #66CCFF">普通券</span>';
            }
        }},
        {"data": "remark"},
        {"data": "create_time"}

    ];
    var table = dt.build($('#activitylog-list'), url.list, columns, function () {
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