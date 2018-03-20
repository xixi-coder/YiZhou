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
            'memberInfo.real_name': 'required;',
            'memberLogin.user_name': 'required;user_name;remote[' + g.ctx + '/admin/member/list/validList, memberLogin.id, memberLogin.user_name, action];',
            'memberInfo.address': '',
            'memberInfo.phone': 'required;mobile',
            'memberInfo.nick_name': 'required',
            'memberInfo.email': 'email'
        },

        valid: function (form) {
            if ($('#company').val() == 0) {
                layer.msg("请选择所属公司");
                return;
            }
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/member/list/save',
                data: $(form).serialize(),
                beforeSend: function () {
                    $("#tijiao").prop("disabled", "true");
                },
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/member/list';
                    } else {
                        layer.msg(data.msg);
                        $("#tijiao").prop("disabled", "");
                    }
                }
            });
        },

    });
    uploadPlug.create("#picker", $('#portrait_show'), $('#head_portrait'));

});