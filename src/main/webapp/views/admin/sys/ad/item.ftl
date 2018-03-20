<#include "../../base/_base.ftl">
<#macro title>
广告设置
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/ad">广告设置</a></li>
    <li class="active"><a>广告<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">广告设置 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="ad_form">
            <input type="hidden" name="ad.id" value="${(ad.id)!}" id="id"/>
            <input type="hidden" name="action" value="${action}"/>
            <input type="hidden" id="pic" name="ad.pic"/>
            <table class="table table-bordered table-striped">
                <tr>
                    <td><label  for="name" class="control-label">广告地址:</label></td>
                    <td>
                        <input id="url" type="text" name="ad.url" value="${(ad.url)!}" class="form-control n-invalid"
                               placeholder="如：www.baidu.com"/>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="type" class="control-label">默认标识:</label></td>
                    <td>
                        <select id="type" class="select2" name="ad.default_flag" style="width: 100%;">
                            <option value="2">请选择</option>
                            <option value=0
                                    <#if ad.default_flag??&&ad.default_flag==false>selected="selected"</#if>>否
                            </option>
                            <option value=1
                                    <#if ad.default_flag??&&ad.default_flag==true>selected="selected"</#if>>是
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <#if hasRole("super_admin")||hasPermission("select-company")>
                        <td><label for="company" class="control-label">所属公司:</label></td>
                        <td>
                            <select id="company" class="select2 select-company" name="ad.company" style="width: 100%;"
                                    data-value="${(ad.company)!}">
                                <option value="0">请选择</option>
                            </select>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </#if>
                    <td>
                        <label for="sort" class="control-label">排序:</label>
                    </td>
                    <td>
                        <input type="number" value="${(ad.sort)!}" name="ad.sort"
                               class="form-control n-invalid" id="sort"/>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="type" class="control-label">广告类型:</label></td>
                    <td>
                        <select id="type" class="select2" name="ad.type" style="width: 100%;">
                            <option value="0">请选择</option>
                            <option value=1
                                    <#if ad.type??&&ad.type==1>selected="selected"</#if>>启动页广告
                            </option>
                            <option value=2
                                    <#if ad.type??&&ad.type==2>selected="selected"</#if>>首页广告
                            </option>
                            <option value=3
                                    <#if ad.type??&&ad.type==3>selected="selected"</#if>>微信广告
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="type" class="control-label">APP类型:</label></td>
                    <td>
                        <select class="select2" id="app_type" name="ad.app_type" style="width: 100%;">
                            <option value="0">请选择</option>
                            <option value=1
                                    <#if ad.app_type??&&ad.app_type==1>selected="selected"</#if>>司机端
                            </option>
                            <option value=2
                                    <#if ad.app_type??&&ad.app_type==2>selected="selected"</#if>>用户端
                            </option>
                            <option value=3
                                    <#if ad.app_type??&&ad.app_type==3>selected="selected"</#if>>微信
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="pic" class="control-label">广告图片:</label></td>
                    <td colspan="3" id="imageList" style="max-width: 400px;">
                        <#if paths?? && paths?size gt 0>
                            <#list paths as item>
                                <div style="margin-left: 10px;margin-top: 10px;height: 144px;float: left;">
                                    <input type="hidden" class="form-control n-invalid image-path" value="${item}"/>
                                    <span class=" rounded-image topbar-profile-image">
                                <img style="height: 100px;width: 100px;" src="${item}"/>
            <a style="margin-top: 10px;" class="btn btn-default" href="javascript:void(0);" onclick="del(this);">删除</a>
        </span>
                                </div>
                            </#list>
                        </#if>
                    </td>
                    <td>
                        <div id="uploader">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker">上传图片</div>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td colspan="6" style="text-align: center;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                        <button class="btn btn-success" type="submit">提交</button>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script id="imageTmp" type="text/x-handlebars-template">
    <div style="margin-left: 10px;margin-top: 10px;height: 144px;float: left;">
        <input type="hidden" class="form-control n-invalid image-path" value="{{d.path}}"/>
        <span class=" rounded-image topbar-profile-image">
                                <img style="height: 100px;width: 100px;" src="{{d.path}}"/>
            <a style="margin-top: 10px;" class="btn btn-default" href="javascript:void(0);" onclick="del(this);">删除</a>
        </span>
    </div>
</script>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/ad_item.js?_${.now?string("hhmmss")}"></script>
</#macro>