<#include "../../base/_base.ftl">
<#macro title>
活动管理
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>活动管理</h4>
    </div>
    <div class="panel-body">
        <div class="table-responsive" style="overflow:auto;">
            <form id="searchForm_">
                <table class="table table-bordered table-striped">
                    <tr>
                        <td width="100">

                            <div class="form-group">
                                <label for="s-da.name-LIKE" class="control-label">活动名称:</label>
                                <input name="s-da.name-LIKE"/>

                            <#if hasRole("super_admin")||hasPermission("select-company")>
                                <td>
                                    <div class="form-group">
                                        <label for="real_name" class="control-label">公司选择:</label>
                                        <select class="select2 select-company" name="s-dy.id-EQ" id="companySelect"
                                                style="width: 50%;">
                                            <option value="">请选择</option>
                                        </select>
                                    </div>
                                </td>
                            </#if>
                            </div>

                        </td>
                        <td>
                            <button type="button" class="btn btn-info search">搜索</button>
                            <#if hasPermission("huodongtianjia")>
                                <a class="btn btn-info" href="${ctx}/admin/activity/list/item">添加</a>
                            </#if>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="table-responsive">
            <table id="activity" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>公司名称</th>
                    <th>活动名称</th>
                    <th>活动事件</th>
                    <th>优惠内容</th>
                    <th>创建时间</th>
                    <th>活动开始时间</th>
                    <th>活动截止时间</th>
                    <th width="80">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script id="action_tpl" type="text/x-handlebars-template">
    <div class="btn-group">
        <button type="button" class="btn btn-success btn-xs">操作</button>
        <button type="button" class="btn btn-success btn-xs dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only"></span></button>
        <ul class="dropdown-menu success btn-xs" role="menu">
            <#if hasPermission("huodongxiugai")>
                <li><a href="${ctx}/admin/activity/list/item/{{d.id}}">修改</a></li></#if>
            <#if hasPermission("huodongshanchu")>
                <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除<span class="warning{{d.id}}" style="color: #880000" onmouseover="warning_message({{d.id}})" data-id="{{d.id}}"> ★</span></a></li></#if>
        </ul>
    </div>
</script>

</#macro>
<#macro javascript>

<script type="text/javascript">
    function warning_message(id){
        //tips层
        layer.tips('此项为敏感项，请谨慎操作！', '.warning'+id,{tips:[3,"red"]});
    }
</script>

<script type="text/javascript" src="${ctx}/static/js/app/admin/activity.js?_${.now?string("hhmmss")}"></script>
</#macro>