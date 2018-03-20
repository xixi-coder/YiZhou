<#include "../../base/_base.ftl">
<#macro title>
条款信息<#if action==1>编辑<#else>新增</#if>
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/provision">条款信息</a></li>
    <li class="active"><a>条款<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">条款信息</h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="provision_form">
            <input type="hidden" name="provision.id" value="${(provision.id)!}" id="id"/>
            <input type="hidden" name="action" value="${action}"/>
            <table class="table table-bordered table-striped">
                <tr>
                    <td><label for="name" class="control-label">条款内容:</label></td>
                    <td>
                        <input type="hidden" id="descInput" name="provision.content">
                        <textarea id="desc" class="form-control" id="desc"
                                  style="height: 140px; width: 100%; resize: none;">${(provision.content)!}</textarea>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="client_type" class="control-label">客户端类型:</label></td>
                    <td>
                        <select id="client_type" data-val="<#if provision.client_type??>${provision.client_type}<#else>1</#if>" class="select2"
                                name="provision.client_type" style="width: 100%;">
                            <option value="0">请选择</option>
                            <option value="1" <#if provision.client_type??&&provision.client_type==1>selected="selected"</#if>>司机端</option>
                            <option value="2" <#if provision.client_type??&&provision.client_type==2>selected="selected"</#if>>客户端</option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="provision" class="control-label">条款类型方式:</label></td>
                    <td>
                        <select id="provision" class="select2" name="provision.provision" style="width: 100%;">
                            <option value="0">请选择</option>
                            <option value="1" <#if provision.provision??&&provision.provision==1>selected="selected"</#if>>
                                软件使用协议及隐私政策
                            </option>
                            <option value="2" <#if provision.provision??&&provision.provision==2>selected="selected"</#if>>
                                代驾服务协议
                            </option>
                            <option value="3" <#if provision.provision??&&provision.provision==3>selected="selected"</#if>>
                                司机服务合作协议
                            </option>
                            <option value="4" <#if provision.provision??&&provision.provision==4>selected="selected"</#if>>
                                关于我们
                            </option>
                            <option value="5" <#if provision.provision??&&provision.provision==5>selected="selected"</#if>>
                                出租车用户协议
                            </option>
                            <option value="6" <#if provision.provision??&&provision.provision==6>selected="selected"</#if>>
                                专车使用条款
                            </option>
                            <option value="7" <#if provision.provision??&&provision.provision==7>selected="selected"</#if>>
                                顺风车协议条款
                            </option>
                            <option value="8" <#if provision.provision??&&provision.provision==8>selected="selected"</#if>>
                                专线协议条款
                            </option>
                            <option value="9" <#if provision.provision??&&provision.provision==9>selected="selected"</#if>>
                                快车协议条款
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td colspan="3" style="text-align: center;">
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
<script charset="utf-8" src="${ctx}/static/kindeditor-4.1.7/kindeditor-min.js"></script>
<script charset="utf-8" src="${ctx}/static/kindeditor-4.1.7/lang/zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/provision_item.js?_${.now?string("hhmmss")}"></script>
</#macro>