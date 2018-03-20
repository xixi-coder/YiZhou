/**
 * Created by Administrator on 2016/9/23.
 */
$(document).ready(function () {

    $('#brandinfo_form').validator({
        // debug: true,
        stopOnError: true,
        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'carBrand.name': 'required;name;remote[' + g.ctx + '/admin/member/driver/validBrand, carBrand.id, carBrand.name, action];',
            'carBrand.py_name': 'required'
        },

        valid: function (form) {
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/member/driver/savebrand?',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/member/driver/carbrand';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        },

    });
    uploadPlug.create("#picker", $('#portrait_show'), $('#logo'));
});