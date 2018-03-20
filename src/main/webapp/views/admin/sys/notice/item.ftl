<#include "../../base/_base.ftl">
<#macro title>
发送通知
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/notice">发送通知</a></li>
    <li class="active"><a>通知<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">发送通知</h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="notice_form">
            <input type="hidden" name="notice.id" value="${(notice.id)!}" id="id"/>
            <input type="hidden" name="action" value="${action}"/>
            <input type="hidden" name="memberInfo" id="memberInfo" value=""/>
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
                        <textarea rows="5" style="width: 95%;" name="notice.content"
                                  value="${(notice.content)!}"></textarea>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="type" class="control-label">通知类型:</label></td>
                    <td>
                        <select id="type" data-val="<#if notice.type??>${notice.type}<#else>1</#if>" class="select2"
                                name="notice.type" style="width: 100%;">
                            <option value="0">请选择</option>
                            <option value="1" <#if notice.type??&&notice.type==1>selected="selected"</#if>>公告</option>
                            <option value="2" <#if notice.type??&&notice.type==2>selected="selected"</#if>>通知</option>
                            <option value="3" <#if notice.type??&&notice.type==3>selected="selected"</#if>>出租车公告</option>
                            <option value="4" <#if notice.type??&&notice.type==4>selected="selected"</#if>>快车公告</option>
                            <option value="5" <#if notice.type??&&notice.type==5>selected="selected"</#if>>专车公告</option>
                            <option value="6" <#if notice.type??&&notice.type==6>selected="selected"</#if>>代驾公告</option>
                            <option value="7" <#if notice.type??&&notice.type==7>selected="selected"</#if>>顺风车公告</option>
                            <option value="8" <#if notice.type??&&notice.type==8>selected="selected"</#if>>专线公告</option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr class="notice">
                    <td><label for="send_type" class="control-label">发送方式:</label></td>
                    <td>
                        <select id="send_type" class="select2" name="notice.send_type" style="width: 100%;">
                            <option value="0">请选择</option>
                            <option value="1" <#if notice.send_type??&&notice.send_type==1>selected="selected"</#if>>
                                短信
                            </option>
                            <option value="2" <#if notice.send_type??&&notice.send_type==2>selected="selected"</#if>>
                                推送
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <#--<tr class="pulish">-->
                    <#--<td><label for="picker" class="control-label">图片:</label></td>-->
                    <#--<td>-->
                        <#--<span style="height: 40px;width: 40px;" class="rounded-image topbar-profile-image">-->
                            <#--<img id="portrait_show" style="height: 40px;"-->
                                 <#--src="${ctx}/<#if notice.img??>${(notice.img)!}<#else>/static/images/default-user.jpg</#if>"/></span>-->
                        <#--<div id="uploader">-->
                            <#--<!--用来存放文件信息&ndash;&gt;-->
                            <#--<div id="thelist" class="uploader-list"></div>-->
                            <#--<div class="btns">-->
                                <#--<div id="picker">选择文件</div>-->
                            <#--</div>-->
                        <#--</div>-->
                        <#--<input type="hidden" name="user.head_portrait" class="form-control n-invalid"-->
                               <#--id="head_portrait" value="${(notice.head_portrait)!}">-->
                    <#--</td>-->
                    <#--<td>-->
                        <#--<div style="padding-top: 10px;color: red;">*</div>-->
                    <#--</td>-->
                <#--</tr>-->
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
                        <input class="sendTime" name="notice.send_time" value="${(notice.send_time)!}"/><div style="padding-top: 10px;color: red;">*&nbsp;&nbsp;不填则立即发送</div>
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
                        <textarea id="selectUser" style="width: 100%;" class="form-control"></textarea>
                        <input type="hidden" id="selectUserHide"/>
                        </br>
                        <div class="input-group col-sm-10">
                            <input type="text" id="searchCondition" class="form-control">
                            <span class="input-group-btn">
										<button id="searchBtn" class="btn btn-default" type="button">搜索</button>
									  </span>
                        </div>
                        <div id="ztree" class="ztree"
                             style="overflow-x: auto; overflow-y: auto; height: 250px;margin-top: 10px;">
                        </div>
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
<script type="text/javascript" src="${ctx}/static/js/app/admin/notice_item.js?_${.now?string("hhmmss")}"></script>
</#macro>