<#include "../../base/_base.ftl">
<#macro title>
车辆品牌管理
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>车辆品牌管理</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <table class="table table-bordered table-striped">
            <tr>
                <td width="80">
                    <div class="row">
                        <a class="btn btn-info" href="${ctx}/admin/member/driver/brandinfo">添加</a>
                    </div>
                </td>
                <td width="80">
                    <form id="searchForm_">
                        <div class="form-group">
                            <label for="s-name-LIKE" class="control-label">品牌名:</label>
                            <input name="s-name-LIKE"/>
                        </div>
                    </form>
                </td>
                <td>
                    <button class="btn btn-info search">搜索</button>
                </td>
            </tr>

            <div class="table-responsive">
                <table id="carbrand-list" cellspacing="0" class="hover table table-striped table-bordered"
                       width="100%">
                    <thead>
                    <tr>
                        <th width="2%"></th>
                        <th>汽车品牌</th>
                        <th>拼音名称</th>
                        <th>品牌描述</th>
                        <th>创建时间</th>
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
            <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">返回</a>
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
            <li><a href="${ctx}/admin/member/driver/brandinfo/{{d.id}}">编辑</a></li>
            <li><a href="${ctx}/admin/member/driver/carmodel/{{d.id}}">车辆型号管理</a></li>
            <li><a class="delete" href="javascript:void(0);" data-id="{{d.id}}">删除</a></li>
        </ul>
    </div>
</script>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/carbrand.js?_${.now?string("hhmmss")}"></script>
</#macro>