<#include "../../base/_base.ftl">
<#macro title>
APP设置
</#macro>
<#macro content>
<div class="col-md-12">
    <ul class="nav nav-tabs navigation">
        <li class="li"><a href="${ctx}/admin/sys/adminsetting/1">系统设置</a></li>
        <#--<li class="li"><a href="${ctx}/admin/sys/adminsetting/2">第一阶段</a></li>-->
        <#--<li class="li"><a href="${ctx}/admin/sys/adminsetting/3">第二阶段</a></li>-->
        <#--<li class="li"><a href="${ctx}/admin/sys/adminsetting/4">第三阶段</a></li>-->
        <li class="li"><a href="${ctx}/admin/sys/appsetting">APP设置</a></li>
        <li class="li"><a href="${ctx}/admin/sys/adminsetting/6">APP下载</a></li>
    </ul>
</div>
<div class="panel-body">
    <form class="form-horizontal" method="post" role="form" autocomplete="off"
          id="appsetting_form">
        <input type="hidden" name="appSetting.id" value="${(appSetting.id)!}">
        <table class="table table-bordered table-striped">
            <tr>
                <td><label for="driver_appkey" class="control-label">司机端极光推送appkey:</label></td>
                <td>
                    <input type="text" name="appSetting.driver_appkey"
                           value="${(appSetting.driver_appkey)!}" class="form-control n-invalid"
                           id="driver_appkey"
                           placeholder="司机端极光推送appkey">
                </td>
                <td>
                    <div style="padding-top: 10px;color: red;">*</div>
                </td>

                <td><label for="driver_secret" class="control-label">司机端极光推送secret:</label></td>
                <td>
                    <input type="text" name="appSetting.driver_secret" value="${(appSetting.driver_secret)!}"
                           class="form-control n-invalid"
                           id="driver_secret"
                           placeholder="司机端极光推送secret ">
                </td>
                <td>
                    <div style="padding-top: 10px;color: red;">*</div>
                </td>
            </tr>

            <tr>
                <td><label for="auto_dispatch" class="control-label">客户端极光推送appkey:</label></td>
                <td>
                    <input type="text" name="appSetting.customer_appkey"
                           value="${(appSetting.customer_appkey)!}" class="form-control n-invalid"
                           id="customer_appkey"
                           placeholder="客户端极光推送appkey">
                </td>
                <td>
                    <div style="padding-top: 10px;color: red;">*</div>
                </td>

                <td><label for="customer_secret" class="control-label">客户端极光推送secret:</label></td>
                <td>
                    <input type="text" name="appSetting.customer_secret"
                           value="${(appSetting.customer_secret)!}" class="form-control n-invalid"
                           id="customer_secret"
                           placeholder="客户端极光推送secret">
                </td>
                <td>
                    <div style="padding-top: 10px;color: red;">*</div>
                </td>
            </tr>

            <tr>
                <td><label for="ali_app_id" class="control-label">支付宝账号appid:</label></td>
                <td>
                    <input type="text" name="appSetting.ali_app_id"
                           value="${(appSetting.ali_app_id)!}" class="form-control n-invalid"
                           id="ali_app_id"
                           placeholder="支付宝账号appid">
                </td>
                <td>
                    <div style="padding-top: 10px;color: red;">*</div>
                </td>

                <td><label for="ali_pid" class="control-label">支付宝账号pid:</label></td>
                <td>
                    <input type="text" name="appSetting.ali_pid"
                           value="${(appSetting.ali_pid)!}" class="form-control n-invalid"
                           id="ali_pid"
                           placeholder="支付宝账号pid">
                </td>
                <td>
                    <div style="padding-top: 10px;color: red;">*</div>
                </td>
            </tr>


            <tr>
                <td><label for="ali_target_id" class="control-label">支付宝账号target_id:</label></td>
                <td>
                    <input type="text" name="appSetting.ali_target_id" value="${(appSetting.ali_target_id)!}"
                           class="form-control n-invalid"
                           id="ali_target_id"
                           placeholder="支付宝账号target_id">
                </td>
                <td>
                    <div style="padding-top: 10px;color: red;">*</div>
                </td>

                <td><label for="ali_notify_url" class="control-label">支付宝账号notify_url:</label></td>
                <td>
                    <input type="text" name="appSetting.ali_notify_url" value="${(appSetting.ali_notify_url)!}"
                           class="form-control n-invalid"
                           id="ali_notify_url"
                           placeholder="支付宝账号notify_ur">
                </td>
                <td>
                    <div style="padding-top: 10px;color: red;">*</div>
                </td>
            </tr>

            <tr>
                <td><label for="ali_rsa_private" class="control-label">支付宝账号rsa_private:</label></td>
                <td>
                    <input type="text" name="appSetting.ali_rsa_private" value="${(appSetting.ali_rsa_private)!}"
                           class="form-control n-invalid"
                           id="ali_rsa_private"
                           placeholder="支付宝账号rsa_private">
                </td>
                <td>
                    <div style="padding-top: 10px;color: red;">*</div>
                </td>
            </tr>


        </table>
        </tr>

        <div class="row" style="margin-top:20px;">
            <div class="col-sm-12">
                <div class="col-sm-6" style="float: right;">
                    <button class="btn btn-success" type="submit">提交</button>
                </div>
            </div>
        </div>

    </form>
</div>
</#macro>
<#macro javascript>
<script>
    var index = ${index!1}
            $($(".li")[index]).addClass("active");
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/appsetting.js?_${.now?string("hhmmss")}"></script>
</#macro>