<#include "../../base/_base.ftl">
<#macro title>
公司设置
</#macro>
<#macro content>
<div class="panel panel-default">
    <ul class="nav nav-tabs" style="margin-top: 5px;margin-left: 5px;">
        <li class="tabs-select">
            <a href="${ctx}/admin/sys/company/distribution/${(company)!}-1-1">客户推荐提成设置</a>
        </li>
        <li class="tabs-select">
            <a href="${ctx}/admin/sys/company/distribution/${(company)!}-2-2">服务人员推荐提成设置</a>
        </li>
    <#--<li class="tabs-select">-->
    <#--<a href="${ctx}/admin/sys/company/agreement/${(company)!}-3-1"">客户服务协议</a>-->
    <#--</li>-->
    <#--<li class="tabs-select">-->
    <#--<a href="${ctx}/admin/sys/company/agreement/${(company)!}-4-2"">服务人员协议</a>-->
    <#--</li>-->
    </ul>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="distribution_form">
            <input type="hidden" name="distribution.id" value="${(distribution.id)!}" id="id"/>
            <input type="hidden" name="distribution.style" value="${(distribution.style)!}"/>
            <input type="hidden" name="distribution.company" value="${(company)!}"/>
            <input type="hidden" name="distribution.service_type" value="${(serviceType)!}"/>
            <input type="hidden" name="distribution.name" value="${(distribution.name)!}"/>
            <input type="hidden" id='tmp_level' value="${(distribution.level)!}"/>
            <input type="hidden" id='tmp_type' value="${(distribution.type)!}"/>
            <input type="hidden" name="type" id="type" value="${(type)!}"/>
            <input type="hidden" name="items" id="items" value=""/>
            <table class="table table-bordered table-striped" style="width: 60%;margin: 0 auto;">
                <tr>
                    <td style="text-align: right;"><label for="company" class="control-label">服务类型:</label></td>
                    <td>
                        <select id="service_type" class="select2" style="width: 100%;" name="distribution.service_type" onchange="MM_jumpMenu('parent',this,0)"
                                style="width: 30%;">
                            <option>请选择服务类型</option>
                            <#list servicetype as item>
                                <option value="${ctx}/admin/sys/company/distribution/${(company)!}-${(type)!}-${(style)!}-${(item.id)!}"
                                        <#if distribution.service_type??&&distribution.service_type==item.id>selected="selected"</#if>>${(item.name)!}</option>
                            </#list>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right;"><label for="company" class="control-label">提成类型:</label></td>
                    <td>
                        <input type="radio" name="distribution.type" value="1"/>&nbsp;一次性提成
                        &nbsp;&nbsp;<input type="radio" name="distribution.type" value="2"/>&nbsp;按单提成
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right;"><label for="company" class="control-label">描述:</label></td>
                    <td>
                        <textarea rows="12" style="width: 100%;"
                                  name="distribution.describe">${(distribution.describe)!}</textarea>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right;"><label for="company" class="control-label">提成等级:</label></td>
                    <td>
                        <select id="level" class="select2" style="width: 100%;" name="distribution.level">
                            <option value="0">不提成</option>
                            <option value="1">一级提成</option>
                            <option value="2">二级提成</option>
                            <option value="3">三级提成</option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr id="one">
                    <td style="text-align: right;">
                        <label for="company" class="control-label">一级:</label>
                    </td>
                    <td>
                        <select name="distribution.first_type" class="form-control input-group-addon unit"
                                data-unit="first_unit"
                                style="float: left;width: 110px;" data-value="${(distribution.first_type)!}">
                            <option value="0">金额</option>
                            <option value="1">比例</option>
                        </select>
                        <div class="input-group">
                            <input type="text" name="distribution.first_level"
                                   value="${(distribution.first_level?c)!}"
                                   class="form-control n-invalid"
                                   placeholder=""/>
                            <span class="input-group-addon" id="first_unit">
                                <#if distribution.first_type??&&distribution.first_type==1>%<#else>元</#if></span>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr id="two">
                    <td style="text-align: right;"><label for="company" class="control-label">二级:</label></td>
                    <td>
                        <select name="distribution.second_type" class="form-control input-group-addon unit"
                                data-unit="second_unit"
                                style="float: left;width: 110px;" data-value="${(distribution.second_type)!}">
                            <option value="0">金额</option>
                            <option value="1">比例</option>
                        </select>
                        <div class="input-group">
                            <input type="text" name="distribution.second_level" value="${(distribution.second_level?c)!}"
                                   class="form-control n-invalid"
                                   placeholder=""/>
                            <span class="input-group-addon"
                                  id="second_unit"><#if distribution.second_type??&&distribution.second_type==1>%<#else>
                                元</#if></span>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr id="three">
                    <td style="text-align: right;"><label for="company" class="control-label">三级:</label></td>
                    <td>
                        <select name="distribution.third_type" class="form-control input-group-addon unit"
                                data-unit="third_unit"
                                style="float: left;width: 110px;" data-value="${(distribution.third_type)!}">
                            <option value="0">金额</option>
                            <option value="1">比例</option>
                        </select>
                        <div class="input-group">
                            <input type="text" name="distribution.third_level" value="${(distribution.third_level?c)!}"
                                   class="form-control n-invalid"
                                   placeholder=""/>
                            <span class="input-group-addon"
                                  id="third_unit"><#if distribution.third_type??&&distribution.third_type==1>%<#else>
                                元</#if></span>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td colspan="3" style="text-align: center;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                        <button class="btn btn-success" type="submit">提交</button>
                    </td>
                </tr>
            </table>
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
<script type="text/javascript" src="${ctx}/static/js/app/admin/distribution_item.js?_${.now?string("hhmmss")}"></script>
</#macro>