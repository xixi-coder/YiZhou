<#include "../../base/_base.ftl">
<#macro title>
服务人员业绩
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>服务人员业绩</h4>
    </div>
    <div class="panel-body">
        <div class="table-responsive">
            <form id="searchForm_" action="${ctx}/admin/count/yeji/export">
                <table class="table table-bordered table-striped">
                    <tr>
                        <td>
                            <label for="s-dml.user_name-LIKE" class="control-label">服务人员账号:</label>
                            <input name="s-dml.user_name-LIKE"/>
                        </td>
                        <td>
                            <label for="s-dml.user_name-LIKE" class="control-label">时间:</label>
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="start" name="startDate"
                                   placeholder="如:1992-11-11">至
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="end" name="endDate"
                                   placeholder="如:1992-11-11">
                        </td>
                        <#if hasRole("super_admin")||hasPermission("select-company")>
                            <td width="30%">
                                <label for="real_name" class="control-label">公司选择:</label>
                                <select class="select2" name="s-ddi.company-EQ" id="companySelect"
                                        style="width: 30%;">
                                    <option value="">请选择</option>
                                    <#list companies as item>
                                        <option value="${(item.id)!}">${(item.name)!}</option>
                                    </#list>
                                </select>
                            </td>
                        </#if>

                        <td>
                            <button id="export_button" type="button" class="btn btn-primary"  onclick="javascript:method1('yeji-list')"><i
                                    class="glyphicon glyphicon-save"></i>导出
                            </button>
                            <button type="button" class="btn btn-info search" id="search">搜索</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="table-responsive">
            <table id="yeji-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th colspan="4" style="text-align:center;">#</th>
                    <th colspan="3" style="text-align:center;">代驾</th>
                    <th colspan="3" style="text-align:center;">专车</th>
                    <th colspan="3" style="text-align:center;">出租车</th>
                    <th colspan="3" style="text-align:center;">快车</th>
                    <th colspan="3" style="text-align:center;">顺风车</th>
                    <th colspan="3" style="text-align:center;">城际专线</th>
                    <th colspan="3" style="text-align:center;">航空专线</th>
                </tr>
                <tr>
                    <th></th>
                    <th>用户名</th>
                    <th>类型</th>
                    <th>所属公司</th>
                    <th>完成单量</th>
                    <th>订单金额</th>
                    <th>提成</th>
                    <th>完成单量</th>
                    <th>订单金额</th>
                    <th>提成</th>
                    <th>完成单量</th>
                    <th>订单金额</th>
                    <th>提成</th>
                    <th>完成单量</th>
                    <th>订单金额</th>
                    <th>提成</th>
                    <th>完成单量</th>
                    <th>订单金额</th>
                    <th>提成</th>
                    <th>完成单量</th>
                    <th>订单金额</th>
                    <th>提成</th>
                    <th>完成单量</th>
                    <th>订单金额</th>
                    <th>提成</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/yeji.js?_${.now?string("hhmmss")}"></script>
</#macro>