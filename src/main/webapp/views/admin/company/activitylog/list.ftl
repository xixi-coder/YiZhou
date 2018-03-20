<#include "../../base/_base.ftl">
<#macro title>
公司活动记录
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>公司活动记录</h4>
    </div>
    <div class="panel-body">
        <div class="table-responsive" style="overflow:auto;">
            <form id="searchForm_">
                <table class="table table-bordered table-striped">
                    <tr>
                        <#if hasRole("super_admin")||hasPermission("select-company")>
                            <td>
                                <label class="control-label">所属公司:</label>
                                <select class="select2 select-company" name="s-dca.company-EQ"
                                        style="width: 100px;">
                                    <option value="">请选择</option>
                                </select>
                            </td>
                        </#if>
                        <td>
                            <label for="s-name-LIKE" class="control-label">时间:</label>
                            <input type="hidden" name="s-dca.create_time-BETWEEN" value="" id="timeSearch">
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="start"
                                   placeholder="如:1992-11-11">至
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="end"
                                   placeholder="如:1992-11-11">
                        </td>
                        <td>
                            <label class="control-label">类型:</label>
                            <select class="select2" name="s-dca.type-EQ"
                                    style="width: 160px;">
                                <option value="ISNOTNULL">请选择</option>
                                <option value="1">三级分销奖励的金额</option>
                                <option value="2">活动赠送的优惠券</option>
                                <option value="3">定向赠送</option>
                                <option value="4">兑换码领取</option>
                                <option value="5">活动奖励余额</option>
                                <option value="6">司机奖励金额</option>
                            </select>
                        </td>
                        <td>
                            <button type="button" class="btn btn-info search">搜索</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="table-responsive" style="overflow:auto;height: 30px;">
            <table>
                <tr>
                    <td>
                        <div class="countSum label label-primary" data-url="${ctx}/admin/total/companyActivityAmount"
                             data-filter="false">
                            <span>剩余费用：</span>
                            <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                            <span></span>
                            <span>元</span>
                        </div>
                    </td>
                    <td>
                        <div class="countSum label label-primary" data-url="${ctx}/admin/total/companyActivityUsed"
                             data-filter="ture">
                            <span>真实使用费用：</span>
                            <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                            <span></span>
                            <span>元</span>
                        </div>
                    </td>
                    <td>
                        <div class="countSum label label-primary" data-url="${ctx}/admin/total/companyActivityGrant"
                             data-filter="ture">
                            <span>已发放费用：</span>
                            <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                            <span></span>
                            <span>元</span>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        <div class="table-responsive">
            <table id="activitylog-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>活动类型</th>
                    <th>费用(元)</th>
                    <th>优惠券类型</th>
                    <th>备注</th>
                    <th>时间</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/company_activity.js?_${.now?string("hhmmss")}"></script>
</#macro>