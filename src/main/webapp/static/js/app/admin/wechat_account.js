/**
 * Created by admin on 2016/9/30.
 */
$(document).ready(function () {
    $('#wechat_ccount_form').validator({
        // debug: true,
        stopOnError: true,
        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        valid: function (form) {
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/wechat/account/save',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        }
    });

    $("#userSubmit").on('click', function () {
        $('#user_form').submit();
    });
});
