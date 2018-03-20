<#include "../../../base/_base.ftl">
<#macro title>
客户评论详情
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/member/driver/grade">客户评论</a></li>
    <li class="active"><a>客户评论详情</a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">客户评论详情</h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="syslog_form">
            <input type="hidden" name="grade.id" value="${(grade.id)!}">
            <table class="table table-bordered table-striped">
                <tr>
                    <td><label for="params" class="control-label">评论内容:</label></td>
                    <td>
                        <textarea cols="30" name="grade.content" class="form-control n-invalid" style="height:100px "
                                  readonly>${(grade.content)!}</textarea>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>
                </tr>
            </table>

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
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/syslog_item.js?_${.now?string("hhmmss")}"></script>
</#macro>