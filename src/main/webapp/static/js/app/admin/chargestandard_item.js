/**
 * Created by BOGONj on 2016/9/18.
 */
/**
 * Created by BOGONj on 2016/8/22.
 */
var url = {
    save: g.ctx + '/admin/sys/chargestandard/save/'+type
}
var editor;
$(document).ready(function () {
    $('.add_item').on('click', chargeStandard.addItem);
    $('.add_mill').on('click', chargeStandard.addMill);
    $('.save').on('click', chargeStandard.save);
    $('.del_item').on('click', chargeStandard.delItem);
    $('.del_mill_item').on('click', chargeStandard.delMill);
    var selects = $('select');
    selects.each(function (index, val) {
        $(val).val($(val).data("value"));
    });

    KindEditor.ready(function (K) {
        editor = K.create('#desc', {
            uploadJson: g.ctx + '/file/edit',
            allowFileManager: false
        });
    });

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
                itemTmp['onedistance1'] = $(csInfo[13]).val();
                itemTmp['onediscount1'] = $(csInfo[14]).val();
                itemTmp['onedistance2'] = $(csInfo[15]).val();
                itemTmp['onediscount2'] = $(csInfo[16]).val();
                itemTmp['onedistance3'] = $(csInfo[17]).val();
                itemTmp['onediscount3'] = $(csInfo[18]).val();
                itemTmp['onedistance4'] = $(csInfo[19]).val();
                itemTmp['onediscount4'] = $(csInfo[20]).val();
                itemTmp['onedistance5'] = $(csInfo[21]).val();
                itemTmp['onediscount5'] = $(csInfo[22]).val();

                itemTmp['twodistance1'] = $(csInfo[23]).val();
                itemTmp['twodiscount1'] = $(csInfo[24]).val();
                itemTmp['twodistance2'] = $(csInfo[25]).val();
                itemTmp['twodiscount2'] = $(csInfo[26]).val();
                itemTmp['twodistance3'] = $(csInfo[27]).val();
                itemTmp['twodiscount3'] = $(csInfo[28]).val();
                itemTmp['twodistance4'] = $(csInfo[29]).val();
                itemTmp['twodiscount4'] = $(csInfo[30]).val();
                itemTmp['twodistance5'] = $(csInfo[31]).val();
                itemTmp['twodiscount5'] = $(csInfo[32]).val();

                itemTmp['threedistance1'] = $(csInfo[33]).val();
                itemTmp['threediscount1'] = $(csInfo[34]).val();
                itemTmp['threedistance2'] = $(csInfo[35]).val();
                itemTmp['threediscount2'] = $(csInfo[36]).val();
                itemTmp['threedistance3'] = $(csInfo[37]).val();
                itemTmp['threediscount3'] = $(csInfo[38]).val();
                itemTmp['threedistance4'] = $(csInfo[39]).val();
                itemTmp['threediscount4'] = $(csInfo[40]).val();
                itemTmp['threedistance5'] = $(csInfo[41]).val();
                itemTmp['threediscount5'] = $(csInfo[42]).val();
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

