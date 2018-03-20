<#include "../../base/_base.ftl">
<#macro title>
发送通知
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>发送通知</h4>
    </div>
    <div class="panel-body">
        <div class="table-responsive">
            <form id="searchForm_">
                <table class="hover table table-bordered table-striped">
                    <tr>
                    <td>
                        <div class="form-group">
                            <label for="s-name-LIKE" class="control-label">通知类型:</label>
                            <select name="s-type-EQ">
                                <option value="">请选择</option>
                                <option value="1">公告</option>
                                <option value="2">通知</option>
                                <option value="3">出租车公告</option>
                                <option value="4">快车公告</option>
                                <option value="5">专车公告</option>
                                <option value="6">代驾公告</option>
                                <option value="7">顺风车公告</option>
                                <option value="8">专线公告</option>
                            </select>
                            <label for="s-name-LIKE" class="control-label">发送方式:</label>
                            <select name="s-send_type-EQ">
                                <option value="">请选择</option>
                                <option value="1">短信</option>
                                <option value="2">推送</option>
                            </select>
                        </div>
                        <#if hasRole("super_admin")||hasPermission("select-company")>
                            <td>
                                <label for="select-company" class="control-label">公司选择:</label>
                                <select class="select2 select-company" name="s-company-EQ"
                                        style="width: 100px;">
                                    <option value="">请选择</option>
                                </select>
                            </td>
                        </#if>
                        </td>
                        <td>
                            <button class="btn btn-info search" type="button">搜索</button>
                            <a class="btn btn-info" href="${ctx}/admin/sys/notice/item">添加</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>

        <div class="table-responsive">
            <table id="notice-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <th width="2%"></th>
                <th>标题</th>
                <th>发送对象</th>
                <th>类型</th>
                <th>发送方式</th>
                <th width="80">操作</th>
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
            <li><a class="lockup" href="${ctx}/admin/sys/notice/lookup/{{d.id}}">查看</a></li>
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

<script type="text/javascript" src="${ctx}/static/js/app/admin/notice.js?_${.now?string("hhmmss")}"></script>
</#macro>