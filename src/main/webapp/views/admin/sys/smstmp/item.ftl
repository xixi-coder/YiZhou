<#include "../../base/_base.ftl">
<#macro title>
短信模板
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/smstmp">短信模板</a></li>
    <li class="active"><a>模板<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">短信模板设置 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="smstmp_form">
            <input type="hidden" name="smstmp.id" value="${(smstmp.id)!}" id="id">
            <input type="hidden" name="servicetype.id" value="${(servicetype.id)!}">
            <input type="hidden" name="action" value="${action}">
            <table class="table table-bordered table-striped">
                <tr>
                    <td><label for="type" class="control-label">模板类型:</label></td>
                    <td>
                        <select id="type" class="select2" name="smstmp.type" style="width: 100%;">
                            <option value="0">请选择</option>
                            <option value=1
                                    <#if smstmp.type??&&smstmp.type==1>selected="selected"</#if>>注册短信模板
                            </option>
                            <option value=2
                                    <#if smstmp.type??&&smstmp.type==2>selected="selected"</#if>>忘记密码短信模板
                            </option>
                            <option value=3
                                    <#if smstmp.type??&&smstmp.type==3>selected="selected"</#if>>更换手机号短信模板
                            </option>
                            <option value=4
                                    <#if smstmp.type??&&smstmp.type=4>selected="selected"</#if>>司机接单短信模板
                            </option>
                            <option value=5
                                    <#if smstmp.type??&&smstmp.type=5>selected="selected"</#if>>司机到达预约地短信模板
                            </option>
                            <option value=6
                                    <#if smstmp.type??&&smstmp.type=6>selected="selected"</#if>>司机订单完成短信模板
                            </option>
                            <option value=7
                                    <#if smstmp.type??&&smstmp.type=7>selected="selected"</#if>>司机有新订单短信模板
                            </option>
                            <option value=8
                                    <#if smstmp.type??&&smstmp.type=8>selected="selected"</#if>>司机充值短信模板
                            </option>
                            <option value=9
                                    <#if smstmp.type??&&smstmp.type=9>selected="selected"</#if>>注册短信模板
                            </option>
                            <option value=10
                                    <#if smstmp.type??&&smstmp.type=10>selected="selected"</#if>>取消定单短信模板
                            </option>
                            <option value=11
                                    <#if smstmp.type??&&smstmp.type=11>selected="selected"</#if>>活动奖励短信模板
                            </option>
                            <option value=12
                                    <#if smstmp.type??&&smstmp.type=12>selected="selected"</#if>>司机余额不足短信模板
                            </option>
                            <option value=13
                                    <#if smstmp.type??&&smstmp.type=13>selected="selected"</#if>>保险单号短信模板
                            </option>
                            <option value=14
                                    <#if smstmp.type??&&smstmp.type=14>selected="selected"</#if>>预约订单短信提醒司机模板
                            </option>
                            <option value=15
                                    <#if smstmp.type??&&smstmp.type=15>selected="selected"</#if>>预约订单短信提醒乘客模板
                            </option>
                            <option value=16
                                    <#if smstmp.type??&&smstmp.type=16>selected="selected"</#if>>预约订单要被销单前5分钟提醒司机模板
                            </option>
                            <option value=18
                                    <#if smstmp.type??&&smstmp.type=18>selected="selected"</#if>>推送订单司机模板
                            </option>
                            <option value=19
                                    <#if smstmp.type??&&smstmp.type=19>selected="selected"</#if>>微信下单模板
                            </option>
                            <option value=20
                                    <#if smstmp.type??&&smstmp.type=20>selected="selected"</#if>>司机审核通过模板
                            </option>
                            <option value=21
                                    <#if smstmp.type??&&smstmp.type=21>selected="selected"</#if>>司机审核不通过模板
                            </option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="content" class="control-label">短信内容:</label></td>
                    <td>
                        <textarea name="smstmp.content" class="form-control n-invalid" id="content"
                                  placeholder="短信内容" style="height: 80px">${(smstmp.content)!}</textarea>
                    </td>
                    <td>
                        <div style="..."></div>
                    </td>
                </tr>


                <tr>
                    <td><label for="company" class="control-label">所属公司:</label></td>

                    <td>
                        <select id="company" class="select2" name="smstmp.company" style="width: 100%;">
                            <option value="0">请选择</option>
                            <#list company as item>
                                <option value="${(item.id)!}"
                                        <#if smstmp.company??&&smstmp.company==item.id>selected="selected"</#if>>${(item.name)!}</option>
                            </#list>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td colspan="3"></td>
                </tr>
            </table>

            </tr>

            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                    </div>
                    <div class="col-sm-2" style="float: right;">
                        <button class="btn btn-success" type="submit">提交</button>
                    </div>
                </div>
            </div>

        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/smstmp_item.js?_${.now?string("hhmmss")}"></script>
</#macro>