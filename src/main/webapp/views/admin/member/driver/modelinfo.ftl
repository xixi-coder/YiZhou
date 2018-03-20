<#include "../../base/_base.ftl">
<#macro title>
车辆型号管理
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/member/driver">司机信息</a></li>
    <li class="active"><a>信息<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-smstmp">
    <div class="panel-heading">
        <h4 class="panel-title">车辆型号设置 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="modelinfo_form">
            <input type="hidden" name="carModel.id" value="${(carModel.id)!}">
            <input type="hidden" name="action" value="${action}">
            <input type="hidden" name="id" value="${id!}">
            <table class="table table-bordered table-striped">
                <tr>
                    <td><label for="name" class="control-label">汽车型号:</label></td>
                    <td><input type="text" name="carModel.name" value="${(carModel.name)!}"
                               class="form-control n-invalid"
                               placeholder="如:宝马X6"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="type" class="control-label">型号类型:</label></td>
                    <td><input type="text" name="carModel" value="${(carModel.type)!}" class="form-control n-invalid"
                               placeholder="如:1"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="content" class="control-label">型号描述:</label></td>
                    <td>
                        <textarea name="carModel.description" class="form-control n-invalid"
                                  placeholder="型号描述" style="height: 80px">${(carModel.description)!}</textarea>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
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
<script>
    var id = ${id!}
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/modelinfo.js?_${.now?string("hhmmss")}"></script>
</#macro>