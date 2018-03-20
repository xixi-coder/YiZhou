/**
 * Created by BOGONj on 2016/8/22.
 */
var url = {
    area: g.ctx + '/admin/common/area',
    save: g.ctx + '/admin/sys/company/save',
    update_service: g.ctx + '/admin/sys/company/serviceTypeDisabled'
}
$(document).ready(function () {

    $('#company_form').validator({
        // debug: true,
        stopOnError: true,
        //自定义用于当前实例的消息
        messages: {
            required: "必须填写"
        },
        //待验证字段集合
        fields: {
            'company.name': 'required;',
            'company.phone': 'required',
            'company.website': 'url',
            'company.qq': 'qq'
        },
        valid: function (form) {
            var isOk = true;
            var tr = $('.area');
            var areasArray = [];
            $(tr).each(function (index, val) {
                var area = {};
                area.province = $(val).find('.province').val();
                if (area.province == null || area.province == 0) {
                    isOk = false;
                    return false;
                }
                area.city = $(val).find('.city').val();
                area.county = $(val).find('.county').val();
                areasArray.push(area);
            });
            $("#area_value").val(JSON.stringify(areasArray));
            if (isOk) {
                $.ajax({
                    type: 'post',
                    url: url.save,
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
        }
    });

    $('.address').on('click', function (a) {
        layer.open({
            area: ['80%', '80%'],
            title: false,
            closeBtn: 0,
            shadeClose: true,
            content: tpl.bind($("#address_map"), {}),
            success: function (layero, index) {
                var map = new AMap.Map('container', {});
                map.plugin(['AMap.ToolBar'], function () {
                    //设置地位标记为自定义标记
                    var toolBar = new AMap.ToolBar();
                    map.addControl(toolBar);
                });
                //实例化城市查询类
                var citysearch = new AMap.CitySearch();
                //自动获取用户IP，返回当前城市
                var cityName;
                map.on('click', company.address);
                var latitude = $('#latitude').val();
                var longitude = $('#longitude').val();
                if (latitude != '' && latitude != '') {
                    map.clearMap();
                    var marker = new AMap.Marker({
                        icon: "http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png",
                        position: [longitude, latitude]
                    });
                    marker.on("click", company.address);
                    marker.setMap(map);
                    map.setFitView();
                    map.setZoomAndCenter(20, [longitude, latitude]);
                } else {
                    map.clearMap();
                    citysearch.getLocalCity(function (status, result) {
                        if (status === 'complete' && result.info === 'OK') {
                            if (result && result.city && result.bounds) {
                                var cityinfo = result.city;
                                var citybounds = result.bounds;
                                //地图显示当前城市
                                cityName = cityinfo;
                                map.setBounds(citybounds);
                            }
                        }
                    });
                }
                $('#search_btn').on('click', function () {
                    map.clearMap();
                    //关键字查询
                    var address = $('#search_input').val();
                    AMap.service(["AMap.PlaceSearch"], function () {
                        var placeSearch = new AMap.PlaceSearch({ //构造地点查询类
                            pageSize: 10,
                            pageIndex: 1,
                            map: map
                        });

                        placeSearch.search(address, function (status, result) {
                            if (status === 'complete' && result.info === 'OK') {
                                map.clearMap();
                                var data = result.poiList.pois;
                                $(data).each(function (index, val) {
                                    var marker = new AMap.Marker({
                                        icon: "http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png",
                                        position: [val.location.lng, val.location.lat],
                                        title: val.name
                                    });
                                    marker.setMap(map);
                                    marker.on("click", company.address);
                                });
                            }
                        });
                    });
                });
            },
            btn: ['取消']
        });
    });
    area.areaChange();
    $(".add-area").on('click', function (e) {
        var parTr = $($($(e.currentTarget).parent()).parent());
        console.log(parTr);
        var tmpHtml = tpl.bind($("#area_tmp"), null);
        parTr.after(tmpHtml);
        $('.select2').select2({
            placeholder: "请选择",
            language: "zh-CN"
        });
        area.areaChange();
        area.del();
    });
    uploadPlug.create("#picker", $('#picture_show'), $('#picture'));
});

var company = {
    address: function (e) {
        $('#latitude').val(e.lnglat.lat);
        $('#longitude').val(e.lnglat.lng);
        var lnglatXY = [e.lnglat.lng, e.lnglat.lat];
        var geocoder = new AMap.Geocoder({
            radius: 1000,
            extensions: "all"
        });
        geocoder.getAddress(lnglatXY, function (status, result) {
            if (status === 'complete' && result.info === 'OK') {
                var address = result.regeocode.formattedAddress; //返回地址描述
                $('#address').val(address);
                layer.closeAll();
            }
        });
    },
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
};

var area = {
    del: function () {
        $(".dele-area").off('click');
        $(".dele-area").on('click', function (e) {
            $($($(e.currentTarget).parent()).parent()).remove();
        });
    },
    areaChange: function () {
        $('.province').change(function (e) {
            var all = $(e.currentTarget).nextAll();
            var city = $(all[1]);
            company.area(city, 'city', $(e.currentTarget).val());
            var county = $(all[3]);
            $(county).empty();
            $(county).append('<option value="0">请选择</option>');
        });
        $('.city').change(function (e) {
            var all = $(e.currentTarget).nextAll();
            var county = $(all[1]);
            company.area(county, 'district', $(e.currentTarget).val());
        });
    }
}


function update_service_type(id, company) {
    layer.confirm('您确定要对此进行操作？', {
        btn: ['是', '否'] //按钮
    }, function () {
        $.ajax({
            type: 'POST',
            url: url.update_service,
            data: {
                serviceType: id,
                company: company
            },
            success: function (data) {
                if (data.status == 'OK') {
                    layer.msg(data.msg);
                    location.reload();
                } else {
                    layer.msg(data.msg);
                }
            }
        });
    }, function () {

    });


}
