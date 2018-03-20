<#--
<#include "../../base/_base.ftl">
<#macro title>
内容详情
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/appLog">APP日志</a></li>
    <li class="active"><a>内容详情</a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-smslog">
    <div class="panel-heading">
        <h4 class="panel-title">内容详情 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off">
            <table class="table table-bordered">
                <tr>
                    <td>
                        <label for="name" class="">发送时间:</label>
                    </td>
                    <td>
                        <textarea cols="30" name="smslog.send_time" readonly>${(smslog.send_time)!}</textarea>
                    </td>

                    <td>
                        <label for="name" class="control-label">发送结果:</label>
                    </td>
                    <td>
                        <textarea cols="30" name="smslog.status" readonly>${(smslog.status)!}</textarea>
                    </td>
                </tr>

                <tr>
                    <td><label for="name" class="control-label">手机号码:</label></td>
                    <td><textarea cols="30" name="smslog.phone" readonly>${(smslog.phone)!}</textarea></td>

                    <td><label for="name" class="control-label">详细内容:</label></td>

                    <td>
                        <textarea cols="30" name="smslog.content" readonly
                                  style="height: 80px">${(smslog.content)!}</textarea>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="row" style="margin-top:20px;">
        <div class="col-sm-12">
            <div class="col-sm-6" style="float: right;">
                <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">返回</a>
            </div>
        </div>
    </div>

</div>
</#macro>
-->
