<#include "../../base/_base.ftl">
<#macro title>
版本控制
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/version">版本控制</a></li>
    <li class="active"><a>版本<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-version">
    <form class="form-horizontal" method="post" role="form" autocomplete="off"
          id="version_form">
        <div class="panel-heading">
            <h4 class="panel-title">版本设置 </h4>
        </div>
        <div class="panel-body">
            <input type="hidden" name="version.id" value="${(version.id)!}">
            <input type="hidden" name="action" value="${action}">
            <#if version.os_type??&&version.os_type==1>
                <input type="hidden" name="version.file_path" class="form-control n-invalid"
                       id="file_path" value="${(version.file_path)!}">
            </#if>
            <table class="table table-bordered table-striped">
            <#---------------------->
                <tr>
                    <td><label for="no" class="control-label">版本号:</label></td>
                    <td>
                        <input type="text" name="version.version_no" value="${(version.version_no)!}"
                               class="form-control n-invalid"
                               placeholder="如：121212121">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="no" class="control-label">版本:</label></td>
                    <td>
                        <input type="text" name="version.version" value="${(version.version)!}"
                               class="form-control n-invalid"
                               placeholder="如：121">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
            <#---------------------->
                <tr>
                    <td><label for="type" class="control-label">版本类型:</label></td>
                    <td><select id="type" class="select2" name="version.type" data-value="${(version.type)!}"
                                style="width: 100%;">
                        <option value="0">请选择</option>
                        <option value=1>司机端</option>
                        <option value=2>客户端</option>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="osType" class="control-label">系统类型:</label></td>
                    <td><select id="osType" class="select2"
                                data-value="<#if version.os_type??>${(version.os_type)!}<#else>2</#if>"
                                name="version.os_type" style="width: 100%;">
                        <option value=0>请选择</option>
                        <option value=2>iOS</option>
                        <option value=1>ANDROID</option>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
            <#---------------------->
                <tr>
                    <td><label for="crc" class="control-label">CRC:</label></td>
                    <td>
                        <input type="text" name="version.crc" value="${(version.crc)!}"
                                class="form-control n-invalid">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="hvalue" class="control-label">哈希值:</label></td>
                    <td>
                        <textarea type="text" name="version.hvalue"
                                  class="form-control n-invalid">${(version.hvalue)!}</textarea>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
            <#---------------------->
                <tr>
                    <td><label for="no" class="control-label">描述:</label></td>
                    <td>
                        <textarea type="text" name="version.description"
                                  class="form-control n-invalid">${(version.description)!}</textarea>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td class="android"><label for="head_portrait" class="control-label">上传文件:</label></td>
                    <td class="android">
                        <!--用来存放文件信息-->
                        <div id="thelist" class="uploader-list" ></div>
                        <div class="btns">
                            <div id="picker">选择文件</div>
                        </div>
                    </td>
                    <td class="android">
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td class="iOS"><label for="head_portrait" class="control-label">下载地址:</label></td>
                    <td class="iOS">
                        <textarea type="text" name="version.file_path"
                                  class="form-control n-invalid">${(version.file_path)!}</textarea>
                    </td>
                    <td class="iOS">
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="force" class="control-label">是否强制更新:</label></td>
                    <td><select id="force" class="select2" name="version.force" data-value="${(version.force)!}"
                                style="width: 100%;">
                        <option value="">请选择</option>
                        <option value=0>不强制更新</option>
                        <option value=1>强制更新</option>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                    </div>
                    <div class="col-sm-2" style="float: right;">
                        <button class="btn btn-success" type="submit">提交</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/version_item.js?_${.now?string("hhmmss")}"></script>
</#macro>