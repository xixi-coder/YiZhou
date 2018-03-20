<#include "../../base/_base.ftl">
<#macro title>
账户流水
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4><strong>账户流水</strong></h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <div class="table-responsive">
            <table id="capital" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>操作类型</th>
                    <th>金额</th>
                    <th>创建时间</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<div class="row" style="margin-top:20px;">
    <div class="col-sm-12">
        <div class="col-sm-6" style="float: right;">
            <a class="btn btn-default" href="javascript:void(0);"
               onclick="window.location.href=g.ctx + '/admin/member/list'">返回</a>
        </div>
    </div>
</div>
<script id="action_tpl" type="text/x-handlebars-template">
    <div class="btn-group">
        <button type="button" class="btn btn-success btn-xs"
                onclick="window.location.href=g.ctx + '/admin/member/list/capitalpage/{{d.id}}-${id1}'">查看详情
        </button>
    </div>
</script>
</#macro>
<#macro javascript>
<script>
    var id = ${id};
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/capital.js?_${.now?string("hhmmss")}"></script>
</#macro>