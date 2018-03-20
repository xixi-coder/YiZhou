<#include "../../base/_base.ftl">
<#macro title>
优惠券管理
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>优惠券管理</h4>
    </div>
    <div class="panel-body">
        <div class="table-responsive" style="overflow:auto;">
            <table class="table table-bordered table-striped">
                <tr>
                    <td width="100">
                        <form id="searchForm_">
                            <div class="form-group">
                                <label for="s-coupon_title-LIKE" class="control-label">优惠券标题:</label>
                                <input name="s-coupon_title-LIKE"/>

                                <td>
                                    <label for="real_name" class="control-label">公司选择:</label>
                                    <select class="select2 select-company" style="width: 200px;" name="s-da.company-EQ"
                                            id="companySelect">
                                        <option value="">请选择</option>
                                    </select>
                                </td>
                            </div>
                        </form>
                    </td>
                    <td>
                        <button class="btn btn-info search">搜索</button>
                        <#if hasPermission("youhuiquantianjia")>
                            <a class="btn btn-info" href="${ctx}/admin/activity/coupon/item">添加</a>
                        </#if>
                    </td>
                </tr>
            </table>
        </div>
        <div class="table-responsive">
            <table id="coupon-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="1%"></th>
                    <th>优惠券标题</th>
                    <th>优惠券说明</th>
                    <th>优惠券类型</th>
                    <th>可用订单类型</th>
                    <th>所属公司</th>
                    <#--<th>最低价格</th>-->
                    <th>最低使用金额</th>
                    <th>最多优惠金额</th>
                    <th>金额</th>
                    <th>折扣</th>
                    <th>创建时间</th>
                    <th>开始时间</th>
                    <th>截止时间</th>
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
            <#if hasPermission("youhuiquanbianji")>
                <li><a href="${ctx}/admin/activity/coupon/item/{{d.id}}">编辑</a></li></#if>
            <#if hasPermission("youhuiquanliebiao")>
                <li><a href="${ctx}/admin/activity/coupon/couponlist/{{d.id}}">优惠券列表</a></li></#if>
            <#if hasPermission("youhuiquandingxiangzengsongyouhuiquan")>
                <li><a href="javascript:void(0);" class="award" data-id="{{d.id}}">定向赠送优惠券</a></li></#if>
            <#if hasPermission("youhuiquanshanchu")>
                <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除<span class="warning{{d.id}}" style="color: #880000" onmouseover="warning_message({{d.id}})" data-id="{{d.id}}"> ★</span></a></li></#if>
        </ul>
    </div>
</script>
<script id="driver_tpl" type="text/html">
    <div class="row">
        <div class="input-group col-sm-10">
            <textarea id="selectUser" style="width: 100%;" rows="5" class="form-control"></textarea>
            <input type="hidden" id="selectUserHide"/>
        </div>
    </div>
    </br>
    <div class="input-group col-sm-10"">
    <input type="text" id="searchCondition" class="form-control">
    <span class="input-group-btn">
										<button id="searchBtn" class="btn btn-default" type="button">搜索</button>
									  </span>
    </div>
    <div class="input-group col-sm-10" style="margin-top: 10px;">
        <div id="ztree_user" class="ztree" style="overflow-y: scroll; height: 300px;"></div>
    </div>
</script>
</#macro>
<#macro javascript>

<script type="text/javascript">
    function warning_message(id){
        //tips层
        layer.tips('此项为敏感项，请谨慎操作！', '.warning'+id,{tips:[3,"red"]});
    }
</script>

<script type="text/javascript" src="${ctx}/static/js/app/admin/coupon.js?_${.now?string("hhmmss")}"></script>
</#macro>