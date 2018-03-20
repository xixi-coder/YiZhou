/**
 * Created by admin on 2016/9/27.
 */
var url = {
    location: g.ctx + '/admin/order/location'
}
var map;
$(document).ready(function () {
    var markers = [];
    var marker, ruler, lineArr = [];
    map = new AMap.Map("adMap", {
        resizeEnable: true,
        center: [116.397428, 39.90923],
        zoom: 17
    });
    map.on("complete", completeEventHandler);
    map.plugin(["AMap.RangingTool"], function () {
        ruler = new AMap.RangingTool(map);
        AMap.event.addListener(ruler, "end", function (e) {
            ruler.turnOff();
        });
    });
    $('#ranging').on('click', function () {
        ruler.turnOn();
    });
    function completeEventHandler() {
        $.ajax(
            {
                url: url.location + '/' + orderId,
                success: function (data) {
                        console.log(data);
                        $(data).each(function (index, val) {
                            var logt = val['longitude'];
                            var lagt = val['latitude'];
                            var tmpMarker = new AMap.Marker({
                                position: [logt, lagt]
                            });
                            if (index == 0) {
                                tmpMarker.setMap(map);
                                tmpMarker.setOffset(new AMap.Pixel(-12, -22));
                                tmpMarker.setIcon(g.ctx + '/static/images/map/start.png');
                            }
                            if (index == $(data).length - 1) {
                                tmpMarker.setOffset(new AMap.Pixel(-10, -22));
                                tmpMarker.setMap(map);
                                tmpMarker.setIcon(g.ctx + '/static/images/map/end.png');
                            }
                            markers.push(tmpMarker);
                            lineArr.push([logt, lagt]);
                            var typeStr = "";
                            if (val.type == 1) {
                                typeStr = 'GPS定位 ';
                            } else if (val.type == 2) {
                                typeStr = '前次定位';
                            } else if (val.type == 4) {
                                typeStr = '缓存定位';
                            } else if (val.type == 5) {
                                typeStr = 'Wifi定位';
                            } else if (val.type == 6) {
                                typeStr = '基站定位';
                            } else if (val.type == 8) {
                                typeStr = '离线定位';
                            } else {
                                typeStr = '未知';
                            }
                            $('#contentBody').append(tpl.bind($('#tr'), {
                                recovice_time: val.rrecovice_time,
                                type: typeStr,
                                lat: val.latitude,
                                lon: val.longitude,
                                speed: val.speed
                            }));
                        });
                        marker = new AMap.Marker({
                            map: map,
                            position: lineArr[0],
                            icon: "http://webapi.amap.com/images/car.png",
                            offset: new AMap.Pixel(-26, -13),
                            autoRotation: true
                        });
                        // 绘制轨迹
                        var polyline = new AMap.Polyline({
                            map: map,
                            path: lineArr,
                            strokeColor: "red",  //线颜色
                            strokeOpacity: 1,     //线透明度
                            strokeWeight: 6,      //线宽
                            strokeStyle: "solid"  //线样式
                        });
                        map.setFitView(markers);
                        map.setZoom(14);
                }
            }
        );
    }

    $('#start').on('click', function () {
        map.setFitView(markers);
        // map.setZoom(17);
        marker.moveAlong(lineArr, 500);
    });
    $('#reset').on('click', function () {
        marker.stopMove();
        marker.setPosition(lineArr[0]);
        ruler.turnOff();
    });
});

var marker;
var infoWindow;
function addMarker(t) {
    var lnglat = [$(t).data('lon'), $(t).data('lat')];
    var speed = $(t).data("speed");
    var reciveTime = $(t).data('time');
    var type = $(t).data('type');
    infoWindow = new AMap.InfoWindow({offset: new AMap.Pixel(0, -30)});
    marker = new AMap.Marker({
        position: lnglat,
        map: map
    });
    map.setZo
    map.setFitView(marker);
    var html = tpl.bind($("#tipContent"), {
        lat: lnglat[1],
        lon: lnglat[0],
        speed: speed + 'KM/H',
        type: type,
        time: reciveTime
    });
    infoWindow.setContent(html);
    infoWindow.open(map, marker.getPosition());
}
function remaoveMarker(t) {
    infoWindow.close();
    map.remove(marker);
}