<#include "../../base/_base.ftl">
<#macro title>
代驾订单汇总
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>代驾订单汇总</h4>
    </div>
    <div class="panel-body">
        <div class="table-responsive">
            <form class="form-inline" id="SearchForm" method="get" action="${ctx}/admin/count/reorder">
                <table id="companyRebate-list" cellspacing="0" class="hover table table-striped table-bordered">
                    <tr>
                        <td><input id="startTime" name="startTime" type="text" value="${startTime!}"
                                   class="form-control datetimepicker-input" placeholder="开始时间"/>至
                            <input id="endTime" name="endTime" type="text" value="${endTime!}"
                                   class="form-control datetimepicker-input" placeholder="结束时间"/>
                        </td>
                        <#if hasRole("super_admin")||hasPermission("select-company")>
                            <td width="30%">
                                <label for="real_name" class="control-label">公司选择:</label>
                                <select class="select2" name="company" id="companySelect"
                                        style="width: 30%;">
                                    <option value="">请选择</option>
                                    <#list companies as item>
                                        <option value="${(item.id)!}"
                                            <#if item.id == company> selected="selected"</#if>
                                        >${(item.name)!}</option>
                                    </#list>
                                </select>
                            </td>
                        </#if>
                        <td>
                            <button type="submit" id="search_button" class="btn btn-primary"><i
                                    class="glyphicon glyphicon-search"></i> 查询
                            </button>
                            <a style="display: none;" id="exporHiden"></a>
                            <button id="export_button" type="button" class="btn btn-primary"  onclick="javascript:method1('companyRebate-list1')"><i
                                    class="glyphicon glyphicon-save"></i>导出
                            </button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="table-responsive">
            <table id="companyRebate-list1" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead align="center">
                <tr style="font-size:14px;">
                    <td style="align=" center
                    "" colspan="9"><b>代驾订单汇总统计</b></span></th>
                </tr>
                </thead>
                <tbody>
                <#-- <tr>
                     <td rowspan="3">#</td>
                     <td colspan="5" align="center">由本公司创建的订单</td>
                     <td colspan="2" align="center">调入订单</td>
                     <td rowspan="3" style=" vertical-align:middle">
                         &nbsp;&nbsp;&nbsp;共计&nbsp;&nbsp;&nbsp;</td>
                 </tr>
                 <tr>
                     <td colspan="2">由本公司服务人员完成订单</td>
                     <td colspan="2">由平台其它公司服务人员完成订单（外派）</td>
                     <td rowspan="2">销单</td>
                     <td colspan="2">平台其它公司创建、由本公司服务人员完成的订单</td>
                 </tr>-->
                <tr>
                    <th>#</th>
                    <th>完成订单</th>
                    <th>执行中</th>
                    <th>销单</th>
                    <th>总计</th>
                </tr>
                <tr>
                    <th>单量（单）
                    </th>
                    <td id="orderFinishedAmount_this">${(order1.count?c)!0}</td>
                    <td id="orderBusyAmount_this">
                    ${(order4.count?c)!0}</td>
                <#-- <td id="orderFinishedAmount_getout">${(order2.count)!0}</td>
                 <td id="orderBusyAmount_getout">${(order4.count)!0}</td>-->
                    <td id="orderCanceledAmount">${(order6.count?c)!0}</td>
                <#--<td id="orderFinishedAmount_comein">0
                </th>
                <td id="orderBusyAmount_comein">0
                <th>-->
                    <td id="orderTotalAmount">
                    ${(order6.count+order1.count+order4.count)?c!0}
                    </td>
                </tr>
                <tr>
                    <th>订单总金额（元）</th>
                    <td id="orderFinishedMoney_this">${(order1.sum?c)!0}</td>
                    <td id="orderBusyMoney_this">
                    ${(order4.sum?c)!0}</td>
                <#-- <td id="orderFinishedMoney_getout">${(order2.sum)!0}</td>
                 <td id="orderBusyMoney_getout">${(order4.sum)!0}</td>-->
                    <td id="orderCanceledMoney">${(order6.sum?c)!0}</td>
                <#-- <td id="orderFinishedMoney_comein">0
                 </th>
                 <td id="orderBusyMoney_comein">0
                 </th>-->
                    <td id="orderTotalMoney">
                    ${(order1.sum+order4.sum+order6.sum)?c!0}
                    </td>
                </tr>
                <tr>
                    <th>优惠券（元）
                    </th>
                    <td id="orderFinishedCouponMoney_this">${(order1.sum1?c)!0}</td>
                    <td id="orderBusyCouponMoney_this">
                    ${(order4.sum1?c)!0}</td>
                <#-- <td id="orderFinishedCouponMoney_getout">${(order2.sum1?c)!0}</td>
                 <td id="orderBusyCouponMoney_getout">${(order4.sum1?c)!0}</td>-->
                    <td id="orderCanceledCouponMoney">${(order6.sum1?c)!0}</td>
                <#--<td id="orderFinishedCouponMoney_comein">0
                </th>
                <td id="orderBusyCouponMoney_comein">0
                </th>-->
                    <td id="orderTotalCouponMoney">
                    ${(order1.sum1+order4.sum1+order6.sum1)?c!0}
                    </td>
                </tr>
                <tr>
                    <th>支付金额（元）
                    </th>
                    <td id="orderFinishedPayMoney_this">${(order1.sum2?c)!0}</td>
                    <td id="orderBusyPayMoney_this">
                    ${(order4.sum2?c)!0}</td>
                <#--  <td id="orderFinishedPayMoney_getout">${(order2.sum2?c)!0}</td>
                  <td id="orderBusyPayMoney_getout">${(order4.sum2?c)!0}</td>-->
                    <td id="orderCanceledPayMoney">${(order6.sum2?c)!0}</td>
                <#-- <td id="orderFinishedPayMoney_comein">0
                 </th>
                 <td id="orderBusyPayMoney_comein">0
                 </th>-->
                    <td id="orderTotalPayMoney">
                    ${(order1.sum2+order4.sum2+order6.sum2)?c!0}
                    </td>
                </tr>
                <tr>
                    <th>公司收入（元）
                    </th>
                    <td id="orderFinishedCompanyMoney_this">${(order3.sum4?c)!0}</td>
                    <td id="orderBusyCompanyMoney_this">
                    ${0}</td>
                <#--<td id="orderFinishedCompanyMoney_getout">${(order2.sum4)!0}</td>
                <td id="orderBusyCompanyMoney_getout">${(order4.sum4)!0}</td>-->
                    <td id="orderCanceledCompanyMoney">${0}</td>
                <#-- <td id="orderFinishedCompanyMoney_comein">0
                 </th>
                 <td id="orderBusyCompanyMoney_comein">0
                 </th>-->
                    <td id="orderTotalCompanyMoney">
                    ${(order3.sum4?c)!0}
                    </td>
                </tr>
                <tr>
                    <th>服务人员收入（元）
                    </th>
                    <td id="orderFinishedDriverMoney_this">${(order2.sum3?c)!0}</td>
                    <td id="orderBusyDriverMoney_this">
                    ${(order5.sum3?c)!0}</td>
                <#-- <td id="orderFinishedDriverMoney_getout">${(order2.sum3)!0}</td>
                 <td id="orderBusyDriverMoney_getout">${(order4.sum3)!0}</td>-->
                    <td id="orderCanceledDriverMoney">${(order7.sum3?c)!0}</td>
                <#-- <td id="orderFinishedDriverMoney_comein">0
                 </th>
                 <td id="orderBusyDriverMoney_comein">0
                 </th>-->
                    <td id="orderTotalDriverMoney">
                    ${(order2.sum3+order5.sum3+order7.sum3)?c!0}
                    </td>
                </tr>
                <tr>
                    <th>保险费/信息费（元）
                    </th>
                    <td id="orderFinishedInfofee_this">${(order8.sum5?c)!0}</td>
                    <td id="orderBusyInfofee_this">
                    ${(order9.sum5?c)!0}
                    </td>
                <#-- <td id="orderFinishedInfofee_getout"></td>
                 <td id="orderBusyInfofee_getout"></td>-->
                    <td id="orderCanceledInfofee">${(order10.sum5?c)!0}</td>
                <#-- <td id="orderFinishedInfofee_comein">
                 </th>
                 <td id="orderBusyInfofee_comein">
                 </th>-->
                    <td id="orderTotalInfofee">
                    ${(order8.sum5+order9.sum5+order10.sum5)?c!0}
                    </td>
                </tr>
                <#-- <tr>
                     <td>客户经理提成（元）
                     </th>
                     <td id="orderFinishedManagerMoney_this"></td>
                     <td id="orderBusyManagerMoney_this">
                     </th>
                     <td id="orderFinishedManagerMoney_getout"></td>
                     <td id="orderBusyManagerMoney_getout"></td>
                     <td id="orderCanceledManagerMoney"></td>
                     <td id="orderFinishedManagerMoney_comein">
                     </th>
                     <td id="orderBusyManagerMoney_comein">
                     </th>
                     <td id="orderTotalManagerMoney">
                     </th>
                 </tr>
                 <tr>
                     <td>介绍提成（元）
                     </th>
                     <td id="orderFinishedRebateMoney_this"></td>
                     <td id="orderBusyRebateMoney_this">
                     </th>
                     <td id="orderFinishedRebateMoney_getout"></td>
                     <td id="orderBusyRebateMoney_getout"></td>
                     <td id="orderCanceledRebateMoney"></td>
                     <td id="orderFinishedRebateMoney_comein">
                     </th>
                     <td id="orderBusyRebateMoney_comein">
                     </th>
                     <td id="orderTotalRebateMoney">
                     </th>
                 </tr>-->
                </tbody>
            </table>
        </div>
        <div>
            <p><font color="red">注：（1）如果外派或者调入订单被销单，则不加入外派和调入订单统计，在订单所在的本公司销单中单独统计！</font></p>
            <p><font color="red">注：（2）所有的营销活动财务记录不在这里体现！</font></p>
        </div>
    </div>
</div>
</#macro>
<#macro javascript>
<script src="${ctx}/static/js/app/ExcelExport.js"></script>
<script type="text/javascript">
    function dataExport() {
        excelExport.Export("companyRebate-list1","导出表格",'代驾订单统计.xls');
    }
</script>
</#macro>