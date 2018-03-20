/**
 * Created by admin on 2016/10/17.
 */
var map;
var driverMap = new AMap.Map('hidden', {
    center: [117.000923, 36.675807],
    zoom: 6
});
;
var url = {
    driverLoactionList: g.ctx + "/admin/member/driver/map/driversRealLocation",
    driverList: g.ctx + '/admin/member/driver/map/kongxiansiji',
    zhixingzhongsiji: g.ctx + '/admin/member/driver/map/zhixingzhongsiji',
    weipaidandingdan: g.ctx + '/admin/member/driver/map/weipaidandingdan',
    paidan: g.ctx + '/admin/member/driver/map/paidan',
    WorkingLineStatus:g.ctx + '/admin/total/manglusiji',
    OffLineStatus:g.ctx + '/admin/total/lixiansiji',
    OnLineStatus:g.ctx + '/admin/total/zaixiansiji'
};
var driverTable;
var manglusijiTable;
var driving;
var statusStr = '3';
var infoWindow = new AMap.InfoWindow({offset: new AMap.Pixel(25, 5)});
var onlineMarkers = [];
var offlineMarkers = [];
var busyMarkers = [];
var table;
$(document).ready(function () {
    statistics.statisticsAll();
    companySelect.init();
    setInterval("drvierMap.loadlist(),OnLineStatus.refresh1(),OnLineStatus.refresh2(),OnLineStatus.refresh3()", 60000);
    window.onresize = function () {
        $("#divNewOrder").css("right", "11px");
    }
    $('.select2').select2({
        placeholder: "请选择",
        language: "zh-CN"
    });
    $(".label").on('click', function (target) {
        var current = $(target.currentTarget);
        var type = current.data("type");
        drvierMap.taggleClass(current, type);
    });
    $("#admap").css("height", window.screen.height - 170);
    map = new AMap.Map('admap', {
        center: [117.000923, 36.675807],
        zoom: 6
    });
    map.plugin(["AMap.ToolBar"], function () {
        map.addControl(new AMap.ToolBar());
    });
    drvierMap.loadlist();
    $("#doing").on('click', function (target) {
        var flag = $(target.currentTarget).data("flag");
        if (!flag) {
            $("#divDoing").animate({opacity: "1", left: "-=400"}, 1000)
                .slideDown("slow");
            $(target.currentTarget).data("flag", true);
        } else {
            $("#divDoing").animate({opacity: "1", left: "+=400"}, 1000)
                .slideDown("slow");
            $(target.currentTarget).data("flag", false);
        }

    });
    $("#newOrder").on('click', function (target) {
        var flag = $(target.currentTarget).data("flag");
        if (!flag) {
            $("#divNewOrder").animate({opacity: "1", right: "-=400"}, 1000)
                .slideDown("slow");
            $(target.currentTarget).data("flag", true);
        } else {
            $("#divNewOrder").animate({opacity: "1", right: "+=400"}, 1000)
                .slideDown("slow");
            $(target.currentTarget).data("flag", false);
        }
    });
    dataList.orderList();
    $("#companySelect").on("change", function () {
        statistics.statisticsAll();
        table.ajax.reload();
        var labal = $(".label");
        $(labal).each(function (index, val) {
            var current = $(val.currentTarget);
            var type = current.data("type");
            drvierMap.taggleClass(current, type);
        });
        if (manglusijiTable != undefined) {
            manglusijiTable.ajax.reload();
        } else {
            dataList.manglusijiList();
        }

    });
    $("#driverName").on("change", function () {
        if (driverTable != undefined) {
            driverTable.ajax.reload();
        } else {
            dataList.driverList();
        }
    });
    dataList.manglusijiList();
});
var tpl = {
    bind: function ($tpl, content) {
        if (content) {
            return laytpl($tpl.html()).render(content);
        } else {
            return laytpl($tpl.html()).render({});
        }
    }
};
var drvierMap = {
    loadlist: function () {
        map.clearMap();
        $.ajax({
            url: url.driverLoactionList,
            data: {company: $("#companySelect").val(), status: statusStr},
            sync: true,
            success: function (data) {
                infoWindow.close();
                $(data.data).each(function (index, val) {
                    var marker = new AMap.Marker({
                        map: map,
                        position: [val.longitude, val.latitude],
                        content: drvierMap.createHtml(val)
                    });
                    drvierMap.addArray(val, marker);
                    marker.on('mouseover', function (e) {
                        $.ajax({
                            url: g.ctx + '/admin/member/driver/driverInfo',
                            data: {id: val.id},
                            success: function (data) {
                                var driverInfo = data.data;
                                // var img = driverInfo.driverLicenseInfo.photo;
                                // if (img == null) {
                                //     img = 'static/images/default-user.jpg';
                                // }
                                var tmpHtml = tpl.bind($("#infoHTML"), {
                                    // img: img,
                                    realName: driverInfo.real_name,
                                    phone: driverInfo.phone,
                                    countDay: driverInfo.dayCount,
                                    countMonth: driverInfo.monthCount,
                                    company: driverInfo.companyInfo.name,
                                    locationDate: driverInfo.locationDate
                                });
                                infoWindow.setContent(tmpHtml);
                                infoWindow.open(map, e.target.getPosition());
                            }
                        });
                    });
                    marker.on('mouseout', function (e) {
                        infoWindow.close();
                    });
                });
                map.setFitView();
            }
        });
    },
    createHtml: function (val) {
        var color;
        var rcolor;
        if (val.status == 0 || val.status == 1) {
            color = 'sjcolor-lixian';
            rcolor = 'rkcolor-lixian';
        } else if (val.status == 3) {
            color = 'sjcolor-online';
            rcolor = 'rkcolor-online';
        } else if (val.status == 4 || val.status == 5 || val.status == 6) {
            color = 'sjcolor-manglu';
            rcolor = 'rkcolor-manglu';
        }
        var html = tpl.bind($("#markerHTML"), {realName: val.real_name, color: color, rcolor: rcolor});
        return html;
    },
    addArray: function (val, marker) {
        if (val.status == 0 || val.status == 1) {
            offlineMarkers.push(marker);
        } else if (val.status == 3) {
            onlineMarkers.push(marker);
        } else if (val.status == 4 || val.status == 5 || val.status == 6) {
            busyMarkers.push(marker);
        }
    },
    taggleClass: function (current, type) {
        if (type == 1) {
            if (current.hasClass("online")) {
                current.removeClass("online");
                current.addClass("label-success");
                statusStr = statusStr + ",3";
            } else {
                current.removeClass("label-success");
                current.addClass("online");
                statusStr = statusStr.replace("3,", "").replace(",3", "").replace("3", "");
                map.remove(onlineMarkers);
            }
        } else if (type == 2) {
            if (current.hasClass("offline")) {
                current.removeClass("offline");
                current.addClass("label-primary");
                statusStr = statusStr + ",0,1";
            } else {
                current.removeClass("label-primary");
                current.addClass("offline");
                statusStr = statusStr.replace(",0,1", "");
                map.remove(offlineMarkers)
            }
        } else if (type == 3) {
            if (current.hasClass("busy")) {
                current.removeClass("busy");
                current.addClass("label-danger");
                statusStr = statusStr + ",4,5,6";
            } else {
                current.removeClass("label-danger");
                current.addClass("busy");
                statusStr = statusStr.replace(",4,5,6", "").replace("4,5,6", "").replace("4,5,6,", "");
                map.remove(busyMarkers)
            }
        }
        drvierMap.loadlist();
    }
}
var OnLineStatus = {
    refresh1: function () {
        $.ajax({
            type : "get",
            async : false, //同步请求
            url : url.OnLineStatus,
            timeout:1000,
            success:function(data){
                $("#OnLineStatus").html(data.data);
            },
            error: function() {
                // alert("失败，请稍后再试！");
            }
        });
    },
    refresh2: function () {
        $.ajax({
            type : "get",
            async : false, //同步请求
            url : url.OffLineStatus,
            timeout:1000,
            success:function(data){
                $("#OffLineStatus").html(data.data);
            },
            error: function() {
                // alert("失败，请稍后再试！");
            }
        });
    },
    refresh3: function () {
        $.ajax({
            type : "get",
            async : false, //同步请求
            url : url.WorkingLineStatus,
            timeout:1000,
            success:function(data){
                $("#WorkingLineStatus").html(data.data);
            },
            error: function() {
                // alert("失败，请稍后再试！");
            }
        });
    }
}
var statistics = {
    statisticsAll: function () {
        var countSum = $(".countSum");
        $(countSum).each(function (index, val) {//统计插件
            statistics.statisticsDeal(val);
        });
    },
    statisticsDeal: function (val) {
        var url = $(val).data('url');
        var filter = $(val).data('filter');
        var filters = {'s-company-EQ': $("#companySelect").val()}
        var span = $(val).children("span");
        var img = $(val).children("img");
        $.ajax({
            url: url,
            data: filters,
            beforeSend: function () {
            }, success: function (data) {
                img.remove();
                if (data.status == 'OK') {
                    if (data.data == null) {
                        $(span[1]).html(0);
                    } else {
                        $(span[1]).html(data.data);
                    }

                } else {
                    $(span[1]).html(data.msg);
                }

            }
        });
    }
}
var dataList = {
    driverList: function () {
        var columns = [
            {
                'render': function (a, b, c) {
                    return '<button type="button" href="javascript:void(0);" class="btn btn-link btn-xs paidan" data-id="' + c.id + '">派单</button>';
                }
            },
            {"data": "real_name"},
            {
                "data": "distance",
                "render": function (display, cell, row) {
                    if (display == null) {
                        return '0';
                    } else {
                        return '<span class="distance" data-startlng="'
                            +$("#lon").val()+'" data-startlat="'+$("#lat").val()
                            +'" data-endlng="'+row.longitude+'" data-endlat="'+row.latitude+'"></span>';
                    }
                }
            }
        ];
        driverTable = $("#driverList").DataTable({
            "ajax": {
                'url': url.driverList,
                type: 'post',
                'data': function (d) {
                    d['company'] = $("#orderCompany").val();
                    d['lat'] = $("#lat").val();
                    d['lon'] = $("#lon").val();
                    d['service'] = $("#orderType").val();
                    d['driverName'] = $("#driverName").val();
                }
            },
            "columns": columns
        });
        $("#driverList").on('draw.dt', function () {
            var lyIndex = 0;
            $('.distance').each(function (index,val) {
                var start = new AMap.LngLat($(val).data('startlng'), $(val).data('startlat'));
                var end = new AMap.LngLat($(val).data('endlng'), $(val).data('endlat'));
                calDistance.getDistance(start, end,val);
            });
            $("#driverList > tbody > tr").on('mouseover', function (target) {
                var id = $($(target.currentTarget).find("button")[0]).data("id");
                $.ajax({
                    url: g.ctx + '/admin/member/driver/driverInfo',
                    data: {id: id},
                    success: function (data) {
                        if (data != '') {
                            var driverInfo = data.data;
                            var tmpHtml = tpl.bind($("#infoTipHTML"), {
                                realName: driverInfo.real_name,
                                phone: driverInfo.phone,
                                countDay: driverInfo.dayCount,
                                countMonth: driverInfo.monthCount,
                                company: driverInfo.companyInfo.name,
                                locationDate: driverInfo.locationDate
                            });
                            lyIndex = layer.tips(tmpHtml, $(target.currentTarget), {
                                tips: [4],
                                area: ['200px', '130px'],
                                time: 0
                            });
                        }
                    }
                });
            });
            $("#driverList > tbody > tr").on('mouseout', function (target) {
                layer.close(lyIndex);
            });
            $(".paidan").on('click', function (target) {
                var id = $($(target.currentTarget)).data("id");
                $.ajax({
                    url: url.paidan,
                    data: {orderId: $("#orderId").val(), driverId: id},
                    success: function (data) {
                        layer.msg(data.msg);
                    }
                });
            });
        });
    },
    orderList: function () {
        var columns = [
            {
                "data": "real_name",
                'render': function (a, b, c) {
                    return '<span class="label label-success" data-service="'
                        + c.service_type + '" data-type="' + c.type + '" data-company="' + c.company + '" data-id="'
                        + c.id + '" data-lat="' + c.start_latitude + '" data-lon="' + c.start_longitude + '" data-start="'
                        + c.reservation_address + '" data-endLat="' + c.end_latitude + '" data-endLon="' + c.end_longitude + '"'
                        + ' data-end="' + c.destination + '">'
                        + c.name + '</span>&nbsp;预约时间:' + c.setouttime;
                }
            }
        ];
        table = dt.build($("#orderList"), url.weipaidandingdan, columns, function () {
            $("#orderList > tbody > tr").on('click', function (target) {
                var orderId = $($(target.currentTarget).find("span")).data("id");
                var company = $($(target.currentTarget).find("span")).data("company");
                var type = $($(target.currentTarget).find("span")).data("type");
                var lat = $($(target.currentTarget).find("span")).data("lat");
                var lon = $($(target.currentTarget).find("span")).data("lon");
                var endLat = $($(target.currentTarget).find("span")).data("endlat");
                var endLon = $($(target.currentTarget).find("span")).data("endlon");
                $("#orderCompany").val(company);
                $("#lat").val(lat);
                $("#lon").val(lon);
                $("#orderType").val(type);
                $("#orderId").val(orderId);
                if (driverTable != undefined) {
                    driverTable.ajax.reload();
                } else {
                    dataList.driverList();
                }
                if (driving != undefined) {
                    driving.clear();
                }
                //构造路线导航类
                driving = new AMap.Driving({
                    map: map,
                });
                driving.search(new AMap.LngLat(lon, lat), new AMap.LngLat(endLon, endLat));
            });
            var lyIndex = 0;
            $("#orderList > tbody > tr").on('mouseover', function (target) {
                var start = $($(target.currentTarget).find("span")).data("start");
                var end = $($(target.currentTarget).find("span")).data("end");
                if (end == 'null' || end == null) {
                    end = '';
                }
                var tmpHtml = tpl.bind($("#orderHtml"), {start: start, end: end});
                lyIndex = layer.tips(tmpHtml, $(target.currentTarget), {tips: [4], time: 0});
            });
            $("#orderList > tbody > tr").on('mouseout', function (target) {
                layer.close(lyIndex);
            });
        });
    },
    manglusijiList: function () {
        var columns = [
            {
                'render': function (a, b, c) {
                    var no = '【' + c.no + '】';
                    return c.real_name + '正在执行订单：' + order.getOrderNo(no);
                }
            }
        ];
        manglusijiTable = $("#zhixingzhong").DataTable({
            "ajax": {
                'url': url.zhixingzhongsiji,
                type: 'post',
                'data': function (d) {
                    d['s-ddi.company-EQ'] = $("#companySelect").val();
                }
            },
            "columns": columns
        });
        $("#zhixingzhong").on('draw.dt', function () {
        });
    }
}

$.extend($.fn.DataTable.defaults, {
    "pagingType": "full_numbers",
    "stateSave": true,
    "searching": false,
    "serverSide": true,
    "ordering": false,
    "dom": '<t><p>',
    "pagingType": "simple",
    "language": {
        "sLoadingRecords": "正在加载数据...",
        "sEmptyTable": "暂无数据",
        "sZeroRecords": "暂无数据",
        "processing": "玩命加载中...",
        "lengthMenu": "显示 _MENU_ 项结果",
        "zeroRecords": "没有匹配结果",
        "info": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
        "infoEmpty": "显示第 0 至 0 项结果，共 0 项",
        "infoFiltered": "(由 _MAX_ 项结果过滤)",
        "infoPostFix": "",
        "url": "",
        "paginate": {
            "first": "首页",
            "previous": "上一页",
            "next": "下一页",
            "last": "末页"
        }
    },
    autoWidth: false
});
var dt = {
    build: function ($table, url, columns, initFunc, noCheck) {
        var table = $table.DataTable({
            "ajax": {
                'url': url,
                type: 'post',
                'data': function (d) {
                    d['s-do.company-EQ'] = $("#companySelect").val();
                }
            },
            "columns": columns,
            "initComplete": function () {
                // initFunc();
            }
        });
        $table.on('draw.dt', function () {
            initFunc();
            //ICHECK
            $('.checkOther').iCheck({
                checkboxClass: 'icheckbox_square-aero',
                radioClass: 'iradio_square-aero',
                increaseArea: '20%' // optional
            });
        });
        return table;
    }
};
var companySelect = {
    init: function () {
        if ($('.select-company')) {
            $.ajax({
                url: g.ctx + '/admin/sys/company/allcompany',
                success: function (data) {
                    var option = '<option value="">请选择</option>';
                    $(data).each(function (index, val) {
                        option += '<option value="' + val.id + '">' + val.name + '</option>';
                    });
                    $('.select-company').append(option);
                    $('.select-company').val($('.select-company').data("value")).trigger("change");
                }
            });
        }
    }
}
var order = {
    getOrderNo: function (str) {
        var f = str.indexOf("【");
        var l = str.lastIndexOf("】");
        var orderNo = '';
        if (f != -1 && l != -1) {
            orderNo = str.substring(f + 1, l);
        }
        var html = '<a target="_blank" href="' + g.ctx + '/admin/order/lockup?no=' + orderNo + '">' + orderNo + '</a>';
        str = str.replace(orderNo, html);
        return str;
    }
}

var calDistance = {
    getDistance: function (start, end,val) {
        var distance = 0;
        //构造路线导航类
        var driving = new AMap.Driving({
            map: driverMap
        });
        driving.search(start, end, function (status, result) {
            distance = parseFloat(result.routes[0].distance) / 1000;
            $(val).html(distance);
        });
    }
}