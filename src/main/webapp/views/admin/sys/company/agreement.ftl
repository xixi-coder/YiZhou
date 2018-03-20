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
        <li class="tabs-select">
            <a href="${ctx}/admin/sys/company/agreement/${(company)!}-3-1"">客户服务协议</a>
        </li>
        <li class="tabs-select">
            <a href="${ctx}/admin/sys/company/agreement/${(company)!}-4-2"">服务人员协议</a>
        </li>
    </ul>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="distribution_form">
            <input type="hidden" name="agreement.id" value="${(agreement.id)!}" id="id"/>
            <input type="hidden" name="agreement.company" value="${(company)!}"/>
            <input type="hidden" name="type" id="type" value="${(type)!}"/>
            <input type="hidden" name="agreement.type" value="${(style)!}"/>
            <table class="table table-bordered table-striped" style="width: 90%;margin: 0 auto;">
                <tr>
                    <td style="text-align: right;"><label for="company" class="control-label">描述:</label></td>
                    <td>
                        <textarea rows="12" style="width: 100%;" name="agreement.content">${(agreement.content)!}</textarea>
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
<script type="text/javascript" src="${ctx}/static/js/app/admin/agreement_item.js?_${.now?string("hhmmss")}"></script>
</#macro>