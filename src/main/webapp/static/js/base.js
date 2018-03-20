/**
 * Created by BOGONm on 16/8/11.
 */
$(document).ready(function () {
    webSocket.init();
    $('.select2').select2({
        placeholder: "请选择",
        language: "zh-CN"
    });
    companySelect.init();
    statistics.statisticsAll();
    $('input').iCheck({
        checkboxClass: 'icheckbox_square',
        radioClass: 'iradio_square',
        increaseArea: '20%' // optional
    });
    $('.datepicker-input').datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0,
        format: 'yyyy-mm-dd'
    });
    $('.datetimepicker-input').datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        format: 'yyyy-mm-dd hh:ii',
        minuteStep: 1
    });
    $('.datetime0picker-input').datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        format: 'yyyy-mm-dd hh:ii:ss'
    });

    $(".updatePw").on('click', function (a) {
        var id = $(a.target).attr("data-id");
        var index = layer.open({
            type: 0,
            content: tpl.bind($('#updatePw_tpl'), {id: id}),
            yes: function (index, layero) {
                $('#password_form').trigger('submit');
            }, success: function (layero, index) {
                $('#password_form').validator({
                    valid: function (form) {
                        $.ajax({
                            type: 'post',
                            url: g.ctx + '/admin/sys/user/updatepd',
                            data: $(form).serialize(),
                            success: function (data) {
                                if (data.status == 'OK') {
                                    layer.msg(data.msg);
                                } else {
                                    layer.tips(data.msg, '#oldpassword');
                                }
                            }
                        });
                    }
                });
            }
        });
    });

    $('#navbar-toggle').on('click', function () {
        $('#navbar').collapse('toggle');
    });
    $('#navbar-toggle1').on('click', function () {
        $('#navbar1').collapse('toggle');
    });
});
$.extend($.fn.DataTable.defaults, {
    "pagingType": "full_numbers",
    "stateSave": true,
    "searching": false,
    "serverSide": true,
    "ordering": false,
    "dom": 'rt<"bottom"iflp<"clear">>',
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
        if (!noCheck) {
            var check = {
                "render": function (a, b, c) {
                    var html = '<input class="table_checkbox" name="" type="checkbox" value="' + a + '">';
                    return html;
                },
                "targets": 0
            };
            columns.unshift(check);
        }
        var table = $table.DataTable({
            "ajax": {
                'url': url,
                type: 'post',
                'data': function (d) {
                    var searchGroup = $('#searchForm_');
                    var inputVal = searchGroup.find("input");
                    var select = searchGroup.find("select");
                    console.log(select);
                    $(inputVal).each(function (index, val) {
                        if (typeof($(val).attr('name')) != 'undefined') {
                            console.log($(val).attr('name'));
                            d[$(val).attr('name')] = $(val).val();
                            console.log($(val).val());
                        }
                    });
                    $(select).each(function (index, val) {
                        if (typeof($(val).attr('name')) != 'undefined') {
                            d[$(val).attr('name')] = $(val).val();
                        }
                    });
                }
            },
            "columns": columns,
            "initComplete": function () {
                // initFunc();
                //ICHECK
                // $('.table_checkbox').iCheck({
                //     checkboxClass: 'icheckbox_square-aero',
                //     radioClass: 'iradio_square-aero',
                //     increaseArea: '20%' // optional
                // });
            },
            "page.dt": function () {
            }
        });
        $table.on('draw.dt', function () {
            initFunc();
            //ICHECK
            $('.table_checkbox').iCheck({
                checkboxClass: 'icheckbox_square-aero',
                radioClass: 'iradio_square-aero',
                increaseArea: '20%' // optional
            });
        });
        return table;
    }
};
/**
 * 模板引擎使用
 * @type {{bind: tpl.bind}}
 */
var tpl = {
    bind: function ($tpl, content) {
        if (content) {
            return laytpl($tpl.html()).render(content);
        } else {
            return laytpl($tpl.html()).render({});
        }

    }
};

var uploadPlug = {
    create: function (picker, $picturesho, $pictureval) {
        var uploader = WebUploader.create({
            pick: picker,//图片选择琪
            auto: true,//自动上传
            server: g.ctx + '/file',//服务器地址
            fileSingleSizeLimit: 2 * 1024 * 1024,
            accept: {
                title: '图片文件',
                extensions: 'gif,jpg,jpeg,bmp,png'
            },
            duplicate: true
        });
        uploader.on('uploadSuccess', function (file, data) {
            $picturesho.attr("src",data.data.path);
            $pictureval.val(data.data.path);
            layer.closeAll('page');
        });
        uploader.on('error', function (errorCode, limitSize, file) {
            if (errorCode == 'F_EXCEED_SIZE') {
                layer.closeAll();
                layer.alert('请上传2M以内头像',
                    {ico: 2}
                )
            } else if (errorCode == 'Q_TYPE_DENIED') {
                layer.closeAll();
                layer.alert('请上传图片文件',
                    {ico: 2}
                )
            }
        });
        uploader.on('uploadProgress', function (a, b, c) {
            $('#uploadProgress').css("width", (b * 100) + '%');
        });
        uploader.on('uploadBeforeSend', function (a, b, c) {
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0, //不显示关闭按钮
                shift: 2,
                shadeClose: false, //开启遮罩关闭
                area: ['100px', '20px'], //宽高
                content: '<div style="height: 20px; width: 100%;">' +
                '<div style="margin-bottom: 0px; border-radius:2px; " class="progress no-rounded progress-striped"><div id="uploadProgress" class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="1" aria-valuemin="0" aria-valuemax="100" style="width: 0%">' +
                '<span class="sr-only">40% Complete (success)</span>' +
                '</div></div></div>'
            });
        });
    }
};

var webSocket = {
    init: function (callBack) {
        // var websocket = null;
        // //判断当前浏览器是否支持WebSocket
        // if ('WebSocket' in window) {
        //     websocket = new WebSocket("ws://" + g.websocket + g.ctx + "/websocket?" + g.user);
        // }
        // else {
        //     alert('当前浏览器 Not support websocket')
        // }
        // //连接发生错误的回调方法
        // websocket.onerror = function () {
        //     console.log("WebSocket连接发生错误");
        // }
        // //连接成功建立的回调方法
        // websocket.onopen = function () {
        //     console.log("WebSocket连接成功");
        // }
        // //接收到消息的回调方法
        // websocket.onmessage = function (event) {
        //     var data = jQuery.parseJSON(event.data);
        //     if (data.type == 'NOTICE') {
        //         $('#notice_sounds')[0].play();
        //         layer.msg('您有新订单了', {
        //             offset: 0,
        //             shift: 6
        //         });
        //     } else if (data.type == 'NOTICE') {
        //         layer.msg(data.real_name + '上线了', {
        //             offset: 0,
        //             shift: 6
        //         });
        //     } else {
        //         callBack(event.data);
        //     }
        // }
        // //连接关闭的回调方法
        // websocket.onclose = function () {
        //     console.log('close');
        // }
        // window.onbeforeunload = function () {
        //     websocket.close();
        // }
    }
}

var companySelect = {
    init: function () {
        if ($('.select-company')) {
            $('.select-company').each(function (index, val) {
                console.log(val);
                $(val).empty();
            });
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
        var searchGroup = $('#searchForm_');
        var inputVal = searchGroup.find("input");
        var select = searchGroup.find("select");
        var filters = {};
        if (filter) {
            $(inputVal).each(function (index, val) {
                if (typeof($(val).attr('name')) != 'undefined') {
                    filters[$(val).attr('name')] = $(val).val();
                }
            });
            $(select).each(function (index, val) {
                if (typeof($(val).attr('name')) != 'undefined') {
                    filters[$(val).attr('name')] = $(val).val();
                }
            });
        }
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
var idTmr;
function  getExplorer() {
    var explorer = window.navigator.userAgent ;
    //ie
    if (explorer.indexOf("MSIE") >= 0) {
        return 'ie';
    }
    //firefox
    else if (explorer.indexOf("Firefox") >= 0) {
        return 'Firefox';
    }
    //Chrome
    else if(explorer.indexOf("Chrome") >= 0){
        return 'Chrome';
    }
    //Opera
    else if(explorer.indexOf("Opera") >= 0){
        return 'Opera';
    }
    //Safari
    else if(explorer.indexOf("Safari") >= 0){
        return 'Safari';
    }
}
function method1(tableid) {//整个表格拷贝到EXCEL中
    if(getExplorer()=='ie')
    {
        var curTbl = document.getElementById(tableid);
        var oXL = new ActiveXObject("Excel.Application");

        //创建AX对象excel
        var oWB = oXL.Workbooks.Add();
        //获取workbook对象
        var xlsheet = oWB.Worksheets(1);
        //激活当前sheet
        var sel = document.body.createTextRange();
        sel.moveToElementText(curTbl);
        //把表格中的内容移到TextRange中
        sel.select();
        //全选TextRange中内容
        sel.execCommand("Copy");
        //复制TextRange中内容
        xlsheet.Paste();
        //粘贴到活动的EXCEL中
        oXL.Visible = true;
        //设置excel可见属性

        try {
            var fname = oXL.Application.GetSaveAsFilename("Excel.xls", "Excel Spreadsheets (*.xls), *.xls");
        } catch (e) {
            print("Nested catch caught " + e);
        } finally {
            oWB.SaveAs(fname);
            oWB.Close(savechanges = false);
            //xls.visible = false;
            oXL.Quit();
            oXL = null;
            //结束excel进程，退出完成
            //window.setInterval("Cleanup();",1);
            idTmr = window.setInterval("Cleanup();", 1);

        }
    }
    else
    {
        tableToExcel(tableid)
    }
}
function Cleanup() {
    window.clearInterval(idTmr);
    CollectGarbage();
}
var tableToExcel = (function() {
    var uri = 'data:application/vnd.ms-excel;base64,',
        template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><meta http-equiv="Content-Type" charset=utf-8"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
        base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) },
        format = function(s, c) {
            return s.replace(/{(\w+)}/g,
                function(m, p) { return c[p]; }) }
    return function(table, name) {
        if (!table.nodeType) table = document.getElementById(table)
        var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
        window.location.href = uri + base64(format(template, ctx))
    }
})()
