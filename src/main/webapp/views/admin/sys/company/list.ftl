<#include "../../base/_base.ftl">
<#macro title>
公司管理
</#macro>
<#macro content>
<!--suppress ALL -->
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>公司管理</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <table class="table table-bordered table-striped">
            <tr>
                <#if hasPermission("gongsitianjia")>
                    <td width="80">
                        <a class="btn btn-info" href="${ctx}/admin/sys/company/item">添加</a>
                    </td>
                </#if>
                <td width="100">
                    <form id="searchForm_">
                        <div class="form-group">
                            <label for="s-dc.name-LIKE" class="control-label">公司名:</label>
                            <input name="s-dc.name-LIKE"/>
                        </div>
                    </form>
                </td>
                <td>
                    <button class="btn btn-info search">搜索</button>
                </td>
            </tr>
        </table>
        <div class="table-responsive">
            <table id="company-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>公司名</th>
                    <th>账户余额(元)</th>
                    <th>短信剩余条数(条)</th>
                    <th>活动金额剩余(元)</th>
                    <th>保险金额剩余(元)</th>
                    <th>所属省</th>
                    <th>所属市</th>
                    <th>所属县</th>
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script id="action_tpl" type="text/html">
    <div class="btn-group">
        <button type="button" class="btn btn-success btn-xs ">操作</button>
        <button type="button" class="btn btn-success  btn-xs dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only"></span></button>
        <ul class="dropdown-menu success btn-xs " role="menu">
            <#if hasPermission("gongsibianji")>
                <li><a href="${ctx}/admin/sys/company/item/{{d.pk_id}}">编辑</a></li></#if>
            <#if hasPermission("gongsishezhi")>
                <li><a href="${ctx}/admin/sys/company/distribution/{{d.id}}-1-1-0">设置</a></li></#if>
            <#if hasPermission("gongsishanchu")>
                <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除<span class="warning{{d.id}}" style="color: #880000" onmouseover="warning_message({{d.id}})" data-id="{{d.id}}"> ★</span></a></li></#if>
            <#if hasPermission("sms_rechange")>
                <li><a class="rechangeSms" href="javascript:void(0);" data-count="{{d.count}}"
                       data-id="{{d.id}}">短信充值</a></li>
            </#if>
            <#if hasPermission("gongsihuodongjinechongzhi")>
                <li><a class="rechangeActivityAmount" href="javascript:void(0);"
                       data-activityamount="{{d.activityamount}}"
                       data-id="{{d.id}}">公司活动金额充值</a></li>
                <li><a class="rechangeInsuranceAmount" href="javascript:void(0);"
                       data-insuranceamount="{{d.insuranceamount}}"
                       data-id="{{d.id}}">公司保险金额充值</a></li>
            </#if>

            <#if hasPermission("jinyonggongsiquanbuyonghu")>
                <li><a href="javascript:void(0);" class="disablealluser" data-id="{{d.id}}"> 禁用所有后台账户</a></li></#if>
            <#if hasPermission("qiyonggongsiquanbuyonghu")>
                <li><a href="javascript:void(0);" class="ablealluser" data-id="{{d.id}}">启用所有后台账户</a></li></#if>
            <#if hasPermission("dongjiegongsiquanbusiji")>
                <li><a href="javascript:void(0);" class="frozenalldriver" data-id="{{d.id}}">冻结所有司机</a></li></#if>
            <#if hasPermission("jiejiegongsiquanbusiji")>
                <li><a href="javascript:void(0);" class="nofrozenalldriver" data-id="{{d.id}}">解冻所有司机</a></li></#if>
        </ul>
    </div>
</script>
<script id="rechangeSms" type="text/html">
    <div>
        <p>剩余短信数量:{{d.count}}条</p>
        <input id="money" placeholder="充值金额"/>元
    </div>
</script>
<script id="rechangeActivityAmount" type="text/html">
    <div>
        <p>活动金额:{{d.activityAmount}}元</p>
        <input id="money" placeholder="充值金额"/>元
    </div>
</script>
<script id="rechangeInsuranceAmount" type="text/html">
    <div>
        <p>保险金额:{{d.insuranceAmount}}元</p>
        <input id="money" placeholder="充值金额"/>元
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


<script type="text/javascript" src="${ctx}/static/js/app/admin/company.js?_${.now?string("hhmmss")}"></script>
</#macro>