/**
 * Created by Administrator on 2016/9/14.
 */
$(document).ready(function () {
    var select = $("select");
    $(select).each(function (index, val) {
        $(val).val($(val).data("value")).trigger("change");
    });
    $('#smstmp_form').validator({
        // debug: true,
        stopOnError: true,

        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'driverInfo.real_name': 'required',
            'memberLogin.user_name': 'required;user_name;remote[' + g.ctx + '/admin/member/driver/validList, memberLogin.id, memberLogin.user_name, action];',
            'driverInfo.phone': 'required;mobile',
            'driverInfo.email': 'email',
            password: 'required; password'
        },

        valid: function (form) {
            if ($('#company').val() == 0) {
                layer.msg("请选择所属公司");
                return;
            }
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/member/driver/save',
                data: $(form).serialize(),
                beforeSend: function () {
                    $("#tijiao").prop("disabled", "true");
                },
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/member/driver';
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