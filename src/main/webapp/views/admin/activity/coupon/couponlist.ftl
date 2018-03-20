<#include "../../base/_base.ftl">
<#macro title>
优惠券列表
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>优惠券列表</h4>
    </div>
    <div class="panel-body">
        <div class="table-responsive" style="overflow:auto;">
            <table class="table table-bordered table-striped">
                <tr>
                    <td>
                        <a id="createcoupon" class="btn btn-info" href="javascript:void(0);"
                           data-id="{{d.id}}">生成优惠券</a>
                        <a class="btn btn-default" href="javascript:void(0);"
                           onclick="window.history.go(-1)">返回</a>

                    </td>
                </tr>
                <#--<form id="searchForm_">-->
                    <#--<label for="s-login.status-EQ" class="control-label">优惠券状态:</label>-->
                    <#--<select name="s-login.status-EQ" style="height: 26px;">-->
                        <#--<option value="">请选择</option>-->
                        <#--<option value="0">未使用</option>-->
                        <#--<option value="1">已使用</option>-->
                        <#--<option value="2">已冻结</option-->
                    <#--</select>-->
                    <#--<button type="button" class="btn btn-info search">搜索</button>-->
                <#--</form>-->
            </table>
        </div>
        <div class="table-responsive">
            <table id="coupon-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="1%"></th>
                    <th>优惠券标题</th>
                    <th>优惠券金额</th>
                    <th>优惠券描述</th>
                    <th>优惠券号码</th>
                    <th>优惠券密码</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>姓名</th>
                    <th>用户名</th>
                    <th>优惠券获取时间</th>
                    <th>优惠券使用情况</th>
                    <th>优惠券使用时间</th>
                    <th>操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<script id="action_tpl" type="text/x-handlebars-template">
    <div class="btn-group">
    <#if hasPermission("shanchuyouhuiquan")>
        <a class="delete" href="javascript:void(0);">
            <button type="button" class="btn btn-success btn-xs" data-id="{{d.did}}">删除优惠券</button>
        </a>
    </#if>
    </div>
</script>
<script id="coupon_tpl" type="text/x-handlebars-template">
    <form id="coupon_form" action="" autocomplete="off">
        <input type="hidden" name="id1" value="${id1}">
        <div class="form-group">
            <label for="amount" class="col-sm-4 control-label">生成优惠券数量:</label>
            <div class="col-sm-7">
                <input type="text" name="amount"
                       placeholder="如：20">
            </div>
            <div style="padding-top: 10px;color: red;">*</div>
        </div>
    </form>
</script>
</#macro>
<#macro javascript>
<script>
    var id1 = ${id1}
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/couponlist.js?_${.now?string("hhmmss")}"></script>
</#macro>