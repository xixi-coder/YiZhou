<#include "../../base/_base.ftl">
<#macro title>
vip操作流水
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4><strong>vip操作流水</strong></h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <div class="table-responsive">
            <table id="vipcapital" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>id</th>
                    <th>用户手机</th>
                    <th>金额(元)</th>
                    <th>备注</th>
                    <th>创建时间</th>
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

</#macro>
<#macro javascript>
<script>
    var id = ${id};
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/vipCapital.js?_${.now?string("hhmmss")}"></script>
</#macro>