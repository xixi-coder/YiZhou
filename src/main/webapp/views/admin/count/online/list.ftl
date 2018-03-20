<#include "../../base/_base.ftl">
<#macro title>
服务人员在线时间统计
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>服务人员在线时间统计</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <div class="table-responsive">
            <table cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <tbody>
                <tr>
                    <td width="20%">时间:<input class="datemonthpick" id="month" readonly="readonly"
                                              value="${currentMonth}"/></td>
                    <td width="20%">服务人员姓名:<input type="text" id="name"/></td>
                    <#if hasRole("super_admin")||hasPermission("select-company")>
                        <td width="30%">
                            <label for="real_name" class="control-label">公司选择:</label>
                            <select class="select2" name="company" id="companySelect"
                                    style="width: 30%;">
                                <option value="">请选择</option>
                                <#list companies as item>
                                    <option value="${(item.id)!}">${(item.name)!}</option>
                                </#list>
                            </select>
                        </td>
                    </#if>
                    <td colspan="3">
                        <button type="button" class="btn btn-success" id="search">搜索</button>
                        <a style="display: none;" id="exporHiden"></a>
                        <button type="button" class="btn btn-success" id="export">导出</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-responsive">
            <table id="onlineTime" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr id="countOfMonth">
                </tr>
                </thead>
                <tbody id="data">
                </tbody>
            </table>
        </div>
    </div>
    <div id="page"></div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/laypage/laypage.js"></script>
<script src="${ctx}/static/js/app/ExcelExport.js"></script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/driver_online.js?_${.now?string("hhmmss")}"></script>
</#macro>