<#include "../../../base/_base.ftl">
<#macro title>
提现列表
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-body">
        <div class="table-responsive">
            <form id="searchForm_">
                <table class="table table-bordered table-striped">
                    <tr>
                        <#if hasRole("super_admin")||hasPermission("select-company")>
                            <td>
                                <label for="real_name" class="control-label">公司选择:</label>
                                <select class="select2 select-company" name="s-dwl.company-EQ"
                                        style="width: 100px;">
                                    <option value="">请选择</option>
                                </select>
                            </td>
                        </#if>
                        <td>
                            <label for="real_name" class="control-label">状态:</label>
                            <select class="select2" name="s-dwl.status-EQ"
                                    style="width: 100px;">
                                <option value="">全部</option>
                                <option value="0">待审核</option>
                                <option value="1">审核通过</option>
                                <option value="2">审核未通过</option>
                            </select>
                        </td>
                        <td>
                            <label for="s-name-LIKE" class="control-label">时间:</label>
                            <input type="hidden" name="s-dwl.create_time-BETWEEN" value="" id="timeSearch">
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="start"
                                   placeholder="如:1992-11-11">至
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="end"
                                   placeholder="如:1992-11-11">
                        </td>
                        <td>
                            <button type="button" class="btn btn-info search">搜索</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="table-responsive">
            <table id="withdrawals-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th></th>
                    <th width="40">提现金额(元)</th>
                    <th>状态</th>
                    <th>创建时间</th>
                    <th>申请人</th>
                    <th>申请人手机号</th>
                    <th>银行卡号</th>
                    <th>持卡人姓名</th>
                    <th>所属银行</th>
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
            {{# if(d.status==0) { }}
            <li><a class="audit" data-id="{{d.id}}" href="javascript:void(0);">审核</a></li>
            {{# } }}
        </ul>
    </div>
</script>
<script id="withdrawals_tpl" type="text/x-handlebars-template">
    <form id="withdrawals_form" action="" autocomplete="off">
        <input type="hidden" name="id" value="{{d.id}}" />
        <input type="hidden" name="status" id="auditStatus"/>
        <table style="border-collapse:separate;border-spacing:10px;">
            <tr>
                <td>
                    <label for="remark" class="col-sm-4 control-label">备注:</label>
                </td>
                <td>
                    <div class="col-sm-7">
                        <textarea name="remark"></textarea>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</script>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/withdrawals.js?_${.now?string("hhmmss")}"></script>
</#macro>