<#include "../../base/_base.ftl">
<#macro title>
批量充值
</#macro>
<#macro content>
<link href="${ctx}/static/css/select-item.css" rel="stylesheet"/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>服务人员充值</h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="rechange_form">
            <table class="table table-bordered table-striped">
                <tr>
                    <td colspan="1"><label for="remoney" class="control-label">充值金额:</label></td>
                    <td colspan="5">
                        <div class="input-group">
                            <input id="remoney" name="" type="number" class="form-control">
                            <span class="input-group-addon">元</span>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="1"><label for="remark" class="control-label">充值说明:</label></td>
                    <td colspan="5">
                        <textarea rows="5" id="remark" name="remark"></textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="6"><h4>筛选条件(<span style="color: red;">默认全部</span>)</h4></td>
                </tr>
                <tr>
                    <td><label for="realName" class="control-label">姓名:</label></td>
                    <td>
                        <div class="input-group">
                            <input id="realName" name="realName" type="text" class="form-control">
                        </div>
                    </td>
                    <td><label for="phone" class="control-label">电话:</label></td>
                    <td>
                        <div class="input-group">
                            <input id="phone" name="phone" type="text" class="form-control">
                        </div>
                    </td>
                    <td><label for="lessthan" class="control-label">余额少于:</label></td>
                    <td>
                        <div class="input-group">
                            <input id="lessthan" name="lessthan" type="number" class="form-control">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td><label for="start_time" class="control-label">加入时间:</label></td>
                    <td colspan="3">
                        <div class="row">
                            <div class="col-sm-4">
                                <input id="startTime" class="form-control datepicker-input" value=""
                                       name="startTime"/>

                            </div>
                            <div class="col-sm-1"><label for="company" class="control-label">至</label></div>
                            <div class="col-sm-4">
                                <input id="endTime" class="form-control datepicker-input" name="endTime" value=""/>
                            </div>
                        </div>
                    </td>
                    <#if hasRole("super_admin")||hasPermission("select-company")>
                        <td><label for="company" class="control-label">服务人员所属公司:</label></td>
                        <td>
                            <select id="company" class="select2 select-company" name="company"
                                    style="width: 100%;">
                            </select>
                        </td>
                    <#else>
                    <td colspan="2"></td>
                    </#if>
                </tr>
                <tr>
                    <td><label for="serviceType" class="control-label">服务类型:</label></td>
                    <td colspan="5">
                        <label><input type="checkbox" value="1" name="serviceType">专车</label>

                        <label><input type="checkbox" value="2" name="serviceType">代驾</label>

                        <label><input type="checkbox" value="3" name="serviceType">出租车</label>

                        <label><input type="checkbox" value="4" name="serviceType">快车</label>
                    </td>
                </tr>
                <tr>
                    <td><label for="status" class="control-label">状态:</label></td>
                    <td colspan="5">
                        <label><input type="checkbox" value="1" name="status">上线</label>
                        <label><input type="checkbox" value="2" name="status">接单</label>
                        <label><input type="checkbox" value="3" name="status">忙碌</label>
                        <label><input type="checkbox" value="4" name="status">未上线</label>
                        <label><input type="checkbox" value="5" name="status">冻结</label>
                    </td>
                </tr>
                <tr>
                    <td>
                    </td>
                    <td colspan="4">
                        <button type="button" id="screen" class="btn btn-success">筛选</button>
                    </td>
                    <td>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/html" id="driverHtml">
    <div class="notify-container">
        <div class="notify-item-wrapper">
            <div class="notify-item notify-type-notice" style="">
                <div class="notify-item-close"></div>
                <input value="{{d.id}}" type="hidden" name="loginIds"/>
                <p>{{d.driverRealName}}-{{d.phone}}</p></div>
        </div>
    </div>
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/prechange.js?_${.now?string("hhmmss")}"></script>
</#macro>