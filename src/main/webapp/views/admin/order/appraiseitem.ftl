<#include "../base/_base.ftl">
<#macro title>
乘客评价信息
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/order">订单信息</a></li>
    <li class="active"><a>信息<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-smstmp">
    <div class="panel-heading">
        <h4 class="panel-title">乘客评价信息 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="carinfo_form">
            <input type="hidden" name="memberAppraise.id" value="${(memberAppraise.id)!}">
            <input type="hidden" name="memberAppraise.OrderId" value="${(memberAppraise.OrderId)!}">
            <table class="table table-bordered table-striped">
                <tr>
                    <div style="padding-top: 10px;color: red;">请严格按照格式填写</div>
                </tr>
                <tr>
                <#--服务满意度-->
                    <td><label for="ServiceScore" class="control-label">服务满意度:</label></td>
                    <td>
                        <select name="memberAppraise.ServiceScore" style="height: 26px;"
                                value="${memberAppraise.ServiceScore!}">
                            <option value="">请选择</option>
                            <option value="1" <#if "${memberAppraise.ServiceScore!}"=="1">selected</#if>>1</option>
                            <option value="2" <#if "${memberAppraise.ServiceScore!}"=="2">selected</#if>>2</option>
                            <option value="3" <#if "${memberAppraise.ServiceScore!}"=="3">selected</#if>>3</option>
                            <option value="4" <#if "${memberAppraise.ServiceScore!}"=="4">selected</#if>>4</option>
                            <option value="5" <#if "${memberAppraise.ServiceScore!}"=="5">selected</#if>>5</option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                <#--驾驶员满意度-->
                    <td><label for="DriverScore" class="control-label">驾驶员满意度:</label></td>
                    <td>
                        <select name="memberAppraise.DriverScore" style="height: 26px;">
                            <option value="">请选择</option>
                            <option value="1" <#if "${memberAppraise.DriverScore!}"=="1">selected</#if>>1</option>
                            <option value="2" <#if "${memberAppraise.DriverScore!}"=="2">selected</#if>>2</option>
                            <option value="3" <#if "${memberAppraise.DriverScore!}"=="3">selected</#if>>3</option>
                            <option value="4" <#if "${memberAppraise.DriverScore!}"=="4">selected</#if>>4</option>
                            <option value="5" <#if "${memberAppraise.DriverScore!}"=="5">selected</#if>>5</option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                <#--车辆满意度-->
                    <td><label for="VehicleScore" class="control-label">车辆满意度:</label></td>
                    <td>
                        <select name="memberAppraise.VehicleScore" style="height: 26px;"
                                value="${memberAppraise.VehicleScore!}">
                            <option value="">请选择</option>
                            <option value="1" <#if "${memberAppraise.VehicleScore!}"=="1">selected</#if>>1</option>
                            <option value="2" <#if "${memberAppraise.VehicleScore!}"=="2">selected</#if>>2</option>
                            <option value="3" <#if "${memberAppraise.VehicleScore!}"=="3">selected</#if>>3</option>
                            <option value="4" <#if "${memberAppraise.VehicleScore!}"=="4">selected</#if>>4</option>
                            <option value="5" <#if "${memberAppraise.VehicleScore!}"=="5">selected</#if>>5</option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                <#--评价内容-->
                    <td><label for="Detail" class="control-label">评价内容:</label></td>
                    <td>
                        <input type="text" name="memberAppraise.Detail" value="${(memberAppraise.Detail)!}"
                               class="form-control n-invalid"
                               placeholder="">
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
<script type="text/javascript" src="${ctx}/static/js/app/admin/appraiseitem.js?_${.now?string("hhmmss")}"></script>
</#macro>