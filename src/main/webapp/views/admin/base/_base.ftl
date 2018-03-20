<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><@title/></title>
<#include "_head.ftl">
</head>
<body role="document" style="padding: 10px;overflow-x: hidden;background-color: #fafafa;">
<nav class="navbar  navbar-static-top"
     style="background-color: white;min-height: 35px; margin-bottom: 12px;height: 35px;margin-top: -12px;"
     role="navigation">
    <div class="container-fluid" style="padding: 0;">
        <div class="navbar-header">
        <#--<button type="button" id="" class="navbar-toggle collapsed " data-toggle="collapse"-->
                    <#--data-target="navbar">-->
                <#--<i class="glyphicon glyphicon-align-justify" style="color: black; font-size: 20px;"></i>-->
            <#--</button>-->
        </div>
        <div id="navbar1" class="navbar-collapse collapse">
        <#if hasPermission("show_sms_count")>
            <div class="navbar-text countSum label label-primary" style="margin-left: 0;line-height: 16px;"
                 data-url="${ctx}/admin/total/alarmCount"
                 data-filter="false">
                <span>报警信息：</span>
                <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                <span></span>
                <span id="Player"></span>
                <span>条</span>
            </div>
            <div class="navbar-text label label-primary"
                 style="margin-left: 0;line-height: 16px;">
                <span>短信剩余：${(_smsCount)!}条</span>
            </div>
            <div class="navbar-text label label-warning"
                 style="margin-left: 0;line-height: 16px;">
                <span>保险剩余：${(_insuranceAmount)!}元</span>
            </div>
        </#if>
            <div class="navbar-text countSum label label-primary" style="margin-left: 0;line-height: 16px;"
                 data-url="${ctx}/admin/total/todayOrderCount"
                 data-filter="false">
                <span>今日订单数：</span>
                <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                <span></span>
                <span>单</span>
            </div>
            <div class="navbar-text countSum label label-primary" style="margin-left: 0;line-height: 16px;"
                 data-url="${ctx}/admin/total/todayOrderAmount"
                 data-filter="false">
                <span>今日订单金额：</span>
                <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                <span></span>
                <span>元</span>
            </div>
        <#if hasPermission("dispatchOrder") || hasRole("super_admin")>
            <div id="zxorder" class="navbar-text countSum label label-primary" style="margin-left: 0;line-height: 16px;"
                 data-filter="false">
                <span>专线待派单：</span>
                <span id="number">0</span>
                <span id="Player"></span>
                <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                <span></span>
                <span>单</span>
            </div>
        </#if>
            <span class="navbar-right navbar-text label"
                  style="line-height: 16px; color: black; margin-right: 10px;font-weight: normal;">
               <a href="#">${(_USER_.username)}</a> 祝您工作愉快!&nbsp;&nbsp;&nbsp;
                <a class="updatePw" href="javascript:void(0);" data-id="${(_USER_.id)}">修改密码</a>&nbsp;&nbsp;&nbsp;
                <a href="${ctx}/admin/logout" data-id="${(_USER_.id)}">退出</a>
            </span>
        </div>
    </div>
</nav>
<nav class="navbar navbar-inverse navbar-static-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" id="navbar-toggle" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="navbar">
                <i class="glyphicon glyphicon-align-justify" style="color: white; font-size: 20px;"></i>
            </button>
            <a class="navbar-brand" style="color: white;" href="#">司机点点</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
            <#if (_RESOURCES_)??&&_RESOURCES_?size gt 0>
                <#list _RESOURCES_ as item>
                    <li class="dropdown">
                        <a <#if item._CHIRLD_?size gt 0>
                                class="dropdown-toggle" data-toggle="dropdown" href="#"
                        <#else>
                            <#if item.code=='fuwurenyuandiaodu'>
                                target="_blank"
                            </#if>
                                href="${ctx}/${(item.path)!}"
                        </#if>>
                        <#--<i class='${(item.ico)!}'></i>-->
                            <span style="color: white;">${(item.show_name)!}</span>
                            <#if item._CHIRLD_?size gt 0>
                                <span class="caret"></span>
                            </#if>

                        </a>
                        <ul class="dropdown-menu" role="menu">
                            <#list item._CHIRLD_ as citem>
                                <li id="${(citem.code)!}"><a href="${ctx}/${(citem.path)!}">
                                <#--<i class='${(citem.ico)!}'></i>-->
                                    <span>${(citem.show_name)!}</span></a></li>
                            </#list>
                        </ul>
                    </li>
                </#list>
            </#if>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</nav>
<div class="row" style="margin-top: 10px;">
    <!-- Top Bar End -->
    <!-- Start right content -->
    <audio src="${ctx}/static/ogg/horse.ogg" id="notice_sounds">
        Your browser does not support the audio element.
    </audio>
    <div class="panel-body">
    <@content/>
    </div>
    <!-- End right content -->
</div>
<script id="updatePw_tpl" type="text/html">
    <form id="password_form" action="" autocomplete="off">
        <input type="hidden" name="user.id" value="{{d.id}}"/>
        <input type="hidden" name="memberInfo.id" value="{{d.id}}"/>
        <input type="hidden" name="driverInfo.id" value="{{d.id}}"/>
        <div class="form-group">
            <label for="oldpassword" class="col-sm-4 control-label">旧密码:</label>
            <div class="col-sm-7">
                <input type="password" name="oldpassword" class="form-control n-invalid" id="oldpassword"
                       placeholder="旧密码" data-rule="required;password">
            </div>
            <div style="padding-top: 10px;color: red;">*</div>
        </div>
        <div class="form-group">
            <label for="password" class="col-sm-4 control-label">密码:</label>
            <div class="col-sm-7">
                <input type="password" name="password" class="form-control n-invalid" id="password"
                       placeholder="密码"
                       data-rule="required;password"
                >
            </div>
            <div style="padding-top: 10px;color: red;">*</div>
        </div>
        <div class="form-group">
            <label for="repassword" class="col-sm-4 control-label">确认密码:</label>
            <div class="col-sm-7">
                <input type="password" name="repassword" class="form-control n-invalid" id="repassword"
                       placeholder="确认密码"
                       data-msg-match="两次输入密码不一致!"
                       data-rule="required;match(password);">
            </div>
            <div style="padding-top: 10px;color: red;">*</div>
        </div>
    </form>
</script>
<#include "_javascript.ftl">
<@javascript/>
</body>
</html>