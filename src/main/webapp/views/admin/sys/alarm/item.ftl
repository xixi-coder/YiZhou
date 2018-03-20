<#include "../../base/_base.ftl">
<#macro title>
报警回复
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li class="active"><a>报警回复</a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">报警回复</h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="alarm_form">
            <input type="hidden" name="alarm.id" value="${(alarm.id)!}" id="id"/>
            <table class="table table-bordered table-striped">
            <#--报警人&处理状态-->
                <tr>
                    <td><label for="alarm_name" class="control-label">报警人:</label></td>
                    <td>
                        <input type="text" name="alarm.alarm_name" value="${(alarm.alarm_name)!}"
                               class="form-control n-invalid" readonly/>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="status" class="control-label">处理状态:</label></td>
                    <td>
                        <select class="select2" name="alarm.status" style="width: 100%;">
                            <option value=0
                                    <#if alarm.status??&&alarm.status==0>selected="selected"</#if>>未处理
                            </option>
                            <option value=1
                                    <#if alarm.status??&&alarm.status==1>selected="selected"</#if>>处理中
                            </option>
                            <option value=2
                                    <#if alarm.status??&&alarm.status==2>selected="selected"</#if>>已处理
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
            <#--报警信息&报警回复-->
                <tr>
                    <td><label for="alarm_message" class="control-label">报警信息:</label></td>
                    <td>
                        <textarea cols="30" name="alarm.alarm_message" class="form-control n-invalid"
                                  style="height:100px "
                                  readonly>${(alarm.alarm_message)!}</textarea>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>
                    <td><label for="dispose_message" class="control-label">报警回复:</label></td>
                    <td>
                        <textarea cols="30" name="alarm.dispose_message" class="form-control n-invalid"
                                  style="height:100px "
                        >${(alarm.dispose_message)!}</textarea>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>
                </tr>

                <tr>
                    <td colspan="6" style="text-align: center;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                        <button class="btn btn-success" type="submit">提交</button>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/alarm_item.js?_${.now?string("hhmmss")}"></script>
</#macro>