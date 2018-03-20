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
            'carBrand.name': 'required',
            'carModel.name': 'required',
            'carInfo.join_time': 'required',
            'car.plate_no': 'required',
            'car.vin': 'required',
            'carInfo.distance': 'required'
        },

        valid: function (form) {
            if ($('#province').val() == 0) {
                layer.msg("请选择省份");
                return;
            }

            $.ajax({
                type: 'post',
                url: g.ctx + '/admin/member/driver/savecar',
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
        },

    });
    uploadPlug.create("#picker", $('#portrait_show'), $('#picture'));
    uploadPlug.create("#picker2", $('#Certificate'), $('#Certificate'));

    $('#brand').change(function (e) {
        car.model($('#model'), $(e.currentTarget).val());
    });
    var car = {
        model: function ($select, brand) {
            $select.empty();
            $.ajax({
                url: g.ctx + '/admin/common/car',
                data: {brand: brand},
                success: function (data) {
                    console.log(data);
                    $select.append('<option value="0">请选择</option>');
                    $(data.data).each(function (index, val) {
                        $select.append('<option value=' + val.id + '>' + val.name + '</option>');
                    });
                }
            })
        }
    }

    $('#province').change(function (e) {
        carInfo.area($('#city'), 'city', $(e.currentTarget).val());
        $('#county').empty();
        $('#county').append('<option value="0">请选择</option>');
    });
    $('#city').change(function (e) {
        carInfo.area($('#county'), 'district', $(e.currentTarget).val());
    });

    var carInfo = {
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