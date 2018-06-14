<#include "../../base/_base.ftl">
<#macro title>
司机证件图片
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/member/driver">司机信息</a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-smstmp">
    <div class="panel-heading">
        <h4 class="panel-title">司机证件信息设置</h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="modelinfo_form">
            <input type="hidden" name="driverLicenseInfo.id" value="${(driverLicenseInfo.id)!}">
            <input type="hidden" name="driverInfo.id" value="${(driverInfo.id)!}">
            <input type="hidden" name="action" value="${action!}">
            <table class="table table-bordered table-striped">
                <tr>
                    <td><label for="driver_license" class="control-label">驾驶证照:</label></td>
                    <td colspan="15">
                        <img id="portrait_show" style="height: 150px;"
                             src="/<#if driverLicenseInfo.driver_license??>${(driverLicenseInfo.driver_license)!}<#else>/static/images/default-user.jpg</#if>"/></span>
                        <div id="uploader">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker">选择文件</div>
                            </div>
                        </div>
                        <input type="hidden" name="driverLicenseInfo.driver_license" class="form-control n-invalid"
                               id="driver_license" value="${(driverLicenseInfo.driver_license)!}">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>
                    <td><label for="driving_license" class="control-label">行驶证照:</label></td>
                    <td colspan="15">
                        <img id="portrait_show1" style="height: 150px;"
                             src="${ctx}/<#if driverLicenseInfo.driving_license??>${(driverLicenseInfo.driving_license)!}<#else>/static/images/default-user.jpg</#if>"/></span>
                        <div id="uploader1">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker1">选择文件</div>
                            </div>
                        </div>
                        <input type="hidden" name="driverLicenseInfo.driving_license" class="form-control n-invalid"
                               id="driving_license" value="${(driverLicenseInfo.driving_license)!}">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>

                </tr>
                <tr>
                    <td><label for="LicenseId" class="control-label">驾驶号码:</label></td>
                    <td colspan="15">
                        <input type="text" name="driverInfo.LicenseId" class="form-control n-invalid"
                               id="LicenseId" value="${(driverInfo.LicenseId)!}">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="DriverLicenseId" class="control-label">行驶证号码:</label></td>
                    <td colspan="15">
                        <input type="text" name="driverInfo.DriverLicenseId" class="form-control n-invalid"
                               id="DriverLicenseId" value="${(driverInfo.DriverLicenseId)!}">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                </tr>
                <tr>
                    <td><label for="id_card" class="control-label">身份证照:</label></td>
                    <td colspan="15">
                        <img id="portrait_show2" style="height: 150px;"
                             src="${ctx}/<#if driverLicenseInfo.id_card??>${(driverLicenseInfo.id_card)!}<#else>/static/images/default-user.jpg</#if>"/></span>
                        <div id="uploader">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker2">选择文件</div>
                            </div>
                        </div>
                        <input type="hidden" name="driverLicenseInfo.id_card" class="form-control n-invalid"
                               id="id_card" value="${(driverLicenseInfo.id_card)!}">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>

                    <td><label for="pgoto" class="control-label">上身照:</label></td>
                    <td colspan="15">
                        <img id="portrait_show3" style="height: 150px;"
                             src="${ctx}/<#if driverLicenseInfo.photo??>${(driverLicenseInfo.photo)!}<#else>/static/images/default-user.jpg</#if>"/></span>
                        <div id="uploader1">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker3">选择文件</div>
                            </div>
                        </div>
                        <input type="hidden" name="driverLicenseInfo.photo" class="form-control n-invalid"
                               id="photo" value="${(driverLicenseInfo.photo)!}">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td><label for="qualification_certificate" class="control-label">从业资格证:</label></td>
                    <td colspan="15">
                        <img id="portrait_show4" style="height: 150px;"
                             src="${ctx}/<#if driverLicenseInfo.qualification_certificate??>${(driverLicenseInfo.qualification_certificate)!}<#else>/static/images/default-user.jpg</#if>"/></span>
                        <div id="uploader1">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker4">选择文件</div>
                            </div>
                        </div>
                        <input type="hidden" name="driverLicenseInfo.qualification_certificate" class="form-control n-invalid"
                               id="qualification_certificate" value="${(driverLicenseInfo.qualification_certificate)!}">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">
                        </div>
                    </td>

                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td colspan="34" style="text-align: center;">

                    <#--<#if auditFlag??&&!auditFlag>-->
                        <a id="audit" href="javascript:void(0);" class="btn btn-success">审核</a>
                    <#--</#if>-->
                        <button class="btn btn-success" type="submit">保存</button>
                        <a class="btn btn-default" href="javascript:void(0);"
                           onclick="window.history.go(-1)">返回</a>

                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script id="audit_tpl" type="text/x-handlebars-template">
    <form id="audit_form" action="" autocomplete="off">
        <input type="hidden" name="id" value="${(driverLicenseInfo.id)!}">
        <div class="form-group">
            <label for="audit_remark" class="col-sm-4 control-label">审核意见:</label>
            <div class="col-sm-7">
                <textarea name="audit_remark" style="height: 100"></textarea>
            </div>
        </div>
    </form>
</script>
</#macro>
<#macro javascript>
<script>
    var id = ${id!0}
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/driverlicense.js?_${.now?string("hhmmss")}"></script>
</#macro>