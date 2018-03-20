/**
 * Created by BOGONm on 16/8/11.
 */
$(document).ready(function () {


    $('#smstmp_form').validator({
        // debug: true,
        stopOnError: true,
        //自定义用于当前实例的消息
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
                url: g.ctx + '/admin/sys/servicetype/save',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/sys/servicetype';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        }
    });

});
