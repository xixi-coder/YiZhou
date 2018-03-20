/**
 * Created by admin on 2016/10/12.
 */
/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    var url = {
        save: g.ctx + '/admin/sys/company/saveAgreement'
    };
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
});