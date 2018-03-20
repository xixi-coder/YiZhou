<#include "../base/_base.ftl">
<#macro title>
乘客投诉信息
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/order">投诉信息</a></li>
    <li class="active"><a>信息<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-smstmp">
    <div class="panel-heading">
        <h4 class="panel-title">乘客投诉信息 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="carinfo_form">
            <input type="hidden" name="memberComplain.id" value="${(memberComplain.id)!}">
            <input type="hidden" name="memberComplain.OrderId" value="${(memberComplain.OrderId)!}">
            <table class="table table-bordered table-striped">
                <tr>
                    <div style="padding-top: 10px;color: red;">请严格按照格式填写</div>
                </tr>
                <tr>
                <#--投诉内容-->
                    <td><label for="Detail" class="control-label">投诉内容:</label></td>
                    <td>
                        <input type="text" name="memberComplain.Detail" value="${(memberComplain.Detail)!}"
                               class="form-control n-invalid"
                               placeholder="">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                <#--处理结果-->
                    <td><label for="Result" class="control-label">处理结果:</label></td>
                    <td>
                        <select name="memberComplain.Result" style="height: 26px;">
                            <option value="等待处理"<#if "${memberComplain.Result!}"=="等待处理">selected</#if> >等待处理</option>
                            <option value="已处理" <#if "${memberComplain.Result!}"=="已处理">selected</#if>>已处理</option>
                            <option value="未处理" <#if "${memberComplain.Result!}"=="未处理">selected</#if>>未处理</option>
                        </select>
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
<script type="text/javascript" src="${ctx}/static/js/app/admin/complainitem.js?_${.now?string("hhmmss")}"></script>
</#macro>