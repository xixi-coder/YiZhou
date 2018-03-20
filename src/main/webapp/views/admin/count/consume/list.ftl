<#include "../../base/_base.ftl">
<#macro title>
客户消费统计
</#macro>
<#macro content>
<input type="hidden" name="type" id="type" value="${type!1}">
<ul class="nav nav-tabs navigation">
    <#list serviceTypes as item>
        <li <#if type==item.id>class="active"</#if>>
            <a href="${ctx}/admin/count/consume/${item.id}">${item.name}</a>
        </li>
    </#list>
</ul>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>客户消费统计</h4>
    </div>
    <div class="panel-body">
        <div class="panel-body">
            <div id="daijiatab" class="tab-pane active">
                <div class="table-responsive" style="overflow: auto;">
                <#-- <input type="hidden" id="sLevel" name="s-dmi.level-EQ"/>-->
                    <input type="hidden" value="${type}" name="type">
                    <form class="form-inline" id="searchForm_" action="${ctx}/admin/count/consume/export">
                        <table cellspacing="0" class="hover table table-striped table-bordered" width="100%">
                            <tr>
                            <#--<td>-->
                            <#--<select class="form-control" name="s-dmi.level-EQ" id="level">-->
                            <#--<option value="">请选择</option>-->
                            <#--<option value="1"-->
                            <#--<#if level??&&level==1>selected="selected"</#if>>普通-->
                            <#--</option>-->
                            <#--<option value="2"-->
                            <#--<#if level??&&level==2>selected="selected"</#if>>VIP-->
                            <#--</option>-->
                            <#--<option value="3"-->
                            <#--<#if level??&&level==3>selected="selected"</#if>>企业用户-->
                            <#--</option>-->
                            <#--</select>-->
                            <#--</td>-->
                                <td>
                                    <label for="real_name" class="control-label">时间选择:</label>

                                    <input type="hidden" name="s-dd.pay_time-BETWEEN" value=""
                                           id="timeSearch">
                                    <input type="text"
                                           class="datepicker-input" id="startTime" name="start1">至
                                    <input type="text"
                                           class="datepicker-input" id="endTime" name="end1">
                                </td>
                                <#if hasRole("super_admin")||hasPermission("select-company")>
                                    <td width="30%">
                                        <label for="real_name" class="control-label">公司选择:</label>
                                        <select class="select2" name="s-dmi.company-EQ" id="companySelect"
                                                style="width: 30%;">
                                            <option value="">请选择</option>
                                            <#list companies as item>
                                                <option value="${(item.id)!}">${(item.name)!}</option>
                                            </#list>
                                        </select>
                                    </td>
                                </#if>
                                <td>
                                    <button type="button" id="search_button" class="btn btn-primary"><i
                                            class="glyphicon glyphicon-search"></i> 查询
                                    </button>
                                <#-- <a class="btn btn-info" id="export"
                                    href="#">导出</a>-->
                                    <button id="export_button" type="button" class="btn btn-primary"  onclick="javascript:method1('consume-list')"><i
                                            class="glyphicon glyphicon-save"></i>导出
                                    </button>
                                </td>
                            </tr>
                        </table>
                    </form>

                </div>
                <div class="table-responsive" style="overflow:auto;height: 30px;">
                    <table>
                        <tr>
                            <td>
                                <div class="countSum label label-primary"
                                     data-url="${ctx}/admin/total/kehuxiaofeitongjidingdanshu/${(type)!}"
                                     data-filter="true">
                                    <span>订单数：</span>
                                    <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                                    <span></span>
                                    <span>单</span>
                                </div>
                                <div class="countSum label label-primary"
                                     data-url="${ctx}/admin/total/kehuxiaofeitongjidingdanjine/${(type)!}"
                                     data-filter="true">
                                    <span>订单金额：</span>
                                    <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                                    <span></span>
                                    <span>元</span>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="table-responsive" style="overflow: auto;">
                    <table id="consume-list" cellspacing="0" class="hover table table-striped table-bordered"
                           width="100%">
                        <thead>
                        <tr>
                            <th width="2%"></th>
                            <th>客户姓名</th>
                            <th>用户名</th>
                            <th>所属公司</th>
                            <th>完成订单数量(单)</th>
                            <th>完成订单金额(元)</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/consume.js?_${.now?string("hhmmss")}"></script>
</#macro>