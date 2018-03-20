<#include "../../base/_base.ftl">
<#macro title>
公司服务类型管理
</#macro>
<#macro content>
<!--suppress ALL -->
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>公司服务类型管理</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <table class="table table-bordered table-striped">
            <tr>
                <#if hasPermission("gongsifuwuleixingtianjia")>
                    <td width="80">
                        <a class="btn btn-info" href="${ctx}/admin/sys/servicetype/itemforcompany">添加</a>
                    </td></#if>
                <td width="100">
                    <form id="searchForm_">
                        <div class="form-group">
                            <label for="s-dsti.`name`-LIKE" class="control-label">名称:</label>
                            <input name="s-dsti.`name`-LIKE"/>
                            <label for="s-dst.`name`-LIKE" class="control-label">服务类型:</label>
                            <input name="s-dst.`name`-LIKE"/>
                            <label class="control-label">所属公司:</label>
                            <select class="select2 select-company" name="s-dc.id-EQ"
                                    style="width: 100px;">
                                <option value="">请选择</option>
                            </select>
                        </div>
                    </form>
                </td>
                <td>
                    <button class="btn btn-info search">搜索</button>
                </td>
            </tr>
        </table>
        <div class="table-responsive">
            <table id="servicetype_company-list" cellspacing="0"
                   class="hover table table-striped table-bordered" width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>名称</th>
                    <th>服务类型</th>
                    <th>提成标准</th>
                    <th>收费标准</th>
                    <th>公司</th>
                    <th>更新时间</th>
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
        <button type="button" class="btn btn-success btn-xs  dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only"></span></button>
        <ul class="dropdown-menu success btn-xs " role="menu">
            <#if hasPermission("gongsifuwuleixingbianji")>
                <li><a href="${ctx}/admin/sys/servicetype/itemforcompany/{{d.id}}">编辑</a></li>
            </#if>
            <#if hasPermission("gongsifuwuleixingshanchu")>
                <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除<span class="warning{{d.id}}" style="color: #880000" onmouseover="warning_message({{d.id}})" data-id="{{d.id}}"> ★</span></a></li>
            </#if>
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


<script type="text/javascript"
        src="${ctx}/static/js/app/admin/servicetype_company.js?_${.now?string("hhmmss")}"></script>
</#macro>