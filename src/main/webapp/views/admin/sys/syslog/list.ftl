<#include "../../base/_base.ftl">
<#macro title>
系统日志
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>系统日志</h4>
    </div>
    <div class="panel-body">
        <div class="table-responsive">
            <table id="syslog-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>访问路径</th>
                    <th>参数</th>
                    <th>创建时间</th>
                    <th>操作备注</th>
                    <th>ip地址</th>
                    <th width="80">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script id="action_tpl" type="text/x-handlebars-template">
    <div class="btn-group">
        <a href="${ctx}/admin/sys/syslog/item/{{d.id}}">
            <button type="button" class="btn btn-success btn-xs ">查看详情</button>
        </a>
    </div>
</script>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/syslog.js?_${.now?string("hhmmss")}"></script>
</#macro>