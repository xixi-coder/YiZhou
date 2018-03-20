<#include "../../../base/_base.ftl">
<#macro title>
客户评论信息
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>客户评论信息</h4>
    </div>
    <div class="panel-body">
        <div class="table-responsive" style="overflow:auto;">
            <form id="searchForm_">
                <table class="table table-bordered table-striped">
                    <tr>
                        <td width="100">
                            <div class="form-group">
                                <label for="s-dd.no-EQ" class="control-label">司机手机号:</label>
                                <input name="s-ddi.phone-EQ"/>
                            </div>
                        </td>
                        <td>
                            <div class="form-group">
                            <label for="s-dml.user_name-LIKE" class="control-label">评论时间:</label>
                            <input type="hidden" name="s-dg.create_time-BETWEEN"  id="timeSearch">
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="start" name="start1"
                                   placeholder="如:1992-11-11">至
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="end" name = "end1"
                                   placeholder="如:1992-11-11">
                                </div>
                        </td>
                        <td>
                            <div class="form-group">
                            <label for="s-dml.user_name-LIKE" class="control-label">评分:</label>
                                <input name="s-score-BETWEEN" id="grade1" type="hidden">
                            <select class="select2" id="score" style="width: 50%;" name="s-score-BETWEEN">
                                <option value="">请选择</option>
                                <option value=0,1>0-1</option>
                                <option value=1,2>1-2</option>
                                <option value=2,3>2-3</option>
                                <option value=3,4>3-4</option>
                                <option value=4,5>4-5</option>
                            </select>
                        </td>
                        <#if hasRole("super_admin")||hasPermission("select-company")>
                            <td>
                                <div class="form-group">
                                    <label for="real_name" class="control-label">公司选择:</label>
                                    <select class="select2 select-company" name="s-ddi.company-EQ" id="companySelect"
                                            style="width: 50%;">
                                        <option value="">请选择</option>
                                    </select>
                                </div>
                            </td>
                        </#if>
                        <td>
                            <button type="button" class="btn btn-info search">搜索</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="table-responsive">
            <table id="grade" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>订单号</th>
                    <th>客户</th>
                    <th>司机</th>
                    <th>评分</th>
                    <th>评论时间</th>
                    <th>操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script id="action_tpl" type="text/x-handlebars-template">
    <div class="btn-group">
        <a href="${ctx}/admin/member/driver/grade/item/{{d.id}}"><button type="button" class="btn btn-success btn-xs">查看评论</button></a>
    </div>
</script>

</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/grade.js?_${.now?string("hhmmss")}"></script>
</#macro>