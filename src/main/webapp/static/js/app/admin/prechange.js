/**
 * Created by admin on 2016/12/3.
 */
var url = {
    screenUrl: g.ctx + '/admin/member/driver/rescreen',
    rechange: g.ctx + '/admin/member/driver/rechangeSave'
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
        var money = $("#remoney").val();
        if (!money) {
            layer.msg("请填写充值金额");
            return;
        }
        var remark = $("#remark").val();
        $.ajax({
            url: url.screenUrl,
            type: 'post',
            data: $('#rechange_form').serialize(),
            success: function (data) {
                rechange.showDriver(data, money, remark);
            }
        });
    });
    var rechange = {
        showDriver: function (data, money, remark) {
            var html = '';
            $(data).each(function (index, val) {
                html += tpl.bind($("#driverHtml"), {
                    driverRealName: val.real_name,
                    phone: val.phone, id: val.id
                });
            });
            if (html == '') {
                html = '<h3>没有符合的司机。。。</h3>';
            }
            layer.open({
                title: '批量充值',
                area: ['80%', '90%'],
                content: html,
                btn: ['执行'],
                yes: function (index, layero) {
                    var loginIds = $('input[name="loginIds"]');
                    var l = '';
                    $(loginIds).each(function (index, val) {
                        l += $(val).val() + ';';
                    });
                    console.log(l, money, remark);
                    $.ajax({
                        url: url.rechange,
                        data: {loginIds: l, money: money, remark: remark},
                        beforeSend: function () {
                            layer.load(1, {
                                shade: [0.1, '#fff'] //0.1透明度的白色背景
                            });
                        },
                        success: function (data) {
                            layer.closeAll();
                            layer.msg(data.msg);

                        }
                    });

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