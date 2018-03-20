/**
 * Created by BOGONj on 2016/8/22.
 */
$(document).ready(function () {


    $('#user_form').validator({
        // debug: true,
        stopOnError: true,
        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'user.name': 'required; chinese;',
            'user.username': 'required;remote[' + g.ctx + '/admin/sys/user/validUser, user.id, user.username, action]',
            password: 'required; password',
            'user.phone': 'required;mobile'
        },
        valid: function (form) {
            if ($('#company').val() == 0) {
                layer.msg("请选择所属公司");
                return;
            }
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/sys/user/save',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/sys/user';
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
    uploadPlug.create("#picker", $('#portrait_show'), $('#head_portrait'));
});
