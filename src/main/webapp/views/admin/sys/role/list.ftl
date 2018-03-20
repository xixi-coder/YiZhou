<#include "../../base/_base.ftl">
<#macro title>
角色管理
</#macro>
<#macro content>
<!--suppress ALL -->

<div class="panel panel-default">
    <div class="panel-heading">
        <h4>角色管理</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <table class="table table-bordered table-striped">
            <tr>
                <td>
                    <form id="searchForm_">
                        <div class="form-group">
                            <label for="s-name-LIKE" class="control-label">角色名称:</label>
                            <input name="s-name-LIKE" value=""/>
                        </div>
                    </form>
                </td>
                <td>
                    <button class="btn btn-info search">搜索</button>
                    <#if hasPermission("juesetianjia")>
                        <a class="btn btn-info" href="${ctx}/admin/sys/role/item">添加</a></#if>
                </td>
            </tr>
        </table>
        <div class="table-responsive">
            <table id="role-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>角色名</th>
                    <th>编码</th>
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
            <#--<#if hasPermission("juesebianji")>-->
                <li><a href="${ctx}/admin/sys/role/item/{{d.id}}">编辑</a></li>
<#--</#if>-->
            <#if hasPermission("jueseshanchu")>
                <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除
                    <span class="warning{{d.id}}" style="color: #880000" onmouseover="warning_message({{d.id}})" data-id="{{d.id}}"> ★</span></a></li></#if>
            <#if hasPermission("jueseshouquan")>
                <li><a class="user" href="javascript:void(0);" data-id="{{d.id}}">授权用户</a></li></#if>
        </ul>
    </div>
</script>
<script id="user_tpl" type="text/html">
    <div class="row">
        <div class="input-group col-sm-10">
            <textarea id="selectUser" style="width: 100%;" class="form-control"></textarea>
            <input type="hidden" id="selectUserHide"/>
        </div>
    </div>
    </br>
    <div class="input-group col-sm-10">
        <input type="text" id="searchCondition" class="form-control">
        <span class="input-group-btn">
										<button id="searchBtn" class="btn btn-default" type="button">搜索</button>
									  </span>
    </div>
    <div class="input-group col-sm-10">
        <div id="ztree_user" class="ztree"></div>
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

<script type="text/javascript" src="${ctx}/static/js/app/admin/role.js?_${.now?string("hhmmss")}"></script>
</#macro>