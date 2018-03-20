<#include "../../base/_base.ftl">
<#macro title>
顺风车行程列表
</#macro>
<#macro content>
<!--suppress ALL -->
<input type="hidden" value="${(type)!}" id="orderType" xmlns="http://java.sun.com/jsf/html"/>
<ul class="nav nav-tabs">
    <li>
        <a href="${ctx}/admin/sys/record/0" class="active">司机发布行程列表</a>
    </li>
    <li>
        <a href="${ctx}/admin/sys/record/1">乘客发布行程列表</a>
    </li>
</ul>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>顺风车行程列表</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <div class="table-responsive" style="overflow:auto;">
            <form id="searchForm_">
                <table class="hover table table-striped table-bordered" cellspacing="0" width="200%">
                    <tr>
                        <td colspan="2">
                            <#if type==1>
                                <label for="s-dm.phone-LIKE" class="control-label">客户手机号码:</label>
                                <input name="s-dm.phone-LIKE"/>
                            </#if>
                            <input type="hidden" name="type" value="${(type)!}"/>
                            <label for="s-dd.phone-LIKE" class="control-label">服务人员手机号码:</label>
                            <input name="s-dd.phone-LIKE"/>
                            <label for="s-name-LIKE" class="control-label">发布时间:</label>
                            <input type="hidden" name="s-dt.create_time-BETWEEN" value="" id="timeSearch">
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="start"
                                   placeholder="如:1992-11-11">至
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="end"
                                   placeholder="如:1992-11-11">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="control-label">状态:</label>
                            <select name="s-ddo.status-EQ" style="height: 26px;" id="pay1">
                                <option value="" selected="selected">全部</option>
                                <option value="1">新行程</option>
                                <option value="2">执行中行程</option>
                                <option value="6">已出发的行程</option>
                                <option value="3">结束行程</option>
                                <option value="4">取消的行程</option>
                            </select>

                            <#if hasRole("super_admin")||hasPermission("select-company")>
                                <label class="control-label">所属公司:</label>
                                <select class="select2 select-company" name="s-dcompany-EQ"
                                        style="width: 100px;">
                                    <option value="">请选择</option>
                                </select>
                            </#if>
                        </td>
                        <td>
                            <button class="btn btn-info search" type="button">搜索</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>

        <div class="table-responsive" style="overflow:auto;">
            <table id="order-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="200%">
                <thead>
                <tr>
                    <th width="10px"></th>
                    <th>行程状态</th>
                    <th>起始时间</th>
                    <th>结束时间</th>
                    <th>创建时间</th>
                    <th>出发城市</th>
                    <th>终点城市</th>
                    <th>出发位置</th>
                    <th>终点位置</th>
                    <th>服务人员电话</th>
                    <th>客户电话</th>
                    <th>拼车价</th>
                    <th>包车价</th>
                    <th>寄货价</th>
                    <th>行程类型</th>
                    <th width="100px;">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
</#macro>
<#macro javascript>
<script id="action_tpl" type="text/html">
    <div class="btn-group" style="width:64px;">
        <button type="button" class="btn btn-success btn-xs ">操作</button>
        <button type="button" class="btn btn-success btn-xs  dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only"></span></button>
        <ul class="dropdown-menu success btn-xs " role="menu">
            <#if hasPermission("xingchengshanchu")>
                <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除</a></li></#if>
        </ul>
    </div>
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/record.js?_${.now?string("hhmmss")}"></script>
</#macro>