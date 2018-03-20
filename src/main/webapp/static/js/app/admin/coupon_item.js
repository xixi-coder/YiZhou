/**
 * Created by Administrator on 2016/9/28.
 */
$(document).ready(function () {
    $('#coupon_form').validator({
        // debug: true,
        stopOnError: true,
        rules: {
            number: [/^[0-9]*$/, "请输入正整数"],
            double: [/^[0-9]+([.]{1}[0-9]+){0,1}$/, "请输入数字（不包含负数）"]
        },

        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'coupon.coupon_title': 'required;',
            'coupon.coupon_desc': 'required;',
            'coupon.mini_price': 'required;double',
            'coupon.base_amount':'required;',
            'coupon.amount': 'required;double',
            'coupon.start_time': 'required;',
            'coupon.end_time': 'required;'
        },

        valid: function (form) {
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/activity/coupon/save',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/activity/coupon';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        },

    });
    //
    // $('#coupon_type').on('select2:select', function (evt) {
    //     // Do something
    //     if ($('#type').val() == 1) {
    //         $(".base-amount").show();
    //         $(".coupon-amount").show();
    //         $(".percen-amount").hide();
    //         $(".coupon-percent").hide();
    //     } else {
    //         $(".coupon-percent").show();
    //         $(".percen-amount").show();
    //         $(".base-amount").hide();
    //         $(".coupon-amount").hide();
    //     }
    //
    // });
    // if ($('#coupon_type').data("value") == 1) {
    //     $(".base-amount").show();
    //     $(".coupon-amount").show();
    //     $(".percen-amount").hide();
    //     $(".coupon-percent").hide();
    // } else {
    //     $(".coupon-percent").show();
    //     $(".percen-amount").show();
    //     $(".base-amount").hide();
    //     $(".coupon-amount").hide();
    // }
});
