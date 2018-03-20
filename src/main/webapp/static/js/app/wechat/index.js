/**
 * Created by admin on 2016/10/26.
 */
$(document).ready(function () {
    //地图加载
    var map, geolocation, map2;
    map = new AMap.Map("container", {
        resizeEnable: true
    });
    map.plugin('AMap.Geolocation', function () {
        geolocation = new AMap.Geolocation({
            enableHighAccuracy: true,//是否使用高精度定位，默认:true
            timeout: 10000,          //超过10秒后停止定位，默认：无穷大
            buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
            zoomToAccuracy: true,      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
            buttonPosition: 'RB'
        });
        map.addControl(geolocation);
        geolocation.getCurrentPosition();
        AMap.event.addListener(geolocation, 'complete', slide);//加载图片
        AMap.event.addListener(geolocation, 'complete', onComplete);//返回定位信息
    });


    var loadGeolocation = '';

    var geolocationEvent = function () {
        console.log(1);
        var map = new AMap.Map("container", {
            resizeEnable: true
        });
        var geolocation = new AMap.Geolocation({
            enableHighAccuracy: true,//是否使用高精度定位，默认:true
            timeout: 100,          //超过10秒后停止定位，默认：无穷大
            buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
            zoomToAccuracy: false,      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
            buttonPosition: 'RB',
            extensions: "base"    //返回周边路口等信息
        });
        geolocation.getCurrentPosition();
        AMap.event.addListener(geolocation, 'complete', onComplete);//返回定位信息
    };
    loadGeolocation = setInterval(geolocationEvent, 120000);


    //输入提示
    var autoOptions2 = {
        input: "end"
    };
    var auto2 = new AMap.Autocomplete(autoOptions2);
    AMap.event.addListener(auto2, "select", select1);//注册监听，当选中某条记录时会触发
    function select1(e) {
        console.log(e);
        $("#end_lat").val(e.poi.location.lat);
        $("#end_lon").val(e.poi.location.lng);
    }

    oldUser();
});

/*判断是否第一次登陆*/
function oldUser() {
    if (localStorage.phone == undefined) { //第一次输入手机号
        sessionStorage.old = 0;
        $("#old").val(sessionStorage.old);
        $("#yzm_a").bind("click", yzm_a);
    } else {                              //默认上次的手机号
        sessionStorage.old = 1;
        $("#old").val(sessionStorage.old);
        $("#phone").val(localStorage.phone);
        layer.msg("您是老用户，无需验证码");
    }
}

$("#phone").change(function () {        //本地有号  但又从新输入一个新号
    if ($("#phone").val() != localStorage.phone) {
        $("#yuyue").html("召唤代驾");
        sessionStorage.old = 0;
        $("#old").val(sessionStorage.old);
        $("#yzm_a").unbind("click");
        $("#yzm_a").bind("click", yzm_a);
    }
})

/*加载图片*/
function slide(data) {
    var geocoder = new AMap.Geocoder({
        radius: 1000,
        extensions: "all"
    });
    var lnglatXY = [data.position.lng, data.position.lat];
    geocoder.getAddress(lnglatXY, function (status, result) {
        if (status === 'complete' && result.info === 'OK') {
            $.ajax({
                url: g.ctx + '/wechat/ads',
                data: {
                    city_code: result.regeocode.addressComponent.adcode
                },
                success: function (tdata) {
                    if (tdata.data != "" || tdata.data.length > 0) {
                        var html = " <div class='item active'><a href='" + tdata.data[0].url + "'><img src='" + tdata.data[0].pic + "' alt=''/></a></div>";
                        var circle = "<li data-slide-to='0' data-target='#div1'></li>";
                        for (var i = 1; i < tdata.data.length; i++) {
                            html += " <div class='item'><a href='" + tdata.data[i].url + "'><img src='" + tdata.data[i].pic + "' alt=''/></a></div>";
                            circle += "<li data-slide-to='" + i + "' data-target='#div1'></li>";
                        }
                        $(".carousel-inner").html(html);
                        $(".carousel-indicators").html(circle);
                        $(".carousel").carousel();
                    }
                }
            });
        }
    });
}

/*获取代驾服务协议*/
$("#provision").click(function () {
    document.getElementById("provision").href = g.ctx + "/app/provision?appType=2&provisionType=2"
});

/*加载服务价格*/
$("#calrule").click(function () {
    document.getElementById("calrule").href = g.ctx + "/www/calrule?serviceType=2&cityCode=" + $("#city_code").val();
});


/*加载信息*/
function onComplete(data) {
    var geocoder = new AMap.Geocoder({
        radius: 1000,
        extensions: "all"
    });
    var lnglatXY = [data.position.lng, data.position.lat];
    localStorage.lng = data.position.lng;
    localStorage.lat = data.position.lat;
    geocoder.getAddress(lnglatXY, function (status, result) {
        console.log(result);
        if (status === 'complete' && result.info === 'OK') {
            $.ajax({
                url: g.ctx + '/wechat/nearby',
                data: {
                    lat: data.position.lat,
                    lon: data.position.lng,
                    city_code: result.regeocode.addressComponent.adcode
                },
                success: function (tdata) {
                    $("#driverCount").html(tdata.data.c);
                    $("#distance").html(tdata.data.distance);
                }
            });
            $("#city_code").val(result.regeocode.addressComponent.adcode);
            $("#start").val(result.regeocode.pois[0].name);
            $("#start_lat").val(result.regeocode.pois[0].location.lat);
            $("#start_lon").val(result.regeocode.pois[0].location.lng);
            // 取消loading
            var loadingMask = document.getElementById('loadingDiv');
            loadingMask.parentNode.removeChild(loadingMask);
            //输入提示
            var autoOptions1 = {
                input: "start",
                city: result.regeocode.addressComponent.adcode,
                citylimit: true
            };
            var auto1 = new AMap.Autocomplete(autoOptions1);

            AMap.event.addListener(auto1, "select", select);//注册监听，当选中某条记录时会触发
            function select(e) {
                console.log(e, 1);
                $("#start_lat").val(e.poi.location.lat);
                $("#start_lon").val(e.poi.location.lng);

                $.ajax({
                    url: g.ctx + '/wechat/nearby',
                    data: {
                        lat: e.poi.location.lat,
                        lon: e.poi.location.lng,
                        city_code: $("#city_code").val()
                    },
                    success: function (tdata) {
                        console.log(tdata);
                        $("#driverCount").html(tdata.data.c);
                        $("#distance").html(tdata.data.distance);
                    }
                });
                $.ajax({
                    url: g.ctx + '/wechat/ads',
                    data: {
                        city_code: $("#city_code").val()
                    },
                    success: function (tdata) {
                        var html = " <div class='item active'><a href='" + tdata.data[0].url + "'><img src='" + tdata.data[0].pic + "' alt=''/></a></div>";
                        var circle = "<li data-slide-to='0' data-target='#div1'></li>";
                        for (var i = 1; i < tdata.data.length; i++) {
                            html += " <div class='item'><a href='" + tdata.data[i].url + "'><img src='" + tdata.data[i].pic + "' alt=''/></a></div>";
                            circle += "<li data-slide-to='" + i + "' data-target='#div1'></li>";
                        }
                        $(".carousel-inner").html(html);
                        $(".carousel-indicators").html(circle);
                        $(".carousel").carousel();
                    }
                });
            }
        }
    });

}


/*手机验证码*/
function yzm_a() {
    var val = document.getElementById('phone').value;
    var reg = /^1[345678]\d{9}$/;
    if (reg.test(val)) {
        $.ajax({
            url: g.ctx + '/wechat/smsCode',
            data: {
                phone: $("#phone").val()
            },
            success: function (data) {
                if (data.status == "OK") {
                    var clock = '';
                    var nums = 60;
                    $("#yzm_a").attr("disabled", "disabled");
                    $("#yzm_a").val(nums + '秒后可重新获取');
                    clock = setInterval(doLoop, 1000);

                    function doLoop() {
                        nums--;
                        if (nums > 0) {
                            $("#yzm_a").val(nums + '秒后重新获取');
                        } else {
                            clearInterval(clock);
                            $("#yzm_a").attr("disabled", false);
                            $("#yzm_a").val('点击发送验证码');
                            nums = 60;
                        }
                    }
                } else {
                    $("#yzm_a").val("获取失败").css("color", "red");
                }
            }
        });
    } else {
        layer.msg("请输入手机号码");
    }
};

/*点击下单*/
$("#yuyue").on('click', function () {
    $.ajax({
        url: g.ctx + '/wechat/create',
        data: $("#data_from").serialize(),
        beforeSend: function () {
            $("#yuyue").attr("disabled", "disabled");
        },
        success: function (data) {
            if (data.status == "OK") {
                console.log(data);
                localStorage.phone = $("#phone").val();//将号码保存到本地
                localStorage.orderId = data.data;
                $("#yuyue").val("下单成功，请耐心等待哟！");
                $("#yuyue").attr("disabled", "disabled");
                layer.msg(data.msg);
            } else {
                $("#yuyue").removeAttr("disabled", "disabled");
                layer.msg(data.msg);
            }
        }
    });
});


/*点击取消*/
$("#quxiao").on('click', function () {
    $.ajax({
        url: g.ctx + '/wechat/undo',
        data: {orderId: localStorage.orderId},
        success: function (data) {
            if (data.status == "OK") {
                $("#quxiao").val("取消成功！");
                $("#yuyue").removeAttr("disabled", "disabled");
                layer.msg(data.msg);
            } else {
                layer.msg(data.msg);
            }
        }
    });
});


/*获取行程*/
$("#hq").on('click', function () {
    $.ajax({
        url: g.ctx + '/wechat/lockup',
        data: {orderId: localStorage.orderId},
        success: function (data) {
            console.log(data.data);
            if (data.status == "OK") {
                $("#div2").fadeIn();
                var arrayLog = data.data;
                var qx = " 您取消了行程！";
                var circle = '';
                for (var i = 0; i < arrayLog.length; i++) {
                    var str_html = "";
                    if (arrayLog[i].action == 2) {
                        /*get_distance(localStorage.lng, localStorage.lat, arrayLog[i].lon, arrayLog[i].lat)*/

                        str_html = " <li class='li2'>" +
                            "<div class='stroke-ul-li-div1'></div>" +
                            "<div class='stroke-ul-li-div2 top10'></div>" +
                            "<div class='stroke-ul-li '>" +
                            " <div >司机：" + arrayLog[i].driverName + "</div>" +
                            " <div id='SJdistance'>距离：<samp></samp>/米</div>" +
                            " <div >电话：<a href='tel:" + arrayLog[i].phone + "'>" + arrayLog[i].phone + "</a></div></div></li>"
                        get_distance(localStorage.lng, localStorage.lat, arrayLog[i].lon, arrayLog[i].lat)
                    }
                    if (arrayLog[i].action == 6) {
                        qx = " 您取消了行程！";
                    } else if (arrayLog[i].action == 1) {
                        qx = " 创建订单成功！";
                    } else if (arrayLog[i].action == 2) {
                        qx = " 司机已接单！";
                    } else if (arrayLog[i].action == 3) {
                        qx = " 司机开始行程！";
                    } else if (arrayLog[i].action == 8) {
                        qx = " 司机到达终点附近！";
                    } else if (arrayLog[i].action == 5) {
                        qx = " 订单已完成！";
                    } else {
                        qx = arrayLog[i].remark;
                    }

                    circle += "<li class='stroke-ul-li li_" + arrayLog[i].action + "'>  " +
                        " <div class='stroke-ul-li-div1'>" + arrayLog[i].time + "</div>   " +
                        " <div class='stroke-ul-li-div2 top" + arrayLog[i].action + "'></div>" +
                        " <div class='stroke-ul-li-div3'>" + qx + "</div>" +
                        " </li>";
                    circle += str_html;
                }
                $(".stroke-ul").html(circle);
                $(".stroke").carousel();
            } else {
                layer.msg("获取行程失败！！");
            }
        }
    });
});


/*清空地址按钮*/
$(".closed").on("click", function () {
    $("#start").val("");
});


/* 司机距离 */
function get_distance(lng, lat, lng1, lat1) {
    //基本地图加载
    var map = new AMap.Map("container", {
        resizeEnable: true,
        center: [116.397428, 39.90923],//地图中心点
        zoom: 13 //地图显示的缩放级别
    });
    //构造路线导航类
    var driving = new AMap.Driving({
        map: map,
        panel: "panel"
    });
    // 根据起终点经纬度规划驾车导航路线
    driving.search(new AMap.LngLat(lng, lat), new AMap.LngLat(lng1, lat1), function (status, result) {
        console.log(result.routes[0].distance);
        $("#SJdistance samp").html(result.routes[0].distance);
    });
}