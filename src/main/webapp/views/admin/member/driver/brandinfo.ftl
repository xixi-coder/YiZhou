<#include "../../base/_base.ftl">
<#macro title>
车辆品牌管理
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/member/driver/carbrand">车辆品牌管理</a></li>
    <li class="active"><a>信息<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-smstmp">
    <div class="panel-heading">
        <h4 class="panel-title">车辆品牌设置 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="brandinfo_form">
            <input type="hidden" name="carBrand.id" value="${(carBrand.id)!}">
            <input type="hidden" name="action" value="${action}">
            <table class="table table-bordered table-striped">
                <tr>
                    <td><label for="name" class="control-label">汽车品牌:</label></td>
                    <td><input type="text" name="carBrand.name" value="${(carBrand.name)!}"
                               class="form-control n-invalid"
                               placeholder="如:奥迪"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="py_name" class="control-label">拼音名称:</label></td>
                    <td><input type="text" name="carBrand.py_name" value="${(carBrand.py_name)!}"
                               class="form-control n-invalid"
                               placeholder="如:aodi"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>


                </tr>

                <tr>
                    <td><label for="content" class="control-label">品牌描述:</label></td>
                    <td>
                        <textarea name="carBrand.description" class="form-control n-invalid"
                                  placeholder="品牌描述" style="height: 80px">${(carBrand.description)!}</textarea>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>

                    <td><label for="logo" class="control-label">品牌LOGO:</label></td>
                    <td colspan="4">
                        <img id="portrait_show" style="height: 40px;"
                             src="<#if carBrand.logo??>${(carBrand.logo)!}<#else>${ctx}/static/images/default-user.jpg</#if>"/></span>
                        <div id="uploader">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker">选择文件</div>
                            </div>
                        </div>
                        <input type="hidden" name="carBrand.logo" class="form-control n-invalid"
                               id="logo" value="${(carBrand.logo)!}">
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
<script type="text/javascript" src="${ctx}/static/js/app/admin/brandinfo.js?_${.now?string("hhmmss")}"></script>
</#macro>