<#include "../../base/_base.ftl">
<#macro title>
收费标准管理
</#macro>
<#macro content>
<link rel="stylesheet" href="${ctx}/static/kindeditor-4.1.7/themes/default/default.css"
      xmlns="http://www.w3.org/1999/html"/>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/chargestandard">收费标准列表</a></li>
    <li class="active"><a>收费标准<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel-body">
    <input type="hidden" name="id" value="${(chargeStandard.id)!}" id="id">
    <div class="panel panel-default">
        <div class="panel-heading">
            <label>名称:</label>
            <input type="text" style="width: 100px;height: 24px;" name="royaltyStandard.name" id="name"
                   value="${(chargeStandard.name)!}"/>
            <#if hasRole("super_admin")||hasPermission("select-company")>
                <td>
                    <label class="control-label">所属公司:</label>
                    <select id="company" class="select2 select-company" data-value="${(chargeStandard.company)!}"
                            style="width: 100px;">
                        <option value="">请选择</option>
                    </select>
                </td>
            </#if>
            <div class="additional-btn">
                <button class="btn btn-info btn-xs save"><i class="glyphicon glyphicon-floppy-disk"></i>保存
                </button>
                <#if type==0>
                    <button class="btn btn-warning btn-xs add_item"><i class="fa fa-plus"></i>添加时间段
                    </button>
                    <button class="btn btn-danger btn-xs del_item"><i class="glyphicon glyphicon-trash"></i>删除
                    </button>
                </#if>

            </div>
        </div>
        <div class="panel-body" id="chargestandard-item">
            <div class="row">
                <div class="form-group">
                    <label>资费说明:</label>
                    <textarea id="desc" class="form-control" id="desc"
                              style="height: 140px; resize: none;">${(chargeStandard.desc)!}</textarea>
                </div>
            </div>
            <#if items??&&items?size gt 0>
                <#list items as item>
                    <div class="row">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <label>开始时间(</label>
                                <select class="item_time" data-value="${item.start_hour}">
                                    <#list 0..23 as item>
                                        <option value="${item?string("00")}">${item?string("00")}</option>
                                    </#list>
                                </select>:
                                <select class="item_time" data-value="${item.start_minute}">
                                    <#list 0..59 as item>
                                        <option value="${item?string("00")}">${item?string("00")}</option>
                                    </#list>
                                </select>
                                <label>至</label>
                                <select class="item_time" data-value="${item.end_hour}">
                                    <#list 0..23 as item>
                                        <option value="${item?string("00")}">${item?string("00")}</option>
                                    </#list>
                                </select>:
                                <select class="item_time" data-value="${item.end_minute}">
                                    <#list 0..59 as item>
                                        <option value="${item?string("00")}">${item?string("00")}</option>
                                    </#list>
                                </select>
                                <label>)起步价(</label>
                                <input style="width: 50px;height: 24px;" name="royaltyStandard.name"
                                       value="${(item.base_amount)?c!}">
                                <label>)元</label>
                                <label>最低消费(</label>
                                <input style="width: 50px;" name="royaltyStandard.name" value="<#if item.min_amount??>${(item.min_amount)?c!}<#else>0</#if>">
                                <label>)元</label>
                                <#if type==0>
                                    <div class="additional-btn">
                                        <button class="btn btn-info btn-xs add_mill">添加里程价格</button>
                                        <button class="btn btn-danger btn-xs del_item">删除</button>
                                    </div>
                                </#if>
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
                                        <tbody class="mill_table">
                                            <#if item.items??&&item.items?size gt 0>
                                                <#list item.items as citem>
                                                <tr>
                                                    <td><#if citem_index==0>
                                                        0
                                                    <#else>
                                                        <input class="mill_item"
                                                               value="${(citem.start)?c!}"/></#if>
                                                    </td>
                                                    <td><input class="mill_item"
                                                               value="${(citem.end)?c!}"/></td>
                                                    <td><input class="mill_item"
                                                               value="${(citem.jiajiajine)?c!}"/></td>
                                                    <td>
                                                        <button class="btn btn-warning btn-xs del_mill_item">
                                                            刪除
                                                        </button>
                                                    </td>
                                                </tr>
                                                </#list>
                                            </#if>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="row">
                                    <table class="table table-bordered table-striped" style="margin-top: 10px;">
                                        <tbody>
                                        <tr>
                                            <td>
                                                <label>超过(</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="${(item.chaoguogonglishu?c)!}"/>
                                                <label>公里),以后每</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="${(item.meiduoshaogongli)?c!}"/>
                                                <label>公里(不足</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="${(item.buzugonglishu)?c!}"/>
                                                <label>公里不计费)，加收</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="${(item.gonglijiashoujine)?c!}"/>
                                                <label>元;</label>
                                            </td>
                                        </tr>
                                        <div id="type0">
                                            <tr>
                                                <td>
                                                    <label>起步后(</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.qibufenzhong)?c!}"/>
                                                    <label>分钟内不收费,以后每</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.meiduoshaofengzhong)?c!}"/>
                                                    <label>分钟(</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.buzufengzhong)?c!}"/>
                                                    <label>分钟不收费)，加收</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.fengzhongjiashoujine)?c!}"/>
                                                    <label>元;</label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <label>免费等侯</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.mianfeidenghoufenzhong)?c!}"/>
                                                    <label>分钟,到达则立即加收</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.denghouchaoguolijijiashoujine)?c!}"/>
                                                    <label>元,后每</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.denghoumeiduoshaofengzhong)?c!}"/>
                                                    <label>分钟(低于</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.denghoudiyuduoshaofenzhong)?c!}"/>
                                                    <label>分钟不计费)加收</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.denghouchaoguojiashoujine)?c!}"/>
                                                    <label>元;</label>
                                                </td>
                                            </tr>
                                        </div>
                                        <div id="type1">
                                            <tr>
                                                <td>
                                                    <label>拼车1人</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.one_distance1)?c!}"/>
                                                    <label>公里之内</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.one_discount1)?c!}"/>
                                                    <label>折，至(</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.one_distance2)?c!}"/>
                                                    <label>公里</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.one_discount2)?c!}"/>
                                                    <label>折，至</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.one_distance3)?c!}"/>
                                                    <label>公里</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.one_discount3)?c!}"/>
                                                    <label>折，</td></tr><tr><td>至</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.one_distance4)?c!}"/>
                                                    <label>公里</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.one_discount4)?c!}"/>
                                                    <label>折，</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.one_distance5)?c!}"/>
                                                    <label>公里以上</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.one_discount5)?c!}"/>
                                                    <label>折；</label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <label>拼车2人</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.two_distance1)?c!}"/>
                                                    <label>公里之内</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.two_discount1)?c!}"/>
                                                    <label>折，至(</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.two_distance2)?c!}"/>
                                                    <label>公里</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.two_discount2)?c!}"/>
                                                    <label>折，至</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.two_distance3)?c!}"/>
                                                    <label>公里</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.two_discount3)?c!}"/>
                                                    <label>折，</td></tr><tr><td>至</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.two_distance4)?c!}"/>
                                                    <label>公里</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.two_discount4)?c!}"/>
                                                    <label>折，</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.two_distance5)?c!}"/>
                                                    <label>公里以上</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.two_discount5)?c!}"/>
                                                    <label>折；</label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <label>拼车3人</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.three_distance1)?c!}"/>
                                                    <label>公里之内</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.three_discount1)?c!}"/>
                                                    <label>折，至</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.three_distance2)?c!}"/>
                                                    <label>公里</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.three_discount2)?c!}"/>
                                                    <label>折，至</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.three_distance3)?c!}"/>
                                                    <label>公里</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.three_discount3)?c!}"/>
                                                    <label>折，</td></tr><tr><td>至</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.three_distance4)?c!}"/>
                                                    <label>公里</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.three_discount4)?c!}"/>
                                                    <label>折，</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.three_distance5)?c!}"/>
                                                    <label>公里以上</label>
                                                    <input class="item_content" style="width: 100px;"
                                                           value="${(item.three_discount5)?c!}"/>
                                                    <label>折；</label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <label>4人及以上按包车价收费</label>
                                                </td>
                                            </tr>
                                        </div>

                                        </tbody>
                                    </table>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="row">
                    <div class="panel panel-default">
                        <div class="panel-heading">

                            <label>开始时间(</label>
                            <select class="item_time">
                                <#list 0..23 as item>
                                    <option value="${item?string("00")}">${item?string("00")}</option>
                                </#list>
                            </select>:
                            <select class="item_time">
                                <#list 0..59 as item>
                                    <option value="${item?string("00")}">${item?string("00")}</option>
                                </#list>
                            </select>
                            <label>至</label>
                            <select class="item_time">
                                <#list 0..23 as item>
                                    <option value="${item?string("00")}">${item?string("00")}</option>
                                </#list>
                            </select>:
                            <select class="item_time">
                                <#list 0..59 as item>
                                    <option value="${item?string("00")}">${item?string("00")}</option>
                                </#list>
                            </select>
                            <label>)起步价(</label>
                            <input style="width: 50px;height: 24px;" name="royaltyStandard.name" value="0">
                            <label>)元</label>
                            <label>最低消费(</label>
                            <input style="width: 50px;height: 24px;" name="royaltyStandard.name" value="0">
                            <label>)元</label>
                            <div class="additional-btn">
                                <button class="btn btn-info btn-xs add_mill">添加里程价格</button>
                                <button class="btn btn-danger btn-xs">删除</button>
                            </div>
                        </div>
                        <div class="panel-body">
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
                                    <tbody class="mill_table">
                                    </tbody>
                                </table>
                            </div>
                            <div class="row">
                                <table class="table table-bordered table-striped" style="margin-top: 10px;">
                                    <tbody>
                                    <tr>
                                        <td>
                                            <label>超过(</label>
                                            <input class="item_content" style="width: 100px;" value="0"/>
                                            <label>公里),以后每</label>
                                            <input class="item_content" style="width: 100px;" value="0"/>
                                            <label>公里(不足</label>
                                            <input class="item_content" style="width: 100px;" value="0"/>
                                            <label>公里不计费)，加收</label>
                                            <input class="item_content" style="width: 100px;" value="0"/>
                                            <label>元;</label>
                                        </td>
                                    </tr>
                                    <div id="type3">
                                        <tr>
                                            <td>
                                                <label>起步后(</label>
                                                <input class="item_content" style="width: 100px;" value="0"/>
                                                <label>分钟内不收费,以后每</label>
                                                <input class="item_content" style="width: 100px;" value="0"/>
                                                <label>分钟(</label>
                                                <input class="item_content" style="width: 100px;" value="0"/>
                                                <label>分钟不收费)，加收</label>
                                                <input class="item_content" style="width: 100px;" value="0"/>
                                                <label>元;</label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label>免费等侯</label>
                                                <input class="item_content" style="width: 100px;" value="0"/>
                                                <label>分钟,到达则立即加收</label>
                                                <input class="item_content" style="width: 100px;" value="0"/>
                                                <label>元,后每</label>
                                                <input class="item_content" style="width: 100px;" value="0"/>
                                                <label>分钟(低于</label>
                                                <input class="item_content" style="width: 100px;" value="0"/>
                                                <label>分钟不计费)加收</label>
                                                <input class="item_content" style="width: 100px;" value="0"/>
                                                <label>元;</label>
                                            </td>
                                        </tr>
                                    </div>
                                    <div id="type4">
                                        <tr>
                                            <td>
                                                <label>拼车1人</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里之内</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折，至</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折，至</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折，</td></tr><tr><td>至</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折，</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里以上</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折；</label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label>拼车2人</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里之内</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折，至</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折，至</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折，</td></tr><tr><td>至</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折，</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里以上</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折；</label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label>拼车3人</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里之内</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折，至</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折，</td></tr><tr><td>至</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折，至</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折，</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>公里以上</label>
                                                <input class="item_content" style="width: 100px;"
                                                       value="0"/>
                                                <label>折；</label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label>4人及以上按包车价收费</label>
                                            </td>
                                        </tr>
                                    </div>

                                    </tbody>
                                </table>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                    </div>
                </div>
            </#if>
        </div>
    </div>
</div>
<script id="item" type="text/html">
    <div class="row">
        <div class="panel panel-default">
            <div class="panel-heading">
                <label>开始时间(</label>
                <select class="item_time">
                    <#list 0..23 as item>
                        <option value="${item?string("00")}">${item?string("00")}</option>
                    </#list>
                </select>:
                <select class="item_time">
                    <#list 0..59 as item>
                        <option value="${item?string("00")}">${item?string("00")}</option>
                    </#list>
                </select>
                <label>至</label>
                <select class="item_time">
                    <#list 0..23 as item>
                        <option value="${item?string("00")}">${item?string("00")}</option>
                    </#list>
                </select>:
                <select class="item_time">
                    <#list 0..59 as item>
                        <option value="${item?string("00")}">${item?string("00")}</option>
                    </#list>
                </select>
                <label>)起步价(</label>
                <input style="width: 50px;" name="royaltyStandard.name" value="0">
                <label>)元</label>
                <label>最低消费(</label>
                <input style="width: 50px;" name="royaltyStandard.name"  value="0">
                <label>)元</label>

                <div class="additional-btn">
                    <button class="btn btn-info btn-xs add_mill">添加里程价格</button>
                    <button class="btn btn-danger btn-xs del_item">删除</button>
                </div>
            </div>
            <div class="panel-body">
                <div class="row">
                    <table class="table table-bordered table-striped">
                        <thead style="font-weight: bolder;">
                        <tr>
                            <td width="25%">开始里程(公里)</td>
                            <td width="25%">结束里程(公里)</td>
                            <td width="25%">增长价(元)</td>
                            <td width="25%">操作</td>
                        </tr>
                        </thead>
                        <tbody class="mill_table">
                        </tbody>
                    </table>
                </div>
                <div class="row">
                    <table class="table table-bordered table-striped" style="margin-top: 10px;">
                        <tbody>
                        <tr>
                            <td>
                                <label>超过(</label>
                                <input class="item_content" style="width: 100px;" value="0"/>
                                <label>公里),以后每</label>
                                <input class="item_content" style="width: 100px;" value="0"/>
                                <label>公里(不足</label>
                                <input class="item_content" style="width: 100px;" value="0"/>
                                <label>公里不计费)，加收</label>
                                <input class="item_content" style="width: 100px;" value="0"/>
                                <label>元;</label>
                            </td>
                        </tr>
                        <div id="type5">
                            <tr>
                                <td>
                                    <label>起步后(</label>
                                    <input class="item_content" style="width: 100px;" value="0"/>
                                    <label>分钟内不收费,以后每</label>
                                    <input class="item_content" style="width: 100px;" value="0"/>
                                    <label>分钟(</label>
                                    <input class="item_content" style="width: 100px;" value="0"/>
                                    <label>分钟不收费)，加收</label>
                                    <input class="item_content" style="width: 100px;" value="0"/>
                                    <label>元;</label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>免费等侯</label>
                                    <input class="item_content" style="width: 100px;" value="0"/>
                                    <label>分钟,到达则立即加收</label>
                                    <input class="item_content" style="width: 100px;" value="0"/>
                                    <label>元,后每</label>
                                    <input class="item_content" style="width: 100px;" value="0"/>
                                    <label>分钟(低于</label>
                                    <input class="item_content" style="width: 100px;" value="0"/>
                                    <label>分钟不计费)加收</label>
                                    <input class="item_content" style="width: 100px;" value="0"/>
                                    <label>元;</label>
                                </td>
                            </tr>
                        </div>


                        <div id="type6">
                            <tr>
                                <td>
                                    <label>拼车1人</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里之内</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折，至</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折，至</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折，</td></tr><tr><td>至</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折，</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里以上</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折；</label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>拼车2人</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里之内</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折，至</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折，至</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折，</td></tr><tr><td>至</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折，</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里以上</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折；</label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>拼车3人</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里之内</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折，至</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折，至</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折，</td></tr><tr><td>至</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折，</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>公里以上</label>
                                    <input class="item_content" style="width: 100px;"
                                           value="0"/>
                                    <label>折；</label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>4人及以上按包车价收费</label>
                                </td>
                            </tr>
                        </div>
                        </tbody>
                    </table>

                </div>
                <div class="clearfix"></div>
            </div>
        </div>
    </div>


</script>
<script id="mill_item" type="text/html">

    <tr>
        <td>
            {{#if (d.isZero){ }}
            0
            {{#}else{ }}
            <input class="mill_item" value="{{d.start}}"/>
            {{#}}}
        </td>
        <td><input class="mill_item" value="0"/></td>
        <td><input class="mill_item" value="0"/></td>
        <td>
            <button class="btn btn-warning btn-xs del_mill_item">刪除</button>
        </td>
    </tr>


</script>
</#macro>
<#macro javascript>
<script>
    var type = ${type}
    if(type==1){
        $('#type0').css("display","block")
        $('#type3').attr("style","display:block")
        $('#type5').hide()
    }else {
        $('#type1').css("display","block")
        $('#type4').attr("style","display:block")
        $('#type6').hide()
    }
</script>
<script charset="utf-8" src="${ctx}/static/kindeditor-4.1.7/kindeditor-min.js"></script>
<script charset="utf-8" src="${ctx}/static/kindeditor-4.1.7/lang/zh_CN.js"></script>
<script type="text/javascript"
        src="${ctx}/static/js/app/admin/chargestandard_item.js?_${.now?string("hhmmss")}"></script>
</#macro>