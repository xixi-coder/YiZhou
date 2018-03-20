<!DOCTYPE html>
<html lang="zh-CN">
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>注册</title>
    <link href="${ctx}/static/js/webuploader/bootstrap.css" rel="stylesheet">
    <link href="${ctx}/static/js/webuploader/register.css" rel="stylesheet">
    <!--[if IE]>
    <script src="http://libs.baidu.com/html5shiv/3.7/html5shiv.min.js"></script>
    <![endif]-->
</head>
<body>
<div id="beijing"
     style="background: -webkit-linear-gradient(left bottom, #1F79BF , #8EC9CF);padding: 20px 0;box-sizing: border-box">
    <div class="container">
        <form method="post" id="data_from">
            <input type="hidden" name="registerIp" id="registerIp" value="${registerIp}">
            <input type="hidden" name="registerIMSI" id="registerIMSI" value="${registerIMSI}">
            <input type="hidden" name="registerIMEI" id="registerIMEI" value="${registerIMEI}">
            <input type="hidden" name="macAddress" id="macAddress" value="${macAddress}">
            <input type="hidden" name="port" id="port" value="${port}">
            <div class="zaixian">
                <span>在线报名</span>
            </div>
            <div class="box">
                <label for="">姓名</label>
                <div class="box_right">
                    <input type="text" name="name" id="name" value=""/>
                </div>
            </div>

            <div class="box">
                <label for="">性别</label>
                <div class="box_right">
                    <input type="radio" name="sex" value="1"/>&nbsp;&nbsp;男
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="radio" name="sex" value="0"/>&nbsp;&nbsp;女
                </div>
            </div>

            <div class="box">
                <label for="">手机号码</label>
                <div class="box_right">
                    <input type="tel" name="phone" id="phone" value="" maxlength="11"/>
                </div>
            </div>

            <div class="box">
                <label for="">验证码</label>
                <div class="box_right">
                    <input type="text" value="" id="smsCode"/>
                    <input type="button" class="" id="sendSms" value="免费获取"/>
                </div>
            </div>

        <#--<div class="box">
            <label for="">推荐人号码</label>
            <div class="box_right">
                <input type="tel" name="tphone" id="tphone" value="" maxlength="11"/>
            </div>
        </div>-->

            <div class="box area" style="height:25px;">
                <label for="">区域选择</label>
                <div class="box_right" style="min-height: 120px">
                    <div id="distpicker2">
                        <select style="margin-top: 5px;width: 100%;border-radius: 5px;" id="province">
                            <option value="0">请选择</option>
                        <#list province as item>
                            <option value="${(item.adcode)!}">${(item.name)!}</option>
                        </#list>
                        </select>
                        <span id="tipProvince"></span>

                        <select style="margin-top: 10px;width: 100%;border-radius: 5px;" id="city">
                            <option value="0">请选择</option>
                        </select>
                        <span id="tipCity"></span>

                        <select style="margin-top: 10px;width: 100%;border-radius: 5px;" id="county">
                            <option value="0">请选择</option>
                        </select>
                        <span id="tipCounty"></span>
                    </div>
                </div>
            </div>
            <div class="box_che box" style="height:25px; clear: both">
                <label for="">选择类型</label>
            </div>
            <div class="box_che box" style="padding-left: 0%;clear: both">
            <#list type as item>
                <div style="display: inline-block;width: 32%;max-height:30px; ">
                    <input type="checkbox" style="margin-left:5px;" value="${(item.id)!}">&nbsp;${(item.name)!}
                </div>
            </#list>
            </div>

            <div class="box">
                <label for="">身份证照片</label>
                <div class="box_right">
                    <div id="picker" class="file_btn" style="color: #CCCCCC">选择文件</div>
                    <span id="idCard" class="glyphicon glyphicon-ok-sign"></span>
                    <input id="id_Card" type="hidden"/>
                </div>
            </div>

            <div class="box">
                <label for="">驾驶证照片</label>
                <div class="box_right">
                    <div id="picker1" class="file_btn" style="color: #CCCCCC">选择文件</div>
                    <span id="driverlicense" class="glyphicon glyphicon-ok-sign"></span>
                    <input id="driver_license" type="hidden"/>
                </div>

            </div>

            <div class="box">
                <label for="">行驶证照片</label>
                <div class="box_right">
                    <div id="picker2" class="file_btn" style="color: #CCCCCC">选择文件</div>
                    <span id="drivinglicense" class="glyphicon glyphicon-ok-sign"></span>
                    <input id="driving_license" type="hidden"/>
                </div>
            </div>

        <#--<div class="box">
            <label for="">从业资格证</label>
            <div class="box_right">
                <div id="picker3" class="file_btn" style="color: #CCCCCC">选择文件</div>
                <span id="qualification" class="glyphicon glyphicon-ok-sign"></span>
                <input id="qualification_certificate" type="hidden"/>
            </div>
        </div>-->

            <div class="box">
                <buttonn type="button" class="tijiao" id="tijiao">报名</buttonn>
                <input type="reset" value="重置" class="chongzhi"/>
            </div>

        </form>
    </div>
</div>
</body>
<script type="text/javascript">
    var g = {
        ctx: '${ctx}'
    }
</script>
<script src="${ctx}/static/js/jquery-1.11.3.js"></script>
<#--<script src="http://www.jq22.com/jquery/bootstrap-3.3.4.js" charset="UTF-8"></script>-->
<script src="${ctx}/static/js/webuploader/register.js" charset="UTF-8"></script>
<script src="${ctx}/static/js/layer/layer.js" type="text/javascript"></script>
<script src="${ctx}/static/js/webuploader/webuploader.js" charset="UTF-8"></script>
</html>