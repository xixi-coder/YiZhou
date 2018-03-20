<#include "../../base/_base.ftl">
<#macro title>
服务人员奖励
</#macro>
<#macro content>
<link href="${ctx}/static/css/select-item.css" rel="stylesheet"/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>服务人员奖励</h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="smstmp_form">
            <table class="table table-bordered table-striped">
                <#if hasRole("super_admin")||hasPermission("select-company")>
                    <tr>
                        <td><label for="company" class="control-label">服务人员所属公司:</label></td>
                        <td>
                            <div class="col-sm-7">
                                <select id="company" class="select2 select-company" name="company"
                                        style="width: 100%;">

                                </select>
                            </div>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                </#if>
                <tr>
                    <td><label for="orderType" class="control-label">订单类型:</label></td>
                    <td>
                        <div class="col-sm-7">
                            <select class="select2" name="orderType" id="orderType"
                                    style="width: 100%;">
                                <option value="0">请选择</option>
                                <#list serviceTypes as item>
                                <#--专车-->
                                    <#if "${item.id}"=="1">
                                        <option value="37">专车-豪华型</option>
                                        <option value="43">专车-舒适型</option>
                                        <option value="45">专车-商务型</option>
                                    </#if>
                                <#--代驾-->
                                    <#if "${item.id}"=="2">
                                        <option value="40">代驾</option>
                                    </#if>
                                <#--出租车-->
                                    <#if "${item.id}"=="3">
                                        <option value="42">出租车</option>
                                    </#if>
                                <#--快车-->
                                    <#if "${item.id}"=="4">
                                        <option value="44">快车</option>
                                    </#if>
                                </#list>
                            </select>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="minOrderCount" class="control-label">至少完成:</label></td>
                    <td>
                        <div class="col-sm-7">
                            <div class="input-group">
                                <input id="minOrderCount" type="number" class="form-control">
                                <span class="input-group-addon">单</span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="minMoneyEach" class="control-label">每单金额不低于:</label></td>
                    <td>
                        <div class="col-sm-7">
                            <div class="input-group">
                                <input id="minMoneyEach" type="number" class="form-control">
                                <span class="input-group-addon">元</span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="company" class="control-label">完成期限:</label></td>
                    <td>
                        <div class="col-sm-3">
                            <input id="startTime" class="form-control datetimepicker-input" value=""
                                   name="start_time"/>

                        </div>
                        <div class="col-sm-1"><label for="company" class="control-label">至</label></div>
                        <div class="col-sm-3">

                            <input id="endTime" class="form-control datetimepicker-input" name="end_time" value=""/>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="rewardType" class="control-label">奖励方式:</label></td>
                    <td>
                        <input value="1" type="radio" checked="checked" name="rewardType">固定金额&nbsp;&nbsp;&nbsp;
                        <input value="2" type="radio" name="rewardType">百分比金额
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td>
                    </td>
                    <td>百分比金额为所有满足条件的订单总金额乘以百分比</td>
                    <td></td>
                </tr>
                <tr id="rebateRe" style="display: none;">
                    <td><label for="rebate" class="control-label">返现比例:</label></td>
                    <td>
                        <div class="col-sm-7">
                            <div class="input-group">
                                <input type="number" id="rebate" class="form-control">
                                <span class="input-group-addon">%</span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr id="moneyRe">
                    <td><label for="money" class="control-label">返现金额:</label></td>
                    <td>
                        <div class="col-sm-7">
                            <div class="input-group">
                                <input type="number" id="money" class="form-control">
                                <span class="input-group-addon">元</span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td>
                    </td>
                    <td>
                        <button type="button" id="screen" class="btn btn-success">筛选</button>
                    </td>
                    <td>

                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/html" id="driverHtml">
    <div class="notify-container">
        <div class="notify-item-wrapper">
            <div class="notify-item notify-type-notice" style="">
                <div class="notify-item-close"></div>
                <input value="{{d.driverInfo}}" type="hidden" name="driverInfo"/>
                <p>{{d.driverRealName}}-{{d.phone}}</p></div>
        </div>
    </div>
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/reward.js?_${.now?string("hhmmss")}"></script>
</#macro>