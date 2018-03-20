var url = {
    area: g.ctx + '/admin/common/area',
}
$(document).ready(function () {
    $('#zxLine_form').validator({
        // debug: true,
        stopOnError: true,

        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'price': 'required;number',
            'sharing_price': 'required;number',
            'price_special': 'required;number',
            'sharing_price_special': 'required;number',
            'distance': 'required;number',

        },

        valid: function (form) {
            if ($('#company').val() == 0) {
                layer.msg("请选择所属公司");
                return;
            }
            if ($('#start_province').val() == 0) {
                layer.msg("请选择起点城市");
                return;
            }
            if ($('#start_province').val() == 0) {
                layer.msg("请选择终点城市");
                return;
            }
            if ($('#lineType').val() == 0) {
                layer.msg("请选择专线类型");
                return;
            }
            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/zxLine/save',
                data: $(form).serialize(),
                beforeSend: function () {
                    $("#tijiao").prop("disabled", "true");
                },
                success: function (data) {
                    if (data.status == 'OK') {
                        layer.msg(data.msg);
                        window.location.href = g.ctx + '/admin/zxLine';
                    } else {
                        layer.msg(data.msg);
                        $("#tijiao").prop("disabled", "");
                    }
                }
            });
        },

    });

    $('#start_province').change(function (e) {
        zxLine.area($('#start_city'), 'city', $(e.currentTarget).val());
        $('#start_county').empty();
        $('#start_county').append('<option value="0">请选择</option>');
    });
    $('#start_city').change(function (e) {
        zxLine.area($('#start_county'), 'district', $(e.currentTarget).val());
    });

    $('#end_province').change(function (e) {
        zxLine.area($('#end_city'), 'city', $(e.currentTarget).val());
        $('#end_county').empty();
        $('#end_county').append('<option value="0">请选择</option>');
    });
    $('#end_city').change(function (e) {
        zxLine.area($('#end_county'), 'district', $(e.currentTarget).val());
    });

    var zxLine = {
        area: function ($select, level, parent) {
            $select.empty();
            $.ajax({
                url: url.area,
                data: {level: level, parent: parent},
                success: function (data) {
                    $select.append('<option value="0">请选择</option>');
                    $(data.data).each(function (index, val) {
                        $select.append('<option value=' + val.adcode + '>' + val.name + '</option>');
                    });
                }
            })
        }
    }
});