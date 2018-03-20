<#include "../../base/_base.ftl">
<#macro title>
客户投诉详情
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li class="active"><a>客户投诉详情</a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">客户投诉详情 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off" id="smstmp_form">
            <input type="hidden" name="callBack.id" value="${(callBack.id)!}" id="id">
            <table class="table table-bordered">
                <tr>
                    <td>
                        <label for="content" class="control-label">投诉信息:</label>
                    </td>
                    <td>
                        <textarea cols="30" name="callBack.content" readonly>${(callBack.content)!}</textarea>
                    </td>

                    <td>
                        <label for="reply_content" class="control-label">回复信息:</label>
                    </td>
                    <td>
                        <textarea cols="30" name="callBack.reply_content">${(callBack.reply_content)!}</textarea>
                    </td>
                </tr>
            </table>
            </tr>
            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                    </div>
                    <div class="col-sm-2" style="float: right;">
                        <button id="tijiao" class="btn btn-success" type="submit">提交</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/capitalpage.js?_${.now?string("hhmmss")}"></script>
</#macro>
