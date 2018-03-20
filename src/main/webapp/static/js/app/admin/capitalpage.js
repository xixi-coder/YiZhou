/**
 * Created by Administrator on 2016/9/13.
 */
$(document).ready(function () {

    $('#smstmp_form').validator({
        // debug: true,
        stopOnError: true,

        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'capital.reply_content': 'required;'
        },

        valid: function (form) {
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/member/list/complainSave',
                data: $(form).serialize(),
                beforeSend: function () {
                    $("#tijiao").prop("disabled", "true");
                },
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.history.go(-1);
                    } else {
                        layer.msg(data.msg);
                        $("#tijiao").prop("disabled", "");
                    }
                }
            });
        },
    });
});