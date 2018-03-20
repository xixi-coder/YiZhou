/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {


    $('#smstmp_form').validator({
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
        fields: {
            'smstmp.creater': 'required;number',
            'smstmp.company': 'required',
            'smstmp.type': 'required;number'
        },

        valid: function (form) {
            if ($('#company').val() == 0) {
                layer.msg("请选择所属公司");
                return;
            }

            if ($('#type').val() == 0) {
                layer.msg("请选择模板类型");
                return;
            }


            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/sys/smstmp/save',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/sys/smstmp';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        },

    });

});

