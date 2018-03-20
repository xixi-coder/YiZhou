<#include "../../base/_base.ftl">
<#macro title>
评论信息详情
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/member/driver/grade/${id}">评论信息</a></li>
    <li class="active"><a>评论信息详情</a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-smstmp">
    <div class="panel-heading">
        <h4 class="panel-title">评论信息详情 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off">
            <table class="table table-bordered">
                <tr>
                    <td>
                        <label for="real_name" class="">被评论人:</label>
                    </td>
                    <td>
                        <textarea cols="30" readonly>${(driverInfo.real_name)!}</textarea>
                    </td>

                    <td>
                        <label class="control-label">评论人:</label>
                    </td>
                    <td>
                        <textarea cols="30" readonly>${(driver.real_name)!(member.real_name)}</textarea>
                    </td>
                </tr>

                <tr>
                    <td><label class="control-label">评分:</label></td>
                    <td><textarea cols="30" readonly>${(grade.score)!}</textarea></td>

                    <td><label class="control-label">评论内容:</label></td>
                    <td>
                        <textarea cols="30" readonly>${(grade.content)!}</textarea>
                    </td>
                </tr>

                <tr>
                    <td><label class="control-label">创建时间:</label></td>
                    <td><textarea cols="30" readonly>${(grade.create_time)!}</textarea></td>

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
