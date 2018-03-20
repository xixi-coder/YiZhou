<#include "../base/_base.ftl">
<#macro title>
    专线管理
</#macro>
<#macro content>
<!--suppress ALL -->
<input type="hidden" value="${(type)!}" id="TypeItem"/>
<ul class="nav nav-tabs">
    <#list serviceTypesItem as item>
        <li <#if type==item.id>class="active"</#if>>
            <a href="${ctx}/admin/zxLine/${item.id}">${item.name}</a>
        </li>
    </#list>
</ul>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>专线管理</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <div class="table-responsive" style="overflow:auto;">
            <form id="searchForm_">
                <table class="hover table table-striped table-bordered" cellspacing="0" width="200%">
                    <tr>
                        <td colspan="2">
                            <label for="s-name-LIKE" class="control-label">发布时间:</label>
                            <input type="hidden" name="s-dzl.create_time-BETWEEN" value="" id="timeSearch">
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="start"
                                   placeholder="如:2017-11-11">至
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="end"
                                   placeholder="如:2017-11-11">

                            <#if hasRole("super_admin")||hasPermission("select-company")>
                                <label class="control-label">所属公司:</label>
                                <select class="select2 select-company" name="s-dzl.company_id-EQ"
                                        style="width: 100px;">
                                    <option value="">请选择</option>
                                </select>
                            </#if>
                        </td>
                        <td>
                            <button class="btn btn-info search" type="button">搜索</button>
                            <a class="btn btn-info" href="${ctx}/admin/zxLine/item">添加</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>

        <div class="table-responsive" style="overflow:auto;">

            <table id="special-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="200%">
                <thead>
                <tr>
                    <th width="10px"></th>
                    <th>专线ID</th>
                    <th>发布公司</th>
                    <th>起点</th>
                    <th>终点</th>
                    <th>里程</th>
                    <th>不拼车价格:元</th>
                    <th>拼车价格:人/元</th>
                    <th>特殊时间段不拼车价格:元</th>
                    <th>特殊时间段拼车价格:人/元</th>
                    <th>添加时间</th>
                    <th>最后修改时间</th>
                    <th width="100px;">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
</#macro>
<#macro javascript>
<script id="action_tpl" type="text/html">
    <div class="btn-group" style="width:64px;">
        <button type="button" class="btn btn-success btn-xs ">操作</button>
        <button type="button" class="btn btn-success btn-xs  dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only"></span></button>
        <ul class="dropdown-menu success btn-xs " role="menu">
            <li><a href="${ctx}/admin/zxLine/item/{{d.id}}">编辑</a></li>
            <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除
                <span class="warning{{d.id}}" style="color: #880000" onmouseover="warning_message({{d.id}})" data-id="{{d.id}}"> ★</span></a></li>

        </ul>
    </div>
</script>
<script type="text/javascript">
    function warning_message(id){
        //tips层
        layer.tips('此项为敏感项，请谨慎操作！', '.warning'+id,{tips:[3,"red"]});
    }
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/zxLine.js?_${.now?string("hhmmss")}"></script>
</#macro>