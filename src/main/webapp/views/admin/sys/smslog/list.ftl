<#include "../../base/_base.ftl">
<#macro title>
短信日志
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>短信日志</h4>
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
                            <label for="s-send_time-EQ" class="control-label">发送时间:</label>
                            <input type="hidden" name="s-send_time-BETWEEN" value="" id="timeSearch">
                            <input type="text"
                                   class="datetime0picker-input" id="start"
                                   placeholder="如:1992-11-11">至
                            <input type="text"
                                   class="datetime0picker-input" id="end"
                                   placeholder="如:1992-11-11">
                        </div>
                    </form>
                </td>
                <td>
                    <button class="btn btn-info search">搜索</button>
                </td>
            </tr>
        </table>
        <div class="table-responsive">
            <table id="smslog-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>类型</th>
                    <th>号码</th>
                    <th>是否成功</th>
                    <th>发送时间</th>
                    <th width="200px">内容</th>
                    <th>操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
<script id="action_tpl" type="text/x-handlebars-template">
    <div class="btn-group">
        <a href="${ctx}/admin/sys/smslog/item/{{d.id}}">
            <button type="button" class="btn btn-success btn-xs ">查看</button>
        </a>
    </div>
</script>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/smslog.js?_${.now?string("hhmmss")}"></script>
</#macro>