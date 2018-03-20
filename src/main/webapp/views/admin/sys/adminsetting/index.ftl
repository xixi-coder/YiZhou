<#include "../../base/_base.ftl">
<#macro title>
系统设置
</#macro>
<#macro content>

<#--<div class="col-md-12">-->
<#--<ul class="nav nav-tabs navigation">-->
<#--&lt;#&ndash;<li class="li"><a href="${ctx}/admin/sys/adminsetting/1">系统设置</a></li>&ndash;&gt;-->
<#--&lt;#&ndash;<li class="li"><a href="${ctx}/admin/sys/adminsetting/2">第一阶段</a></li>&ndash;&gt;-->
<#--&lt;#&ndash;<li class="li"><a href="${ctx}/admin/sys/adminsetting/3">第二阶段</a></li>&ndash;&gt;-->
<#--&lt;#&ndash;<li class="li"><a href="${ctx}/admin/sys/adminsetting/4">第三阶段</a></li>&ndash;&gt;-->
<#--&lt;#&ndash;<li class="li"><a href="${ctx}/admin/sys/appsetting">APP设置</a></li>&ndash;&gt;-->
<#--</ul>-->
<#--</div>-->
<div class="panel panel-default">
    <div class="panel-heading">
        <h4><strong>系统设置</strong></h4>
    </div>
    <div class="panel-heading">
        <#if hasRole("super_admin")>
            <tr>
                <td width="30%">
                    <label for="real_name" class="control-label">公司选择:</label>
                    <select class="select2" name="company" id="SysSelectCompany" onchange="MM_jumpMenu('parent',this,0)"
                            style="width: 30%;">
                <#list companies as item>
                    <option value="${ctx}/admin/sys/adminsetting/1-${(item.id)!}" id="companyid">
                        ${(item.name)!}
                    </option>
                </#list>
                    </select>
                </td>
            </tr>
        </#if>
    </div>
    <div class="panel-body">

        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="adminsetting_form">
            <input type="hidden" name="adminSetting.id" value="${(adminSetting.id)!}">
            <table class="table table-bordered table-striped">
            <#if index==1>
                <tr>
                    <td><label for="order_recive_time" class="control-label">订单回收时间:</label></td>
                    <td>
                        <input type="text" name="adminSetting.order_recive_time" class="form-control n-invalid"
                               value="${(adminSetting.order_recive_time)!}"
                               id="order_recive_time"
                               placeholder="订单回收时间">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="auto_dispatch" class="control-label">自动派单 :</label></td>
                    <td>
                        <select id="auto_dispatch" class="select2" name="adminSetting.auto_dispatch"
                                style="width: 100%;">
                            <option value="2">请选择</option>
                            <option value=0
                                    <#if adminSetting.auto_dispatch??&&!adminSetting.auto_dispatch>selected="selected"</#if>>
                                否
                            </option>
                            <option value=1
                                    <#if adminSetting.auto_dispatch??&&adminSetting.auto_dispatch>selected="selected"</#if>>
                                是
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                </tr>

                <tr>
                    <td><label for="change_order_flag" class="control-label">转单标记:</label></td>
                    <td>
                        <select id="change_order_flag" class="select2" name="adminSetting.change_order_flag"
                                style="width: 100%;">
                            <option value="2">请选择</option>
                            <option value=0
                                    <#if adminSetting.change_order_flag??&&!adminSetting.change_order_flag>selected="selected"</#if>>
                                否
                            </option>
                            <option value=1
                                    <#if adminSetting.change_order_flag??&&adminSetting.change_order_flag>selected="selected"</#if>>
                                是
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="grab_single_flag" class="control-label">抢单标记:</label></td>
                    <td>
                        <select id="grab_single_flag" class="select2" name="adminSetting.grab_single_flag"
                                style="width: 100%;">
                            <option value="2">请选择</option>
                            <option value=0
                                    <#if adminSetting.grab_single_flag??&&!adminSetting.grab_single_flag>selected="selected"</#if>>
                                否
                            </option>
                            <option value=1
                                    <#if adminSetting.grab_single_flag??&&adminSetting.grab_single_flag>selected="selected"</#if>>
                                是
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="zx_cj_dispatch" class="control-label">城际专线后台派单:</label></td>
                    <td>
                        <select id="zx_cj_dispatch" class="select2" name="adminSetting.zx_cj_dispatch"
                                style="width: 100%;">
                            <option value="2">请选择</option>
                            <option value="0"
                                    <#if adminSetting.zx_cj_dispatch??&&adminSetting.zx_cj_dispatch==0>selected="selected"</#if>>
                                否
                            </option>
                            <option value="1"
                                    <#if adminSetting.zx_cj_dispatch??&&adminSetting.zx_cj_dispatch==1>selected="selected"</#if>>
                                是
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="zx_hk_dispatch" class="control-label">航空专线后台派单:</label></td>
                    <td>
                        <select id="zx_hk_dispatch" class="select2" name="adminSetting.zx_hk_dispatch"
                                style="width: 100%;">
                            <option value="2">请选择</option>
                            <option value=0
                                    <#if adminSetting.zx_hk_dispatch??&&adminSetting.zx_hk_dispatch==0>selected="selected"</#if>>
                                否
                            </option>
                            <option value=1
                                    <#if adminSetting.zx_hk_dispatch??&&adminSetting.zx_hk_dispatch==1>selected="selected"</#if>>
                                是
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="min_amount" class="control-label">最低上线金额 :</label></td>
                    <td>
                        <input type="text" name="adminSetting.min_amount" value="${(adminSetting.min_amount)!}"
                               class="form-control n-invalid"
                               id="min_amount"
                               placeholder="最低上线金额 ">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="tixian_amount" class="control-label">最低提现金额:</label></td>
                    <td>
                        <input type="text" name="adminSetting.tixian_amount"
                               value="${(adminSetting.tixian_amount)!}" class="form-control n-invalid"
                               id="tixian_amount"
                               placeholder="最低提现金额">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="deposit_amount1" class="control-label">充值金额1:</label></td>
                    <td>
                        <input type="text" name="adminSetting.deposit_amount1"
                               value="${(adminSetting.deposit_amount1)!}" class="form-control n-invalid"
                               id="deposit_amount1"
                               placeholder="充值金额1">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="deposit_amount2" class="control-label">充值金额2:</label></td>
                    <td>
                        <input type="text" name="adminSetting.deposit_amount2"
                               value="${(adminSetting.deposit_amount2)!}" class="form-control n-invalid"
                               id="deposit_amount2"
                               placeholder="充值金额2">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="deposit_amount3" class="control-label">充值金额3:</label></td>
                    <td>
                        <input type="text" name="adminSetting.deposit_amount3"
                               value="${(adminSetting.deposit_amount3)!}" class="form-control n-invalid"
                               id="deposit_amount3"
                               placeholder="充值金额3">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="bind_device_flag" class="control-label">登陆设备绑定标记:</label></td>
                    <td>
                        <select id="bind_device_flag" class="select2" name="adminSetting.bind_device_flag"
                                style="width: 100%;">
                            <option value="">请选择</option>
                            <option value=0
                                    <#if adminSetting.bind_device_flag??&&!adminSetting.bind_device_flag>selected="selected"</#if>>
                                否
                            </option>
                            <option value=1
                                    <#if adminSetting.bind_device_flag??&&adminSetting.bind_device_flag>selected="selected"</#if>>
                                是
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>


                <tr>
                    <td><label for="vip_amount" class="control-label">VIP充值金额(元):</label></td>
                    <td>
                        <input type="text" name="adminSetting.vip_amount"
                               value="${(adminSetting.vip_amount)!}" class="form-control n-invalid"
                               id="vip_amount"
                               placeholder="VIP充值金额">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="vip_single_spending_amount" class="control-label">VIP单笔消费金额(元):</label></td>
                    <td>
                        <input type="text" name="adminSetting.vip_single_spending_amount"
                               value="${(adminSetting.vip_single_spending_amount)!}" class="form-control n-invalid"
                               id="deposit_amount2"
                               placeholder="VIP单笔消费金额">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>


                <tr>
                    <td><label for="deposit_amount1" class="control-label">代驾保险费(元):</label></td>
                    <td>
                        <input type="text" name="adminSetting.dj_insurance_amount"
                               value="${(adminSetting.dj_insurance_amount)!}" class="form-control n-invalid"
                               id="dj_insurance_amount"
                               placeholder="代驾保险费">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="deposit_amount2" class="control-label">快车保险费(元):</label></td>
                    <td>
                        <input type="text" name="adminSetting.kc_insurance_amount"
                               value="${(adminSetting.kc_insurance_amount)!}" class="form-control n-invalid"
                               id="kc_insurance_amount"
                               placeholder="快车保险费">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="deposit_amount1" class="control-label">专车-舒适型保险费(元):</label></td>
                    <td>
                        <input type="text" name="adminSetting.ss_insurance_amount"
                               value="${(adminSetting.ss_insurance_amount)!}" class="form-control n-invalid"
                               id="ss_insurance_amount"
                               placeholder="专车-舒适型保险费">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="deposit_amount2" class="control-label">专车-豪华型保险费(元):</label></td>
                    <td>
                        <input type="text" name="adminSetting.hh_insurance_amount"
                               value="${(adminSetting.hh_insurance_amount)!}" class="form-control n-invalid"
                               id="hh_insurance_amount"
                               placeholder="专车-豪华型保险费">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="deposit_amount1" class="control-label">专车-商务型保险费(元):</label></td>
                    <td>
                        <input type="text" name="adminSetting.sw_insurance_amount"
                               value="${(adminSetting.sw_insurance_amount)!}" class="form-control n-invalid"
                               id="sw_insurance_amount"
                               placeholder="专车-商务型保险费">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="deposit_amount2" class="control-label">出租车保险费(元):</label></td>
                    <td>
                        <input type="text" name="adminSetting.cz_insurance_amount"
                               value="${(adminSetting.cz_insurance_amount)!}" class="form-control n-invalid"
                               id="cz_insurance_amount"
                               placeholder="出租车保险费">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
            <tr>
                <td><label for="deposit_amount2" class="control-label">保险费最高透支金额(元):</label></td>
                <td>
                    <input type="text" name="adminSetting.insurance_overdraft_amount"
                           value="${(adminSetting.insurance_overdraft_amount)!}" class="form-control n-invalid"
                           id="insurance_overdraft_amount"
                           placeholder="保险费最高透支金额">
                </td>
                <td>
                    <div style="padding-top: 10px;color: red;">*(请填写负数)</div>
                </td>

                <td><label for="customer_info_flag" class="control-label">客户必填信息标记:</label></td>
                <td>
                    <select id="customer_info_flag" class="select2" name="adminSetting.customer_info_flag"
                            style="width: 100%;">
                        <option value="2">请选择</option>
                        <option value=0
                                    <#if adminSetting.customer_info_flag??&&!adminSetting.customer_info_flag>selected="selected"</#if>>
                            否
                        </option>
                        <option value=1
                                    <#if adminSetting.customer_info_flag??&&adminSetting.customer_info_flag>selected="selected"</#if>>
                            是
                        </option>
                    </select>
                </td>
                <td>
                    <div style="padding-top: 10px;color: red;">*</div>
                </td>
            </tr>
            </#if>

            <#if index==1>
                <th>第一阶段</th>
                <tr>
                    <td><label for="dispatch_mill1" class="control-label">派单第一阶段公里数:</label></td>
                    <td>
                        <input type="text" name="adminSetting.dispatch_mill1"
                               value="${(adminSetting.dispatch_mill1)!}"
                               class="form-control n-invalid"
                               id="dispatch_mill1"
                               placeholder="派单第一阶段公里数">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="dispatch_time_out1" class="control-label">派单第一阶段超时 :</label></td>
                    <td>
                        <input type="text" name="adminSetting.dispatch_time_out1"
                               value="${(adminSetting.dispatch_time_out1)!}" class="form-control n-invalid"
                               id="dispatch_time_out1"
                               placeholder="派单第一阶段超时 ">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="grab_single_mill1" class="control-label">抢单第一阶段公里数:</label></td>
                    <td>
                        <input type="text" name="adminSetting.grab_single_mill1"
                               value="${(adminSetting.grab_single_mill1)!}" class="form-control n-invalid"
                               id="grab_single_mill1"
                               placeholder="抢单第一阶段公里数2">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="grab_single_time_out1" class="control-label">抢单第一阶段超时:</label></td>
                    <td>
                        <input type="text" name="adminSetting.grab_single_time_out1"
                               value="${(adminSetting.grab_single_time_out1)!}" class="form-control n-invalid"
                               id="grab_single_time_out1"
                               placeholder="抢单第一阶段超时2 ">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            </#if>

            <#if index==1>
                <th>第二阶段</th>
                <tr>
                    <td><label for="dispatch_mill2" class="control-label">派单第二阶段公里数:</label></td>
                    <td>
                        <input type="text" name="adminSetting.dispatch_mill2"
                               value="${(adminSetting.dispatch_mill2)!}"
                               class="form-control n-invalid"
                               id="dispatch_mill2"
                               placeholder="派单第二阶段公里数">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="dispatch_time_out2" class="control-label">派单第二阶段超时:</label></td>
                    <td>
                        <input type="text" name="adminSetting.dispatch_time_out2"
                               value="${(adminSetting.dispatch_time_out2)!}" class="form-control n-invalid"
                               id="dispatch_time_out2"
                               placeholder="派单第二阶段超时">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>


                <tr>
                    <td><label for="grab_single_mill2" class="control-label">抢单第二阶段公里数:</label></td>
                    <td>
                        <input type="text" name="adminSetting.grab_single_mill2"
                               value="${(adminSetting.grab_single_mill2)!}" class="form-control n-invalid"
                               id="grab_single_mill2"
                               placeholder="抢单标记 ">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="grab_single_time_out2" class="control-label">抢单第二阶段超时:</label></td>
                    <td>
                        <input type="text" name="adminSetting.grab_single_time_out2"
                               value="${(adminSetting.grab_single_time_out2)!}" class="form-control n-invalid"
                               id="grab_single_time_out2"
                               placeholder="抢单第二阶段超时2">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            </#if>

            <#if index==1>
                <th>第三阶段</th>
                <tr>
                    <td><label for="dispatch_mill3" class="control-label">派单第三阶段公里数:</label></td>
                    <td>
                        <input type="text" name="adminSetting.dispatch_mill3"
                               value="${(adminSetting.dispatch_mill3)!}"
                               class="form-control n-invalid"
                               id="dispatch_mill3"
                               placeholder="派单第三阶段公里数">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="dispatch_time_out3" class="control-label">派单第三阶段超时:</label></td>
                    <td>
                        <input type="text" name="adminSetting.dispatch_time_out3"
                               value="${(adminSetting.dispatch_time_out3)!}" class="form-control n-invalid"
                               id="dispatch_time_out3"
                               placeholder="派单第三阶段超时">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="grab_single_mill3" class="control-label">抢单第三阶段公里数:</label></td>
                    <td>
                        <input type="text" name="adminSetting.grab_single_mill3"
                               value="${(adminSetting.grab_single_mill3)!}" class="form-control n-invalid"
                               id="grab_single_mill3"
                               placeholder="抢单第三阶段公里数2">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="grab_single_time_out3" class="control-label">抢单第三阶段超时:</label></td>
                    <td>
                        <input type="text" name="adminSetting.grab_single_time_out3"
                               value="${(adminSetting.grab_single_time_out3)!}" class="form-control n-invalid"
                               id="grab_single_time_out3"
                               placeholder="抢单第三阶段超时2 ">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>


            </#if>
            </table>
            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <button class="btn btn-success" type="submit">提交</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script>
    var index = ${index!1}
            $($(".li")[index - 1]).addClass("active");

    function MM_jumpMenu(targ, selObj, restore) { //v3.0
        eval(targ + ".location='" + selObj.options[selObj.selectedIndex].value + "'");
        if (restore) selObj.selectedIndex = 0;
    }
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/adminsetting.js?_${.now?string("hhmmss")}"></script>
</#macro>