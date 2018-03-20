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
        #test {
            width: 100%;
            height: 100%;
            background-color: #000;
            position: absolute;
            top: 0;
            left: 0;
            z-index: 2;
            opacity: 0.3;
            /*兼容IE8及以下版本浏览器*/
            filter: alpha(opacity=30);
            display: block;
        }

        #log_window {
            width: 60px;
            height: 60px;
            margin: auto;
            position: absolute;
            z-index: 3;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            display: block;
        }
    </style>
</head>
<body>
<img src="${ctx}/static/www/images/bg01.png" width="100%" height="100%"
     style="z-index:-100;position:absolute;left:0;top:0">
<div class="top"></div>
<div class="nav">
    <form id="submit_form" action="" method="post">
        <input type="hidden" id="tphone" name="tphone" value="${phone}"/>
        <input type="hidden" id="type" name="type" value="${type}"/>
        <div class="input-group">
            <input id="phone" name="phone" type="number" class="form-control input-lg" placeholder="请填写您的手机号码"
                   aria-describedby="basic-addon2">
            <span class="input-group-addon" style="background-color: #F69D10;color: white;" id="getCode"
                  disable="false">获取验证码</span>
        </div>
        <div class="input-group">
            <input id="code" name="code" type="number" class="form-control input-lg" placeholder="请填写验证码"
                   aria-describedby="basic-addon2">
            <span class="input-group-addon" style="background-color: #31BF2F;color: white;" id="submit_button">确认</span>
        </div>
    </form>
</div>

<div id="test"></div>
<div id="log_window"><img src="${ctx}/static/www/images/loading.gif"/></div>
</body>
<script src="${ctx}/static/js/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctx}/static/js/bootstrap.js" type="text/javascript"></script>
<script src="${ctx}/static/www/js/sco.message.js" type="text/javascript"></script>
<script>
    var g = {ctx: '${ctx}'};
    $(document).ready(function () {
        $("#test").hide();
        $("#log_window").hide();
        //表单提交
        $("#submit_button").click(function () {
            var phone = $("#phone").val();
            var isphone = /^[0-9]{1,15}$/;
            if (!isphone.test(phone)) {
                $.scojs_message("请填写正确的手机号码！", $.scojs_message.TYPE_ERROR);
                return;
            }
            var code = $("#code").val();
            if (code == "" || code.length != 6) {
                $.scojs_message("请填写正确的验证码！", $.scojs_message.TYPE_ERROR);
                return;
            }
            var params = {phone: phone, code: code, tphone: $("#tphone").val(), type: $("#type").val()}
            $("#submit_button").removeAttr("id");
            $.post(g.ctx + '/driver/enroll/register', params, function (data) {
                if (data.status == 'OK') {
                    $("#phone").val("");
                    $("#code").val("");
                    var androidUrl = g.ctx + '/app/download/' + $("#type").val() + '-1';
                    var iosUrl = g.ctx + '/app/download/' + $("#type").val() + '-2';
                    zdxz(androidUrl, iosUrl);
                } else {
                    $("#submit_button").attr("disabled", false);
                    $.scojs_message(data.msg, $.scojs_message.TYPE_ERROR);
                }
            });
        });
        $("#getCode").click(function () {
            if ($("#getCode").attr("disable") == "false") {
                var phone = $("#phone").val();
                if (phone == "" || phone.length != 11) {
                    $.scojs_message("请填写正确的手机号码！", $.scojs_message.TYPE_ERROR);
                    return;
                }
                $("#getCode").attr("disable", true);
                $.ajax({
                    url: g.ctx + '/api/smscode',
                    data: {phone: phone, type: 1},
                    headers: {
                        'appType': $("#type").val(),
                        'devicetype': 3
                    },
                    success: function (result) {
                        if (result.status == 'OK') {
                            $.scojs_message("验证码已发送成功", $.scojs_message.TYPE_OK);
                            smsOverTime(120);
                        } else {
                            $.scojs_message(result.msg, $.scojs_message.TYPE_ERROR);
                            $("#getCode").attr("disable", false);
                        }
                    }
                });
            }
        });

        $("#phone").focus(function () {
            $(".nav").css("bottom", "10%");
        }).blur(function () {
            $(".nav").css("bottom", "20px");
        });

        $("#codeNumber").focus(function () {
            $(".nav").css("bottom", "10%");
        }).blur(function () {
            $(".nav").css("bottom", "20px");
        });

    });

    function smsOverTime(t) {
        var howlong = t;

        howlong--;
        $("#getCode").html(howlong);

        var timeID = setTimeout("smsOverTime(" + howlong + ")", 1000);
        if (howlong <= 0) {
            clearTimeout(timeID);
            $("#getCode").html("获取验证码");
            $("#getCode").attr("disable", false);
        }

    }

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
            window.location = g.ctx + '/share/wechat?type=' + $("#type").val();
        } else {
            if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
                window.location = iosUrl;
            } else if (browser.versions.android) {
                window.location = androidUrl;
            } else {
                $.scojs_message("请使用手机端打开", $.scojs_message.TYPE_OK);
            }
        }
    }
</script>
</html>