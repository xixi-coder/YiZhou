<#include "../../base/_base.ftl">
<#macro title>
订单來源统计
</#macro>
<#macro content>
<ul class="nav nav-tabs navigation">
    <#list serviceTypes as item>
        <li <#if type==item.id>class="active"</#if>>
            <#if type==item.id><input type="hidden" value="${item.name}" id="title"></#if>
            <a href="${ctx}/admin/count/orderfrom/${item.id}">${item.name}</a>
        </li>
    </#list>
</ul>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>订单來源统计</h4>
    </div>
    <div class="panel-body">
        <div id="daijiatab" class="tab-pane active">
            <div class="table-responsive">
                <form class="form-inline" id="SearchForm" method="get"
                      action="${ctx}/admin/count/orderfrom/${type}">
                    <div class="input-group">
                        <input id="startTime" name="startTime" type="text"
                               value="${startTime!}"
                               class="form-control datetimepicker-input"
                               placeholder="开始时间"/>
                    </div>
                    <div class="form-group">
                        <input id="endTime" name="endTime" type="text" value="${endTime!}"
                               class="form-control datetimepicker-input"
                               placeholder="结束时间"/>
                    </div>
                    <#if hasRole("super_admin")||hasPermission("select-company")>
                        <div class="form-group">
                            <label for="real_name" class="control-label">公司选择:</label>
                            <select class="select2" name="company" id="companySelect">
                                <option value="">请选择</option>
                                <#list companies as item>
                                    <option value="${(item.id)!}"
                                        <#if item.id == company> selected="selected"</#if>
                                    >${(item.name)!}</option>
                                </#list>
                            </select>
                        </div>
                    </#if>
                    <button type="submit" id="search_button" class="btn btn-primary"><i
                            class="glyphicon glyphicon-search"></i> 查询
                    </button>
                </form>
            </div>
            <div class="table-responsive">
                <div id="main" style="height:400px"></div>
            </div>
            <div class="table-responsive">
                <table id="daijiaTable" class="table table-hover table-striped">
                    <thead id="daijiaHead">
                    <tr style="font-size:14px;">
                        <th style="width:90px;">数量\类型</th>
                        <th>APP预约</th>
                        <th>微信预约</th>
                        <th>电话预约</th>
                        <th>服务人员补单</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <th>订单数量
                        </th>
                        <td id="orderFinishedAmount_this">${(order1.count)!0}(单)</td>
                        <td id="orderFinishedAmount_getout">${(order2.count)!0}(单)</td>
                        <td id="orderBusyAmount_getout">${(order3.count)!0}(单)</td>
                        <td id="orderCanceledAmount">${(order4.count)!0}(单)</td>

                    </tr>
                    <tr>
                        <th>订单金额
                        </th>
                        <td id="orderFinishedMoney_this">${(order1.sum)!0}(元)</td>
                        <td id="orderFinishedMoney_getout">${(order2.sum)!0}(元)</td>
                        <td id="orderBusyMoney_getout">${(order3.sum)!0}(元)</td>
                        <td id="orderCanceledMoney">${(order4.sum)!0}(元)</td>
                    </tr>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</#macro>
<#macro javascript>
<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        var title = $("#title").val() + '订单来源统计';
        var type = ${(type)!1};
        $($('.tabs-select')[type]).addClass("active");
        // 路径配置
        require.config({
            paths: {
                echarts: 'http://echarts.baidu.com/build/dist'
            }
        });
        // 使用
        require(
                [
                    'echarts',
                    'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
                ],
                function (ec) {
                    // 基于准备好的dom，初始化echarts图表
                    var myChart = ec.init(document.getElementById('main'));

                    option = {
                        title: {
                            text: title,
                            subtext: '',
                            x: 'center'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        legend: {
                            orient: 'vertical',
                            x: 'left',
                            data: ['APP预约', '微信预约', '电话预约', '服务人员补单']
                        },
                        calculable: true,
                        series: [
                            {
                                name: '订单来源',
                                type: 'pie',
                                radius: '55%',
                                center: ['50%', '60%'],
                                data: [
                                    {value:${(order1.count)!0}, name: 'APP预约'},
                                    {value:${(order2.count)!0}, name: '微信预约'},
                                    {value:${(order3.count)!0}, name: '电话预约'},
                                    {value:${(order4.count)!0}, name: '服务人员补单'},
                                ]
                            }
                        ]
                    };
                    // 为echarts对象加载数据
                    myChart.setOption(option);
                }
        );
    });

</script>

</#macro>

