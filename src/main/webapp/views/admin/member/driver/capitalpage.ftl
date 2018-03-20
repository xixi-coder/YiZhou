<#include "../../base/_base.ftl">
<#macro title>
账户流水详情
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/member/driver/capital/${id}">账户流水</a></li>
    <li class="active"><a>账户流水详情</a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-smstmp">
    <div class="panel-heading">
        <h4 class="panel-title">账户流水详情 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off">
            <table class="table table-bordered">
                <tr>
                    <td>
                        <label for="operater" class="">操作人:</label>
                    </td>
                    <td>
                        <textarea cols="30" name="capital.operater" readonly>${(capital.operater)!}</textarea>
                    </td>

                    <td>
                        <label class="control-label">操作类型:</label>
                    </td>
                    <td>
                        <textarea cols="30" name="capital.operation_type"
                                  readonly>${(capital.operation_type)!}</textarea>
                    </td>
                </tr>

                <tr>
                    <td><label class="control-label">金额:</label></td>
                    <td><textarea cols="30" name="capital.amount" readonly>${(capital.amount)!}</textarea></td>

                    <td><label class="control-label">创建时间:</label></td>

                    <td>
                        <textarea cols="30" name="capital.create_time" readonly>${(capital.create_time)!}</textarea>
                    </td>
                </tr>

                <tr>
                    <td><label class="control-label">评价:</label></td>
                    <td><textarea cols="30" name="capital.remark" readonly>${(capital.remark)!}</textarea></td>

                </tr>
            </table>
            </tr>
            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">返回</a>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>
