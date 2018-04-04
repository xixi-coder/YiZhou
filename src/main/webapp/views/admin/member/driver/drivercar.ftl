<#include "../../base/_base.ftl">
<#macro title>
车辆信息
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>车辆信息</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <br>
        <div class="row">
            <a class="btn btn-info" href="${ctx}/admin/member/driver/carinfo?id=${id}">添加</a>
            <#if hasPermission("cheliangpingpaiguanli")><a class="btn btn-info"
                                                           href="${ctx}/admin/member/driver/carbrand">车辆品牌管理</a>
            </#if>
        </div>
        <br>
        <div class="table-responsive">
            <table id="drivercar-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>汽车品牌</th>
                    <th>状态</th>
                    <th>车辆类型</th>
                    <th>汽车型号</th>
                    <!--<th>型号描述</th>-->
                    <th>车牌号</th>
                    <th>识别码</th>
                    <th>行驶里程</th>
                    <th>创建时间</th>
                    <th>加入时间</th>
                    <!-- <th>所属省份</th>
                     <th>所属城市</th>-->
                    <th width="80">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<div class="row" style="margin-top:20px;">
    <div class="col-sm-12">
        <div class="col-sm-6" style="float: right;">
            <a class="btn btn-default" href="javascript:void(0);"
               onclick="window.location.href= g.ctx+'/admin/member/driver'">返回</a>
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
            <li><a href="${ctx}/admin/member/driver/carinfo/{{d.car_id}}-${id}?id=${id}">编辑</a></li>
            <li><a href="${ctx}/admin/member/driver/carInsurance/?plateNo={{d.plateNo}}">车辆保险信息</a></li>
            <li><a class="delete" href="javascript:void(0);" data-id="{{d.car_id}}">删除</a></li>
            <li><a class="audit" href="javascript:void(0);" data-id="{{d.car_id}}" status="{{d.status}}">审核</a></li>
        </ul>
    </div>
</script>
<script id="audit_tpl" type="text/x-handlebars-template">
    <form id="audit_form" action="" autocomplete="off">
        <input type="hidden" name="id" value="${id!}">
    </form>
</script>
</#macro>
<#macro javascript>
<script type="text/javascript">
    var id = ${id!};
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/drivercar.js?_${.now?string("hhmmss")}"></script>
</#macro>