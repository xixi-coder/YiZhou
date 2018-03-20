/**
 * Created by BOGONj on 2016/9/18.
 */
/**
 * Created by BOGONj on 2016/8/22.
 */
var url = {
    save: g.ctx + '/admin/sys/chargestandard/save'
}
var editor;
$(document).ready(function () {
    KindEditor.ready(function (K) {
        editor = K.create('#desc', {
            uploadJson: g.ctx + '/file/edit',
            allowFileManager: false
        });
    });

    $('#charge_form').validator({
        stopOnError: true,

        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        valid: function (form) {
            $.ajax({
                type:'post',
                data:$('#charge_form').serialize(),
                url:g.ctx + '/admin/sys/chargestandard/saves',
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/sys/chargestandard';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            })
        }
    })





});



var chargeStandard = {
    addItem: function () {
        $('#chargestandard-item').append(tpl.bind($("#item"), {}));
        $('.del_item').off('click');
        $('.del_item').on('click', chargeStandard.delItem);
        $('.add_mill').off('click');
        $('.add_mill').on('click', chargeStandard.addMill);
        $('.timepicker-input').datetimepicker({
            language: 'zh-CN',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 1,
            minView: 0,
            maxView: 1,
            forceParse: 0
        });
    },
    delItem: function (target) {
        $($(target.currentTarget).parent().parent().parent().parent()[0]).remove();
    },
    addMill: function (target) {
        var tables = $($($($($(target.currentTarget).parent().parent()[0]).next('div')[0]).children('div')[0]).children('table')[0]).children('tbody')[0];
        var tr = $(tables).children("tr");
        if (!tr.length) {
            $(tables).append(tpl.bind($("#mill_item"), {isZero: true, start: ''}));
        } else {
            var start = $($($(tr[tr.length - 1]).children('td')[1]).children("input")[0]).val();
            if (start) {
                $(tables).append(tpl.bind($("#mill_item"), {isZero: false, start: start}));
            } else {
                layer.msg("请填写里程数");
                return;
            }

        }
        $('.del_mill_item').off('click');
        $('.del_mill_item').on('click', chargeStandard.delMill);
    },
    delMill: function (target) {
        $($(target.currentTarget).parent().parent()).remove();
    },
    save: function () {
        var chargestandardJson = {};
        var alls = $('#chargestandard-item').children('div');
        chargestandardJson['name'] = $('#name').val();
        chargestandardJson['desc'] = editor.html();
        chargestandardJson['cdItem'] = [];
        chargestandardJson['company'] = $("#company").val();
        $(alls).each(function (index, val) {
            if (index > 0) {
                var itemTmp = {};
                itemTmp['item'] = [];
                var Selects = $(val).find('.item_time');
                var Inputs = $(val).find('input[name="royaltyStandard.name"]');
                itemTmp['startTime'] = $(Selects[0]).val() + ':' + $(Selects[1]).val();
                itemTmp['endTime'] = $(Selects[2]).val() + ':' + $(Selects[3]).val();
                itemTmp['baseTime'] = $(Inputs[0]).val();
                itemTmp['minAmount'] = $(Inputs[1]).val();
                var millItem = $(val).find('.mill_item');
                var millIndex = 0;
                $(millItem).each(function (index, val) {
                    if (index == 1) {
                        var millTmp = {};
                        millTmp['start'] = 0;
                        millTmp['end'] = $($(millItem)[index - 1]).val();
                        millTmp['money'] = $($(millItem)[index]).val();
                        itemTmp['item'][millIndex] = millTmp;
                        millIndex += 1;

                    } else if (index % 3 == 1) {
                        var millTmp = {};
                        millTmp['start'] = $($(millItem)[index - 2]).val();
                        millTmp['end'] = $($(millItem)[index - 1]).val();
                        millTmp['money'] = $(val).val();
                        itemTmp['item'][millIndex] = millTmp;
                        millIndex += 1;
                    }
                });
                var csInfo = $(val).find('.item_content');
                itemTmp['chaoguogongli'] = $(csInfo[0]).val();
                itemTmp['meigongli'] = $(csInfo[1]).val();
                itemTmp['buzugongli'] = $(csInfo[2]).val();
                itemTmp['jiashou'] = $(csInfo[3]).val();


                itemTmp['qibuhoufenzhonglei'] = $(csInfo[4]).val();
                itemTmp['meiduoshaofenzhong'] = $(csInfo[5]).val();
                itemTmp['buzufenzhong'] = $(csInfo[6]).val();
                itemTmp['jiashoufeiyong'] = $(csInfo[7]).val();

                itemTmp['mianfeidenghou'] = $(csInfo[8]).val();
                itemTmp['lijichaoshijiashoufeiyong'] = $(csInfo[9]).val();
                itemTmp['chaoshimeiduoshaofenzhong'] = $(csInfo[10]).val();
                itemTmp['chaoshibuzufenzhong'] = $(csInfo[11]).val();
                itemTmp['chaoshijiashoufeiyong'] = $(csInfo[12]).val();
                chargestandardJson['cdItem'][index - 1] = itemTmp;
            }
        });
        $.ajax({
            url: url.save,
            type: 'POST',
            data: {id: $('#id').val(), item: JSON.stringify(chargestandardJson)},
            beforeSend: function () {
                $('.save').attr("disabled", "disabled");
            },
            success: function (data) {
                if (data.status == 'OK') {
                    layer.msg(data.msg);
                    window.history.back().reload();
                } else {
                    $('.save').removeAttr("disabled");
                    layer.msg(data.msg);
                }
            }
        });
    }
}

