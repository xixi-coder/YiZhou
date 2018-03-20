/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var url = {
        save: g.ctx + '/admin/sys/company/saveDistribution'
    };
    service_type
    $("input[name='distribution.type']").each(function (index, val) {
        if ($(val).val() == $('#tmp_type').val()) {
            $(val).iCheck('check');
        }
    });
    var units = $(".unit");
    $(units).each(function (index, val) {
        $(val).val($(val).data('value'));
    });
    $('.unit').on('change', function (target) {
        if($(target.currentTarget).val()==1){
            $("#"+$(target.currentTarget).data("unit")).html("%");
        }else{
            $("#"+$(target.currentTarget).data("unit")).html("元");
        }
    });
    $('#level').val($('#tmp_level').val()).trigger('change');
    distribution.showLevel($('#tmp_level').val());
    var orderType = Number($('#type').val());
    $($('.tabs-select')[orderType - 1]).addClass("active ");
    $('#distribution_form').validator({
        // debug: true,
        stopOnError: true,
        rules: {
            number: [/^[0-9]*$/, "请输入数字"],
            url: [/^((https?|ftp|news):\/\/)?([a-z]([a-z0-9\-]*[\.。])+([a-z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))(\/[a-z0-9_\-\.~]+)*(\/([a-z0-9_\-\.]*)(\?[a-z0-9+_\-\.%=&]*)?)?(#[a-z][a-z0-9_]*)?$/, "请输入正确的网址"]
        },
        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        valid: function (form) {
            var items = [];
            $('.d-item').each(function (index, val) {
                var display = $($(val).parent().parent().parent()).css("display");
                if (display == 'block') {
                    var item = {};
                    var selects = $(val).find('select');
                    var inputs = $(val).find('input');
                    item['re_type'] = $(selects[0]).val();
                    item['type'] = $(inputs[0]).val();
                    item['amount'] = $(inputs[1]).val();
                    item['times'] = $(inputs[2]).val();
                    items.push(item);
                }
            });
            $('#items').val(JSON.stringify(items));
            $.ajax({
                type: 'post',
                url: url.save,
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/sys/company';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        }
    });
    $('.ticheng_tt').on('change', distribution.selectType);
    $('#level').on('change', function (target) {
        distribution.showLevel($(target.currentTarget).val());
    });
});

var distribution = {
    showLevel: function (val) {
        switch (val) {
            case '0':
                $('#one').hide();
                $('#two').hide();
                $('#three').hide();
                break;
            case '1':
                $('#one').show();
                $('#two').hide();
                $('#three').hide();
                break;
            case '2':
                $('#one').show();
                $('#two').show();
                $('#three').hide();
                break;
            case '3':
                $('#one').show();
                $('#two').show();
                $('#three').show();
                break;
        }
    }
}