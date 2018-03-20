<#include "../../base/_base.ftl">
<#macro title>
车辆列表
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>车辆列表</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <div class="table-responsive">
            <table class="table table-bordered table-striped">
                <tr>
                    <td width="80">
                        <form id="searchForm_">
                            <div class="form-group">
                                <label for="s-ddi.real_name-LIKE" class="control-label">司机姓名:</label>
                                <input name="s-ddi.real_name-LIKE"/>&nbsp;&nbsp;&nbsp;
                                <label for="s-ddi.phone-EQ" class="control-label">手机号:</label>
                                <input name="s-ddi.phone-EQ"/>&nbsp;&nbsp;&nbsp;
                                <label for="s-dbc.plate_no-LIKE" class="control-label">车牌号:</label>
                                <input name="s-dbc.plate_no-LIKE"/>&nbsp;&nbsp;&nbsp;
                                <label for="s-user_name-LIKE" class="control-label">类型:</label>
                                <select name="s-dbc.status-EQ" style="height: 26px;">
                                    <option value="ISNULL" selected="selected">待审核</option>
                                    <option value="1">审核通过</option>
                                    <option value="2">审核未通过</option>
                                </select>
                            </div>
                        </form>
                    </td>
                    <td>
                        <button class="btn btn-info search">搜索</button>
                        <#if hasPermission("cheliangpingpaiguanli")>
                            <a class="btn btn-info" href="${ctx}/admin/member/driver/carbrand">车辆品牌管理</a></#if>
                    </td>

                </tr>
            </table>
        </div>
        <div class="table-responsive">
            <table id="aduitcar-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>司机姓名</th>
                    <th>汽车品牌</th>
                    <th>状态</th>
                    <th>车辆类型</th>
                    <th>汽车型号</th>
                    <th>车牌号</th>
                    <th>识别码</th>
                    <th>行驶里程</th>
                    <th>加入时间</th>
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
            <li><a href="${ctx}/admin/member/driver/carinfo/{{d.id}}?id={{d.driverid}}">编辑</a></li>
            <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}"><span class="warning{{d.id}}" style="color: #880000" onmouseover="warning_message({{d.id}})" data-id="{{d.id}}">删除★</span></a></li>
            <#if hasPermission("shenHeChe")>
                {{# if(d.status==null){ }}
                <li><a class="audit" href="javascript:void(0);" data-id="{{d.id}}" status="{{d.status}}">审核</a></li>
                {{# } }}
            </#if>
        </ul>
    </div>
</script>
<div class="row" style="margin-top:20px;">
    <div class="col-sm-12">
        <div class="col-sm-6" style="float: right;">
            <a class="btn btn-default" href="javascript:void(0);"
               onclick="window.location.href= g.ctx+'/admin/member/driver'">返回</a>
        </div>
    </div>
</div>
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
<script type="text/javascript" src="${ctx}/static/js/app/admin/aduitcar.js?_${.now?string("hhmmss")}"></script>
</#macro>