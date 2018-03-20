<#include "../../base/_base.ftl">
<#macro title>
用户管理
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/user">用户管理</a></li>
    <li class="active"><a>用户<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">用户管理 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="user_form">
            <input type="hidden" name="user.id" value="${(user.id)!}" id="id">
            <input type="hidden" name="user.login_id" value="${(user.login_id)!}">
            <input type="hidden" name="action" value="${action}">
            <table class="table table-bordered table-striped">
                <tr>
                    <td><label for="name" class="control-label">真实姓名:</label></td>
                    <td><input type="text" name="user.name" value="${(user.name)!}" class="form-control n-invalid"
                               id="name"
                               placeholder="真实姓名"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="phone" class="control-label">手机号:</label></td>
                    <td><input type="text" name="user.phone" value="${(user.phone)!}" class="form-control n-invalid"
                               id="phone"
                               placeholder="如:18000000000"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="username" class="control-label">用户名:</label></td>
                    <td><input type="text" name="user.username" value="${(user.username)!}"
                               class="form-control n-invalid"
                               id="username"
                               placeholder="如:admin"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="head_portrait" class="control-label">头像:</label></td>
                    <td><span style="height: 40px;width: 40px;" class="rounded-image topbar-profile-image">
                            <img id="portrait_show" style="height: 40px;"
                                 src="<#if user.head_portrait??>${(user.head_portrait)!}<#else>${ctx}/static/images/default-user.jpg</#if>"/></span>
                        <div id="uploader">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker">选择文件</div>
                            </div>
                        </div>
                        <input type="hidden" name="user.head_portrait" class="form-control n-invalid"
                               id="head_portrait" value="${(user.head_portrait)!}">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <#if action!=1>
                    <tr>
                        <td><label for="password" class="control-label">密码:</label></td>
                        <td><input type="password" name="password" class="form-control n-invalid" id="password"
                                   placeholder="密码"></td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>

                        <td><label for="repassword" class="control-label">确认密码:</label></td>
                        <td><input type="password" name="repassword" class="form-control n-invalid" id="repassword"
                                   placeholder="确认密码"
                                   data-msg-match="两次输入密码不一致!"
                                   data-rule="required;match(password);"></td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                </#if>
                <tr>
                    <td><label for="birthday" class="control-label">生日:</label></td>
                    <td><input type="text" name="user.birthday" readonly="readonly" data-date-format="yyyy-MM-dd"
                               class="form-control datepicker-input" id="birthday" value="${(user.birthday)!}"
                               placeholder="如:1992-11-11"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="gender" class="control-label">性别:</label></td>
                    <td><input type="radio" <#if (user.gender??)&&(user.gender)==1>checked="checked"</#if>
                               name="user.gender" value="1" class="form-control n-invalid" id="man">&nbsp;男
                        <input type="radio" <#if (user.gender??)&&(user.gender)==0>checked="checked"</#if>
                               name="user.gender" value="0" class="form-control n-invalid" id="waman">&nbsp;女
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <#if hasRole("super_admin")||hasPermission("select-company")>
                    <tr>
                        <td><label for="birthday" class="control-label">所属公司:</label></td>
                        <td><select id="company" class="select2" name="user.company" style="width: 100%;">
                            <option value="0">请选择</option>
                            <#list company as item>
                                <option value="${(item.id)!}"
                                        <#if user.company??&&user.company==item.id>selected="selected"</#if>>${(item.name)!}</option>
                            </#list>
                        </select></td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                        <td><label for="role" class="control-label">所属所属角色:</label></td>
                        <td><select id="role" class="select2" name="user.role" style="width: 100%;">
                            <option value="0">请选择</option>
                            <#list roles as item>
                                <option value="${(item.id)!}"
                                        <#if user.role??&&user.role==item.id>selected="selected"</#if>>${(item.name)!}</option>
                            </#list>
                        </select></td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                <#else>

                    <tr>
                        <td>
                            <label for="type" class="control-label">类型:</label>
                        </td>
                        <td>
                            <select id="type" class="select2" name="user.type"
                                    style="width: 100%;">
                                <option value="1" <#if user.type==1>selected="selected"</#if>>普通</option>
                                <option value="2" <#if user.type==2>selected="selected"</#if>>联盟</option>
                            </select>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                        <td colspan="3"></td>
                    </tr>
                </#if>
                <tr>
                    <td colspan="6" style="text-align: center;">
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
<script type="text/javascript" src="${ctx}/static/js/app/admin/user_item.js?_${.now?string("hhmmss")}"></script>
</#macro>