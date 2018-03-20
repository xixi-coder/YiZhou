<#include "../../base/_base.ftl">
<#macro title>
提成管理
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>提成管理</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <table class="table table-bordered table-striped">
            <tr>
                <td width="100">
                    <form id="searchForm_">
                        <div class="form-group">
                            <label for="s-name-LIKE" class="control-label">名称:</label>
                            <input name="s-name-EQ"/>
                        </div>
                    </form>
                </td>
                <td>
                    <button class="btn btn-info search">搜索</button>
                    <a class="btn btn-info" href="${ctx}/admin/sys/royalty/item">添加</a>
                </td>
            </tr>
        </table>
        <div class="table-responsive">
            <table id="royalty-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>名称</th>
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
            <li><a href="${ctx}/admin/sys/royalty/item/{{d.id}}">编辑</a></li>
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

<script type="text/javascript" src="${ctx}/static/js/app/admin/royalty.js?_${.now?string("hhmmss")}"></script>
</#macro>