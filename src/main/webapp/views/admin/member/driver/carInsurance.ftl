<#include "../../base/_base.ftl">
<#macro title>
车辆保险信息
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/member/driver">司机信息</a></li>
    <li class="active"><a>信息<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-smstmp">
    <div class="panel-heading">
        <h4 class="panel-title">车辆保险信息 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="carinfo_form">
            <input type="hidden" name="insurance.VehicleNo" value="${(insurance.VehicleNo)!}">
            <input type="hidden" name="insurance.id" value="${(insurance.id)!}">
            <table class="table table-bordered table-striped">
                <tr>
                    <div style="padding-top: 10px;color: red;">请严格按照格式填写</div>
                </tr>
                <tr>
                <#--保险公司名称-->
                    <td><label for="InsurCom" class="control-label">保险公司名称:</label></td>
                    <td>
                        <input type="text" name="insurance.InsurCom" value="${(insurance.InsurCom)!}"
                               class="form-control n-invalid"
                               placeholder="如:太平洋保险">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                <#--保险号-->
                    <td><label for="InsurNum" class="control-label">保险号:</label></td>
                    <td>
                        <input type="text" name="insurance.InsurNum" value="${(insurance.InsurNum)!}"
                               class="form-control n-invalid"
                               placeholder="如:PDAA 2012110101000000">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <#--保险类型-->
                    <td><label for="InsurType" class="control-label">保险类型:</label></td>
                    <td>
                        <input type="text" name="insurance.InsurType" value="${(insurance.InsurType)!}"
                               class="form-control n-invalid"
                               placeholder="如:机动车保险">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <#--保险金额-->
                    <td><label for="InsurCount" class="control-label">保险金额:</label></td>
                    <td>
                        <input type="text" name="insurance.InsurCount" value="${(insurance.InsurCount)!}"
                               class="form-control n-invalid"
                               placeholder="如:5000">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <#--保险生效时间-->
                    <td><label for="InsurEff" class="control-label">保险生效时间:</label></td>
                    <td>
                        <input type="text" name="insurance.InsurEff" value="${(insurance.InsurEff)!}"
                               class="form-control n-invalid"
                               placeholder="如:20170517">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <#--保险到期日期-->
                    <td><label for="InsurExp" class="control-label">保险到期日期:</label></td>
                    <td>
                        <input type="text" name="insurance.InsurExp" value="${(insurance.InsurExp)!}"
                               class="form-control n-invalid"
                               placeholder="如:20170517">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
            </table>

            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">返回</a>
                    </div>
                    <div class="col-sm-2" style="float: right;">
                        <button class="btn btn-success" type="submit">提交</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/carInsurance.js?_${.now?string("hhmmss")}"></script>
</#macro>