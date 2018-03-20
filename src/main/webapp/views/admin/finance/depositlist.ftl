<#include "../base/_base.ftl">
<#macro title>
财务管理
</#macro>
<#macro content>
<!--suppress ALL -->
<input type="hidden" value="${(type)!}" id="orderType"/>
<ul class="nav nav-tabs" style="margin-top: 5px;margin-left: 5px;">
    <li class="tabs-select">
        <a href="${ctx}/admin/finance/1">财务明细</a>
    </li>
<#--<li class="tabs-select">-->
<#--<a href="${ctx}/admin/finance/2">财务明细(根据服务人员公司)</a>-->
<#--</li>-->
    <li class="tabs-select">
        <a href="${ctx}/admin/finance/depositlist/3">服务人员预存款</a>
    </li>
    <li class="tabs-select">
        <a href="${ctx}/admin/finance/depositlist/4">客户预存款</a>
    </li>
    <li class="tabs-select">
        <a href="${ctx}/admin/finance/loglist/5">服务人员充值记录</a>
    </li>
    <li class="tabs-select">
        <a href="${ctx}/admin/finance/loglist/6">客户账户明细</a>
    </li>
</ul>
<div class="panel-body">
    <div class="table-responsive" style="overflow:auto;">
        <form id="searchForm_">
            <table class="table table-bordered table-striped">
                <tr>
                    <td width="80">
                        <div class="form-group">
                            <label for="s-dmc.phone-LIKE" class="control-label">账户:</label>
                            <input name="s-dmc.phone-LIKE"/>
                        </div>
                    </td>
                    <#if hasRole("super_admin")||hasPermission("select-company")>
                        <td>
                            <label class="control-label">所属公司:</label>
                            <select class="select2 select-company" name="s-company-EQ"
                                    style="width: 100px;">
                                <option value="">请选择</option>
                            </select>
                        </td>
                    </#if>
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

                    <div class="countSum label label-primary" data-url="${ctx}/admin/total/yucunkuantongji/${type}"
                         data-filter="true">
                        <span>搜索总预存款：</span>
                        <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                        <span></span>
                        <span>元</span>
                    </div>
                    <div class="countSum label label-primary" data-url="${ctx}/admin/total/yucunkuang/${type}"
                         data-filter="true">
                        <span>搜索账号余额：</span>
                        <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                        <span></span>
                        <span>元</span>
                    </div>

                    <div class="countSum label label-primary" data-url="${ctx}/admin/total/yucunkuantongji/${type}"
                         data-filter="false">
                        <span>总预存款：</span>
                        <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                        <span></span>
                        <span>元</span>
                    </div>
                    <div class="countSum label label-primary" data-url="${ctx}/admin/total/yucunkuang/${type}"
                         data-filter="false">
                        <span>全部账号余额：</span>
                        <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                        <span></span>
                        <span>元</span>
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <div class="table-responsive" style="overflow:auto;">
        <table id="order-list" cellspacing="0" class="hover table table-striped table-bordered"
               width="200%">
            <thead>
            <tr>
                <th>姓名</th>
            <#--<th>账户</th>-->
                <th>手机号</th>
                <th>总预存款(元)</th>
                <th>余额(元)</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
</#macro>
<#macro javascript>
<script id="action_tpl" type="text/html">
    <button type="button" class="btn btn-success btn-xs ">审核</button>
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/deposit.js?_${.now?string("hhmmss")}"></script>
</#macro>