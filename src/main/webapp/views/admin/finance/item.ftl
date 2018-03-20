<#include "../base/_base.ftl">
<#macro title>
订单查看
</#macro>
<link href="${ctx}/static/css/sacss.css" rel="stylesheet"/>
<link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/company">订单管理</a></li>
    <li class="active"><a>订单查看</a></li>
</ol>
    <ul class="timeline" id="timeline">
        <li class="li <#if order.status == 1>complete</#if>">
            <#if orderLog??&&orderLog?size gt 0>
                <#list orderLog as item>
                    <#if item_index==0>
                        <div class="timestamp">
                            <span class="date">${(item.operation_time?string("yyyy/MM/dd HH:mm"))}<span>
                        </div>
                        <div class="status">
                            <h4> 客户下单 </h4>
                        </div>
                        <#break>
                    <#elseif item_has_next>

                    <#else>
                        <div class="timestamp">
                            <span class="date">&nbsp;<span>
                        </div>
                        <div class="status">
                            <h4> 客户下单 </h4>
                        </div>
                        <#break>
                    </#if>

                </#list>
            </#if>
        </li>
        <li class="li <#if order.status == 2>complete</#if>">
            <#if orderLog??&&orderLog?size gt 0>
                <#list orderLog as item>
                    <#if item_index==1>
                        <div class="timestamp">
                            <span class="date">${(item.operation_time?string("yyyy/MM/dd HH:mm"))}<span>
                        </div>
                        <div class="status">
                            <h4> 已接单 </h4>
                        </div>
                    <#elseif item_has_next>

                    <#else>
                        <div class="timestamp">
                            <span class="date">&nbsp;<span>
                        </div>
                        <div class="status">
                            <h4> 已接单 </h4>
                        </div>
                        <#break>
                    </#if>
                </#list>
            </#if>
        </li>
        <li class="li <#if order.status == 3>complete</#if>">
            <#if orderLog??&&orderLog?size gt 0>
                <#list orderLog as item>
                    <#if item_index==2>
                        <div class="timestamp">
                            <span class="date">${(item.operation_time?string("yyyy/MM/dd HH:mm"))}<span>
                        </div>
                        <div class="status">
                            <h4> 开始服务 </h4>
                        </div><#break>
                    <#elseif item_has_next>

                    <#else>
                        <div class="timestamp">
                            <span class="date">&nbsp;<span>
                        </div>
                        <div class="status">
                            <h4> 开始服务 </h4>
                        </div><#break>
                    </#if>
                </#list>
            </#if>
        </li>
        <li class="li <#if order.status == 4>complete</#if>">
            <#if orderLog??&&orderLog?size gt 0>
                <#list orderLog as item>
                    <#if item_index==3>
                        <div class="timestamp">
                            <span class="date">${(item.operation_time?string("yyyy/MM/dd HH:mm"))}<span>
                        </div>
                        <div class="status">
                            <h4> 已确认 </h4>
                        </div><#break>
                    <#elseif item_has_next>

                    <#else>
                        <div class="timestamp">
                            <span class="date">&nbsp;<span>
                        </div>
                        <div class="status">
                            <h4> 已确认 </h4>
                        </div><#break>
                    </#if>
                </#list>
            </#if>
        </li>
        <li class="li <#if order.status == 5>complete</#if>">
            <#if orderLog??&&orderLog?size gt 0>
                <#list orderLog as item>
                    <#if item_index==4>
                        <div class="timestamp">
                            <span class="date">${(item.operation_time?string("yyyy/MM/dd HH:mm"))}<span>
                        </div>
                        <div class="status">
                            <h4> 已支付 </h4>
                        </div><#break>
                    <#elseif item_has_next>

                    <#else>
                        <div class="timestamp">
                            <span class="date"> &nbsp;<span>
                        </div>
                        <div class="status">
                            <h4> 已支付 </h4>
                        </div><#break>
                    </#if>
                </#list>
            </#if>
        </li>
    </ul>
    <ul class="nav nav-tabs">
        <li class="active">
            <a href="#order-info" data-toggle="tab">订单跟踪</a>
        </li>
        <li class="">
            <a href="#map" data-toggle="tab">订单追踪</a>
        </li>
    </ul>
    <div class="tab-content padding" style="background: white;border:1px solid #ddd; height: 100%;">
        <div class="tab-pane fade active in" id="order-info">
            <div class="row padding">
                <div class="table-responsive col-sm-12 padding">
                    <table data-sortable="" style="border:1px solid #ddd;" class="table table-hover table-striped"
                           data-sortable-initialized="true">
                        <thead style="background: whitesmoke;">
                        <tr>
                            <th style="width: 20%">时间</th>
                            <th style="width: 80%" data-sortable="false">
                                跟踪时间
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                            <#if allOrderlogs??&&allOrderlogs?size gt 0>
                                <#list allOrderlogs as item>
                                <tr>
                                    <td>${(item.operation_time?string("MM月dd HH:mm:ss"))!}</td>
                                    <td><a>${(item.remark)!}</a></td>
                                </tr>
                                </#list>
                            </#if>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="row padding" style="margin-top: 5px;">
                <div class="col-md-12">
                    <div class="table-responsive col-md-6">
                        <table data-sortable="" style="border:1px solid #ddd;" class="table table-hover table-striped"
                               data-sortable-initialized="true">
                            <thead style="background: whitesmoke;">
                            <tr>
                                <th style="width: 20%">订单信息</th>
                                <th style="width: 80%" data-sortable="false">
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>创建时间</td>
                                <td>${(order.create_time)!}</td>
                            </tr>
                            <tr>
                                <td>订单类型</td>
                                <td>
                                    <#if order.type==1>
                                        专车
                                    <#elseif order.type==2>
                                        代驾
                                    <#elseif order.type==3>
                                        出租车
                                    </#if>
                                    <span class="label label-success">
                                        <#if order.from_type==1>
                                            APP
                                        <#elseif order.from_type==2>
                                            微信
                                        <#elseif order.from_type==3>
                                            电话预约
                                        <#elseif order.from_type==4>
                                            服务人员补单
                                        </#if>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td>推荐人</td>
                                <td>${(order.introducer_phone)!}<span
                                        class="label label-success">${(order.introducer_name)!}</span></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="table-responsive col-md-6">
                        <table data-sortable="" style="border:1px solid #ddd;" class="table table-hover table-striped"
                               data-sortable-initialized="true">
                            <thead style="background: whitesmoke;">
                            <tr>
                                <th style="width: 20%">付款信息</th>
                                <th style="width: 80%" data-sortable="false">
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>等候时间</td>
                                <td>${(order.wait_time)!}分钟</td>
                            </tr>
                            <tr>
                                <td>消耗时间</td>
                                <td>${(order.consume_time)!}分钟</td>
                            </tr>
                                <#if payLogs??&&payLogs?size gt 0>
                                    <#list payLogs as item>
                                    </#list>
                                </#if>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
            <div class="row padding" style="margin-top: 5px;">
                <div class="table-responsive col-md-12" style="text-align: center;">
                    <a href="#" onClick="javascript :history.back(-1);" class="btn btn-default">返回</a>
                </div>
            </div>
        </div>
        <div class="tab-pane fade in" id="map">
            <div class="row padding" style="margin-top: 5px;">
                <div class="table-responsive col-md-12" style="text-align: left;height: 35px;">
                    <div class="col-md-2" style="width:150px;">行驶中的轨迹:</div>
                    <div class="col-md-5" style="width:80px;height:5px;margin-top:9px;background-color:red;"></div>
                    <span class="col-md-1" id="run_distance_span">${(order.real_distance)!}公里</span>
                    <div class="col-md-2" style="width:150px;">等待中的轨迹:</div>
                    <div class="col-md-5" style="width:80px;height:5px;margin-top:9px;background-color:#ff8a18;"></div>
                    <span class="col-md-1" id="wait_distance_span">${(order.wait_distance)!}公里</span>
                </div>
            </div>
            <div class="row padding" style="margin-top: 5px;">
                <div class="col-md-9">
                    <div class="widget" style="border: 1px solid #ddd;">
                        <div class="panel-heading">
                            <h2>轨迹回放</h2>
                            <div class="additional-btn">
                                <a id="start" href="javascript:void(0);" class="btn btn-success btn-sm">开始回放</a>
                                <a id="reset" href="javascript:void(0);" class="btn btn-info btn-sm">重置</a>
                                <a id="ranging" href="javascript:void(0);" class="btn btn-default btn-sm">测距</a>
                            </div>
                        </div>
                        <div class="panel-body">
                            <div class="row padding" style="margin-top: 5px;">
                                <div id="adMap" style="height: 500px;margin-left: 18px;width: 96%;">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="table-responsive col-md-3">
                    <table data-sortable="" style="border:1px solid #ddd;" class="table table-hover table-striped"
                           data-sortable-initialized="true">
                        <thead style="background: whitesmoke;">
                        <tr>
                            <th style="width: 70%">定位时间</th>
                            <th style="width: 30%">类型
                            </th>
                        </tr>
                        </thead>
                        <tbody id="contentBody">
                        <tr>
                            <td colspan="2" style="text-align: center;">GPS定位精准</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</#macro>
<#macro javascript>
<script id="tr" type="text/html">
    <tr>
        <td>{{d.recovice_time}}</td>
        <td><span class="label label-success">{{d.type}}</span></td>
    </tr>

</script>
<script src="http://webapi.amap.com/maps?v=1.3&key=3847e08227b5479aa1596a7a8b944026"></script>
<script type="text/javascript">
    var orderId = ${order.id};
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/order_item.js?_${.now?string("hhmmss")}"></script>
</#macro>