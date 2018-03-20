/**
 * Created by BOGONj on 2016/8/22.
 */
var url = {
    save: g.ctx + '/admin/sys/royalty/save'
};
$(document).ready(function () {
    $('#plus_time').on('click', royalty.plus_time);
    $("#type").change(function () {
        index = 1;
        $('#item_type').empty();
    });
    $('#sub_btn').on('click', royalty.subData);
    $('.plus_money').on('click', royalty.plusMoney);
    $('.dele_self').on('click', royalty.deleSelf);
    $(".ticheng_tt").on('change', royalty.selectType);
    $('.remove_money').on('click', royalty.deleMoney);
    var selects = $('select');
    console.log(selects);
    selects.each(function (index, val) {
        $(val).val($(val).data("value"));
    });
});
var royalty = {
    plus_time: function (target) {
        var type = $('#type').val();
        if (type == 2) {
            $('#item_type').append(tpl.bind($('#money_type'), {index: index}));
            $('.plus_money').off('click');
            $('.plus_money').on('click', royalty.plusMoney);
            $('.ticheng_tt').on('change', royalty.selectType);
        } else if (type == 1) {
            $('#item_type').append(tpl.bind($('#simple_type'), {index: index}));
            $('input:not(.ios-switch)').iCheck({
                checkboxClass: 'icheckbox_square-aero',
                radioClass: 'iradio_square-aero',
                increaseArea: '20%' // optional
            });
        }
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
        index += 1;
        $('.dele_self').on('click', royalty.deleSelf);

    },
    selectType: function (target) {
        if ($(target.currentTarget).val() == 2) {
            $(target.currentTarget).next().next().next().next().next().html('%')
        } else {
            $(target.currentTarget).next().next().next().next().next().html("元")
        }
    },
    plusMoney: function (target) {
        var tmp = $(target.currentTarget).parent().parent().parent().parent().children();
        var tmpLenght = tmp.length;
        var end = $($(tmp[tmpLenght - 1]).find('.money')[0]).val();
        if (end == '') {
            layer.msg('请填写金额');
            return
        }
        $(target.currentTarget).parent().parent().parent().parent().append(tpl.bind($('#money_type_item'), {start: end}));
        $('.remove_money').on('click', royalty.deleMoney);
        $('.ticheng_tt').on('change', royalty.selectType);
    },
    deleMoney: function (target) {
        $(target.currentTarget).parent().parent().parent().remove();
    },
    deleSelf: function (target) {
        $(target.currentTarget).parent().parent().parent().parent().remove();
    }, subData: function (target) {
        $('#sub_btn').attr("disabled", "disabled");
        var name = $('#name').val();
        var type = $('#type').val();
        var timePoint = $('#time_point').val();
        if (name == '') {
            layer.msg('名称不能为空');
            $('#name').focus();
            return
        }

        var typeArray = $('#item_type').children();
        var type = $('#type').val();
        var array = [];
        $(typeArray).each(function (index, val) {
            if (type == 2) {
                var tmpRow = $(val).find(".row");
                var moneyItems = [];
                var money = {};
                $(tmpRow).each(function (index, val) {
                    var moneyItem = {};
                    var tmpInput = $(val).find('input');
                    var selects = $(val).find('select');
                    if (index == 0) {
                        money['startTime'] = $(selects[0]).val() + ':' + $(selects[1]).val();
                        money['endTime'] = $(selects[2]).val() + ':' + $(selects[3]).val();
                    } else {
                        moneyItem['startMoney'] = $(tmpInput[0]).val();
                        moneyItem['endMoney'] = $(tmpInput[1]).val();
                        moneyItem['type'] = $($(val).find('.ticheng_tt')).val();
                        moneyItem['eachMoney'] = $(tmpInput[2]).val();
                        moneyItem['tiMoney'] = $(tmpInput[3]).val();
                        moneyItems[index - 1] = moneyItem;
                    }
                });
                money['item'] = moneyItems;
                array[index] = money;
            } else if (type == 1) {
                var simple = {};
                var tmpInput = $(val).find('input');
                var selects = $(val).find('select');
                simple['startTime'] = $(selects[0]).val() + ':' + $(selects[1]).val();
                simple['endTime'] = $(selects[2]).val() + ':' + $(selects[3]).val();
                simple['guTiYuan'] = $(tmpInput[0]).val();
                simple['guShaoYuYuan'] = $(tmpInput[1]).val();
                simple['guTiCheng'] = $(tmpInput[2]).val();
                simple['biLiTiYuan'] = $(tmpInput[3]).val();
                simple['biLiShaoYuYuan'] = $(tmpInput[4]).val();
                simple['biLiTiCheng'] = $(tmpInput[5]).val();
                simple['type'] = $($(val).find('input[name="ticheng_type"]:checked')).val();
                array[index] = simple;
            }
        });
        $.ajax({
            url: url.save,
            type: 'POST',
            data: {id: $('#id').val(), name: name, type: type, timePoint: timePoint, item: JSON.stringify(array)},
            success: function (data) {
                if (data.status == 'OK') {
                    layer.msg(data.msg);
                    window.history.back().reload();
                } else {
                    $('#sub_btn').removeAttr("disabled");
                    layer.msg(data.msg);
                }
            }
        });
    }
};

