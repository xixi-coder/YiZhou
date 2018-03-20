
$(document).ready(function () {

    /*默认加载优惠券*/
    $.get(g.ctx + "/admin/activity/coupon/cascading?id=" + g.company,
        function (data) {
            console.log(data);
            if (data['code'] == 200) {
                plateOption(data['data'])
            }
        }
    )
    var plateOption = function (dataArray) {
        var select1 = $("#e_spe2");

        $("#e_spe2 option").remove();
        $("<option>--请选择--</option>").appendTo(select1);
        dataArray.forEach(function (data) {
            $("<option value='" + data['id'] + "'>" + data['coupon_title'] + "</option>").appendTo(select1);

        })
    }

    if ($('#event').data("value") != 4) {
        $('#type').prop("disabled", false);
    }
    if ($('#event').data("value") == 1) {
        $(".service_type").hide();
    } else if ($('#event').data("value") == 4) {
        $('#type').val("2").trigger("change");
        $('#type').prop("disabled", true);
        $(".service_type").hide();
        $(".couponSelect").show();
        $(".moneyInput").hide();
    } else {
        $(".service_type").show();
    }

    $('#event').on('select2:select', function (evt) {
        // Do something
        if ($('#event').val() == 1) {
            $(".service_type").hide();
        } else if ($('#event').val() == 4) {
            $('#type').val("2").trigger("change");
            $('#type').prop("disabled", true);
            $(".service_type").hide();
            $(".couponSelect").show();
            $(".moneyInput").hide();
        } else {
            $(".service_type").show();
        }
        if ($('#event').val() != 4) {
            $('#type').prop("disabled", false);
        }
    });

    $('#type').on('select2:select', function (evt) {
        // Do something
        if ($('#type').val() == 1) {
            $(".moneyInput").show();
            $(".couponSelect").hide();
        } else {
            $(".couponSelect").show();
            $(".moneyInput").hide();
        }

    });
    if ($('#type').data("value") == 1) {
        $(".moneyInput").show();
        $(".couponSelect").hide();
    } else {
        $(".couponSelect").show();
        $(".moneyInput").hide();
    }
    $("select").each(function (index, val) {
        $(val).val($(val).data("value"));

        $(val).trigger('change.select2');
    });

    $('#company').change(function () {
        var groupId = $("#company").find("option:selected").val();
        if (groupId == "") {
            groupId = -1;
        }
        $.get(g.ctx + "/admin/activity/coupon/cascading?id=" + groupId,
            function (data) {
                console.log(data);
                if (data['code'] == 200) {
                    plateOption(data['data'])
                }
            }
        )
        var plateOption = function (dataArray) {
            var select1 = $("#e_spe2");

            $("#e_spe2 option").remove();
            $("<option>--请选择--</option>").appendTo(select1);
            dataArray.forEach(function (data) {
                $("<option value='" + data['id'] + "'>" + data['coupon_title'] + "</option>").appendTo(select1);

            })
        }
        // var plateOption = function (dataArray) {
        //     var select1 = $("#e_spe2");
        //     $("#e_spe2 option").remove();
        //     dataArray.forEach(function (data) {
        //         $("<label><input type='checkbox' value='" + data['id'] + "'>" + data['coupon_title'] + "</label>").appendTo(select1);
        //
        //     })
        // }

    });

    $('#activity_form').validator({
        // debug: true,
        stopOnError: true,

        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        rules: {
            number: [/^[0-9]*$/, "请输出数字"]
        },
        //待验证字段集合
        fields: {
            'activity.name': 'required;',
            'activity.event': 'required',
            'activity.start_time': 'required',
            'activity.end_time': 'required'
        },
        valid: function (form) {
            if ($("#event").val() == 0) {
                layer.msg("请选择事件类型")
                return
            }
            $('#type').prop("disabled", false);
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/activity/list/save',
                data: $(form).serialize(),
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/activity/list';
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        },
    });
    uploadPlug.create("#picker", $('#portrait_show'), $('#logo'));
});
var activity = {
    show: function () {
    }
}
