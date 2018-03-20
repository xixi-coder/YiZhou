<#include "../../base/_base.ftl">
<#macro title>
APP日志
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>APP日志</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <table class="table table-bordered table-striped">
            <tr>
                <td>
                  <form id="searchForm_">
                        <div class="form-group">
                            <label for="s-create_time-EQ" class="control-label">时间:</label>
                            <input type="hidden" name="s-create_time-BETWEEN" value="" id="timeSearch">
                            <input type="text"
                                   class="datetime0picker-input" id="start"
                                   placeholder="如:1992-11-11">至
                            <input type="text"
                                   class="datetime0picker-input" id="end"
                                   placeholder="如:1992-11-11">

                            <select name="s-app_type-EQ" style="height: 26px;" id="type">
                                <option value="" selected="selected">全部</option>
                                <option value="1">司机端</option>
                                <option value="2">客户端</option>
                            </select>

                            <select name="s-device_type-EQ" style="height: 26px;" id="type2">
                                <option value="" selected="selected">全部</option>
                                <option value="1">安卓</option>
                                <option value="2">iOS</option>
                            </select>
                        </div>
                    </form>
                </td>
                <td>
                    <button class="btn btn-info search">搜索</button>
                </td>
            </tr>
        </table>
        <div class="table-responsive">
            <table id="applog-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>序号</th>
                    <th>设备端</th>
                    <th>系统</th>
                    <th>版本号</th>
                    <th width="200px">Log路径</th>
                    <th width="200px">生成时间</th>
                    <th >操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script id="action_tpl" type="text/x-handlebars-template">
    <div class="btn-group">
        <a href="http://{{d.path}}" target="view_window">
            查看
        </a>
    </div>
</script>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/applog.js?_${.now?string("hhmmss")}"></script>
</#macro>