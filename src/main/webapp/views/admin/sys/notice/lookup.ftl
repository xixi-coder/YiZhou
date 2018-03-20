<#include "../../base/_base.ftl">
<#macro title>
查看通知
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/notice">查看通知</a></li>
    <li class="active"><a>通知查看</a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">发送通知</h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="notice_form">
            <input type="hidden" name="notice.id" value="${(notice.id)!}" id="id"/>
            <table class="table table-bordered table-striped">
                <tr>
                    <td><label for="name" class="control-label">通知标题:</label></td>
                    <td>
                        <input type="text" name="notice.title" value="${(notice.title)!}" class="form-control n-invalid"
                               placeholder="如：明天放假"/>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="name" class="control-label">通知内容:</label></td>
                    <td>
                        <textarea rows="5" style="width: 95%;" name="notice.content">${(notice.content)!}</textarea>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="type" class="control-label">通知类型:</label></td>
                    <td>
                        <select id="type" data-value="${(notice.type)!}" class="select2"
                                name="notice.type" style="width: 100%;">
                            <option value="0">请选择</option>
                            <option value="1">公告</option>
                            <option value="2">通知</option>
                            <option value="3">出租车公告</option>
                            <option value="4">快车公告</option>
                            <option value="5">专车公告</option>
                            <option value="6">代驾公告</option>
                            <option value="7">顺风车公告</option>
                            <option value="8">专线公告</option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr class="notice">
                    <td><label for="send_type" class="control-label">发送方式:</label></td>
                    <td>
                        <input type="hidden">
                        <select id="send_type" class="select2" data-value="${(notice.send_type)!}"
                                name="notice.send_type" style="width: 100%;">
                            <option value="0">请选择</option>
                            <option value="1">
                                短信
                            </option>
                            <option value="2">
                                推送
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr class="pulish">
                    <td><label for="url" class="control-label">链接地址:</label></td>
                    <td>
                        <input type="text" name="notice.url" value="${(notice.url)!}"
                               class="form-control n-invalid"
                               id="url"
                               placeholder="如:http://www.baidu.com">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr class="pulish">
                    <td><label for="company" class="control-label">公告有效时间:</label></td>
                    <td>
                        <input class="datetimepicker-input" value="${(notice.start_time?string("yyyy-MM-dd HH:mm"))!}"
                               name="notice.start_time"/>
                        至
                        <input class="datetimepicker-input" name="notice.end_time"
                               value="${(notice.end_time?string("yyyy-MM-dd HH:mm"))!}"/>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr class="notice">
                    <td><label for="company" class="control-label">发送时间:</label></td>
                    <td>
                        <input class="sendTime" name="notice.send_time" value="${(notice.send_time)!}"/>
                        <div style="padding-top: 10px;color: red;">*&nbsp;&nbsp;不填则立即发送</div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <#if hasRole("super_admin")||hasPermission("select-company")>
                    <tr>
                        <td><label for="company" class="control-label">所属公司:</label></td>
                        <td>
                            <select id="company" class="select2 select-company" name="notice.company"
                                    style="width: 100%;"
                                    data-value="${(notice.company)!}">
                                <option value="0">请选择</option>
                            </select>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                </#if>
                <tr class="notice">
                    <input type="hidden" id="reciverType" value="${(notice.recive_type)!}"/>
                    <td><label for="company" class="control-label">发送对象:</label></td>
                    <td>
                        <input type="radio" class="reciverType" name="reciverType" value="-1"/>全部(司机端和乘客端)
                        <input type="radio" class="reciverType" name="reciverType" value="-2"/>全部客户(乘客端)
                        <input type="radio" class="reciverType" name="reciverType" value="-3"/>全部司机(司机端)
                        <input type="radio" class="reciverType" name="reciverType" value="-4"/>指定客户
                        <input type="radio" class="reciverType" name="reciverType" value="-5"/>指定司机
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr class="pulish">
                    <td><label for="reciverType" class="control-label">发送对象:</label></td>
                    <td>
                        <input type="radio" class="reciverType" name="reciverType" value="-1"/>全部(司机端和乘客端)
                        <input type="radio" class="reciverType" name="reciverType" value="-2"/>乘客端
                        <input type="radio" class="reciverType" name="reciverType" value="-3"/>司机端
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr id="memberList" style="display: none;">
                    <td colspan="3">
                        <textarea id="selectUser" style="width: 100%;" class="form-control">${(notice.members)!} </textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="3" style="text-align: center;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">返回</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/notice_lockup.js?_${.now?string("hhmmss")}"></script>
</#macro>