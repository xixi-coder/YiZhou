<#include "../../base/_base.ftl">
<#macro title>
顺风车收费标准管理
</#macro>
<#macro content>
<link rel="stylesheet" href="${ctx}/static/kindeditor-4.1.7/themes/default/default.css"/>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/chargestandard">收费标准列表</a></li>
    <li class="active"><a>顺风车收费标准<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel-body">
<form class="form-horizontal" method="post" role="form" autocomplete="off"
      id="charge_form">
    <input type="hidden" name="chargeStandard.id" value="${(chargeStandard.id)!}" id="id">
    <input type="hidden" name="chargestandarditem.id" value="${(chargestandarditem.id)!}" id="ids">
    <div class="panel panel-default">
        <div class="panel-heading">
            <label>名称:</label>
            <input type="text" style="width: 100px;height: 24px;" name="chargeStandard.name" id="name"
                   value="${(chargeStandard.name)!}"/>
            <#if hasRole("super_admin")||hasPermission("select-company")>
                <td>
                    <label class="control-label">所属公司:</label>
                    <select id="company" class="select2 select-company" data-value="${(chargeStandard.company)!}""
                            style="width: 100px;">
                        <option value="">请选择</option>
                    </select>
                </td>
            </#if>
        </div>
        <div class="panel-body" id="chargestandard-item">
                  <div class="row">
                    <label>资费说明:</label>
                    <textarea id="desc" class="form-control" id="desc" name="chargeStandard.desc"
                              style="height: 140px; resize: none;>${(chargeStandard.desc)!}</textarea>
                  </div>
                    <div class="row">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <label>起步价(</label>
                                <input style="width: 50px;height: 24px;" name="chargestandarditem.base_amount"
                                       value="${(chargestandarditem.base_amount)!}">
                                <label>)元</label>
                                <label>最低消费(</label>
                                <input style="width: 50px;" name="chargestandarditem.min_amount" value="<#if chargestandarditem.min_amount??>${(chargestandarditem.min_amount)!}<#else>0</#if>">
                                <label>)元</label>
                                <#--<div class="additional-btn">
                                    <button class="btn btn-info btn-xs add_mill">添加里程价格</button>
                                    <button class="btn btn-danger btn-xs del_item">删除</button>
                                </div>-->
                            </div>
                            <div class="panel-body" id="chargestandard-item">
                                <div class="row">
                                    <table class="table table-bordered table-striped mill_table">
                                        <thead style="font-weight: bolder;">
                                        <tr>
                                            <td width="25%">开始里程(公里)</td>
                                            <td width="25%">结束里程(公里)</td>
                                            <td width="25%">增长价(元)</td>
                                            <td width="25%">操作</td>
                                        </tr>
                                        </thead>
                                </div>
                                <div class="row">
                                    <table class="table table-bordered table-striped" style="margin-top: 10px;">
                                        <tbody>
                                        <tr>
                                            <td>
                                                <label>超过(</label>
                                                <input class="item_content" style="width: 100px;" name="chargestandarditem.chaoguogonglishu"
                                                       value="${(chargestandarditem.chaoguogonglishu)!}"/>
                                                <label>公里),以后每</label>
                                                <input class="item_content" style="width: 100px;" name="chargestandarditem.meiduoshaogongli"
                                                       value="${(chargestandarditem.meiduoshaogongli)!}"/>
                                                <label>公里(不足</label>
                                                <input class="item_content" style="width: 100px;" name="chargestandarditem.buzugonglishu"
                                                       value="${(chargestandarditem.buzugonglishu)!}"/>
                                                <label>公里不计费)，加收</label>
                                                <input class="item_content" style="width: 100px;" name="chargestandarditem.gonglijiashoujin"
                                                       value="${(chargestandarditem.gonglijiashoujine)!}"/>
                                                <label>元;</label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label>拼车(1人</label>
                                                <input class="item_content" style="width: 100px;" name="chargestandarditem.one_discount"
                                                       value="${(chargestandarditem.one_discount)!}"/>
                                                <label>折，2人</label>
                                                <input class="item_content" style="width: 100px;" name="chargestandarditem.two_discount"
                                                       value="${(chargestandarditem.two_discount)!}"/>
                                                <label>折，3人(</label>
                                                <input class="item_content" style="width: 100px;" name="chargestandarditem.three_discount"
                                                       value="${(chargestandarditem.three_discount)!}"/>
                                                <label>折)，满4人按包车价计算
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                        </div>
                    </div>
            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                    </div>
                    <#if hasPermission("huiyuanbianji")>
                        <div class="col-sm-2" style="float: right;">
                            <button class="btn btn-success" type="submit">提交</button>
                        </div></#if>
                </div>
            </div>
    </form>
</div>
</#macro>
<#macro javascript>
<script charset="utf-8" src="${ctx}/static/kindeditor-4.1.7/kindeditor-min.js"></script>
<script charset="utf-8" src="${ctx}/static/kindeditor-4.1.7/lang/zh_CN.js"></script>
<script type="text/javascript"
        src="${ctx}/static/js/app/admin/chargestandard_items.js?_${.now?string("hhmmss")}"></script>
</#macro>