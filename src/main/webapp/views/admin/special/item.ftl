<#include "../base/_base.ftl">
<#macro title>
专线修改
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/special/">专线管理</a></li>
    <li class="active"><a>专线编辑</a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">专线修改</h4>
    </div>
    <div class="panel-body">
        <#--<div class="col-sm-6" >-->
            <form class="form-horizontal" method="post" role="form" autocomplete="off"
                  id="createLine_form">
                <table data-sortable="" style="border:1px solid #ddd;" class="table table-hover table-striped"
                       data-sortable-initialized="true">
                    <#if hasRole("super_admin")||hasPermission("select-company")>
                        <tr>
                            <td><label for="company" class="control-label">公司选择:</label></td>
                            <td>
                                <select class="select2" name="company" id="companySelect"  data-value="${(specialCars.company_id)!}" style="width: 100%;">
                                    <#list companies as item>
                                        <option value="${(item.id)!}">${(item.name)!}</option>
                                    </#list>
                                </select>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                    </#if>
                    <tr>
                        <td><label for="company" class="control-label">专线类型选择:</label></td>
                        <td>
                            <select class="select2" name="serviceTypeItems" id="serviceTypeItems" data-value="${(specialCars.type)!}" style="width: 100%;">
                                <#list serviceTypeItems as item>
                                    <option value="${(item.id)!}">${(item.name)!}</option>
                                </#list>
                            </select>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="reservation_address" class="control-label">展示起点位置:</label></td>
                        <td>
                            <div class="input-group">
                                <input type="text" name="special.reservation_address" value="${(specialCars.reservationAddress)!}"
                                       class="form-control n-invalid"
                                       id="reservation_address"
                                       placeholder="注：修改此处只会改变展示地点，公里数与预约时间将不会改变">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" >起点</button>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="destination" class="control-label">展示终点位置:</label></td>
                        <td>
                            <div class="input-group">
                                <input type="text" name="special.destination" value="${(specialCars.destination)!}"
                                       class="form-control n-invalid"
                                       id="destination"
                                       placeholder="注：修改此处只会改变展示地点，公里数与预约时间将不会改变">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" >终点</button>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="real_name" class="control-label">拼车价格:</label></td>
                        <td>
                            <div class="input-group">
                                <input type="text" name="special.sharing_price" value="${(specialCars.sharing_price)!}"
                                       class="form-control n-invalid"
                                       id="sharing_price">
                                <div class="input-group-addon">每人/元</div>
                            </div>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="real_name" class="control-label">不拼车价格:</label></td>
                        <td>
                            <div class="input-group">
                                <input type="text" name="special.index_price" value="${(specialCars.index_price)!}"
                                       class="form-control n-invalid"
                                       id="index_price">
                                <div class="input-group-addon">元</div>
                            </div>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>

                    <tr>
                        <td><label>特殊时间段设置</label></td>
                        <td>
                            <select class="item_time" data-value="${(specialCars.start_hour)!}" name="special.setouttime1">
                                <#list 0..23 as item>
                                    <option value="${item?string("00")}">${item?string("00")}</option>
                                </#list>
                            </select>:
                            <select class="item_time" data-value="${(specialCars.start_minute)!}" name="special.setouttime3">
                                <#list 0..59 as item>
                                    <option value="${item?string("00")}">${item?string("00")}</option>
                                </#list>
                            </select>
                            <label>至</label>
                            <select class="item_time" data-value="${(specialCars.end_hour)!}" name="special.setouttime2">
                                <#list 0..23 as item>
                                    <option value="${item?string("00")}">${item?string("00")}</option>
                                </#list>
                            </select>:
                            <select class="item_time" data-value="${(specialCars.end_minute)!}" name="special.setouttime4">
                                <#list 0..59 as item>
                                    <option value="${item?string("00")}">${item?string("00")}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td><label for="real_name" class="control-label">特殊时间段拼车价格:</label></td>
                        <td>
                            <div class="input-group">
                                <input type="text" name="special.sharing_esyimated_price" value="${(specialCars.sharing_esyimated_price)!}"
                                       class="form-control n-invalid"
                                       id="sharing_esyimated_price" placeholder="如不需按时间段收费，设置则价格设置与普通拼车相同">
                                <div class="input-group-addon">每人/元</div>
                            </div>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="real_name" class="control-label">特殊时间段不拼车价格:</label></td>
                        <td>
                            <div class="input-group">
                                <input type="text" name="special.estimated_price" value="${(specialCars.estimated_price)!}"
                                       class="form-control n-invalid"
                                       id="estimated_price" placeholder="如不需按时间段收费，则价格设置与普通不拼车相同">
                                <div class="input-group-addon">元</div>
                            </div>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>

                    <tr>
                        <td><label for="real_name" class="control-label">备注:</label></td>
                        <td>
                            <textarea style="width: 99%;" rows="10" name="special.tag">${(specialCars.tag)!}</textarea>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>
                    <tr id="show_mill" style="display: none;">
                    </tr>
                    <tr>
                        <td colspan="3">
                            <button type="button" id="updateLine" class="btn btn-primary btn-lg btn-block">修改路线
                            </button>
                        </td>
                    </tr>
                </table>
                <input type="hidden" hidden="hidden" name="special.id" value="${(specialCars.id)!}">
            </form>
        <#--</div>-->
    </div>
</div>
</#macro>
<#macro javascript>
<script src="${ctx}/static/js/app/admin/special_item.js?_${.now?string("HHmmss")}"></script>
</#macro>