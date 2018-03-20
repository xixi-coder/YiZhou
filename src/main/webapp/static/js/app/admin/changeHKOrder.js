/**
 * Created by admin on 2016/10/17.
 */
var map;
var currentCity;
var start = true;
var reservation_lng = $("#reservation_lng").val().replace(",",".");
var reservation_lat = $("#reservation_lat").val().replace(",",".");
var destination_lng = $("#destination_lng").val().replace(",",".");
var destination_lat = $("#destination_lat").val().replace(",",".");
var url = {
    memberInfo: g.ctx + '/admin/order/memberInfo',
    celAmount: g.ctx + '/admin/order/celAmount',
    driverList: g.ctx + '/admin/order/memberlist',
    create: g.ctx + '/admin/order/changeHKOrder',
    drivers: g.ctx + '/admin/member/driver/map/driversRealLocation?company=&status=3'
}
var driverMap = new AMap.Map('hidden', {
    //center: [127.000923, 36.675807],
    zoom: 10
});

var driving = new AMap.Driving({
    map: map
});
// 根据起终点经纬度规划驾车导航路线
driving.search(new AMap.LngLat(reservation_lng, reservation_lat), new AMap.LngLat(destination_lng, destination_lat));

map = new AMap.Map('admap', {
    //center: [118.000923, 36.675807],
    zoom: 10
});
var table;
var infoWindow = new AMap.InfoWindow({offset: new AMap.Pixel(25, 5)});
$(document).ready(function () {

    $(".label").on('click', function (target) {
        var current = $(target.currentTarget);
        var type = current.data("type");
        drvierMap.taggleClass(current, type);
    });

    $("#admap").css("height", window.screen.height - 170);
    drvierMap.loadlist();
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
        createOrder.celAmount();
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
    $('#companySelect').on('change', createOrder.celAmount);
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
    $("#changeOrder").on('click', function () {
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
            data: $("#changeOrder_form").serialize(),
            async: false,
            beforeSend: function () {
                $("#createOrder").prop("disabled", "true");
            },
            success: function (data) {
                if (data.status == 'OK') {
                    layer.msg(data.msg);
                    window.opener = null;
                    window.open(g.ctx+"/admin/order", '_self');
                    // window.close();
                } else {
                    layer.msg(data.msg);
                    $("#changeOrder").prop("disabled", "");
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
        //map.clearMap();
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
                    //map.clearMap();
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
    //         driving.search(new AMap.LngLat($("#reservation_lng").val(), $("#reservation_lat").val()),
    //             new AMap.LngLat($("#destination_lng").val(), $("#destination_lat").val()), function (status, result) {
    //                 var distance = parseFloat(result.routes[0].distance) / 1000;
    //                 var time = parseFloat(result.routes[0].time) / 60;
    //                 $("#time").val(time);
    //                 $("#distance").val(distance);
    //                 createOrder.celAmount();
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
    celAmount: function () {
        var distance = $("#distance").val(), time = $("#time").val();
        $.ajax({
            url: url.celAmount,
            data: {
                mid: $("#memberId").val(),
                setOutTime: $("#setoutime").val(),
                distance: distance,
                company: $("#companySelect").val(),
                time: time
            },
            success: function (data) {
                if (data.status = "OK") {
                    $("#yg_amount").val(data.data[0].totalAmount);
                    var distanceInfo = tpl.bind($("#order_distance"), {distance: distance, times: time});
                    $("#show_mill").html(distanceInfo);
                    $("#show_mill").show();
                }
            }
        });
    },
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
                    if (a == null) {
                        return a;
                    } else {
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
                    d['lat'] = $("#reservation_lat").val();
                    d['lon'] = $("#reservation_lng").val();
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
        var distance = 0;
        //构造路线导航类
        var driving = new AMap.Driving({
            map: driverMap
        });
        driving.search(start, end, function (status, result) {
            distance = parseFloat(result.routes[0].distance) / 1000;
            $(val).html(distance);
        });
    }
}


var drvierMap = {
    loadlist: function () {
        $.ajax({
            url: url.drivers,
            data: {company: '', status: 3},
            sync: true,
            success: function (data) {
                if(data.data.length>0){

                }


                var maps = [];
                //map = new AMap.Map('admap', {
                //    center: [data.data[0].longitude, data.data[0].latitude],
                //    zoom: 19
                //});
                map.center=[data.data[0].longitude, data.data[0].latitude];
                map.zoom=19;
                $(data.data).each(function (index, val) {

                    var marker = new AMap.Marker({
                        content: div(val.real_name),
                        position: [val.longitude, val.latitude],
                        offset: new AMap.Pixel(-12, -12),
                        map: map,
                        title: val.real_name
                    });
                    maps.push(marker);
                    ///////////
                    marker.on('mouseover', function (e) {
                        $.ajax({
                            url: g.ctx + '/admin/member/driver/driverInfo',
                            data: {id: val.id},
                            success: function (data) {
                                var driverInfo = data.data;
                                var img = driverInfo.driverLicenseInfo.photo;
                                if (img == null) {
                                    img = 'static/images/default-user.jpg';
                                }
                                var tmpHtml = tpl.bind($("#infoHTML"), {
                                    img: img,
                                    realName: driverInfo.real_name,
                                    phone: driverInfo.phone,
                                    countDay: driverInfo.dayCount,
                                    countMonth: driverInfo.monthCount,
                                    company: driverInfo.companyInfo.name,
                                    locationDate: driverInfo.locationDate
                                });
                                infoWindow.setContent(tmpHtml);
                                infoWindow.open(map, e.target.getPosition());
                            }
                        });
                    });
                    marker.on('mouseout', function (e) {
                        infoWindow.close();
                    });


                    //////////////






                });
            }

        });
    }
}

var div =function (count){
    var div = document.createElement('div');
    div.className = 'circle';
    div.style.backgroundColor ='#09f';
    div.innerHTML = count || 0;
    //div.style.css({
    // padding: "2px 10px",
    // borderRadius:"20px",
    // width: "80px",
    // textAlign: "center",
    // color: "#ffffff"
    //});
    return div;

}