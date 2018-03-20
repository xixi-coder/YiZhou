<#include "../../base/_base.ftl">
<#macro title>
角色管理
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/company">角色管理</a></li>
    <li class="active"><a>角色<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">角色管理 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="role_form">
            <input type="hidden" name="role.id" value="${(role.id)!}" id="id">
            <input type="hidden" name="resourcesIds" id="resourcesIds">
            <div class="row ">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="name" class="col-sm-4 control-label">角色名称:</label>
                    <div class="col-sm-7">
                        <input type="text" name="role.name" value="${(role.name)!}" class="form-control n-invalid"
                               id="name"
                               placeholder="角色名称">
                    </div>
                    <div style="padding-top: 10px;color: red;">*</div>
                </div>
            </div>
            <div class="row ">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="description" class="col-sm-4 control-label">描述:</label>
                    <div class="col-sm-7">
                        <textarea id="description"
                                  class="form-control n-invalid"
                                  name="role.description">${(role.description)!}</textarea>
                    </div>
                </div>
            </div>
            <#if hasRole("super_admin")||hasPermission("select-company")>
                <div class="row">
                    <div class="form-group col-xs-12 col-md-12">
                        <label for="company" class="col-sm-4 control-label">所属公司:</label>
                        <div class="col-sm-7">
                            <select id="company" class="select2 select-company" name="role.company"
                                    style="width: 100%;"
                                    data-value="${(role.company)!}">
                                <option value="0">请选择</option>
                            </select>
                        </div>
                    </div>
                </div>
            </#if>
            <div class="row">
                <#if resources??&&resources?size gt 0>
                    <#list resources as item>
                        <div class="panel panel-success" style="margin-right: 5px; margin-left: 5px;">
                            <div class="panel-heading">
                                <h3 class="panel-title"><input type="checkbox" class="parent"
                                                               value="${(item.id)!}"/>&nbsp;&nbsp;${(item.show_name)!}
                                </h3>
                            </div>
                            <div class="panel-body">
                                <#if item.children??&&item.children?size gt 0>
                                    <#list item.children as citem>
                                        <div style="margin-left: 5px;white-space:nowrap;margin-bottom: 5px;float: left;">
                                            <input type="checkbox" class="children" value="${(citem.id)}"/>&nbsp;${(citem.show_name)!}&nbsp;&nbsp;
                                        </div>
                                    </#list>
                                </#if>
                            </div>
                        </div>
                    </#list>
                </#if>
            </div>
            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                    </div>
                    <div class="col-sm-2" style="float: right;">
                        <button class="btn btn-success" type="submit">提交</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript">
    var roleId = <#if role.id??>${(role.id)!}<#else>0</#if>;
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/role_item.js?_${.now?string("hhmmss")}"></script>
</#macro>