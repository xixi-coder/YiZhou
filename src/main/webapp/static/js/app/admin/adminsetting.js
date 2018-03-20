/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {


    $('#adminsetting_form').validator({
        // debug: true,
        stopOnError: true,
        rules: {
            number: [/^[0-9]*$/, "请输出数字"]
        },

        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {},

        valid: function (form) {
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/sys/adminsetting/save/'+index,
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        var index = data.data;
                        layer.msg(data.msg);
                        // window.location.href = g.ctx + '/admin/sys/adminsetting/'+index;
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        },

    });

});

