/**
 * Created by Administrator on 2016/9/9.
 */
$(document).ready(function () {

    $('#dj_insurance_amount').val($('#dj_insurance_amount').val().replace(',', '.'));
    $('#kc_insurance_amount').val($('#kc_insurance_amount').val().replace(',', '.'));
    $('#ss_insurance_amount').val($('#ss_insurance_amount').val().replace(',', '.'));
    $('#hh_insurance_amount').val($('#hh_insurance_amount').val().replace(',', '.'));
    $('#sw_insurance_amount').val($('#sw_insurance_amount').val().replace(',', '.'));
    $('#cz_insurance_amount').val($('#cz_insurance_amount').val().replace(',', '.'));
    $('#insurance_overdraft_amount').val($('#insurance_overdraft_amount').val().replace(',', '.'));
    $('#u_car_charge_rate').val($('#u_car_charge_rate').val().replace(',', '.'));
    $('#additional_charges').val($('#additional_charges').val().replace(',', '.'));
    $('.mill').each(function (i, o) {
        $(o).val($(o).val().replace(',', '.'));
    })


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

