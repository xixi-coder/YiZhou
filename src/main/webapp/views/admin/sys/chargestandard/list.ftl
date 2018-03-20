<#include "../../base/_base.ftl">
<#macro title>
收费标准管理
</#macro>
<#macro content>
<!--suppress ALL -->
<div class="panel panel-default">
    <input type="hidden" value="${(type)!}" id="type"/>
    <div class="panel-heading">
        <h4>收费标准管理</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <ul class="nav nav-tabs" style="margin-top: 5px;margin-left: 5px;">
        <li class="tabs-select">
            <a href="${ctx}/admin/sys/chargestandard">普通收费标准</a>
        </li>
        <li class="tabs-select">
            <a href="${ctx}/admin/sys/chargestandard/1">顺风车收费标准</a>
        </li>
    </ul>
    <div class="panel-body">
        <form id="searchForm_">
            <table class="table table-bordered table-striped">
                <tr>
                    <#if hasRole("super_admin")||hasPermission("select-company")>
                        <td>
                            <label class="control-label">所属公司:</label>
                            <select class="select2 select-company" name="s-company-EQ"
                                    style="width: 100px;">
                                <option value="">请选择</option>
                            </select>
                        </td>
                    </#if>
                    <td width="100">
                        <div class="form-group">
                            <label for="s-name-LIKE" class="control-label">收费名称:</label>
                            <input name="s-name-EQ"/>
                        </div>
                    </td>
                    <td>
                        <button class="btn btn-info search" type="button">搜索</button>
                        <a class="btn btn-info" href="${ctx}/admin/sys/chargestandard/item?type=${type}">添加</a>
                    </td>
                </tr>
            </table>
        </form>
        <div class="table-responsive">
            <table id="chargestandard-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>收费名称</th>
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script id="action_tpl" type="text/html">
    <div class="btn-group">
        <button type="button" class="btn btn-success btn-xs ">操作</button>
        <button type="button" class="btn btn-success btn-xs dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only"></span></button>
        <ul class="dropdown-menu success btn-xs " role="menu">
            <li><a href="${ctx}/admin/sys/chargestandard/item/{{d.id}}?type=${type!}">编辑</a></li>
            <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除                <span class="warning{{d.id}}" style="color: #880000" onmouseover="warning_message({{d.id}})" data-id="{{d.id}}"> ★</span></a></li>
        </ul>
    </div>
</script>
</#macro>
<#macro javascript>
<script>
    var type = Number($('#type').val());
    var select = $('.tabs-select')
    $(select[type]).addClass("active")
    function warning_message(id){
        //tips层
        layer.tips('此项为敏感项，请谨慎操作！', '.warning'+id,{tips:[3,"red"]});
    }
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/chargestandard.js?_${.now?string("hhmmss")}"></script>
</#macro>