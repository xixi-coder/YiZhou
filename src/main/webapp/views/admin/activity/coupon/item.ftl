<#include "../../base/_base.ftl">
<#macro title>
优惠券管理
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/activity/coupon">优惠券管理</a></li>
    <li class="active"><a>优惠券<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-activity">
    <div class="panel-heading">
        <h4 class="panel-title">优惠券管理 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="coupon_form">
            <input type="hidden" name="coupon.id" value="${(coupon.id)!}" id="id">
            <input type="hidden" name="action" value="${action}">
            <div class="table-responsive" style="overflow:auto;">
                <table class="table table-bordered table-striped">
                    <tr>
                        <td><label for="name" class="control-label">优惠券标题:</label></td>
                        <td>
                            <input type="text" name="coupon.coupon_title" value="${(coupon.coupon_title)!}"
                                   class="form-control n-invalid"
                                   placeholder="优惠券标题">
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                        <td><label for="content" class="control-label">优惠类型:</label></td>
                        <td>
                            <select id="coupon_type" style="width: 25%;" class="select2" name="coupon.coupon_type">
                                <option value="1" <#if "${(coupon.coupon_type)!}"=="1">selected</#if>>普通券</option>
                                <option value="2" <#if "${(coupon.coupon_type)!}"=="2">selected</#if>>折扣券</option>
                                <#--<option value="3" <#if "${(coupon.coupon_type)!}"=="3">selected</#if>>免单券</option>-->
                            </select>
                        </td>

                        <td colspan="4">
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>

                    </tr>
                    <tr>
                        <td><label for="content" class="control-label base-amount">超过金额可用:</label></td>
                        <td>
                            <input type="text" name="coupon.base_amount" value="${(coupon.base_amount)!}"
                                   class="form-control n-invalid base-amount"
                                   placeholder="订单金额需要超过该金额才可以使用此优惠券">
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                        <td><label for="content" class="coupon-amount">优惠券金额(适用于普通券):</label></td>
                        <td>
                            <input type="text" name="coupon.amount" value="${(coupon.amount?c)!}"
                                   class="form-control n-invalid coupon-amount"
                                   placeholder="如：50">
                        </td>
                        <td colspan="4">
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="content" class="control-label percent-amount">最多可折扣金额(适用于折扣券):</label></td>
                        <td>
                            <input type="text" name="coupon.percent_amount" value="${(coupon.percent_amount)!}"
                                   class="form-control n-invalid percent-amount"
                                   placeholder="订单最多可以折扣金额（不填写代表不限制）">
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                        <td><label for="content" class="control-label coupon-percent">折扣(适用于折扣券):</label></td>
                        <td>
                            <input type="text" name="coupon.percent" value="${(coupon.percent)!}"
                                   class="form-control n-invalid coupon-percent"
                                   placeholder="请输入折扣（如：8，即为订单价格的百分之80）">
                        </td>
                        <td colspan="4">
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>


                    <tr>
                        <td><label for="name" class="control-label">优惠券开始时间:</label></td>
                        <td>
                            <input type="text" name="coupon.start_time"
                                   value="${(coupon.start_time?string("yyyy-MM-dd"))!}"
                                   class="form-control datepicker-input"
                                   id="start_time" readonly
                                   placeholder="优惠券开始时间">
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>

                        <td><label for="content" class="control-label">优惠券结束时间:</label></td>
                        <td>
                            <input type="text" name="coupon.end_time" value="${(coupon.end_time?string("yyyy-MM-dd"))!}"
                                   class="form-control datepicker-input"
                                   id="end_time" readonly
                                   placeholder="优惠券结束时间">
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                    <tr>
                        <#if hasRole("super_admin")||hasPermission("select-company")>
                            <td><label for="company" class="control-label">所属公司:</label></td>
                            <td>
                                <select id="company" data-value="${(coupon.company)!}" class="select2 select-company" name="coupon.company" style="width: 100%;">
                                    <option value="0">请选择</option>
                                </select>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </#if>
                        <td><label for="company" class="control-label">优惠券可用服务类型:</label></td>
                        <td width="40%">
                            <select id="sname" class="select2" name="coupon.service_type" style="width: 50%;">
                                <option value="0">全部类型可用</option>
                                <#if serviceType??&&serviceType?size gt 0>
                                    <#list serviceType as item>
                                        <option value="${(item.id)!}"
                                                <#if coupon.service_type??&&coupon.service_type==item.id>selected="selected"</#if>>${(item.name)!}</option>
                                    </#list>
                                </#if>
                            </select>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="content" class="control-label">优惠券说明:</label></td>
                        <td colspan="4">
                                <textarea type="text" name="coupon.coupon_desc" style="width: 100%;" rows="5"
                                          class="form-control n-invalid">${(coupon.coupon_desc)!}</textarea>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="5" style="text-align: center;">
                            <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                            <button class="btn btn-success" type="submit">提交</button>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/coupon_item.js?_${.now?string("hhmmss")}"></script>
</#macro>