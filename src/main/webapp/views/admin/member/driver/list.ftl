<#include "../../base/_base.ftl">
<#macro title>
司机列表
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>司机列表</h4>
        <#if hasPermission("sijicheliangguanli")>
            <a href="${ctx}/admin/member/driver/aduitCar">待审核车辆(${count?c}辆未审核)</a>&nbsp;&nbsp;
        </#if>
        <#if hasPermission("sijijiangli")>
            <a href="${ctx}/admin/member/driver/reward">司机奖励</a>&nbsp;&nbsp;
        </#if>
        <#if hasPermission("pliangchongzhi")>
            <a href="${ctx}/admin/member/driver/rechange">司机批量充值</a>&nbsp;&nbsp;
        </#if>
        <#if hasPermission("daishenhesiji")>
            <a href="javascript:void(-1);" id="noAduit">待审核司机</a>&nbsp;&nbsp;</#if>
        <#if hasPermission("daishenheshunfengchesiji")>
            <a href="javascript:void(-1);" id="shunfengche">待审核顺风车司机</a>&nbsp;&nbsp;</#if>
    <#--<a href="${ctx}/admin/member/driver/drivermap" target="_blank">司机分布图</a>-->
    </div>
    <div class="panel-body">
        <div class="table-responsive">
            <form id="searchForm_">
                <table class="table table-bordered table-striped">
                    <tr>
                        <td width="80">
                            <div class="form-group">
                                <label for="s-info.real_name-LIKE" class="control-label">司机姓名:</label>
                                <input name="s-info.real_name-LIKE"/>
                                <label for="s-login.user_name-LIKE" class="control-label">用户名:</label>
                                <input name="s-login.user_name-LIKE"/>
                                <label for="s-login.user_name-LIKE" class="control-label">账户状态:</label>
                                <select name="s-info.status-EQ" style="height: 26px;">
                                    <option value="ISNULL">待审核</option>
                                    <option value="1" selected="selected">审核通过</option>
                                    <option value="2">审核未通过</option>
                                    <option value="3">冻结</option>
                                </select>
                                <label for="s-login.status-EQ" class="control-label">登陆状态:</label>
                                <select name="s-login.status-EQ" style="height: 26px;">
                                    <option value="">请选择</option>
                                    <option value="1">已登录</option>
                                    <option value="0">未登录</option>
                                    <option value="3">上线中</option>
                                    <option value="6">忙碌中</option>
                                </select>

                            </div>

                        </td>
                        <td width="80">

                            <div class="form-group">
                                <label for="s-inst.user_name-LIKE" class="control-label">推荐人手机号:</label>
                                <input name="s-inst.user_name-LIKE"/>
                            </div>

                        </td>
                        <input type="hidden" name="s-info.level-EQ" id="shunfen">
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
                            <a class="btn btn-info" href="${ctx}/admin/member/driver/item">添加</a>
                            <button type="button" class="btn btn-info search">搜索</button>
                            <a class="btn btn-info" href="${ctx}/admin/member/driver/export">导出全部司机</a>
                            <#if hasPermission("huiyuandaochu")>
                                <button type="button" class="btn btn-primary button-margin"
                                        onclick="javascript:method1('driver-list')">
                                    <i class="fa fa-file-excel-o"></i>&nbsp;导出此页面司机
                                </button>
                            </#if>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="table-responsive">
            <table id="driver-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th></th>
                    <th width="80">操作</th>
                    <th width="40">姓名</th>
                    <th>用户名</th>
                    <th>状态</th>
                    <th>审核状态</th>
                    <th>家庭住址</th>
                    <th>所属公司</th>
                    <th>电话</th>
                    <th>推荐人手机号</th>
                    <th>余额(元)</th>
                    <th width="40">性别</th>
                    <th>司机类型</th>
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
        <button type="button" class="btn btn-success btn-xs dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only"></span></button>
        <ul class="dropdown-menu success btn-xs" role="menu">
            {{# if(d.level==1){ }}
            <#if hasPermission("daishenheshunfengchesiji")>
                <li><a class="shunfengche" href="javascript:void(0);" data-id="{{d.id}}">顺风车审核</a></li></#if>
            {{# } }}
            <#if hasPermission("sijibianji")|| hasPermission("sijichakan")>
                <li><a href="${ctx}/admin/member/driver/item/{{d.id}}">编辑和查看</a></li></#if>
            <#if hasPermission("sijicheliangguanli")>
                <li><a href="${ctx}/admin/member/driver/drivercar/{{d.id}}">车辆信息</a></li></#if>
            <#if hasPermission("sijizhengjianxinxin")>
                <li><a href="${ctx}/admin/member/driver/license/{{d.id}}">司机证件信息</a></li></#if>
            <#if hasPermission("zhanghuliushui")>
                <li><a href="${ctx}/admin/member/driver/capital/{{d.id}}">账户流水</a></li></#if>
            <#if hasPermission("pinglunxinxi")>
                <li><a href="${ctx}/admin/member/driver/grade/{{d.id}}">评论信息</a></li></#if>
            <#if hasPermission("sijichongzhi")>
                <li><a class="amount" href="javascript:void(0);" data-id="{{d.id}}" data-realname="{{d.realName}}"
                       data-amount="{{d.amount}}">充值</a></li></#if>
            <#if hasPermission("sijixiugaimima")>
                <li><a class="updatePw" href="javascript:void(0);" data-id="{{d.id}}">修改密码</a></li></#if>
            <#if hasPermission("sijichongzhimima")></#if>
            <li><a class="rePw" href="javascript:void(0);" data-id="{{d.id}}">重置密码</a></li>
            <#if hasRole("super_admin")||hasPermission("foz-driver-action")>
                <li>
                    <a class="frozen" href="javascript:void(0);" data-id="{{d.id}}" data-status="{{d.status}}">
                        {{# if(d.status==1){ }}
                        冻结
                        {{# }else{ }}
                        解冻
                        {{# } }}

                    </a>
                </li></#if>
            <#if hasPermission("sijiqiangzhixiaxian")>
                <li><a class="offline" href="javascript:void(0);" data-id="{{d.id}}">强制下线</a></li></#if>
            {{# if(d.inst_phone!=null){ }}
            <#if hasPermission("sijichongzhituijianren")>
                <li><a class="delinst" href="javascript:void(0);" data-id="{{d.id}}">删除推荐人绑定</a></li>
            </#if>
            {{# } }}

            <#if hasPermission("sijichongzhishebei")>
                <li><a class="resetdevice" href="javascript:void(0);" data-id="{{d.id}}">重置设备</a></li></#if>
            <#if hasPermission("sijichongzhigongsi")>
                <li><a class="resetcompany" href="javascript:void(0);" data-id="{{d.id}}">重置公司</a></li></#if>
            <#if hasRole("super_admin")||hasPermission("select-company")>
            <#else>
                <li><a href="${ctx}/admin/sys/user/item/0-{{d.login_id}}" data-id="{{d.id}}">关联管理员</a></li>
            </#if>
            <#if hasPermission("sijishanchu")>
                <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除<span class="warning{{d.id}}" style="color: #880000" onmouseover="warning_message({{d.id}})" data-id="{{d.id}}"> ★</span></a></li></#if>
        </ul>
    </div>
</script>

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
<script id="frozen_tpl" type="text/x-handlebars-template">
    <form id="frozen_form" action="" autocomplete="off">
        <table style="table table-bordered table-striped">
            <tr>
                <td colspan="2">
                    <input type="hidden" value="{{d.id}}" name="id"/>
                    <input type="radio" class="frozenType" checked="checked" name="frozenType" value="0"/>&nbsp;永久冻结
                    &nbsp;&nbsp;<input type="radio" class="frozenType" name="frozenType" value="1"/>&nbsp;期限冻结
                </td>
            </tr>
            <tr class="qixianxuanze" style="display: none;">
                <td colspan="2">
                    <div style="padding-top: 10px;">
                        <select name="unitType" class="form-control input-group-addon unit"
                                data-unit="first_unit"
                                style="float: left;width: 80px;" data-value="">
                            <option value="天">天</option>
                            <option value="小时">小时</option>
                        </select>
                        <div class="input-group" style="width: 100px;">
                            <input type="number" name="counts"
                                   class="form-control"
                                   placeholder=""/>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div style="padding-top: 10px;">
                        <label for="remark" class="col-sm-4 control-label">备注:</label>
                    </div>
                </td>
                <td>
                    <div class="col-sm-7" style="padding-top: 10px;">
                        <textarea name="remark"></textarea>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</script>
</#macro>
<#macro javascript>
<script type="text/javascript">
    function warning_message(id){
        //tips层
        layer.tips('此项为敏感项，请谨慎操作！', '.warning'+id,{tips:[3,"red"]});
    }
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/driver.js?_${.now?string("hhmmss")}"></script>
</#macro>