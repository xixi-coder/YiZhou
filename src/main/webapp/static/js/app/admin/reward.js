/**
 * Created by admin on 2016/12/3.
 */
var url = {
    screenUrl: g.ctx + '/admin/member/driver/screen',
    reward:g.ctx+'/admin/member/driver/rewardSave'
}
$(document).ready(function () {
    $('input').on('ifChecked', function (event) { //ifCreated 事件应该在插件初始化之前绑定
        if ($(event.currentTarget).val() == 1) {
            $('#moneyRe').show();
            $('#rebateRe').hide();

        } else {
            $('#rebateRe').show();
            $('#moneyRe').hide();
        }
    });
    $('#screen').on('click', function () {
        var company = $("#company").val();
        var orderType = $("#orderType").val();
        var minOrderCount = $("#minOrderCount").val();
        var minMoneyEach = $("#minMoneyEach").val();
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        var rewardType = $("input[name='rewardType']:checked").val();// $("#rewardType").val();
        if(!rewardType){
            layer.msg("请选择奖励类型");
            return;
        }
        var rebate = $("#rebate").val();
        var money = $("#money").val();
        if(rewardType==1){
            if(!money){
                layer.msg("请填写奖励金额");
                return;
            }
        }

        if(rewardType==2){
            if(!rebate){
                layer.msg("请填写奖励比例");
                return;
            }
        }
        $.ajax({
            url: url.screenUrl,
            data: {
                company: company,
                orderType: orderType,
                orderCount: minOrderCount,
                minMoney: minMoneyEach,
                startDate: startTime,
                endDate: endTime,
                rewardType: rewardType,
                rebate: rebate,
                money: money
            },
            success: function (data) {
                reward.showDriver(data, rewardType,money,rebate);
            }
        });
    });
    var reward = {
        showDriver: function (data, rewardType,rewardMuch,rebate) {
            var html = '';
            $(data).each(function (index, val) {
                var driverInfo = {id: val.id, money: val.total, rewardType: rewardType,rewardMuch:rewardMuch,rebate:rebate};
                html += tpl.bind($("#driverHtml"), {
                    driverRealName: val.real_name,
                    phone:val.phone,
                    driverInfo: JSON.stringify(driverInfo).replace(/\"/g,'\'')
                });
            });
            if(html==''){
                html='<h3>没有符合的司机。。。</h3>';
            }
            layer.open({
                title:'奖励司机',
                area: ['80%', '90%'],
                content: html,
                btn: ['执行'],
                yes: function (index, layero) {
                    var inputArray = $('input[name="driverInfo"]');
                    var driverArray = [];
                    $(inputArray).each(function(index,val){
                        var a = $.parseJSON($(val).val().replace(/\'/g,'\"'));
                        driverArray.push(a);
                    });
                    var driverInfo = JSON.stringify(driverArray);
                    console.log(driverInfo);
                    $.ajax({
                        url:url.reward,
                        type:'post',
                        data:{driverInfo:driverInfo},
                        beforeSend:function(){
                            layer.load(1, {
                                shade: [0.1,'#fff'] //0.1透明度的白色背景
                            });
                        },
                        success:function(data){
                            layer.closeAll();
                            layer.msg(data.msg);
                        }
                    })

                },
                closeBtn: 1,
                shadeClose: true,
                success: function () {
                    $('.notify-item-close').on('click', function (target) {
                        $(target.currentTarget).parent().parent().parent().empty();
                    });
                }
            });

        }
    }
});