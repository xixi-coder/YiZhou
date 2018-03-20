
var map;
var currentCity;
var start = true;

var reservation_lng = $("#reservation_lng").val().replace(",",".");
var reservation_lat = $("#reservation_lat").val().replace(",",".");
var destination_lng = $("#destination_lng").val().replace(",",".");
var destination_lat = $("#destination_lat").val().replace(",",".");
var orderId = $("#orderId").val();
var url = {
    memberInfo: g.ctx + '/admin/order/memberInfo',
    driverList: g.ctx + '/admin/order/memberlistZx',
    create: g.ctx + '/admin/order/dispatchZxOrder'
}
var driverMap = new AMap.Map('hidden', {
    center: [117.000923, 36.675807],
    zoom: 6
});
map = new AMap.Map('admap', {
    center: [117.000923, 36.675807],
    zoom: 6
});
var driving = new AMap.Driving({
    map: map
});
var table;
$(document).ready(function () {

    //基本地图加载
    var map = new AMap.Map("admap", {
        resizeEnable: true,
        center: [116.397428, 39.90953],//地图中心点
        zoom: 6 //地图显示的缩放级别
    });
    //构造路线导航类
    var driving = new AMap.Driving({
        map: map,

    });
    // 根据起终点经纬度规划驾车导航路线
    driving.search(new AMap.LngLat(reservation_lng, reservation_lat), new AMap.LngLat(destination_lng, destination_lat));



    $("#admap").css("height", window.screen.height - 170);
    map.plugin(["AMap.ToolBar"], function () {
        map.addControl(new AMap.ToolBar());
    });
    //实例化城市查询类
    var citysearch = new AMap.CitySearch();
    citysearch.getLocalCity(function (status, result) {
        if (status === 'complete' && result.info === 'OK') {
            if (result && result.city && result.bounds) {
                var cityinfo = result.city;
                var citybounds = result.bounds;
                //地图显示当前城市
                cityName = cityinfo;
                currentCity = cityinfo;
                map.setBounds(citybounds);
            }
        }
    });
    map.on('click', createOrder.clickF);
    $('#start_address').on('click', function () {
        start = true;
        createOrder.search();
        // createOrder.celAmount();
    });
    $('#end_address').on('click', function () {
        start = false;
        createOrder.search();

    });
    $("#phone").on('blur', function (target) {
        var phone = $(target.currentTarget).val();
        createOrder.memberInfo(phone);
    });
    $('.select2').select2({
        placeholder: "请选择",
        language: "zh-CN"
    });
    $('.datetimepicker-input').datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
        startDate: new Date(),
        autoclose: 1,
        format: 'yyyy-mm-dd hh:ii',
        minuteStep: 1
    });
    createOrder.loadList();
    // $('#companySelect').on('change', createOrder.celAmount);
    $('#orderAmount').iCheck({
        checkboxClass: 'icheckbox_square-aero',
        radioClass: 'iradio_square-aero',
        increaseArea: '20%' // optional
    });
    $('#orderAmount').on('ifChecked', function (event) {
        $($("#amount").parent()).show();
    });
    $('#orderAmount').on('ifUnchecked', function (event) {
        $($("#amount").parent()).hide();
    });
    $("#referes").on('click', function () {
        if ($("#reservation_lng").val() == '' || $("#reservation_lat").val() == '') {
            layer.msg("起点未选择!");
            return false;
        } else {
            table.ajax.reload();
        }
    });
    $("#createOrder").on('click', function () {
        var driverIds = '';
        $(".checkOther").each(function (index, val) {
            if ($(val).is(':checked')) {
                driverIds += $(val).val() + ',';
            }
        });
        $("#driverIds").val(driverIds);
        $.ajax({
            type: 'post',
            url: url.create,
            data: $("#createOrder_form").serialize(),
            success: function (data) {
                if (data.status == 'OK') {
                    layer.msg(data.msg);
                    window.opener = null;
                    window.open('', '_self');
                    window.close();
                } else {
                    layer.msg(data.msg);
                }
            }
        });
    });
});
var createOrder = {
    address: function (e) {
        var result = {};
        result['lat'] = e.lnglat.lat;
        result['lng'] = e.lnglat.lng;
        return result;
    },
    search: function () {
        map.clearMap();
        AMap.service(["AMap.PlaceSearch"], function () {
            var placeSearch = new AMap.PlaceSearch({ //构造地点查询类
                pageSize: 10,
                pageIndex: 1,
                map: map,
                city: currentCity
            });
            //关键字查询
            var keyWord = '';
            if (start) {
                keyWord = $("#reservation_address").val();
            } else {
                keyWord = $("#destination").val();
            }
            placeSearch.search(keyWord, function (status, result) {
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
                        marker.on('click', createOrder.clickF);
                    });
                }
            });
        });
    },
    // clickF: function (e) {
    //     var dataInfo = {};
    //     dataInfo['lng'] = e.lnglat.lng;
    //     dataInfo['lat'] = e.lnglat.lat;
    //     if (start) {
    //         console.log(e.lnglat.lat);
    //         $("#reservation_lat").val(e.lnglat.lat);
    //         $("#reservation_lng").val(e.lnglat.lng);
    //         table.ajax.reload();
    //     } else {
    //         $("#destination_lat").val(e.lnglat.lat);
    //         $("#destination_lng").val(e.lnglat.lng);
    //         driving.clear();
    //         driving.search(new AMap.LngLat(reservation_lng,reservation_lat),
    //             new AMap.LngLat(destination_lng, destination_lat), function (status, result) {
    //                 var distance = parseFloat(result.routes[0].distance) / 1000;
    //                 var time = parseFloat(result.routes[0].time) / 60;
    //                 $("#time").val(time);
    //                 $("#distance").val(distance);
    //                 // createOrder.celAmount();
    //             });
    //     }
    //     var lnglatXY = [e.lnglat.lng, e.lnglat.lat];
    //     var geocoder = new AMap.Geocoder({
    //         radius: 1000,
    //         extensions: "all"
    //     });
    //     geocoder.getAddress(lnglatXY, function (status, result) {
    //         if (status === 'complete' && result.info === 'OK') {
    //             var address = result.regeocode.formattedAddress; //返回地址描述
    //             dataInfo['address'] = address;
    //             if (start) {
    //                 $("#reservation_address").val(address);
    //             } else {
    //                 $("#destination").val(address);
    //             }
    //         }
    //     });
    // },
    memberInfo: function (phone) {
        $.ajax({
            url: url.memberInfo,
            data: {'phone': phone},
            success: function (data) {
                if (data.status == 'OK') {
                    var member = data.data;
                    $("#real_name").val(member.nick_name);
                    $("#memberId").val(member.id);
                    if (member.instPhone == '') {
                        $("#inst_phone").val(member.instPhone);
                        $("#inst_phone").removeAttr("readonly");
                    } else {
                        $("#inst_phone").val(member.instPhone);
                        $("#inst_phone").attr("readonly", "readonly");
                    }

                    var memberInfoHtml = tpl.bind($('#member_info_tmp'),
                        {
                            totalOrderCount: member.totalOrderCount,
                            realTotalOrderCount: member.realTotalOrderCount,
                            totalOrderCountOfThisMonth: member.totalOrderCountOfThisMonth,
                            realTotalOrderCountOfThisMonth: member.realTotalOrderCountOfThisMonth
                        });
                    $("#show_member_info_pancel").show();
                    $("#show_member_info_pancel").html(memberInfoHtml);
                    // $("#member_level").html('<span class="label label-primary">' + dict.memberLevel(member.level) + '</span>');
                } else {
                    $("#show_member_info_pancel").hide();
                    $("#inst_phone").val("");
                    $("#inst_phone").removeAttr("readonly");
                }
            }
        });
    },
    loadList: function () {
        var columns = [
            {"data": "real_name"},
            {
                "data": "distance",
                'render': function (a) {
                    if(a == null){
                        return a;
                    }else {
                        return a.toFixed(2);
                    }
                }
                // "render": function (display, cell, row) {
                //     if (display == null) {
                //         return '0';
                //     } else {
                //         return '<span class="distance" data-startlng="'
                //             + $("#reservation_lng").val() + '" data-startlat="' + $("#reservation_lat").val()
                //             + '" data-endlng="' + row.longitude + '" data-endlat="' + row.latitude + '"></span>';
                //     }
                // }
            },
            {"data": "day_order_count"},
            {"data": "month_order_count"}
        ];
        table = dt.build($("#driverList"), url.driverList, columns, function () {
            // $('.distance').each(function (index, val) {
            //     var start = new AMap.LngLat($(val).data('startlng'), $(val).data('startlat'));
            //     var end = new AMap.LngLat($(val).data('endlng'), $(val).data('endlat'));
            //     calDistance.getDistance(start, end, val);
            // });
        });
    }
}
var tpl = {
    bind: function ($tpl, content) {
        if (content) {
            return laytpl($tpl.html()).render(content);
        } else {
            return laytpl($tpl.html()).render({});
        }
    }
};

$.extend($.fn.DataTable.defaults, {
    "pagingType": "full_numbers",
    "stateSave": true,
    "searching": false,
    "serverSide": true,
    "ordering": false,
    "dom": 'rt<"bottom"iflp<"clear">>',
    "language": {
        "sLoadingRecords": "正在加载数据...",
        "sEmptyTable": "暂无数据",
        "sZeroRecords": "暂无数据",
        "processing": "玩命加载中...",
        "lengthMenu": "显示 _MENU_ 项结果",
        "zeroRecords": "没有匹配结果",
        "info": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
        "infoEmpty": "显示第 0 至 0 项结果，共 0 项",
        "infoFiltered": "(由 _MAX_ 项结果过滤)",
        "infoPostFix": "",
        "url": "",
        "paginate": {
            "first": "首页",
            "previous": "上一页",
            "next": "下一页",
            "last": "末页"
        }
    },
    autoWidth: false
});
var dt = {
    build: function ($table, url, columns, initFunc, noCheck) {
        if (!noCheck) {
            var check = {
                "render": function (a, b, c) {
                    var html = '<input class="checkOther" name="" type="checkbox" value="' + c.id + '">';
                    return html;
                },
                "targets": 0
            };
            columns.unshift(check);
        }
        var table = $table.DataTable({
            "ajax": {
                'url': url,
                type: 'post',
                'data': function (d) {
                    d['company'] = $("#companyId").val();
                    d['orderId'] = $("#orderId").val();
                    // d['lat'] = $("#reservation_lat").val();
                    // d['lon'] = $("#reservation_lng").val();
                }
            },
            "columns": columns,
            "initComplete": function () {
                initFunc();
                //ICHECK
                $('.checkOther').iCheck({
                    checkboxClass: 'icheckbox_square-aero',
                    radioClass: 'iradio_square-aero',
                    increaseArea: '20%' // optional
                });
            }
        });
        $table.on('draw.dt', function () {
            initFunc();
            //ICHECK
            $('.checkOther').iCheck({
                checkboxClass: 'icheckbox_square-aero',
                radioClass: 'iradio_square-aero',
                increaseArea: '20%' // optional
            });
        });
        return table;
    }
};

var calDistance = {
    getDistance: function (start, end, val) {
        //构造路线导航类
        var driving = new AMap.Driving({
            map: driverMap
        });
        driving.search(new AMap.LngLat(116.379028, 39.865042), new AMap.LngLat(116.427281, 39.903719));
    }
}