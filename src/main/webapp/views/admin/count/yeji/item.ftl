<#include "../../base/_base.ftl">
<#macro title>
服务人员订单明细
</#macro>
<#macro content>
<ul class="nav nav-tabs" style="margin-top: 5px;margin-left: 5px;">
    <#list serviceTypes as item>
        <li class="tabs-select">
            <a href="${ctx}/admin/count/yeji/item?type=${item.id}&username=${username}">${item.name}</a>
        </li>
    </#list>
</ul>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>服务人员订单明细</h4>
    </div>
    <div class="panel-body">
        <input type="hidden" name="type" id="type" value="${type!1}">
        <div id="daijiatab" class="tab-pane active">
            <div class="table-responsive">
                <table class="table table-bordered table-striped">
                    <tr>
                        <td>
                            <form id="searchForm_">
                                <div class="form-group">
                                    <input type="hidden" name="s-dd.pay_time-BETWEEN" value=""
                                           id="timeSearch">
                                    <input type="text"
                                           class="datepicker-input" id="start">至
                                    <input type="text"
                                           class="datepicker-input" id="end">
                                </div>
                            </form>
                        </td>
                        <td>
                            <button class="btn btn-info search">搜索</button>
                            <a class="btn btn-default" href="javascript:void(0);"
                               onclick="window.location.href=g.ctx + '/admin/count/yeji'">返回</a>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="table-responsive">
                <table id="order-list" cellspacing="0" class="hover table table-striped table-bordered"
                       width="100%">
                    <thead>
                    <tr>
                        <th width="2%"></th>
                        <th>时间</th>
                        <th>订单号</th>
                        <th>订单金额(元)</th>
                        <th>服务人员提成(元)</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript">
    var username = ${username!}
    var type = ${type!1};
    $($('.tabs-select')[type]).addClass("active");
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/yeji_item.js?_${.now?string("hhmmss")}"></script>
</#macro>