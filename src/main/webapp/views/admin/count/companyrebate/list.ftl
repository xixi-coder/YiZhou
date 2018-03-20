<#include "../../base/_base.ftl">
<#macro title>
公司提成统计
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>公司提成统计</h4>
    </div>
    <div class="panel-body">
        <div class="table-responsive" style="overflow: auto;">
            <form id="searchForm_" action="${ctx}/admin/count/companyrebate/export">
                <table class="table table-bordered table-striped">
                    <tr>
                        <td width="100">

                            <div class="form-group">
                                <label for="s-user_name-EQ" class="control-label">司机用户名:</label>
                                <input name="s-user_name-EQ"/>
                            </div>


                        </td>
                        <td>
                            <label for="s-dml.user_name-LIKE" class="control-label">时间:</label>
                            <input type="hidden" name="s-dcr.rebate_time-BETWEEN" value="" id="timeSearch">
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="start" name="start1"
                                   placeholder="如:1992-11-11">至
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="end" name = "end1"
                                   placeholder="如:1992-11-11">
                        </td>
                        <#if hasRole("super_admin")||hasPermission("select-company")>
                            <td>
                                <div class="form-group">
                                    <label for="real_name" class="control-label">公司选择:</label>
                                    <select class="select2" name="s-dc.id-EQ" id="companySelect"
                                            style="width: 30%;">
                                        <option value="">请选择</option>
                                        <#list companies as item>
                                            <option value="${(item.id)!}">${(item.name)!}</option>
                                        </#list>
                                    </select>
                                </div>
                            </td>
                        </#if>

                        <td>
                            <button type="button" class="btn btn-info search">搜索</button>
                            <button type="button" class="btn btn-primary button-margin" onclick="javascript:method1('companyRebate-list')">
                                <i class="fa fa-file-excel-o"></i>&nbsp;导出此页面数据
                            </button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="table-responsive" style="overflow: auto;">
            <table id="companyRebate-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>所属公司</th>
                    <th>提成金额</th>
                    <th>提成订单</th>
                    <th>提成时间</th>
                    <th>提成司机</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/companyrebate.js?_${.now?string("hhmmss")}"></script>
</#macro>