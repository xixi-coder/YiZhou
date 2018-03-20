<!doctype html>
<html xmlns="http://www.w3.org/1999/html">
<head lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>微信代驾</title>
    <link href="${ctx}/static/css/bootstrap.css" rel="stylesheet"/>
    <link href="${ctx}/static/wechat/1.css" rel="stylesheet"/>

    <script>
        var _PageHeight = document.documentElement.clientHeight,
                _PageWidth = document.documentElement.clientWidth;
        var _LoadingTop = _PageHeight > 61 ? (_PageHeight - 61) / 2 : 0,
                _LoadingLeft = _PageWidth > 215 ? (_PageWidth - 215) / 2 : 0;
        var _LoadingHtml = '<div id="loadingDiv" style="position:absolute;left:0;width:100%;height:' + _PageHeight + 'px;top:0;background:#f3f8ff;opacity:0.8;filter:alpha(opacity=80);z-index:10000;"><div style="position: absolute; cursor1: wait; left: ' + _LoadingLeft + 'px; top:' + _LoadingTop + 'px; width: auto; height: 57px; line-height: 57px; padding-left: 50px; padding-right: 5px; background: #fff; color: #696969; font-family:\'Microsoft YaHei\';">页面加载中，请等待...</div></div>';
        document.write(_LoadingHtml);
        document.onreadystatechange = completeLoading;

        function completeLoading() {
            if (document.readyState == "complete") {

            }
        }
    </script>

</head>
<body>

<div id="main">
    <div class="top_one">
        <h3 align="center">您附近有<span id="driverCount">0</span>位司机准备接驾</h3>
        <p align="center">距您最近<span id="distance">0</span>m</p>
    </div>
    <form method="get" id="data_from">
        <input type="hidden" value="" id="city_code" name="city_code"/>
        <input type="hidden" value="" id="start_lon" name="trip.start_longitude"/>
        <input type="hidden" value="" id="start_lat" name="trip.start_latitude"/>
        <input type="hidden" value="" id="end_lon" name="trip.end_longitude"/>
        <input type="hidden" value="" id="end_lat" name="trip.end_latitude"/>
        <input type="hidden" value="" id="old" name="old"/><#--判断是否第一次上线-->
        <div class="top_position top_two">
            <span class="glyphicon glyphicon-map-marker" align="center"></span>
            <input type="text" id="start" value="" name="order.reservation_address" placeholder="出发点"/>
            <div class="closed">
                <span class="glyphicon glyphicon-remove-sign"></span>
            </div>
        </div>
        <div class="top_tel top_two">
            <span class="glyphicon glyphicon-earphone" align="center"></span>
            <input type="number" value="" placeholder="请输入您的手机号" name="phone" id="phone" maxlength="11"/>
            <div class="huoqu">
                <input type="button" id="yzm_a" value="获取验证码" />
            </div>
        </div>
        <div class="top_yzm top_two" id="top_yzm">
            <span class="glyphicon glyphicon-edit" align="center"></span>
            <input id="security" name="security" type="text" value="" placeholder="请输入验证码"/>
        </div>
        <div class="top_btn">
            <button type="button" id="yuyue" class="btn btn-success btn-sm" style="width: 100%;margin-bottom: 10px">
                呼叫代驾
            </button>
            <button type="button" id="quxiao" class="btn btn-danger btn-sm" style="width: 49%">取消订单</button>
            <button type="button" id="hq" class="btn btn-danger btn-sm" style="width: 49%">获取行程信息</button>
        </div>
        <div class="top_xy">
            <div class="top_xy_left">
                <a id="provision">
                    <span class="glyphicon glyphicon-ok-circle bbb"></span>
                    <span class="aaa">已阅读并同意</span>
                    《代驾服务协议》
                </a>
            </div>
            <div class="top_xy_right">
                <a id="calrule">
                    <span>服务价格</span>
                </a>
            </div>
        </div>

        <div class="clear"></div>
    </form>

</div>

<div class="container">
<#-- 广告模块  -->
<#-- <div id="div1" class="carousel slide" data-ride="carousel" data-interval="2000">
     <div class="carousel-inner">
     </div>
     <ul class="carousel-indicators"></ul>
 </div>-->
</div>
<div id="container" style="display: none"></div>
<div id="panel" style="display: none"></div>

<div>
    <div id="div2" class="stroke slide" data-ride="stroke" data-interval="2000" style="display: none">
        <ul class="stroke-ul  list-unstyled">
        <#-- <li class="stroke-ul-li">
             <div class="stroke-ul-li-div1">11:00</div>
             <div class="stroke-ul-li-div2">●</div>
             <div class="stroke-ul-li-div3">dasdadadsada</div>
         </li>
         <li class="stroke-ul-li">
             <div class="stroke-ul-li-div1">22:00</div>
             <div class="stroke-ul-li-div2">●</div>
             <div class="stroke-ul-li-div3">2</div>
         </li>
         <li class="stroke-ul-li">
             <div class="stroke-ul-li-div1">33:00</div>
             <div class="stroke-ul-li-div2">●</div>
             <div class="stroke-ul-li-div3">3</div>
         </li>-->
            <li class="stroke-ul-li"></li>
        </ul>
    </div>
</div>


</body>
<script type="text/javascript">
    var g = {
        ctx: '${ctx}'
    }
</script>
<script type="text/javascript" src="${ctx}/static/js/jquery-1.11.3.js"></script>
<script type="text/javascript"
        src="http://webapi.amap.com/maps?v=1.3&key=3847e08227b5479aa1596a7a8b944026&plugin=AMap.Driving,AMap.Autocomplete,AMap.PlaceSearch,AMap.Geocoder"></script>
<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script>
<script type="text/javascript" src="${ctx}/static/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctx}/static/js/app/wechat/index.js?${.now?string("mmss")}"></script>
</html>