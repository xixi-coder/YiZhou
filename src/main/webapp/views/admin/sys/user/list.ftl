<#include "../../base/_base.ftl">
<#macro title>
用户管理
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>用户管理</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <table class="table table-bordered table-striped">
            <tr>
                <#if hasPermission("yonghutianjia")>
                    <td width="80">
                        <a class="btn btn-info" href="${ctx}/admin/sys/user/item">添加</a>
                    </td></#if>
                <td width="100">
                    <form id="searchForm_">
                        <div class="form-group">
                            <label for="s-name-LIKE" class="control-label">用户名:</label>
                            <input name="s-name-LIKE"/>
                            <label for="s-username-EQ" class="control-label">登陆名:</label>
                            <input name="s-username-EQ"/>
                        </div>
                    </form>
                </td>
                <td>
                    <button class="btn btn-info search">搜索</button>
                </td>
            </tr>
        </table>
        <div class="table-responsive">
            <table id="user-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>用户名</th>
                    <th>登陆名</th>
                    <th>状态</th>
                    <th>手机号</th>
                    <th>生日</th>
                    <th>最后登陆日期</th>
                    <th>最后登陆IP</th>
                    <th>操作</th>
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
            <#if hasPermission("yonghubianji")>
                <li><a href="${ctx}/admin/sys/user/item/{{d.id}}">编辑</a></li>
            </#if>
            <li><a class="updatePw" href="javascript:void(0);" data-id="{{d.id}}">修改密码</a></li>
            <li><a class="rePw" href="javascript:void(0);" data-id="{{d.id}}">重置密码</a></li>
            <#if hasPermission("yonghushanchu")>
                <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除<span class="warning{{d.id}}" style="color: #880000" onmouseover="warning_message({{d.id}})" data-id="{{d.id}}"> ★</span></a></li>
            </#if>
            {{#if (d.status==1){ }}
            <#if hasPermission("jinyongyonghu")>
                <li><a class="disable" href="javascript:void(0);" data-id="{{d.id}}" data-status="{{d.status}}">禁用</a></li>
            </#if>
            {{# }else if(d.status==2){ }}
            <#if hasPermission("jinyongyonghu")>
                <li><a class="disable" href="javascript:void(0);" data-id="{{d.id}}" data-status="{{d.status}}">启用</a></li>
            </#if>
            {{#} }}
            {{#if(d.type==2){ }}
            <li><a class="drivers" href="javascript:void(0);" data-id="{{d.id}}">管理所属司机</a></li>
            {{#}}}
        </ul>
    </div>
</script>
<script id="driver_tpl" type="text/html">
    <div class="row">
        <div class="input-group col-sm-10">
            <textarea id="selectUser" style="width: 100%;" rows="5" class="form-control"></textarea>
            <input type="hidden" id="selectUserHide"/>
        </div>
    </div>
    </br>
    <div class="input-group col-sm-10"">
    <input type="text" id="searchCondition" class="form-control">
    <span class="input-group-btn">
										<button id="searchBtn" class="btn btn-default" type="button">搜索</button>
									  </span>
    </div>
    <div class="input-group col-sm-10" style="margin-top: 10px;">
        <div id="ztree_user" class="ztree" style="overflow-y: scroll; height: 300px;"></div>
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


<script type="text/javascript" src="${ctx}/static/js/app/admin/user.js?_${.now?string("hhmmss")}"></script>
</#macro>