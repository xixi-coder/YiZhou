<#include "../../base/_base.ftl">
<#macro title>
公司服务类型管理
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/servicetype">公司服务类型管理</a></li>
    <li class="active"><a>公司服务类型<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">公司服务类型管理 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="company_form">
            <input type="hidden" name="companyService.id" value="${(companyService.id)!}" id="id">
            <table class="table table-bordered table-striped">
                <tr>
                    <td width="30%" style="text-align: right;"><label for="name" class="control-label">服务类型:</label>
                    </td>
                    <td width="40%">
                        <select id="sname" class="select2" name="companyService.service_type_item" style="width: 50%;">
                            <option value="0">请选择</option>
                            <#if serviceTypeItems??&&serviceTypeItems?size gt 0>
                                <#list serviceTypeItems as item>
                                    <option value="${(item.id)!}"
                                            <#if companyService.service_type_item??&&companyService.service_type_item==item.id>selected="selected"</#if>>${(item.name)!}</option>
                                </#list>
                            </#if>
                        </select>
                    </td>
                    <td width="30%">
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right;"><label for="royalty_standard" class="control-label">提成标准:</label>
                    </td>
                    <td>
                        <select id="rname" class="select2" name="companyService.royalty_standard" style="width: 50%;">
                            <option value="0">请选择</option>
                            <#list royaltyStandards as item>
                                <option value="${(item.id)!}"
                                        <#if companyService.royalty_standard??&&companyService.royalty_standard==item.id>selected="selected"</#if>>${(item.name)!}</option>
                            </#list>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                <td style="text-align: right;"><label for="royalty_standard_forcreate" class="control-label">服务人员补单提成标准:</label>
                </td>
                <td>
                    <select id="rname" class="select2" name="companyService.royalty_standard_forcreate" style="width: 50%;">
                        <option value="0">请选择</option>
                        <#list royaltyStandards as item>
                            <option value="${(item.id)!}"
                                    <#if companyService.royalty_standard_forcreate??&&companyService.royalty_standard_forcreate==item.id>selected="selected"</#if>>${(item.name)!}</option>
                        </#list>
                    </select>
                </td>
                <td>
                    <div style="padding-top: 10px;color: red;">*</div>
                </td>
                </tr>

                <#if hasPermission("charage_strand")>
                    <tr>
                        <td style="text-align: right;"><label for="royalty_standard" class="control-label">收费标准:</label>
                        </td>
                        <td>
                            <select id="cname" class="select2" name="companyService.charge_standard"
                                    style="width: 50%;">
                                <option value="0">请选择</option>
                                <#list chargeStandards as item>
                                    <option value="${(item.id)!}"
                                            <#if companyService.charge_standard??&&companyService.charge_standard==item.id>selected="selected"</#if>>${(item.name)!}</option>
                                </#list>
                            </select>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                </#if>
                <#if hasRole("super_admin")||hasPermission("select-company")>
                    <tr>
                        <td style="text-align: right;"><label for="company" class="control-label">所属公司:</label></td>
                        <td>
                            <select id="company" class="select2 select-company" name="companyService.company"
                                    style="width: 100%;"
                                    data-value="${(companyService.company)!}">
                                <option value="0">请选择</option>
                            </select>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr></#if>
                <tr>
                    <td style="text-align: right;">

                    </td>
                    <td>
                        <div class="col-sm-6" style="float: right;">
                            <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                        </div>
                        <div class="col-sm-2" style="float: right;">
                            <button class="btn btn-success" type="submit">提交</button>
                        </div>
                    </td>
                    <td>
                    </td>
                </tr>
            </table>
    </div>
    </form>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/companyitem.js?_${.now?string("hhmmss")}"></script>
</#macro>