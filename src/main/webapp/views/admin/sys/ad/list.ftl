<#include "../../base/_base.ftl">
<#macro title>
广告设置
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4><strong>广告设置</strong></h4>
    </div>
    <div class="panel-body">
        <table class="table table-bordered table-striped">
            <tr>
                <td>
                    <form id="searchForm_">
                        <div class="form-group">
                            <label for="s-name-LIKE" class="control-label">广告名称:</label>
                            <select name="s-type-EQ">
                                <option value="">请选择</option>
                                <option value="1">主页</option>
                                <option value="2">启动页</option>
                                <option value="3">微信页</option>
                            </select>
                            <label for="s-name-LIKE" class="control-label">客户端类型:</label>
                            <select name="s-app_type-EQ">
                                <option value="">请选择</option>
                                <option value="1">司机端</option>
                                <option value="2">会员端</option>
                                <option value="3">微信</option>
                            </select>
                         <#if hasRole("super_admin")||hasPermission("select-company")>
                            <label for="real_name" class="control-label">公司选择:</label>
                            <select class="select2 select-company" name="s-dc.id-EQ" id="companySelect"
                                    style="width: 50%;">
                                <option value="">请选择</option>
                            </select>
                         </#if>
                        </div>
                    </form>
                </td>
                <td>
                    <button class="btn btn-info search">搜索</button>
                    <a class="btn btn-info" href="${ctx}/admin/sys/ad/item">添加</a>
                </td>
            </tr>
        </table>
        <div class="table-responsive">
            <table id="ad-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>所属公司</th>
                    <th>广告地址</th>
                    <th>默认标识</th>
                    <th>广告类型</th>
                    <th>客户端类型</th>
                    <th>排序</th>
                    <th width="80">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script id="action_tpl" type="text/x-handlebars-template">
    <div class="btn-group">
        <button type="button" class="btn btn-success btn-xs ">操作</button>
        <button type="button" class="btn btn-success btn-xs  dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only"></span></button>
        <ul class="dropdown-menu success btn-xs " role="menu">
            <li><a href="${ctx}/admin/sys/ad/item/{{d.id}}">修改</a></li>
            <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除</a></li>
        </ul>
    </div>
</script>

</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/ad.js?_${.now?string("hhmmss")}"></script>
</#macro>