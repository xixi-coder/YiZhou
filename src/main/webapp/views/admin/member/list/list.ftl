<#include "../../base/_base.ftl">
<#macro title>
会员列表
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>会员列表</h4>
    </div>
    <div class="panel-body">
        <div class="table-responsive">
            <form id="searchForm_">
                <table class="table table-bordered table-striped">
                    <tr>
                        <td width="80">
                            <div class="form-group">
                                <label for="s-info.real_name-LIKE" class="control-label">会员姓名:</label>
                                <input name="s-info.real_name-LIKE"/>
                                <label for="s-login.user_name-LIKE" class="control-label">用户名:</label>
                                <input name="s-login.user_name-LIKE"/>
                            </div>
                        </td>
                        <td width="80">

                            <div class="form-group">
                                <label for="s-inst.user_name-LIKE" class="control-label">推荐人手机号:</label>
                                <input name="s-inst.user_name-LIKE"/>
                            </div>

                        </td>
                        <td>
                            <div class="form-group">
                                <label for="s-info.status-EQ" class="control-label">会员状态:</label>
                                <select name="s-info.status-EQ">
                                    <option value="">请选择</option>
                                    <option value="1">正常用户</option>
                                    <option value="2">异常用户</option>
                                </select>
                            </div>
                        </td>
                        <#if hasRole("super_admin")||hasPermission("select-company")>
                            <td>
                                <label for="real_name" class="control-label">公司选择:</label>
                                <select class="select2 select-company" name="s-info.company-EQ"
                                        style="width: 100px;">
                                    <option value="">请选择</option>
                                </select>
                            </td>
                        </#if>
                        <td>
                            <#if hasPermission("huiyuantianjia")>
                                <a class="btn btn-info" href="${ctx}/admin/member/list/item">添加</a>
                            </#if>
                            <button class="btn btn-info search" type="button">搜索</button>
                            <#if hasPermission("huiyuandaochu")>
                                <a class="btn btn-info" href="${ctx}/admin/member/list/export">导出全部会员</a>
                            </#if>
                            <#if hasPermission("huiyuandaochu")>
                                <button type="button" class="btn btn-primary button-margin"
                                        onclick="javascript:method1('mem-list')">
                                    <i class="fa fa-file-excel-o"></i>&nbsp;导出此页面会员
                                </button>
                            </#if>

                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="table-responsive">
            <table id="mem-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th></th>
                    <th width="80">操作</th>
                    <th width="40">姓名</th>
                    <th>用户名</th>
                    <th>家庭住址</th>
                    <th>所属公司</th>
                    <th>电话</th>
                    <th>推荐人电话</th>
                    <th>余额(元)</th>
                    <th>vip余额(元)</th>
                    <th>vip历史余额(元)</th>
                    <!--<th>邮箱</th>-->
                    <th width="40">性别</th>
                    <!--<th>会员类型</th>>
                    <th>紧急电话</th>
                    <th>紧急联系人</th>
                    <th>邮政编码</th>
                    <th>设备号</th>
                    <th>设备类型</th>-->
                    <th>注册时间</th>
                    <th>最后登陆时间</th>
                    <th width="80">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script id="action_tpl" type="text/x-handlebars-template">
    <div class="btn-group">
        <button type="button" class="btn btn-success btn-xs">操作</button>
        <button type="button" class="btn btn-success dropdown-toggle btn-xs" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only"></span></button>
        <ul class="dropdown-menu success btn-xs" role="menu">
            <#if hasPermission("huiyuanchakan")||hasPermission("huiyuanbianji")>
                <li><a href="${ctx}/admin/member/list/item/{{d.id}}">编辑和查看</a></li></#if>
            <#if hasPermission("huiyuanzhanghuliushui")>
                <li><a href="${ctx}/admin/member/list/capital/{{d.id}}">账户流水</a></li></#if>
            <#if hasPermission("huiyuanzhanghuliushui")>
             <li><a href="${ctx}/admin/member/list/vipCapitalToList/{{d.id}}">vip流水</a></li>
            </#if>
            <#if hasPermission("huiyuanpingjiaxingxi")>
                <li><a href="${ctx}/admin/member/list/grade/{{d.id}}">评论信息</a></li></#if>

            <li><a href="${ctx}/admin/member/list/complain/{{d.id}}">投诉回复</a></li>

            <#if hasPermission("huiyuanchongzhi")>
                <li><a class="amount" href="javascript:void(0);" data-id="{{d.id}}" data-realname="{{d.realName}}"
                       data-amount="{{d.amount}}">充值</a></li></#if>
            <#if hasPermission("huiyuanxiugaimima")>
                <li><a class="updatePw" href="javascript:void(0);" data-id="{{d.id}}">修改密码</a></li></#if>
            <#if hasPermission("huiyuanchongzhimima")>
                <li><a class="rePw" href="javascript:void(0);" data-id="{{d.id}}">重置密码</a></li></#if>
            <#if hasPermission("huiyuanshanchu")>
                <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除<span class="warning{{d.id}}"
                                                                                            style="color: #880000"
                                                                                            onmouseover="warning_message({{d.id}})"
                                                                                            data-id="{{d.id}}"> ★</span></a>
                </li></#if>
            {{# if(d.inst_phone!=null){ }}
            <#if hasPermission("huiyuanchongzhituijianren")>
                <li><a class="delinst" href="javascript:void(0);" data-id="{{d.id}}">删除推荐人绑定</a></li>
            </#if>
            {{# }else{ }}
            <#if hasPermission("huiyuantianjiatuijianren")>
                <li><a class="addinst" href="javascript:void(0);" data-id="{{d.id}}" data-realname="{{d.realName}}"
                       data-amount="{{d.amount}}">添加推荐人绑定</a></li>
            </#if>
            {{# } }}
            <#if hasPermission("huiyuanchongzhishebei")>
                <li><a class="resetdevice" href="javascript:void(0);" data-id="{{d.id}}">重置设备</a></li></#if>
            <#if hasPermission("huiyuanchongzhigongsi")>
                <li><a class="resetcompany" href="javascript:void(0);" data-id="{{d.id}}">重置公司</a></li></#if>
            <#if hasPermission("huiyuansheweiyichang")>
                <li><a class="blackAdd" href="javascript:void(0);" data-status="{{d.status}}" data-id="{{d.id}}">
                    {{#if(d.status==1){ }}
                    设为异常
                    {{#} else if(d.status==2){ }}
                    解除异常
                    {{#} }}
                </a></li></#if>
        </ul>
    </div>
</script>
<#--<script id="updatePw_tpl" type="text/x-handlebars-template">-->
<#--<form id="password_form" action="" autocomplete="off">-->
<#--<input type="hidden" name="memberInfo.id" value="{{d.id}}"/>-->
<#--<div class="form-group">-->
<#--<label for="oldpassword" class="col-sm-4 control-label">旧密码:</label>-->
<#--<div class="col-sm-7">-->
<#--<input type="password" name="oldpassword" class="form-control n-invalid" id="oldpassword"-->
<#--placeholder="旧密码" data-rule="required;password">-->
<#--</div>-->
<#--<div style="padding-top: 10px;color: red;">*</div>-->
<#--</div>-->
<#--<div class="form-group">-->
<#--<label for="password" class="col-sm-4 control-label">密码:</label>-->
<#--<div class="col-sm-7">-->
<#--<input type="password" name="password" class="form-control n-invalid" id="password"-->
<#--placeholder="密码"-->
<#--data-rule="required;password"-->
<#-->-->
<#--</div>-->
<#--<div style="padding-top: 10px;color: red;">*</div>-->
<#--</div>-->
<#--<div class="form-group">-->
<#--<label for="repassword" class="col-sm-4 control-label">确认密码:</label>-->
<#--<div class="col-sm-7">-->
<#--<input type="password" name="repassword" class="form-control n-invalid" id="repassword"-->
<#--placeholder="确认密码"-->
<#--data-msg-match="两次输入密码不一致!"-->
<#--data-rule="required;match(password);">-->
<#--</div>-->
<#--<div style="padding-top: 10px;color: red;">*</div>-->
<#--</div>-->
<#--</form>-->
<#--</script>-->
<script id="amount_tpl" type="text/x-handlebars-template">
    <form id="amount_form" action="" autocomplete="off">
        <table style="border-collapse:separate;border-spacing:10px;">
            <tr>
                <td>
                    <label for="amount" class="col-sm-4 control-label">充值金额:</label>
                </td>
                <td>
                    <div class="col-sm-7">
                        <input type="text" name="amount"
                               placeholder="如：500">
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <label for="remark" class="col-sm-4 control-label">备注:</label>
                </td>
                <td>
                    <div class="col-sm-7">
                        <textarea name="remark"></textarea>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</script>
<script id="addinst_tpl" type="text/x-handlebars-template">
    <form id="addinst_form" action="" autocomplete="off">
        <table style="border-collapse:separate;border-spacing:10px;">
            <tr>
                <td>
                    <label for="amount" class="col-sm-4 control-label">推荐人手机号:</label>
                </td>
                <td>
                    <div class="col-sm-7">
                        <input type="text" name="phone"
                               placeholder="如：18123456789">
                    </div>
                </td>
            </tr>
        </table>
    </form>
</script>
</#macro>
<#macro javascript>

<script type="text/javascript">
    function warning_message(id) {
        //tips层
        layer.tips('此项为敏感项，请谨慎操作！', '.warning' + id, {tips: [3, "red"]});
    }
</script>

<script type="text/javascript" src="${ctx}/static/js/app/admin/list.js?_${.now?string("hhmmss")}"></script>
</#macro>