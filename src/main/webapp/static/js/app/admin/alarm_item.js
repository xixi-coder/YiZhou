/**
 * Created by Administrator on 2016/9/28.
 */
$(document).ready(function () {
    $('#alarm_form').validator({
        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'alarm.status': 'required;',
            'alarm.dispose_message': 'required;'
        },

        valid: function (form) {
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/sys/alarm/save',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/sys/alarm';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        },
    });
});
