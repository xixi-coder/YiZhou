<#include "../../base/_base.ftl">
<#macro title>
订单统计
</#macro>
<#macro content>
<ul class="nav nav-tabs navigation">
    <#list serviceTypes as item>
        <li <#if type==item.id>class="active"</#if>>
            <#if type==item.id><input type="hidden" value="${item.name}" id="title"></#if>
            <a href="${ctx}/admin/count/order/${item.id}">${item.name}</a>
        </li>
    </#list>
</ul>
<div class="panel-body">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4>订单统计</h4>
        </div>
        <div class="panel-body">
            <div role="tabpanel" class="tab-pane active" id="daijiatab">
                <div class="table-responsive">
                    <form id="submit_form" class="form-horizontal" role="form"
                          action="${ctx}/admin/count/order/${type!1}">
                        <table id="daijiaTable" class="table table-hover table-striped">
                            <tr>
                                <td>
                                    <select name="year" class="select2" id="year" style="width: 100px;">
                                        <#list years as item>
                                            <option value="${item}"
                                                    <#if year??&&year?string==item?string>selected</#if>>${item}</option>
                                        </#list>
                                    <#--<option value="2017" <#if year??&&year==2017>selected</#if>>2017</option>-->
                                    <#--<option value="2016" <#if year??&&year==2016>selected</#if>>2016</option>-->
                                    <#--<option value="2015" <#if year??&&year==2015>selected</#if>>2015</option>-->
                                    <#--<option value="2014" <#if year??&&year==2014>selected</#if>>2014</option>-->
                                    </select>

                                </td>
                                <#if hasRole("super_admin")||hasPermission("select-company")>
                                    <td width="30%">
                                        <label for="real_name" class="control-label">公司选择:</label>
                                        <select class="select2" name="company" id="companySelect"
                                                style="width: 30%;">
                                            <option value="">请选择</option>
                                            <#list companies as item>
                                                <option value="${(item.id)!}"
                                                    <#if item.id == company> selected="selected"</#if>
                                                >${(item.name)!}</option>
                                            </#list>
                                        </select>
                                    </td>
                                </#if>
                                <td>
                                    <button type="submit" id="search_button" class="btn btn-primary"><i
                                            class="glyphicon glyphicon-search"></i> 查询
                                    </button>
                                    <#--<button id="export_button" type="button"
                                            class="btn btn-primary col-sm-offset-0"><i
                                            class="glyphicon glyphicon-save"></i>导出
                                    </button>-->
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
                <div class="table-responsive">
                    <div id="main" style="height:400px"></div>
                </div>
                <div class="table-responsive">
                    <table id="daijiaTable" class="table table-hover table-striped">
                        <thead id="daijiaHead">
                        <tr style="font-size:14px;">
                            <th style="width:90px;">分类\月份</th>
                            <#list 1..12 as item>
                                <th>${item}</th>
                            </#list>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <th>销单量
                            </th>
                            <#list 1..12 as item>
                                <td id="xtd${item}"><#list orders2 as citem ><#if citem.month==item>${(citem.count)!0}</#if></#list></td>
                            </#list>
                        </tr>
                        <tr>
                            <th>完成订单
                            </th>
                            <#list 1..12 as item>
                                <td id="wtd${item}"><#list orders1 as citem ><#if citem.month==item>${(citem.count)!0}</#if></#list></td>
                            </#list>
                        </tr>
                        <tr>
                            <th>金额（元）
                            </th>
                            <#list 1..12 as item>
                                <td id="jtd${item}"><#list orders as citem ><#if citem.month==item>${(citem.sum?c)!0}</#if></#list></td>
                            </#list>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</#macro>
<#macro javascript>
<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        var title = $("#title").val() + '订单统计';
        var type = ${(type)!1};
        $($('.tabs-select')[type-1]).addClass("active");
        var data = [];
        for (var i = 0; i < 12; i++) {
            data.push((i + 1) + '月');
        }
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
                    'echarts/chart/bar',
                    'echarts/chart/line'// 使用柱状图就加载bar模块，按需加载
                ],
                function (ec) {
                    // 基于准备好的dom，初始化echarts图表
                    var myChart = ec.init(document.getElementById('main'));
                    option = {
                        title: {
                            text: title,
                            subtext: '',
                            x: 'left'
                        },
                        tooltip: {
                            trigger: 'axis'
                        },
                        toolbox: {
                            show: false,
                            feature: {
                                mark: {show: true},
                                dataView: {show: true, readOnly: false},
                                magicType: {show: true, type: ['line', 'bar']},
                                restore: {show: true},
                                saveAsImage: {show: true}
                            }
                        },
                        calculable: true,
                        legend: {
                            data: ['销单量', '完成订单', '金额']
                        },
                        xAxis: [
                            {
                                type: 'category',
                                data: data
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value',
                                name: '单量',
                                axisLabel: {
                                    formatter: '{value} 单'
                                }
                            },
                            {
                                type: 'value',
                                name: '总价',
                                axisLabel: {
                                    formatter: '{value} 元'
                                }
                            }
                        ],
                        series: [
                            {
                                name: '销单量',
                                type: 'line',
                                data:[
                                    <#list 1..12 as item>
                                        $('#xtd${item}').text(),
                                    </#list>]
                            },
                            {
                                name: '完成订单',
                                type: 'line',
                                data:[ <#list 1..12 as item>
                                    $('#wtd${item}').text(),
                                </#list>]
                            },
                            {
                                name: '金额',
                                type: 'bar',
                                yAxisIndex: 1,
                                data:[ <#list 1..12 as item>
                                    $('#jtd${item}').text(),
                                </#list>]
                            }]
                    };

                    // 为echarts对象加载数据
                    myChart.setOption(option);
                    myChart.on('click', function (param) {
                        var name = param.name;
                        if (name == '1月') {
                            <#if status==0>
                                window.location.href = '${ctx}/admin/count/order/month/1-' + type + "-${year!}" + "-" + $("#companySelect").val();
                            <#else >
                                window.location.href = '${ctx}/admin/count/order/month/1-' + type + "-${year!}" + "-" + ${company};
                            </#if>
                        } else if (name == '2月') {
                            <#if status==0>
                                window.location.href = '${ctx}/admin/count/order/month/2-' + type + "-${year!}" + '-' + $("#companySelect").val();
                            <#else >
                                window.location.href = '${ctx}/admin/count/order/month/2-' + type + "-${year!}" + "-" + ${company};
                            </#if>
                        } else if (name == '3月') {
                            <#if status==0>
                                window.location.href = '${ctx}/admin/count/order/month/3-' + type + "-${year!}" + '-' + $("#companySelect").val();
                            <#else >
                                window.location.href = '${ctx}/admin/count/order/month/3-' + type + "-${year!}" + "-" + ${company};
                            </#if>
                        } else if (name == '4月') {
                            <#if status==0>
                                window.location.href = '${ctx}/admin/count/order/month/4-' + type + "-${year!}" + '-' + $("#companySelect").val();
                            <#else >
                                window.location.href = '${ctx}/admin/count/order/month/4-' + type + "-${year!}" + "-" + ${company};
                            </#if>
                        } else if (name == '5月') {
                            <#if status==0>
                                window.location.href = '${ctx}/admin/count/order/month/5-' + type + "-${year!}" + '-' + $("#companySelect").val();
                            <#else >
                                window.location.href = '${ctx}/admin/count/order/month/5-' + type + "-${year!}" + "-" + ${company};
                            </#if>
                        } else if (name == '6月') {
                            <#if status==0>
                                window.location.href = '${ctx}/admin/count/order/month/6-' + type + "-${year!}" + '-' + $("#companySelect").val();
                            <#else >
                                window.location.href = '${ctx}/admin/count/order/month/6-' + type + "-${year!}" + "-" + ${company};
                            </#if>
                        } else if (name == '7月') {
                            <#if status==0>
                                window.location.href = '${ctx}/admin/count/order/month/7-' + type + "-${year!}" + '-' + $("#companySelect").val();
                            <#else >
                                window.location.href = '${ctx}/admin/count/order/month/7-' + type + "-${year!}" + "-" + ${company};
                            </#if>
                        } else if (name == '8月') {
                            <#if status==0>
                                window.location.href = '${ctx}/admin/count/order/month/8-' + type + "-${year!}" + '-' + $("#companySelect").val();
                            <#else >
                                window.location.href = '${ctx}/admin/count/order/month/8-' + type + "-${year!}" + "-" + ${company};
                            </#if>
                        } else if (name == '9月') {
                            <#if status==0>
                                window.location.href = '${ctx}/admin/count/order/month/9-' + type + "-${year!}" + '-' + $("#companySelect").val();
                            <#else >
                                window.location.href = '${ctx}/admin/count/order/month/9-' + type + "-${year!}" + "-" + ${company};
                            </#if>
                        } else if (name == '10月') {
                            <#if status==0>
                                window.location.href = '${ctx}/admin/count/order/month/10-' + type + "-${year!}" + '-' + $("#companySelect").val();
                            <#else >
                                window.location.href = '${ctx}/admin/count/order/month/10-' + type + "-${year!}" + "-" + ${company};
                            </#if>
                        } else if (name == '11月') {
                            <#if status==0>
                                window.location.href = '${ctx}/admin/count/order/month/11-' + type + "-${year!}" + '-' + $("#companySelect").val();
                            <#else >
                                window.location.href = '${ctx}/admin/count/order/month/11-' + type + "-${year!}" + "-" + ${company};
                            </#if>
                        } else if (name == '12月') {
                            <#if status==0>
                                window.location.href = '${ctx}/admin/count/order/month/12-' + type + "-${year!}" + '-' + $("#companySelect").val();
                            <#else >
                                window.location.href = '${ctx}/admin/count/order/month/12-' + type + "-${year!}" + "-" + ${company};
                            </#if>
                        }
                    })
                }
        );

        var tds = $('td');
        for (var i = 0; i < tds.length; i++) {
            if ($(tds[i]).html() == 0) {
                $(tds[i]).html(0);
            }
        }
    });
</script>

</#macro>