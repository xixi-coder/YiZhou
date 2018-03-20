/**
 * Created by Administrator on 2016/9/23.
 */
$(document).ready(function () {

    $('#modelinfo_form').validator({
        // debug: true,
        stopOnError: true,
        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'carModel.name': 'required;name;remote[' + g.ctx + '/admin/member/driver/validModel, carModel.id, carModel.name, action];',
            'carModel.model_type': 'required'
        },

        valid: function (form) {
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/member/driver/savemodel',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/member/driver/carmodel/' + id;
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        },

    });


});