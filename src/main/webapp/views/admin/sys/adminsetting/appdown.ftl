<#include "../../base/_base.ftl">
<#macro title>
系统设置
</#macro>
<#macro content>
<div class="panel-body">
    <form class="form-horizontal" method="post" role="form" autocomplete="off"
          id="adminsetting_form">
        <input type="hidden" name="adminSetting.id" value="${adminSetting.id}">
        <table class="table table-bordered table-striped">

            <tr>
                <td><label for="dispatch_mill3" class="control-label">客户端二维码:</label></td>
                <td>
                    <img src="${(memberClient.qcode)!}" style="width: 300px;height: 300px;"/>
                </td>

                <td><label for="dispatch_time_out3" class="control-label">司机端二维码:</label></td>
                <td>
                    <img src="${(driverClient.qcode)!}" style="width: 300px;height: 300px;"/>
                </td>
            </tr>

            <tr>
                <td><label for="grab_single_mill3" class="control-label">客户端下载地址:</label></td>
                <td>
                    <textarea rows="5" style="width: 300px;">${(memberClient.file_path)!}</textarea>
                </td>
                <td><label for="grab_single_mill3" class="control-label">司机端下载地址:</label></td>
                <td>
                    <textarea rows="5" style="width: 300px;">${(driverClient.file_path)!}</textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/adminsetting.js?_${.now?string("hhmmss")}"></script>
</#macro>