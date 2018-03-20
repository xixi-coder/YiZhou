<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" id="viewport"
          content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no"/>
    <title>登陆系统</title>
    <style type="text/css">
    </style>
    <link href="${ctx}/static/css/bootstrap.css" rel="stylesheet"/>
    <link href="${ctx}/static/login/animate.min.css" rel="stylesheet"/>
    <link href="${ctx}/static/login/component.css" rel="stylesheet"/>
    <link href="${ctx}/static/login/style.css" rel="stylesheet"/>
    <style type="text/css" rel="stylesheet">
        body,button, input, select, textarea,h1 ,h2, h3, h4, h5, h6 { font-family: Microsoft YaHei,'宋体' , Tahoma, Helvetica, Arial, "\5b8b\4f53", sans-serif;}
    </style>
</head>
<body class="fixed-left login-page">
<img style="width: 100%; position: absolute;z-index: -1;" class="blur" src="${ctx}/static/img/pexels-photo-225607.jpeg"
     id="BgImg"/>
<div class="container">
    <div class="full-content-center">
        <div class="login-wrap animated flipInX">
            <div class="login-block">
                <img style="min-width: 100px;min-height: 100px;max-width: 100px;max-height: 100px;"
                     src="${ctx}/static/images/default-user.jpg" id="portrait" class="img-circle not-logged-avatar">
                <form id="loginForm" role="form" method="post">
                    <div class="form-group login-input">
                        <i class="fa fa-user overlay"></i>
                        <input type="text" name="username" data-id="1" style="color: #0b0b0b;" id="username"
                               class="form-control text-input"
                               placeholder="用户名">
                    </div>
                    <div class="form-group login-input">
                        <i class="fa fa-key overlay"></i>
                        <input type="password" style="color: #0b0b0b;" name="password" id="password"
                               class="form-control text-input"
                               placeholder="密码">
                    </div>
                    <div class="row">
                        <div class="col-sm-4 col-xs-4">
                            <img id="captcha" onclick="this.src='${ctx}/admin/captcha?'+Math.random()" src="${ctx}/admin/captcha"
                                 style="width: 100%;cursor:pointer;height: 34px;"/>
                        </div>
                        <div class="col-sm-8 col-xs-8">
                            <input type="text" style="color: #0b0b0b;" name="captcha" id="captcha"
                                   class="form-control text-input"
                                   placeholder="验证码">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <button id="login" type="submit" class="btn btn-success btn-block">登陆</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- the overlay modal element -->
</body>
<script src="${ctx}/static/js/jquery-1.9.1.min.js"></script>
<script src="${ctx}/static/js/jquery.form.js"></script>
<script src="${ctx}/static/js/layer/layer.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.cookie.js"></script>
<script src="${ctx}/static/js/StackBlur.js"></script>
<script type="text/javascript">
    var g = {ctx: '${ctx}'};
</script>
<script type="text/javascript" src="${ctx}/static/js/app/login.js?_${.now?string("yyyymmddhhmmss")!}"></script>

</html>