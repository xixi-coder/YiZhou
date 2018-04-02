<#include "../base/_base.ftl">
<#macro title>
订单管理
</#macro>
<#macro content>


<!--suppress ALL -->
<input type="hidden" value="${(type)!}" id="orderType" xmlns="http://java.sun.com/jsf/html"/>
<ul class="nav nav-tabs">
    <#list serviceTypes as item>
        <li <#if type==item.id>class="active"</#if>>
            <a href="${ctx}/admin/order?type=${item.id}">${item.name}</a>
        </li>
    </#list>
</ul>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>订单管理</h4>
        <div class="time"></div>
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
                            <label for="s-dmi.phone-LIKE" class="control-label">客户手机号码:</label>
                            <input type="hidden" name="type" value="${(type)!}"/>
                            <input name="s-dmi.phone-LIKE"/>
                            <label for="s-ddi.phone-LIKE" class="control-label">服务人员手机号码:</label>
                            <input name="s-ddi.phone-LIKE"/>
                            <label for="s-name-LIKE" class="control-label">下单时间:</label>
                            <input type="hidden" name="s-ddo.create_time-BETWEEN" value="" id="timeSearch">
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="start"
                                   placeholder="如:1992-11-11">至
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="end"
                                   placeholder="如:1992-11-11">
                            <label for="s-no-LIKE" class="control-label">订单号:</label>
                            <input name="s-no-LIKE"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="control-label">状态:</label>
                            <select name="s-ddo.status-EQ" style="height: 26px;" id="pay1">
                                <option value="" selected="selected">全部</option>
                                <option value="1">新订单</option>
                                <#if type==5>
                                    <option value="8">已接单</option>
                                <#else >
                                    <option value="2">已接单</option>
                                </#if>
                                <option value="3">执行中</option>
                                <option value="4">到达终点</option>
                                <option value="5">已支付</option>
                                <option value="6">已销单</option>
                                <option value="7">司机开始等待</option>
                            </select>

                            <label class="control-label">支付状态:</label>
                            <select name="s-ddo.pay_status-EQ" style="height: 26px;" id="pay2">
                                <option value="">全部</option>
                                <option value="5">已支付</option>
                                <option value="6">未支付</option>
                            </select>


                            <#if hasRole("super_admin")||hasPermission("select-company")>
                                <label class="control-label">所属公司:</label>
                                <select class="select2 select-company" name="s-ddo.company-EQ"
                                        style="width: 100px;">
                                    <option value="">请选择</option>
                                </select>
                            </#if>
                            <#if hasPermission("union_parent")>
                                <label class="control-label">联盟:</label>
                                <select class="select2 select-company" name="s-ddi.creater-EQ"
                                        style="width: 100px;">
                                    <option value="">请选择</option>
                                </select>
                            </#if>
                        </td>
                        <td>
                            <button class="btn btn-info search" type="button">搜索</button>
                            <#if hasPermission("dingdandaochu")>
                                <button id="tijiao" class="btn btn-info excel" type="button">导出订单</button>
                            </#if>
                            <#if hasPermission("huiyuandaochu")>
                                <button type="button" class="btn btn-primary button-margin"
                                        onclick="javascript:method1('order-list')">
                                    <i class="fa fa-file-excel-o"></i>导出此页面订单
                                </button>
                            </#if>
                            <#if "${type!}"=="2">
                                <#if hasPermission("order_create")>
                                    <button type="button" class="btn btn-info"
                                            onclick="window.open('${ctx}/admin/order/createitem', '_blank', 'height=770,width=1220,scrollbars=yes,resizable=yes,alwaysRaised=yes,toolbar=no');">
                                        <b>+</b>&nbsp;&nbsp;添加订单
                                    </button>
                                </#if>
                            </#if>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <input type="hidden" value="4" name="s-ddo.status-EQ" id="pay">
        <div class="table-responsive" style="overflow:auto;height: 30px;">
            <table>
                <tr>
                    <td>
                        <div class="countSum label label-primary" data-url="${ctx}/admin/total/dingdansousuogzonge"
                             data-filter="true">
                            <span>搜索总额：</span>
                            <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                            <span></span>
                            <span>元</span>
                        </div>
                        <div class="countSum label label-primary"
                             data-url="${ctx}/admin/total/dingdanzonge?type=${(type)!}"
                             data-filter="false">
                            <span>总额：</span>
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
                    <th width="10px"></th>
                    <th width="100px;">操作</th>
                    <th>订单编号</th>
                    <th>所属公司</th>
                    <th>状态</th>
                    <th>预约时间</th>
                    <th>预约地</th>
                    <th>客户名称</th>
                    <th>客户电话</th>
                    <th>目的地</th>
                    <th>服务人员</th>
                    <th>服务人员电话</th>
                    <th>来源</th>
                    <th>结算时间</th>
                    <th>里程</th>
                    <th>结算金额</th>
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
            <#if hasPermission("dingdanbianji")>
                <li><a href="${ctx}/admin/order/lockup/{{d.id}}">编辑</a></li></#if>

            <#if hasPermission("dingdanbianji")>{{# if(d.status==5){ }}
                <li><a href="${ctx}/admin/order/getAppraiseByNo?no={{d.no}}">评价</a></li>
                {{#} }}</#if>
            <#if hasPermission("dingdanbianji")>{{# if(d.status==5){ }}
                <li><a href="${ctx}/admin/order/getComplainByNo?no={{d.no}}">投诉</a></li>
                {{#} }}</#if>
            <#if hasPermission("dingdanshanchu")>
                <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除<span class="warning{{d.id}}"
                                                                                            style="color: #880000"
                                                                                            onmouseover="warning_message({{d.id}})"
                                                                                            data-id="{{d.id}}"> ★</span></a>
                </li></#if>
            <#if hasPermission("dingdanxiaodan")>{{# if(d.status!=6&&d.status!=5&&d.status!=9){ }}
                <li><a class="cancel" href="javascript:void(0);" data-id="{{d.id}}" stu-id="{{d.status}}">销单</a></li>
                {{#} }}</#if>
            <#if hasPermission("zhuijiadingdan")>{{# if(d.status!=6&&d.status!=5&&d.status!=9){ }}
                <li><a class="add" href="javascript:void(0);" data-id="{{d.id}}">追加</a></li>
                {{#} }}</#if>
            <#if hasPermission("zhuijiadingdan")>{{# if(d.status==1&&d.serviceType==2){ }}
                <li><a href="${ctx}/admin/order/change/{{d.id}}" target="_blank"">改派</a></li>
                {{#} }}</#if>
            <#if hasPermission("dispatchOrder")>{{# if(d.status==1&&d.serviceType==6){ }}
                <li><a href="${ctx}/admin/order/dispatchOrder/{{d.id}}" target="_blank"">城际专线派单</a></li>
                {{#} }}</#if>
            <#if hasPermission("dispatchOrder")>{{# if(d.status==1&&d.serviceType==7){ }}
                <li><a href="${ctx}/admin/order/dispatchOrder/{{d.id}}" target="_blank"">航空专线派单</a></li>
                {{#} }}</#if>
            <li><a class="select-push-list" href="javascript:void(0);" data-id="{{d.id}}" onclick='find_push_list({{d.id}})'>推送列表</a></li>
        </ul>
    </div>


    <table id="HiddenList" cellspacing="0" class="hover table table-striped table-bordered"
           width="100%" hidden="hidden">
        <thead>
        <tr>
            <th>昵称</th>
            <th>手机号</th>
            <th>操作状态</th>
        </tr>
        </thead>
        <tbody>
        </tbody>


</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/order.js?_${.now?string("hhmmss")}"></script>
<script type="text/javascript">
    // countDown();
    function warning_message(id) {
        //tips层
        layer.tips('此项为敏感项，请谨慎操作！', '.warning' + id, {tips: [3, "red"]});
    }



</script>
</#macro>