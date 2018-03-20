/**
 * Created by Administrator on 2016/9/22.
 */
$(document).ready(function () {
    $("#audit").on('click', function (a) {
        var index = layer.open({
            type: 0,
            content: tpl.bind($('#audit_tpl')),
            btn: ['审核通过', '审核不通过'],
            yes: function (index, layero) {
                $('#audit_form').validator({
                    valid: function (form) {
                        $.ajax({
                            type: 'post',
                            url: g.ctx + '/admin/member/driver/audit/1',
                            data: $(form).serialize(),
                            success: function (data) {
                                if (data.status == 'OK') {
                                    layer.msg(data.msg);
                                    window.location.href = g.ctx + '/admin/member/driver';
                                } else {
                                    layer.alert(data.msg);
                                    //window.location.href=g.ctx+'/admin/member/driver';
                                }
                            }
                        });
                    }
                });
                $('#audit_form').trigger('submit');
            },
            btn2: function (index, layero) {
                $('#audit_form').validator({
                    valid: function (form) {
                        $.ajax({
                            type: 'post',
                            url: g.ctx + '/admin/member/driver/audit/0',
                            data: $(form).serialize(),
                            success: function (data) {
                                if (data.status == 'OK') {
                                    layer.msg(data.msg);
                                    window.location.href = g.ctx + '/admin/member/driver';
                                } else {
                                    layer.alert(data.msg);
                                    //window.location.href=g.ctx+'/admin/member/driver';
                                }
                            }
                        });

                    }
                });
                $('#audit_form').trigger('submit');
            }
        });
    });

    $('#modelinfo_form').validator({
        // debug: true,
        stopOnError: true,

        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {},


        valid: function (form) {
            /*if ($('driver_license').val() == null) {
                 layer.msg("请上传驾驶证照！")
            }
*/
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/member/driver/savelicense/' + id,
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.reload();
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        },


    });
    uploadPlug.create("#picker1", $('#portrait_show1'), $('#driving_license'));
    uploadPlug.create("#picker", $('#portrait_show'), $('#driver_license'));
    uploadPlug.create("#picker2", $('#portrait_show2'), $('#id_card'));
    uploadPlug.create("#picker3", $('#portrait_show3'), $('#photo'));
    uploadPlug.create("#picker4", $('#portrait_show4'), $('#qualification_certificate'));

})