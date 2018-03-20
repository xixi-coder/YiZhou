<#include "../../base/_base.ftl">
<#macro title>
报警信息
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4><strong>报警信息</strong></h4>
    </div>
    <div class="panel-body">
        <div class="table-responsive">
            <table id="ad-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th width="80">操作</th>
                    <th>报警人</th>
                    <th>报警电话</th>
                    <th>所属公司</th>
                    <th>报警信息</th>
                    <th>报警日期</th>
                    <th>处理状态</th>
                    <th>处理信息</th>
                    <th>处理人</th>
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
            <li><a href="${ctx}/admin/sys/alarm/item/{{d.id}}">回复信息</a></li>
        </ul>
    </div>
</script>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/alarm.js?_${.now?string("hhmmss")}"></script>
</#macro>