<#include "../../base/_base.ftl">
<#macro title>
服务类型管理
</#macro>
<#macro content>
<!--suppress ALL -->
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>服务类型管理</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <table class="table table-bordered table-striped">
            <tr>
                <td width="80">
                    <a class="btn btn-info" href="${ctx}/admin/sys/servicetype/item">添加</a>
                </td>
                <td width="100">
                    <form id="searchForm_">
                        <div class="form-group">
                            <label for="s-sname-LIKE" class="control-label">名称:</label>
                            <input name="s-sname-LIKE"/>
                        </div>
                    </form>
                </td>
                <td>
                    <button class="btn btn-info search">搜索</button>
                </td>
            </tr>
        </table>
        <div class="table-responsive">
            <table id="servicetype-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>姓名</th>
                    <th>提成标准</th>
                    <th>收费标准</th>
                    <!-- <th>收费标准描述</th-->
                    <th>创建时间</th>
                    <th>最后更新时间</th>
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
        <button type="button" class="btn btn-success  btn-xs dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only"></span></button>
        <ul class="dropdown-menu success btn-xs " role="menu">
            <li><a href="${ctx}/admin/sys/servicetype/item/{{d.id}}">编辑</a></li>
            <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除
                <span class="warning{{d.id}}" style="color: #880000" onmouseover="warning_message({{d.id}})" data-id="{{d.id}}"> ★</span></a></li>
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

<script type="text/javascript" src="${ctx}/static/js/app/admin/servicetype.js?_${.now?string("hhmmss")}"></script>
</#macro>