<#include "../../base/_base.ftl">
<#macro title>
版本控制
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>版本控制</h4>
    </div>
    <div class="panel-body">
        <table class="table table-bordered table-striped">
            <tr>
                <td width="80">
                    <a class="btn btn-info" href="${ctx}/admin/sys/version/item">添加</a>
                </td>
                <td width="100">
                    <form id="searchForm_">
                        <div class="form-group">
                            <label for="s-version_no-LIKE" class="control-label">版本号:</label>
                            <input name="s-version_no-LIKE"/>
                            <label for="s-type-EQ" class="control-label">版本类型:</label>
                            <select name="s-type-EQ">
                                <option value="">请选择</option>
                                <option value="1">司机端</option>
                                <option value="2">客户端</option>
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
            <table id="version-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>版本号</th>
                    <th>版本</th>
                    <th>系统类型</th>
                    <th>版本类型</th>
                    <th>更新时间</th>
                    <th>创建时间</th>
                    <th width="80">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script id="action_tpl" type="text/x-handlebars-template">
    <div class="btn-group">
        <button type="button" class="btn btn-success btn-xs ">操作</button>
        <button type="button" class="btn btn-success btn-xs  dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only"></span></button>
        <ul class="dropdown-menu success btn-xs " role="menu">
            <li><a href="${ctx}/admin/sys/version/item/{{d.id}}">修改</a></li>
            <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除<span class="warning{{d.id}}" style="color: #880000" onmouseover="warning_message({{d.id}})" data-id="{{d.id}}"> ★</span></a></li>
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
<script type="text/javascript" src="${ctx}/static/js/app/admin/version.js?_${.now?string("hhmmss")}"></script>
</#macro>