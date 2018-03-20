<#include "../base/_base.ftl">
<#macro title>
订单查看
</#macro>
<link href="${ctx}/static/css/timepoint/timepoint.css" rel="stylesheet"/>
<link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/order">订单管理</a></li>
    <li class="active"><a>订单查看</a></li>
</ol>
<div class="col-sm-12">
    <div class="schedule-box">
        <ul>
            <li class="sc-text1 <#if order.status == 1>l-Blue</#if>"><b>客户下单</b></li>
            <li class="sc-text1 <#if order.status == 2>l-Blue</#if>"><b>已接单</b></li>
            <li class="sc-text1 <#if order.status == 3>l-Blue</#if>"><b>开始服务</b></li>
            <li class="sc-text1 <#if order.status == 4>l-Blue</#if>"><b>已确认</b></li>
            <li class="sc-text1 <#if order.pay_status??&&order.pay_status == 5>l-Blue</#if>"><b>已支付</b></li>
        </ul>
        <div class="sc-t5"></div>
        <div class="sc-time">
                <span>
                    <#if orderLog??&&orderLog?size gt 0>
                        <#list orderLog as item>
                        <#if item_index==0&&item??>${(item.operation_time?string("yyyy/MM/dd HH:mm"))}
                            <#break>
                        <#else>
                        </#if>
                    </#list>
                    </#if>
                </span>
            <span>
                <#if orderLog??&&orderLog?size gt 0>
                        <#list orderLog as item>
                    <#if item_index==1&&item??>${(item.operation_time?string("yyyy/MM/dd HH:mm"))}
                        <#break>
                    <#else>
                    </#if>
                </#list>
                    </#if>
                </span>
            <span>
                <#if orderLog??&&orderLog?size gt 0>
                        <#list orderLog as item>
                    <#if item_index==2&&item??>${(item.operation_time?string("yyyy/MM/dd HH:mm"))}
                        <#break>
                    <#else>
                    </#if>
                </#list>
                    </#if>
                </span>
            <span>
                <#if orderLog??&&orderLog?size gt 0>
                        <#list orderLog as item>
                    <#if item_index==3&&item??>${(item.operation_time?string("yyyy/MM/dd HH:mm"))}
                        <#break>
                    <#else>
                    </#if>
                </#list>
                    </#if>
                </span>
            <span>
                <#if orderLog??&&orderLog?size gt 0>
                        <#list orderLog as item>
                    <#if item??&&item.action==5>${(item.operation_time?string("yyyy/MM/dd HH:mm"))}
                        <#break>
                    <#else>
                    </#if>
                </#list>
                    </#if>
                </span>
        </div>
    </div>
</div>
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
                            <td>订单号</td>
                            <td>${(order.no)!}</td>
                        </tr>
                        <tr>
                            <td>订单类型</td>
                            <td>
                                <#if order.serviceType==1>
                                    专车
                                <#elseif order.serviceType==2>
                                    代驾
                                <#elseif order.serviceType==3>
                                    出租车
                                <#elseif order.serviceType==4>
                                    快车（
                                    <#if order.pd_flag==false>
                                      不拼车
                                    <#elseif order.pd_flag==true>
                                        拼车
                                    </#if>
                                    ）
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
                            <td>预约时间</td>
                            <td>${(order.setouttime)!}<span
                                    class="label label-success">${(order.introducer_name)!}</span></td>
                        </tr>
                        <tr>
                            <td>预约地址</td>
                            <td>${(order.reservation_address)!}<span
                                    class="label label-success">${(order.introducer_name)!}</span></td>
                        </tr>
                        <tr>
                            <td>目的地</td>
                            <td>${(order.destination)!}<span
                                    class="label label-success">${(order.introducer_name)!}</span></td>
                        </tr>
                        <tr>
                            <td>客户姓名</td>
                            <td>${(order.memberInfo.real_name)!}<span
                                    class="label label-success">${(order.introducer_name)!}</span></td>
                        </tr>
                        <tr>
                            <td>客户手机</td>
                            <td>${(order.memberInfo.phone)!}<span
                                    class="label label-success">${(order.introducer_name)!}</span></td>
                        </tr>
                        <tr>
                            <td>下单手机</td>
                            <td>${(order.memberInfo.phone)!}<span
                                    class="label label-success">${(order.introducer_name)!}</span></td>
                        </tr>
                        <tr>
                            <td>所属公司</td>
                            <td>${(order.companyInfo.name)!}<span
                                    class="label label-success">${(order.introducer_name)!}</span></td>
                        </tr>
                        <tr>
                            <td>服务人员备注</td>
                            <td>${(order.remark)!}<span
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
                            <td>支付时间</td>
                            <td>
                            ${ (order.pay_time??) ?string((order.pay_time)!,"")}
                            </td>
                        </tr>
                        <tr>
                            <td>等候时间</td>
                            <td>
                            ${ (order.wait_time??) ?string((order.wait_time)!,"0")}分钟
                            </td>
                        </tr>
                        <tr>
                            <td>消耗时间</td>
                            <td>
                            ${ (order.consume_time??) ?string((order.consume_time)!,"0")}分钟
                            </td>
                        </tr>
                        <tr>
                            <td>实际距离</td>
                            <td>
                            ${ (order.distance??) ?string((order.real_distance?c)!,"0")}千米
                            </td>
                        </tr>
                        <tr>
                            <td>支付方式</td>
                            <td>
                                <#if order.pay_channel??&&order.pay_channel==1>支付宝支付
                                <#elseif order.pay_channel??&&order.pay_channel==2>司机代收
                                <#elseif order.pay_channel??&&order.pay_channel==3>余额支付
                                <#elseif order.pay_channel??&&order.pay_channel==4>微信支付
                                <#else>
                                </#if>
                            </td>
                        </tr>
                        <tr>
                            <td>订单金额</td>
                            <td>
                            ${ (order.real_pay??) ?string((order.real_pay?c)!,"0")}元
                            </td>
                        </tr>
                        <tr>
                            <td>起步价</td>
                            <td>
                            ${ (order.base_amount??) ?string((order.base_amount?c)!,"0")}元
                            </td>
                        </tr>
                        <tr>
                            <td>里程价格</td>
                            <td>
                            ${ (order.distance_amount??) ?string((order.distance_amount?c)!,"0")}元
                            </td>
                        </tr>
                        <tr>
                            <td>超时价格</td>
                            <td>
                            ${ (order.time_out_amount??) ?string((order.time_out_amount?c)!,"0")}元
                            </td>
                        </tr>

                        <tr>
                            <td>等待时间计费</td>
                            <td>
                            ${ (order.wait_amount??) ?string((order.wait_amount?c)!,"0")}元
                            </td>
                        </tr>
                        <tr>
                            <td>过路费</td>
                            <td>
                            ${ (order.road_toll??) ?string((order.road_toll?c)!,"0")}元
                            </td>
                        </tr>
                        <tr>
                            <td>远程费</td>
                            <td>
                            ${ (order.remote_fee??) ?string((order.remote_fee?c)!,"0")}元
                            </td>
                        </tr>
                        <tr>
                            <td>其他费用</td>
                            <td>
                            ${ (order.other_charges??) ?string((order.other_charges?c)!,"0")}元
                            </td>
                        </tr>
                        <tr>
                            <td>加价费用</td>
                            <td>
                            ${ (order.add_amount??) ?string((order.add_amount?c)!,"0")}元
                            </td>
                        </tr>
                        <tr>
                            <td>优惠券信息</td>
                            <td>
                                <#if order.couponInfo??>
                                    使用了优惠券，券号是${(order.couponInfo.no)!},优惠金额:${(order.couponInfo.amount?c)!}
                                <#else>
                                    未使用优惠券
                                </#if>
                            </td>
                        </tr>
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
                <span class="col-md-1" id="run_distance_span">${(order.real_distance?c)!}公里</span>
                <div class="col-md-2" style="width:150px;">等待中的轨迹:</div>
                <div class="col-md-5" style="width:80px;height:5px;margin-top:9px;background-color:#ff8a18;"></div>
                <span class="col-md-1" id="wait_distance_span">${(order.wait_distance?c)!}公里</span>
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
            <div class="table-responsive col-md-3" style="overflow-x: auto; overflow-y: auto; height: 650px;">
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
    <tr data-lat="{{d.lat}}" data-time="{{d.recovice_time}}" data-lon="{{d.lon}}" data-speed="{{d.speed}}"
        onmouseover="addMarker(this)"
        onmouseleave="remaoveMarker(this)" data-type="{{d.type}}">
        <td>{{d.recovice_time}}</td>
        <td><span class="label label-success">{{d.type}}</span></td>
    </tr>
</script>

<script id="tipContent" type="text/html">
    <div>
        <p>经度:{{d.lat}}</p>
        <p>纬度:{{d.lon}}</p>
        <p>速度:{{d.speed}}</p>
        <p>定位类型:{{d.type}}</p>
        <p>时间:{{d.time}}</p>
    </div>
</script>

<script src="http://webapi.amap.com/maps?v=1.3&key=3847e08227b5479aa1596a7a8b944026"></script>
<script type="text/javascript">
    var orderId = ${order.id};
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/order_item.js?_${.now?string("hhmmss")}"></script>
</#macro>