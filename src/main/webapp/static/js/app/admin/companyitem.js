/**
 * Created by Administrator on 2016/9/28.
 */
$(document).ready(function () {
    $('#company_form').validator({
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
        fields: {},

        valid: function (form) {
            if ($('#sname').val() == 0) {
                layer.msg("请选择服务类型");
                return;
            }
            if ($('#rname').val() == 0) {
                layer.msg("请选择提成标准");
                return;
            }
            if ($('#cname').val() == 0) {
                layer.msg("请选择收费标准");
                return;
            }

            if ($('#company').val() == 0) {
                layer.msg("请选择所属公司");
                return;
            }
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/sys/servicetype/companyservicesave',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/sys/servicetype/companyindex';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        },

    });

});
