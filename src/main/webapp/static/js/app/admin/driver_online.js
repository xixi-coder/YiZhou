/**
 * Created by Administrator on 2016/10/18.
 */
var url = {
    list: g.ctx + '/admin/count/online/list',
    monthDays: g.ctx + '/admin/count/online/countOfMonth',
}
var nums = 10; //每页出现的数量
$(document).ready(function () {
    $('.datemonthpick').datetimepicker({
        format: 'yyyy-mm',
        weekStart: 1,
        autoclose: true,
        startView: 3,
        minView: 3,
        forceParse: false,
        language: 'zh-CN'
    });
    //调用分页
    onlineTime.createMonth();
    var memberData = onlineTime.listDate(1, nums);
    var pages = Math.ceil(memberData.count / nums); //得到总页数
    onlineTime.laypage(pages);
    $("#search").on('click', function () {
        var memberData = onlineTime.listDate(1, nums);
        var pages = Math.ceil(memberData.count / nums); //得到总页数
        onlineTime.laypage(pages);
    });
    $("#export").on('click', function () {
        excelExport.Export("onlineTime", "导出表格",'');
    });
});
var onlineTime = {
    listDate: function (start, pageSize) {
        var memberData;
        var month = $("#month").val();
        var name = $("#name").val();
        var company = $("#companySelect").val();
        $.ajax({
            url: url.list,
            async: false,
            beforeSend: function () {
                layer.load(1, {
                    shade: [0.5, '#fff'] //0.1透明度的白色背景
                });
            },
            data: {start: start, length: pageSize, month: month, name: name, company: company},
            success: function (data) {
                memberData = data.data;
            }
        });
        return memberData;
    },
    laypage: function (pages) {
        laypage({
            cont: 'page',
            pages: pages,
            jump: function (obj) {
                onlineTime.createMonth();
                var memberData = onlineTime.listDate(obj.curr, nums);
                var countOfMonth = memberData.countOfMonth;
                var htmlStr = '';
                $(memberData.driverOnlineDetails).each(function (index, val) {
                    var totalTimes = 0;
                    var html = '<tr>';
                    if (val.real_name == null) {
                        html += '<td>' + val.phone + '</td>';
                    } else {
                        html += '<td>' + val.real_name + '(' + val.phone + ')</td>';
                    }

                    if (val.dayDatil) {
                        var jsonStr = "[" + val.dayDatil + "]";
                        var jsonArray = eval('(' + jsonStr + ')');
                        if (jsonArray.length > 0) {
                            for (var i = 0; i < countOfMonth; i++) {
                                var dayOfMonth = i + 1;
                                var times = 0;
                                $(jsonArray).each(function (index, val) {
                                    if (val.date == dayOfMonth) {
                                        times = val.time;
                                    }
                                });
                                times = times / 60 ;
                                if (times >= 24){
                                    times = 23.98
                                }
                                totalTimes += times
                                html += '<td>' + times.toFixed(2) + '</td>';
                            }
                        }
                    } else {
                        for (var i = 0; i < countOfMonth; i++) {
                            var times = 0;
                            html += '<td>' + times.toFixed(2) + '</td>';
                        }
                    }
                    html += '<td>' + totalTimes.toFixed(2) + '</td>';
                    html += '</tr>';
                    htmlStr += html;

                });
                $("#data").empty();
                $("#data").append(htmlStr);
                layer.closeAll();
            }
        })
    },
    createMonth: function () {
        $.ajax({
            url: url.monthDays,
            data: {month: $("#month").val()},
            async: false,
            success: function (data) {
                var html = '<th width="40">姓名</th>';
                for (var i = 0; i < data.data; i++) {
                    html += '<th>' + (i + 1) + '</th>';
                }
                html += '<th>月计</th>';
                $("#countOfMonth").empty();
                $("#countOfMonth").append(html);
            }
        })
    }
}