<#include "../base/_base.ftl">
<#macro title>
专线管理
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="#">专线列表</a></li>
    <li class="active"><a>专线<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="zxLine_form">
    <div class="panel-heading">
        <h4 class="panel-title">专线设置 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="zxLine_form">
            <input type="hidden" name="action" value="${action}">
            <input type="hidden" name="id" value="${(zxLine.id)!}">

            <table class="table table-bordered table-striped">
                <tr>
                    <td><label class="control-label">出发地区</label></td>

                    <td colspan="4">
                    <select id="start_province" style="width: 25%;" class="select2" name="start_province">
                        <option value="0">请选择</option>
                        <#if province??&&province?size gt 0>
                            <#list province as item>
                                <option value="${(item.adcode)!}"
                                        <#if zxLine.start_province??&&zxLine.start_province==item.adcode>selected="selected"</#if>>${(item.name)!}</option>
                            </#list>
                        </#if>
                    </select>
                    <select id="start_city" style="width: 25%;" class="select2" name="start_city">
                        <option value="0">请选择</option>
                        <#if start_city??&&start_city?size gt 0>
                            <#list start_city as item>
                                <option value="${(item.adcode)!}"
                                        <#if zxLine.start_city??&&zxLine.start_city==item.adcode>selected="selected"</#if>>${(item.name)!}</option>
                            </#list>
                        </#if>
                    </select>
                    <select id="start_county" style="width: 25%;" class="select2" name="start_county">
                        <option value="0">请选择</option>
                        <#if start_county??&&start_county?size gt 0>
                            <#list start_county as item>
                                <option value="${(item.adcode)!}"
                                        <#if zxLine.start_county??&&zxLine.start_county==item.adcode>selected="selected"</#if>>${(item.name)!}</option>
                            </#list>
                        </#if>
                    </select>
                </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label class="control-label">终点地区</label></td>
                    <td colspan="4">
                        <select id="end_province" style="width: 25%;" class="select2" name="end_province">
                            <option value="0">请选择</option>
                            <#if province??&&province?size gt 0>
                                <#list province as item>
                                    <option value="${(item.adcode)!}"
                                            <#if zxLine.end_province??&&zxLine.end_province==item.adcode>selected="selected"</#if>>${(item.name)!}</option>
                                </#list>
                            </#if>
                        </select>
                        <select id="end_city" style="width: 25%;" class="select2" name="end_city">
                            <option value="0">请选择</option>
                            <#if end_city??&&end_city?size gt 0>
                                <#list end_city as item>
                                    <option value="${(item.adcode)!}"
                                            <#if zxLine.end_city??&&zxLine.end_city==item.adcode>selected="selected"</#if>>${(item.name)!}</option>
                                </#list>
                            </#if>
                        </select>
                        <select id="end_county" style="width: 25%;" class="select2" name="end_county">
                            <option value="0">请选择</option>
                            <#if end_county??&&end_county?size gt 0>
                                <#list end_county as item>
                                    <option value="${(item.adcode)!}"
                                            <#if zxLine.end_county??&&zxLine.end_county==item.adcode>selected="selected"</#if>>${(item.name)!}</option>
                                </#list>
                            </#if>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="start_address_detail" class="control-label">起点位置:</label></td>
                    <td><input type="text" name="start_address_detail" value="${(zxLine.start_address_detail)!}"
                               class="form-control n-invalid"
                               placeholder="该名称为在APP端展示起点" id="start_address_detail"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="end_address_detail" class="control-label">终点名称:</label></td>
                    <td><input type="text" name="end_address_detail" value="${(zxLine.end_address_detail)!}"
                               class="form-control n-invalid"
                               id="end_address_detail"
                               placeholder="该名称为在APP端展示终点"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="sharing_price" class="control-label">拼车价格:</label></td>
                    <td>
                        <div class="input-group">
                            <input type="text" name="sharing_price" value="${(zxLine.sharing_price)!}"
                                   class="form-control n-invalid"
                                   id="sharing_price" placeholder="该线路拼车价格（在特定公里数之内）">
                            <div class="input-group-addon">每人/元</div>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="price" class="control-label">包车价格:</label></td>
                    <td>
                        <div class="input-group">
                            <input type="text" name="price" value="${(zxLine.price)!}"
                                   class="form-control n-invalid"
                                   id="price" placeholder="该线路包车价格（特定公里数内）">
                            <div class="input-group-addon">元</div>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label>上次设置为</label></td>
                    <td>
                        <div class="input-group">
                            <input type="text" readonly="readonly" value="${(zxLine.setout_time1)!}"
                            <label>至</label>
                            <input type="text" readonly="readonly" value="${(zxLine.setout_time2)!}"
                        </div>
                    </td>
                    <td></td>
                    <td><label>修改时间段设置</label></td>
                    <td>
                        <select class="item_time" data-value="" name="setouttime1">
                            <#list 0..23 as item>
                                <option value="${item?string("00")}">${item?string("00")}</option>
                            </#list>
                        </select>:
                        <select class="item_time" data-value="" name="setouttime3">
                            <#list 0..59 as item>
                                <option value="${item?string("00")}">${item?string("00")}</option>
                            </#list>
                        </select>
                        <label>至</label>
                        <select class="item_time" data-value="" name="setouttime2">
                            <#list 0..23 as item>
                                <option value="${item?string("00")}">${item?string("00")}</option>
                            </#list>
                        </select>:
                        <select class="item_time" data-value="" name="setouttime4">
                            <#list 0..59 as item>
                                <option value="${item?string("00")}">${item?string("00")}</option>
                            </#list>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="sharing_price_special" class="control-label">特殊时间段拼车价格:</label></td>
                    <td>
                        <div class="input-group">
                            <input type="text" name="sharing_price_special" value="${(zxLine.sharing_price_special)!}"
                                   class="form-control n-invalid"
                                   id="sharing_price_special" placeholder="如不需按时间段收费，段设置则价格设置与普通拼车相同">
                            <div class="input-group-addon">每人/元</div>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="price_special" class="control-label">特殊时间段包车价格:</label></td>
                    <td>
                        <div class="input-group">
                            <input type="text" name="price_special" value="${(zxLine.price_special)!}"
                                   class="form-control n-invalid"
                                   id="price_special" placeholder="如不需按时间段收费，则价格设置与普通不拼车相同">
                            <div class="input-group-addon">元</div>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>


                    <td><label for="urgent_phone" class="control-label">线路公里数:</label></td>
                    <td><input type="text" name="distance" value="${(zxLine.distance)!}"
                               class="form-control n-invalid"
                               id="urgent_phone"
                               placeholder="当距离超过该公里数时，将收取额外的价格"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="lineType" class="control-label">专线类型:</label></td>
                        <td>
                            <div class="col-sm-7">
                                <select id="lineType" class="select2" name="lineType"
                                        style="width: 100%;"
                                        data-value="${(zxLine.type)!}">
                                    <option value="0">请选择</option>
                                    <#list serviceTypesItem as item>
                                        <option value="${(item.id)!}"
                                                <#if zxLine.type??&&zxLine.type==item.id>selected="selected"</#if>>${(item.name)!}</option>
                                    </#list>
                                </select>
                            </div>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                </tr>
                <tr>
                     <#if hasRole("super_admin")||hasPermission("select-company")>
                            <td><label for="company" class="control-label">所属公司:</label></td>
                            <td>
                                <select id="company" data-value="${(zxLine.company)!}" class="select2 select-company" name="company" style="width: 100%;">
                                    <option value="">请选择</option>
                                </select>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                     </#if>

                    <td></td>
                    <td>
                        <div class="col-sm-7">
                        </div>
                    </td>
                    <td>
                        <div></div>
                    </td>
                </tr>
            </table>
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
<script type="text/javascript" src="${ctx}/static/js/app/admin/zxLine_item.js?_${.now?string("hhmmss")}"></script>
</#macro>