/**
 * Created by Administrator on 2016/9/23.
 */
var url = {
    area: g.ctx + '/admin/common/area',
}
$(document).ready(function () {
    $('#carinfo_form').validator({
        // debug: true,
        stopOnError: true,
        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'memberAppraise.ServiceScore': 'required',
            'memberAppraise.DriverScore': 'required',
            'memberAppraise.VehicleScore': 'required',
            'memberAppraise.Detail': 'required'
        },
        valid: function (form) {
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/order/appraiseSaveOrUpdate',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.history.back().reload();
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        }
    });
});