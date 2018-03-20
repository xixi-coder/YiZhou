<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <meta name="description" content="学会分享，让我们得到快乐
学会分享，让我们得到尊重
学会分享，让我们得到财富
"/>
    <title>分享爱与快乐</title>
    <link rel="stylesheet" href="${ctx}/static/css/bootstrap.css"/>
    <link rel="stylesheet" href="${ctx}/static/www/css/sco.message.css"/>
    <link rel="stylesheet" href="${ctx}/static/www/css/shareqrcode.css"/>
    <style>

    </style>
</head>

<body>
<img id="wechatTip" src="${ctx}/static/www/images/wechat_tip.jpg" width="100%"
     style="z-index:-100;position:absolute;left:0;top:0">
<input type="hidden" id="type" name="type" value="${type}"/>
</body>
<script src="${ctx}/static/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script>
    var g = {ctx: '${ctx}'};
    $(document).ready(function () {
        var androidUrl = g.ctx + '/app/download/' + $("#type").val() + '-1';
        var iosUrl = g.ctx + '/app/download/' + $("#type").val() + '-2';
        zdxz(androidUrl, iosUrl);
    });



    function is_weixin() {
        var ua = navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == "micromessenger") {
            return true;
        } else {
            return false;
        }
    }

    function is_qq() {
        var ua = navigator.userAgent.toLowerCase();
        if (ua.match(/MQQBrowser/i) == "mqqbrowser") {
            return true;
        } else {
            return false;
        }
    }

    var browser = {
        versions: function () {
            var u = navigator.userAgent, app = navigator.appVersion;
            return {//移动终端浏览器版本信息
                trident: u.indexOf('Trident') > -1, //IE内核
                presto: u.indexOf('Presto') > -1, //opera内核
                webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
                iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
                iPad: u.indexOf('iPad') > -1, //是否iPad
                webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
            };
        }(),
        language: (navigator.browserLanguage || navigator.language).toLowerCase()
    }


    function zdxz(androidUrl, iosUrl) {
        debugger;
        if (is_weixin()) {
            $("#wechatTip").show();
        } else {
            $("#wechatTip").hide();
            if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
//                $.scojs_message("操作成功,准备开始下载客户端APP", $.scojs_message.TYPE_OK);
                window.location = iosUrl;
            }
            else if (browser.versions.android) {
//                $.scojs_message("操作成功,准备开始下载客户端APP", $.scojs_message.TYPE_OK);
                window.location = androidUrl;
            }
        }
    }

</script>
</html>