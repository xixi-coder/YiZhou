<#include "../../base/_base.ftl">
<#macro title>
订单统计
</#macro>
<#macro content>
<div class="panel-body">
    <input type="hidden" value="${serviceType.name}" id="title"/>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4>月订单统计</h4>
        </div>
        <div class="panel-body">
            <div class="table-responsive">
                <button id="export_button" type="button" class="btn btn-primary col-sm-offset-0"><i
                        class="glyphicon glyphicon-save"></i>导出
                </button>
            </div>
            <div class="table-responsive">
                <div id="main" style="height:400px"></div>
            </div>
            <div class="table-responsive">
                <table id="daijiaTable" class="table table-hover table-striped">
                    <thead id="daijiaHead">
                    <tr style="font-size:14px;">
                        <th style="width:90px;">分类\天</th>
                        <#list 1..days+1 as item>
                            <th>${item?string}</th>
                        </#list>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>销单量
                        </td>
                        <#list 1..days+1 as item>
                            <td id="xtd${item}"><#list orders2 as citem ><#if citem.day==item>${(citem.count)!0}</#if></#list></td>
                        </#list>
                    </tr>
                    <tr>
                        <th>完成订单
                        </th>
                        <#list 1..days+1 as item>
                            <td id="wtd${item}"><#list orders1 as citem ><#if citem.day==item>${(citem.count)!0}</#if></#list></td>
                        </#list>
                    </tr>
                    <tr>
                        <th>金额（元）
                        </th>
                        <#list 1..days+1 as item>
                            <td id="jtd${item}"><#list orders as citem ><#if citem.day==item>${(citem.sum?c)!0}</#if></#list></td>
                        </#list>
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
        var year = ${year!};
        var month = ${month!};
        var type = ${type!};
        var days = ${days+1};
        var data = [];
        for (var i = 0; i < days; i++) {
            data.push(i + 1);
        }
        var title = year + '年' + month + '月' + $("#title").val() + '订单统计';
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
                                    <#list 1..31 as item>
                                        $('#xtd${item}').text(),
                                    </#list>]
                            },
                            {
                                name: '完成订单',
                                type: 'line',
                                data:[
                                    <#list 1..31 as item>
                                        $('#wtd${item}').text(),
                                    </#list>]
                            },
                            {
                                name: '金额',
                                type: 'bar',
                                yAxisIndex: 1,
                                data: [
                                    <#list 1..31 as item>
                                        $('#jtd${item}').text(),
                                    </#list>]
                            }
                        ]
                    };

                    // 为echarts对象加载数据
                    myChart.setOption(option);
                    myChart.on('click', function (param) {
                        var name = param.name;
                        if (name == '1') {
                            window.location.href = '${ctx}/admin/count/order/day/1-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '2') {
                            window.location.href = '${ctx}/admin/count/order/day/2-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '3') {
                            window.location.href = '${ctx}/admin/count/order/day/3-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '4') {
                            window.location.href = '${ctx}/admin/count/order/day/4-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '5') {
                            window.location.href = '${ctx}/admin/count/order/day/5-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '6') {
                            window.location.href = '${ctx}/admin/count/order/day/6-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '7') {
                            window.location.href = '${ctx}/admin/count/order/day/7-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '8') {
                            window.location.href = '${ctx}/admin/count/order/day/8-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '9') {
                            window.location.href = '${ctx}/admin/count/order/day/9-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '10') {
                            window.location.href = '${ctx}/admin/count/order/day/10-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '11') {
                            window.location.href = '${ctx}/admin/count/order/day/11-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '12') {
                            window.location.href = '${ctx}/admin/count/order/day/12-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '13') {
                            window.location.href = '${ctx}/admin/count/order/day/13-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '14') {
                            window.location.href = '${ctx}/admin/count/order/day/14-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '15') {
                            window.location.href = '${ctx}/admin/count/order/day/15-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '16') {
                            window.location.href = '${ctx}/admin/count/order/day/16-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '17') {
                            window.location.href = '${ctx}/admin/count/order/day/17-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '18') {
                            window.location.href = '${ctx}/admin/count/order/day/18-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '19') {
                            window.location.href = '${ctx}/admin/count/order/day/19-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '20') {
                            window.location.href = '${ctx}/admin/count/order/day/20-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '21') {
                            window.location.href = '${ctx}/admin/count/order/day/21-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '22') {
                            window.location.href = '${ctx}/admin/count/order/day/22-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '23') {
                            window.location.href = '${ctx}/admin/count/order/day/23-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '24') {
                            window.location.href = '${ctx}/admin/count/order/day/24-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '25') {
                            window.location.href = '${ctx}/admin/count/order/day/25-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '26') {
                            window.location.href = '${ctx}/admin/count/order/day/26-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '27') {
                            window.location.href = '${ctx}/admin/count/order/day/27-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '28') {
                            window.location.href = '${ctx}/admin/count/order/day/28-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '29') {
                            window.location.href = '${ctx}/admin/count/order/day/29-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '30') {
                            window.location.href = '${ctx}/admin/count/order/day/30-' + type + "-${year!}-${month!}-${company}"
                        } else if (name == '31') {
                            window.location.href = '${ctx}/admin/count/order/day/31-' + type + "-${year!}-${month!}-${company}"
                        }
                    })
                }
        );
    })

    var tds = $('td');
    for (var i = 0; i < tds.length; i++) {
        if ($(tds[i]).html() == 0) {
            $(tds[i]).html(0);
        }
    }
</script>

</#macro>