<#include "../../base/_base.ftl">
<#macro title>
系统日志
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/syslog">系统日志</a></li>
    <li class="active"><a>日志<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">系统日志设置</h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="syslog_form">
            <input type="hidden" name="syslog.id" value="${(syslog.id)!}">
            <input type="hidden" name="action" value="${action}">
            <table class="table table-bordered table-striped">
                <tr>
                    <td><label for="url" class="control-label">访问路径:</label></td>
                    <td>
                        <input type="text" name="syslog.url" value="${(syslog.url)!}" class="form-control n-invalid"
                               id="name" readonly>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>

                    <td><label for="params" class="control-label">参数:</label></td>
                    <td>
                        <textarea cols="30" name="syslog.params" class="form-control n-invalid" style="height:100px "
                                  readonly>${(syslog.params)!}</textarea>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>
                </tr>
                <tr>
                    <td><label for="url" class="control-label">操作人:</label></td>
                    <td>
                        <input type="text" readonly value="${(syslog.operater)!}" class="form-control n-invalid">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>

                    <td>
                        <label for="remark" class="">操作备注:</label>
                    </td>
                    <td>
                        <textarea cols="30" name="syslog.remark" readonly class="form-control n-invalid"
                                  style="height:100px "
                                  placeholder="如：超级管理员在公众号配置中进行，内容:[{}]。">${(syslog.remark)!}</textarea>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>

                </tr>

                <tr>
                    <td><label for="url" class="control-label">创建时间:</label></td>
                    <td>
                        <input type="text" readonly value="${(syslog.create_time)!}" class="form-control n-invalid">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>

                    <td><label for="url" class="control-label">IP地址:</label></td>
                    <td>
                        <input type="text" readonly value="${(syslog.ip_address)!}" class="form-control n-invalid">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>


                </tr>
            </table>

            </tr>

            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">返回</a>
                    </div>
                </div>
            </div>

        </form>
    </div>
</div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/syslog_item.js?_${.now?string("hhmmss")}"></script>
</#macro>