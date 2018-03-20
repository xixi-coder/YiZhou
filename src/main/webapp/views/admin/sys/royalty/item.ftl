<#include "../../base/_base.ftl">
<#macro title>
提成管理
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/royalty">提成列表</a></li>
    <li class="active"><a>提成<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        提成管理
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <form class="form-horizontal">
            <input type="hidden" name="royaltyStandard.id" value="${(royaltyStandard.id)!}" id="id">
            <div class="row ">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="name" class="col-sm-4 control-label">提成方式:</label>
                    <div class="col-sm-4">
                        <select style="width: 100%;border: 1px solid #ccc;height: 34px;" id="type"
                                name="royaltyStandard.type" data-value="${(royaltyStandard.type)!}">
                            <option value="1">简单模式
                            </option>
                            <option value="2">金额区间模式
                            </option>
                        </select>
                    </div>
                    <div style="padding-top: 10px;color: red;">*</div>
                </div>
            </div>
            <div class="row ">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="name" class="col-sm-4 control-label">名称:</label>
                    <div class="col-sm-4">
                        <input type="text" name="royaltyStandard.name" value="${(royaltyStandard.name)!}"
                               class="form-control n-invalid"
                               id="name"
                               placeholder="名称">
                    </div>
                    <div style="padding-top: 10px;color: red;">*</div>
                </div>
            </div>
            <div class="row">

            </div>
            <div class="row ">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="name" class="col-sm-4 control-label">提成时间点:</label>
                    <div class="col-sm-4">
                        <select style="width: 100%;border: 1px solid #ccc;height: 34px;" id="time_point"
                                name="royaltyStandard.time_point" data-value="${(royaltyStandard.time_point)!}">
                            <option value="1">
                                预约时间
                            </option>
                            <option value="2">
                                开始代驾时间
                            </option>
                            <option value="3">
                                结算时间
                            </option>
                        </select>
                    </div>
                    <div style="padding-top: 10px;color: red;">*</div>
                </div>
            </div>
            <div class="row ">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="name" style="color: red;" class="col-sm-4 control-label">注:</label>
                    <div class="col-sm-7">
                        固定提成 / 提成比例为公司所得提成，不使用固定提成或比例提成请设置为0；</br>若工单实收金额小于填写的最低金额，则公司提成填写的固定金额，不使用请</br>设置为0或不填。
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="panel panel-default" style="border: 1px solid #e5e6e7;">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="col-lg-1"><h5>添加时间段</h5></div>
                                <div class="additional-btn col-lg-11" style="text-align: right;">
                                    <button type="button" id="plus_time" class="btn btn-success">
                                        <i class="glyphicon glyphicon-plus"></i> 添加时间段
                                    </button>
                                </div>
                            </div>

                        </div>
                    </div>
                    <div class="panel-body">
                        <ul id="item_type" class="todo-list ui-sortable">
                            <#if royaltyStandard.type??&&royaltyStandard.type==1&&royaltyStandardEasyList??&&royaltyStandardEasyList?size gt 0>
                                <#list royaltyStandardEasyList as item>
                                    <div style="margin-top: 10px;">
                                        <div style="border-bottom:1px solid #ccc;margin-bottom: 10px; font-weight: bolder;">
                                            时段${(item_index+1)!}
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-xs-12 col-md-12">
                                                <label for="name" class="col-sm-4 control-label">时间:</label>
                                                <div class="col-sm-4">
                                                    <select data-value="${item.start_hour}">
                                                        <#list 0..23 as item>
                                                            <option value="${item?string("00")}">${item?string("00")}</option>
                                                        </#list>
                                                    </select>:
                                                    <select data-value="${item.start_minute}">
                                                        <#list 0..59 as item>
                                                            <option value="${item?string("00")}">${item?string("00")}</option>
                                                        </#list>
                                                    </select>
                                                    <label>至</label>
                                                    <select data-value="${item.end_hour}">
                                                        <#list 0..23 as item>
                                                            <option value="${item?string("00")}">${item?string("00")}</option>
                                                        </#list>
                                                    </select>:
                                                    <select data-value="${item.end_minute}">
                                                        <#list 0..59 as item>
                                                            <option value="${item?string("00")}">${item?string("00")}</option>
                                                        </#list>
                                                    </select>
                                                </div>
                                                <div class="col-sm-4" style="text-align: right;">
                                                    <button type="button" class="btn btn-danger dele_self">
                                                        <i class=" icon-trash-3"></i> 删除
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-xs-12 col-md-12">
                                                <label for="name" class="col-sm-4 control-label">提成（固定）:</label>
                                                <div class="col-sm-2">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control"
                                                               value="${(item.get_money1)}">
                                                        <span class="input-group-addon">元</span>
                                                    </div>
                                                </div>
                                                <div class="col-sm-5">
                                                    <div class="input-group">
                                                        <span class="input-group-addon">低于</span>
                                                        <input type="text" class="form-control"
                                                               value="${(item.less_than_money1)}">
                                                        <span class="input-group-addon">元提</span>
                                                        <input type="text" class="form-control"
                                                               value="${(item.fixed_money)!}">
                                                        <span class="input-group-addon">元</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-xs-12 col-md-12">
                                                <label for="name" class="col-sm-4 control-label">提成（比例）:</label>
                                                <div class="col-sm-2">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control"
                                                               value="${(item.ratio)!}">
                                                        <span class="input-group-addon">%</span>
                                                    </div>
                                                </div>
                                                <div class="col-sm-5">
                                                    <div class="input-group">
                                                        <span class="input-group-addon">低于</span>
                                                        <input type="text" class="form-control"
                                                               value="${(item.less_than_money2)!}">
                                                        <span class="input-group-addon">元提</span>
                                                        <input type="text" class="form-control"
                                                               value="${(item.get_money2)!}">
                                                        <span class="input-group-addon">元</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <form class="form-horizontal">
                                                <div class="form-group col-xs-12 col-md-12">
                                                    <label for="name" class="col-sm-4 control-label">提成类型:</label>
                                                    <div class="radio iradio col-sm-3">
                                                        <label>
                                                            <input type="radio" name="ticheng_type" value="1"
                                                                   <#if item.type==1>checked</#if>/>
                                                            使用固定提成
                                                        </label>
                                                    </div>
                                                    <div class="radio iradio col-sm-3">
                                                        <label>
                                                            <input type="radio" name="ticheng_type" value="2"
                                                                   <#if item.type==2>checked</#if>/>
                                                            使用比例提成
                                                        </label>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </#list>
                            <#elseif royaltyStandard.type??&&royaltyStandard.type==2>
                                <#list royaltyStandardMoney as item>
                                    <div style="margin-top: 10px;">
                                        <div style="border-bottom:1px solid #ccc;margin-bottom: 10px; font-weight: bolder;">
                                            时段${item_index+1}</div>
                                        <div class="row">
                                            <div class="form-group col-xs-12 col-md-12">
                                                <label for="name" class="col-sm-1 control-label">时间:</label>
                                                <div class="col-sm-4">
                                                    <select data-value="${item.start_hour}">
                                                        <#list 0..23 as item>
                                                            <option value="${item?string("00")}">${item?string("00")}</option>
                                                        </#list>
                                                    </select>:
                                                    <select data-value="${item.start_minute}">
                                                        <#list 0..59 as item>
                                                            <option value="${item?string("00")}">${item?string("00")}</option>
                                                        </#list>
                                                    </select>
                                                    <label>至</label>
                                                    <select data-value="${item.end_hour}">
                                                        <#list 0..23 as item>
                                                            <option value="${item?string("00")}">${item?string("00")}</option>
                                                        </#list>
                                                    </select>:
                                                    <select data-value="${item.end_minute}">
                                                        <#list 0..59 as item>
                                                            <option value="${item?string("00")}">${item?string("00")}</option>
                                                        </#list>
                                                    </select>
                                                </div>
                                                <div class="col-sm-7" style="text-align: right;">
                                                    <button type="button" class="btn btn-danger dele_self">
                                                        <i class=" icon-trash-3"></i> 删除
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                        <#if item.item??&&item.item?size gt 0>
                                            <#list item.item as citem>
                                                <div class="row">
                                                    <div class="form-group col-xs-12 col-md-12">
                                                        <div class="col-sm-1"
                                                             style="text-align: right;margin-top:5px;">
                                                            <#if citem_index==0>
                                                                <button type="button"
                                                                        class="btn btn-default btn-xs plus_money">
                                                                    <i class="icon-plus-2"></i> 添加
                                                                </button>
                                                            <#else>
                                                                <button type="button"
                                                                        class="btn btn-default btn-xs remove_money">
                                                                    <i class="icon-minus-1"></i> 删除
                                                                </button>
                                                            </#if>
                                                        </div>
                                                        <div class="col-sm-11">
                                                            <div class="input-group">
                                                                <span class="input-group-addon">从</span>
                                                                <input type="text" class="form-control"
                                                                       value="${(citem.start_money)!}">
                                                                <span class="input-group-addon">元到</span>
                                                                <input type="text" class="form-control money"
                                                                       value="${(citem.end_money)!}">
                                                                <span class="input-group-addon">元，提成方式为</span>
                                                                <select class="form-control ticheng_tt"
                                                                        style="float: left;width: 110px;"
                                                                        data-value="${(citem.type)}">
                                                                    <option value="1">固定提成</option>
                                                                    <option value="2">比例提成</option>
                                                                </select>
                                                                <span class="input-group-addon">每</span>
                                                                <input type="text" class="form-control"
                                                                       value="${(citem.each_money)!}">
                                                                <span class="input-group-addon">元，提</span>
                                                                <input type="text" class="form-control"
                                                                       value="${(citem.get_money)!}">
                                                                <span class="input-group-addon"><#if citem.type==1>
                                                                    元<#else>%</#if></span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </#list>
                                        </#if>
                                    </div>
                                </#list>
                            </#if>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                    </div>
                    <div class="col-sm-2" style="float: right;">
                        <button class="btn btn-success" id="sub_btn" type="button">提交</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript">
    var index = <#if royaltyStandardEasyList??>${royaltyStandardEasyList?size+1}<#elseif royaltyStandardMoney??>${royaltyStandardMoney?size+1}<#else>1</#if>
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/royalty_item.js?_${.now?string("hhmmss")}"></script>
<script id="money_type" type="text/html">
    <div style="margin-top: 10px;">
        <div style="border-bottom:1px solid #ccc;margin-bottom: 10px; font-weight: bolder;">时段{{d.index}}</div>
        <div class="row">
            <div class="form-group col-xs-12 col-md-12">
                <label for="name" class="col-sm-1 control-label">时间:</label>
                <div class="col-sm-4">
                    <select>
                        <#list 0..23 as item>
                            <option value="${item?string("00")}">${item?string("00")}</option>
                        </#list>
                    </select>:
                    <select>
                        <#list 0..59 as item>
                            <option value="${item?string("00")}">${item?string("00")}</option>
                        </#list>
                    </select>
                    <label>至</label>
                    <select>
                        <#list 0..23 as item>
                            <option value="${item?string("00")}">${item?string("00")}</option>
                        </#list>
                    </select>:
                    <select>
                        <#list 0..59 as item>
                            <option value="${item?string("00")}">${item?string("00")}</option>
                        </#list>
                    </select>
                </div>
                <div class="col-sm-7" style="text-align: right;">
                    <button type="button" class="btn btn-danger dele_self">
                        <i class=" icon-trash-3"></i> 删除
                    </button>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="form-group col-xs-12 col-md-12">
                <div class="col-sm-1" style="text-align: right;margin-top:5px;">
                    <button type="button" class="btn btn-default btn-xs plus_money">
                        <i class="icon-plus-2"></i> 添加
                    </button>
                </div>
                <div class="col-sm-11">
                    <div class="input-group">
                        <span class="input-group-addon">从</span>
                        <input type="text" class="form-control" value="0"/>
                        <span class="input-group-addon">元到</span>
                        <input type="text" class="form-control money" value="0"/>
                        <span class="input-group-addon">元，提成方式为</span>
                        <select class="form-control ticheng_tt" style="float: left;width: 110px;">
                            <option value="1" selected="selected">固定提成</option>
                            <option value="2">比例提成</option>
                        </select>
                        <span class="input-group-addon">每</span>
                        <input type="text" class="form-control" value="0"/>
                        <span class="input-group-addon">元，提</span>
                        <input type="text" class="form-control" value="0"/>
                        <span class="input-group-addon">元</span>
                    </div>
                </div>
            </div>
        </div>
    </div>


</script>
<script id="money_type_item" type="text/html">
    <div class="row">
        <div class="form-group col-xs-12 col-md-12">
            <div class="col-sm-1" style="text-align: right;margin-top:5px;">
                <button type="button" class="btn btn-default btn-xs remove_money">
                    <i class="icon-minus-1"></i> 删除
                </button>
            </div>
            <div class="col-sm-11">
                <div class="input-group">
                    <span class="input-group-addon">从</span>
                    <input type="text" value="{{d.start}}" class="form-control" value="0"/>
                    <span class="input-group-addon">元到</span>
                    <input type="text" class="form-control money" value="0"/>
                    <span class="input-group-addon">元，提成方式为</span>
                    <select class="form-control ticheng_tt" style="float: left;width: 110px;">
                        <option value="1" selected="selected">固定提成</option>
                        <option value="2">比例提成</option>
                    </select>
                    <span class="input-group-addon">每</span>
                    <input type="text" class="form-control" value="0"/>
                    <span class="input-group-addon">元，提</span>
                    <input type="text" class="form-control" value="0"/>
                    <span class="input-group-addon">元</span>
                </div>
            </div>
        </div>
    </div>


</script>
<script id="simple_type" type="text/html">
    <form class="form-horizontal">
        <div style="margin-top: 10px;">
            <div style="border-bottom:1px solid #ccc;margin-bottom: 10px; font-weight: bolder;">
                时段{{d.index}}
            </div>
            <div class="row">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="name" class="col-sm-4 control-label">时间:</label>
                    <div class="col-sm-4">
                        <select>
                            <#list 0..23 as item>
                                <option value="${item?string("00")}">${item?string("00")}</option>
                            </#list>
                        </select>:
                        <select>
                            <#list 0..59 as item>
                                <option value="${item?string("00")}">${item?string("00")}</option>
                            </#list>
                        </select>
                        <label>至</label>
                        <select>
                            <#list 0..23 as item>
                                <option value="${item?string("00")}">${item?string("00")}</option>
                            </#list>
                        </select>:
                        <select>
                            <#list 0..59 as item>
                                <option value="${item?string("00")}">${item?string("00")}</option>
                            </#list>
                        </select>
                    </div>
                    <div class="col-sm-4" style="text-align: right;">
                        <button type="button" class="btn btn-danger dele_self">
                            <i class=" icon-trash-3"></i> 删除
                        </button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="name" class="col-sm-4 control-label">提成（固定）:</label>
                    <div class="col-sm-2">
                        <div class="input-group">
                            <input type="text" class="form-control" value="0"/>
                            <span class="input-group-addon">元</span>
                        </div>
                    </div>
                    <div class="col-sm-5">
                        <div class="input-group">
                            <span class="input-group-addon">低于</span>
                            <input type="text" class="form-control" value="0"/>
                            <span class="input-group-addon">元提</span>
                            <input type="text" class="form-control" value="0"/>
                            <span class="input-group-addon">元</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="name" class="col-sm-4 control-label">提成（比例）:</label>
                    <div class="col-sm-2">
                        <div class="input-group">
                            <input type="text" class="form-control" value="0"/>
                            <span class="input-group-addon">%</span>
                        </div>
                    </div>
                    <div class="col-sm-5">
                        <div class="input-group">
                            <span class="input-group-addon">低于</span>
                            <input type="text" class="form-control" value="0"/>
                            <span class="input-group-addon">元提</span>
                            <input type="text" class="form-control" value="0"/>
                            <span class="input-group-addon">元</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="name" class="col-sm-4 control-label">提成类型:</label>
                    <div class="radio iradio col-sm-3">
                        <label>
                            <input type="radio" name="ticheng_type" value="1" checked>
                            使用固定提成
                        </label>
                    </div>
                    <div class="radio iradio col-sm-3">
                        <label>
                            <input type="radio" name="ticheng_type" value="2">
                            使用比例提成
                        </label>
                    </div>
                </div>
            </div>
        </div>
    </form>
</script>
</#macro>