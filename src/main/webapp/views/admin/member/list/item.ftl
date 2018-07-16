<#include "../../base/_base.ftl">
<#macro title>
会员信息
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/member/list">会员列表</a></li>
    <li class="active"><a>信息<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-smstmp">
    <div class="panel-heading">
        <h4 class="panel-title">会员信息设置 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="smstmp_form">
            <input type="hidden" name="memberLogin.id" value="${(memberLogin.id)!}" id="id">
            <input type="hidden" name="memberInfo.id" value="${(memberInfo.id)!}" id="idg">
            <input type="hidden" name="memberLogin.type" value="${(memberLogin.type)!}" id="type">
            <input type="hidden" name="action" value="${action}">
            <table class="table table-bordered table-striped">
                <tr>
                    <td><label for="real_name" class="control-label">真实姓名:</label></td>
                    <td><input type="text" name="memberInfo.real_name" value="${(memberInfo.real_name)!}"
                               class="form-control n-invalid"
                               id="real_name"
                               placeholder="如：张三"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="address" class="control-label">家庭地址:</label></td>
                    <td><input type="text" name="memberInfo.address" value="${(memberInfo.address)!}"
                               class="form-control n-invalid"
                               id="address"
                               placeholder="如：安徽省合肥市蜀山区.."></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>


                </tr>

                <tr>
                    <td><label for="nick_name" class="control-label">昵称:</label></td>
                    <td><input type="text" name="memberInfo.nick_name" value="${(memberInfo.nick_name)!}"
                               class="form-control n-invalid"
                               placeholder="如:小鱼儿" id="nick_name"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="user_name" class="control-label">用户名:</label></td>
                    <td><input type="text" name="memberLogin.user_name" value="${(memberLogin.user_name)!}"
                               class="form-control n-invalid"
                               id="user_name"
                               placeholder="如:18000000000"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <#if action!=1>
                    <tr>
                        <td><label for="password" class="control-label">密码:</label></td>
                        <td><input type="password" name="password" class="form-control n-invalid" id="password"
                                   placeholder="密码" id="password"></td>
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
                    <td><label for="phono" class="control-label">电话:</label></td>
                    <td><input type="text" name="memberInfo.phone" value="${(memberInfo.phone)!}"
                               class="form-control n-invalid"
                               id="phono"
                               placeholder="如:18000000000"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="head_portrait" class="control-label">头像:</label></td>
                    <td><span style="height: 40px;width: 40px;" class="rounded-image topbar-profile-image">
                            <img id="portrait_show" style="height: 40px;"
                                 src="<#if memberInfo.head_portrait??>${(memberInfo.head_portrait)!}<#else>${ctx}/static/images/default-user.jpg</#if>"/></span>
                        <div id="uploader">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker">选择文件</div>
                            </div>
                        </div>
                        <input type="hidden" name="memberInfo.head_portrait" class="form-control n-invalid"
                               id="head_portrait" value="${(memberInfo.head_portrait)!}">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>
                </tr>

                <tr>
                    <td><label for="birthday" class="control-label">生日:</label></td>
                    <td><input type="text" name="memberInfo.birthday" data-date-format="yyyy-mm-dd" readonly
                               class="form-control datepicker-input" id="birthday" value="${(memberInfo.birthday)!}"
                               placeholder="如:1992-11-11"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>

                    <td><label for="gender" class="control-label">性别:</label></td>
                    <td><input type="radio" <#if (memberInfo.gender??)&&(memberInfo.gender)==1>checked="checked"</#if>
                               name="memberInfo.gender" value="1" class="form-control n-invalid" id="man">&nbsp;男
                        <input type="radio" <#if (memberInfo.gender??)&&(memberInfo.gender)==0>checked="checked"</#if>
                               name="memberInfo.gender" value="0" class="form-control n-invalid" id="waman">&nbsp;女
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>
                </tr>

                <tr>


                    <td><label for="urgent_phone" class="control-label">紧急电话:</label></td>
                    <td><input type="text" name="memberInfo.urgent_phone" value="${(memberInfo.urgent_phone)!}"
                               class="form-control n-invalid"
                               id="urgent_phone"
                               placeholder="如:18000000000"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>


                    <#if hasRole("super_admin")||hasPermission("select-company")>
                        <td><label for="company" class="control-label">所属公司:</label></td>
                        <td>
                            <div class="col-sm-7">
                                <select id="company" class="select2 select-company" name="memberInfo.company"
                                        style="width: 100%;"
                                        data-value="${(memberInfo.company)!}">
                                    <option value="0">请选择</option>
                                </select>
                            </div>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </#if>
                </tr>

                <tr>
                <#--<td><label for="company" class="control-label">收费标准:</label></td>-->
                <#--<td>-->
                <#--<div class="col-sm-7">-->
                <#--<select id="chargeStandard" class="select2" name="memberInfo.charge_standard"-->
                <#--style="width: 100%;">-->
                <#--<option value="0">请选择(不选默认为0)</option>-->
                <#--<#list chargeStandards as item>-->
                <#--<option value="${(item.id)!}"-->
                <#--<#if memberInfo.charge_standard??&&memberInfo.charge_standard==item.id>selected="selected"</#if>>${(item.name)!}</option>-->
                <#--</#list>-->
                <#--</select>-->
                <#--</div>-->
                <#--</td>-->
                <#--<td>-->
                <#--<div style="padding-top: 10px;color: red;"></div>-->
                <#--</td>-->

                    <td><label for="email" class="control-label">电子邮箱:</label></td>
                    <td><input type="text" name="memberInfo.email" value="${(memberInfo.email)!}"
                               class="form-control n-invalid"
                               id="email"
                               placeholder="如:8507@qq.com"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td colspan="3"></td>
                </tr>


            </table>
            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                    </div>
                    <#if hasPermission("huiyuanbianji")>
                        <div class="col-sm-2" style="float: right;">
                            <button id="tijiao" class="btn btn-success" type="submit">提交</button>
                        </div></#if>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/list_item.js?_${.now?string("hhmmss")}"></script>
</#macro>