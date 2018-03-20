<#include "../../base/_base.ftl">
<#macro title>
服务类型管理
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/servicetype">服务类型管理</a></li>
    <li class="active"><a>服务类型<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">服务类型管理 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="smstmp_form">
            <input type="hidden" name="serviceTypeItem.id" value="${(serviceTypeItem.id)!}" id="id">
            <input type="hidden" name="action" value="${action}">
            <table class="table table-bordered table-striped">
                <tr>
                    <td><label for="name" class="control-label">名称:</label></td>
                    <td>
                        <input type="text" name="serviceTypeItem.name" value="${(serviceTypeItem.name)!}"
                               class="form-control n-invalid"
                               placeholder="如:张三">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="royalty_standard" class="control-label">提成标准:</label></td>
                    <td>
                        <select id="cname" class="select2" name="serviceTypeItem.royalty_standard" style="width: 100%;">
                            <option value="0">请选择</option>
                            <#list royaltyStandards as item>
                                <option value="${(item.id)!}"
                                        <#if serviceTypeItem.royalty_standard??&&serviceTypeItem.royalty_standard==item.id>selected="selected"</#if>>${(item.name)!}</option>
                            </#list>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="royalty_standard" class="control-label">收费标准:</label></td>
                    <td>
                        <select id="cname" class="select2" name="serviceTypeItem.charge_standard" style="width: 100%;">
                            <option value="0">请选择</option>
                            <#list chargeStandards as item>
                                <option value="${(item.id)!}"
                                        <#if serviceTypeItem.charge_standard??&&serviceTypeItem.charge_standard==item.id>selected="selected"</#if>>${(item.name)!}</option>
                            </#list>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="name" class="control-label">创建人:</label></td>
                    <td>
                        <input type="text" name="serviceTypeItem.creater" value="${(serviceTypeItem.creater)!}"
                               class="form-control n-invalid"
                               placeholder="如:1" id="time_point">
                    <td>
                        <div style="padding-top: 10px;color: red;" id="ty">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="royalty_standard" class="control-label">服务类型:</label></td>
                    <td>
                        <select id="cname" class="select2" name="serviceTypeItem.type" style="width: 100%;">
                            <option value="0">请选择</option>
                            <#list serviceTypes as item>
                                <option value="${(item.id)!}"
                                        <#if serviceTypeItem.type??&&serviceTypeItem.type==item.id>selected="selected"</#if>>${(item.name)!}</option>
                            </#list>
                        </select>
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
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/servicetype_item.js?_${.now?string("hhmmss")}"></script>
</#macro>