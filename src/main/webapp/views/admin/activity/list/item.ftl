<#include "../../base/_base.ftl">
<#macro title>
活动管理
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/activity/list">活动管理</a></li>
    <li class="active"><a>活动<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="activity-list">
    <div class="panel-heading">
        <h4 class="panel-title">活动管理 </h4>
    </div>
    <form class="form-horizontal" method="post" role="form" autocomplete="off"
          id="activity_form">
        <div class="panel-body">
            <div class="table-responsive" style="overflow:auto;">
                <input type="hidden" name="activity.id" value="${(activity.id)!}" id="id">
                <input type="hidden" name="action" value="${action}">
                <table class="table table-bordered table-striped">
                    <tr>
                        <td><label for="name" class="control-label">活动名称:</label></td>
                        <td>
                            <input type="text" name="activity.name" value="${(activity.name)!}"
                                   class="form-control n-invalid"
                                   id="name"
                                   placeholder="活动名称">
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>

                        <td><label for="event" class="control-label">活动事件:</label></td>
                        <td>
                            <select id="event" class="select2" style="width: 90%;" name="activity.event"
                                    data-value="${(activity.event)!}">
                                <option value="0">请选择</option>
                                <option value="1">注册</option>
                                <option value="2">首次下单</option>
                                <option value="3">下单</option>
                                <option value="4">打开APP</option>
                            </select>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="name" class="control-label">活动开始时间:</label></td>
                        <td>
                            <input type="text" name="start_time"
                                   value="${(activity.start_time?string("yyyy-MM-dd HH:mm"))!}"
                                   class="form-control datetimepicker-input"
                                   id="start_time" readonly
                                   placeholder="活动开始时间">
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>

                        <td><label for="content" class="control-label">活动结束时间:</label></td>
                        <td>
                            <input type="text" name="end_time"
                                   value="${(activity.end_time?string("yyyy-MM-dd HH:mm"))!}"
                                   class="form-control datetimepicker-input"
                                   id="end_time" readonly
                                   placeholder="活动结束时间">
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>

                    <tr>
                        <td><label for="content" class="control-label">活动描述:</label></td>
                        <td colspan="4">
                        <textarea name="activity.description" class="form-control n-invalid" rows="5"
                                  placeholder="活动描述" style="height: 80px">${(activity.description)!}</textarea>
                        </td>
                        <td>
                        </td>
                    </tr>
                    <tr class="service_type">
                        <td><label for="service_type" class="control-label">下单类型:</label></td>
                        <td>
                            <#list serviceTypes as item>
                                <label><input type="checkbox" value="${item.id}" name="c1"
                                    <#list types as i ><#if i=="${item.id}">checked</#if></#list>>${item.name}</label>
                            </#list>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                        <td><label for="c2" class="control-label">支付方式:</label></td>
                        <td>
                            <label><input type="checkbox" value="1" name="c2"
                                <#list ptypes as item ><#if item=="1">checked</#if></#list>>支付宝</label>
                            <label><input type="checkbox" value="2" name="c2"
                                <#list ptypes as item ><#if item=="2">checked</#if></#list>>代收</label>
                            <label><input type="checkbox" value="3" name="c2"
                                <#list ptypes as item ><#if item=="3">checked</#if></#list>>余额</label>
                            <label><input type="checkbox" value="4" name="c2"
                                <#list ptypes as item ><#if item=="4">checked</#if></#list>>微信</label>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                    <tr class="service_type">
                        <td><label for="c4" class="control-label">订单来源:</label></td>
                        <td>
                            <label><input type="checkbox" value="1" name="c4"
                                <#list fromTypes as item ><#if item=="1">checked</#if></#list>>APP下单</label>

                            <label><input type="checkbox" value="2" name="c4"
                                <#list fromTypes as item ><#if item=="2">checked</#if></#list>>微信预约</label>
                            <label><input type="checkbox" value="3" name="c4"
                                <#list fromTypes as item ><#if item=="3">checked</#if></#list>>电话预约</label>
                            <label><input type="checkbox" value="4" name="c4"
                                <#list fromTypes as item ><#if item=="4">checked</#if></#list>>服务人员补单</label>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                        <td><label for="c3" class="control-label">提醒方式:</label></td>
                        <td>
                            <label><input type="checkbox" value="1" name="c3"
                                <#list nTypes as item ><#if item=="1">checked</#if></#list>>短信</label>

                            <label><input type="checkbox" value="2" name="c3"
                                <#list nTypes as item ><#if item=="2">checked</#if></#list>>推送</label>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>

                    <#if hasRole("super_admin")||hasPermission("select-company")>
                        <tr>

                            <td><label for="company" class="control-label">所属公司:</label></td>
                            <td>
                                <select id="company" class="select2 select-company" data-value="${(activity.company)!}"
                                        name="activity.company" style="width: 100%;">
                                    <option value="0">请选择</option>
                                </select>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>

                            <td colspan="3"></td>
                        </tr>
                    </#if>
                    <tr>

                        <td><label for="type" class="control-label">返利方式:</label></td>
                        <td>
                            <select class="select2" id="type" style="width: 90%;" name="activity.type"
                                    data-value="${(activity.type)!}">
                                <option value="">请选择</option>
                                <option value="1">现金</option>
                                <option value="2">优惠券</option>
                            </select>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                        <td class="couponSelect"><label for="coupon" class="control-label">活动优惠券:</label></td>
                        <td class="couponSelect">
                            <select id="e_spe2" name="activity.coupon" class="select2" style="width: 100%;"
                                    data-value="${(activity.coupon)!}">
                            <#-- <option value="">请选择</option>
                                <#list coupons as item>
                                    <option value="${item.id}"
                                            <#if activity.coupon??&&activity.coupon==item.id>selected="selected"</#if>>${(item.coupon_title)!}</option>
                                </#list>-->
                            </select>
                            <#--<input type="hidden" id="coupons" name="coupons" value="0">-->
                            <#--<label id="e_spe2"></label>-->
                        </td>

                        <td class="moneyInput"><label for="rebate" class="control-label">返利金额(元):</label></td>
                        <td class="moneyInput">
                            <input type="text" name="activity.rebate" value="${(activity.rebate?c)!}"
                                   class="form-control n-invalid"
                                   id="rebate"
                                   placeholder="100">
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="6" style="text-align: center;">
                            <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                            <button class="btn btn-success" type="submit">提交</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </form>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/activity_item.js?_${.now?string("hhmmss")}"></script>
</#macro>