/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {
    $('#syslog_form').validator({
        // debug: true,
        stopOnError: true,
        rules: {
            number: [/^[0-9]*$/, "请输出数字"],
            url: [/\\{([^\}]+?)\\}/, "请输入正确格式的路径"]
        },

        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'syslog.url': 'required',
            'syslog.params': 'required',
            'syslog.remark': 'required'
        },

        valid: function (form) {
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/sys/syslog/save',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/sys/syslog';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        },

    });

});

