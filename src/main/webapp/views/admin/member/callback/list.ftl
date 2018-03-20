<#include "../../base/_base.ftl">
<#macro title>
反馈信息
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>反馈信息</h4>
    </div>
    <div class="panel-body">
        <div class="table-responsive" style="overflow:auto;">
            <form id="searchForm_">
                <table class="table table-bordered table-striped">
                    <tr>
                        <td>
                            <div class="form-group">
                            <label for="s-dml.user_name-LIKE" class="control-label">反馈时间:</label>
                            <input type="hidden" name="s-dc.create_time-BETWEEN"  id="timeSearch">
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="start" name="start1"
                                   placeholder="如:1992-11-11">至
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="end" name = "end1"
                                   placeholder="如:1992-11-11">
                                </div>
                        </td>
                        <td>
                            <div class="form-group">
                                <label for="s-dml.user_name-LIKE" class="control-label">APP类型:</label>
                               <select class="select2" name="s-app_type-EQ" style="width: 40%">
                                   <option value="">请选择</option>
                                   <option value="1">司机端</option>
                                   <option value="2">客户端</option>
                               </select>
                            </div>
                        </td>
                        <td>
                            <button type="button" class="btn btn-info search">搜索</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="table-responsive">
            <table id="callback" cellspacing="0" class="hover table table-striped table-bordered"
                   width="100%">
                <thead>
                <tr>
                    <th width="2%"></th>
                    <th>用户名</th>
                    <th>APP类型</th>
                    <th>反馈时间</th>
                    <th>反馈内容</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/callback.js?_${.now?string("hhmmss")}"></script>
</#macro>
