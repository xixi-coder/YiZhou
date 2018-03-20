<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>司机分布图</title>
    <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">

    <!-- Base Css Files -->
    <link href="${ctx}/static/css/bootstrap.css" rel="stylesheet"/>
    <link href="${ctx}/static/css/bootstrap-theme.css" rel="stylesheet"/>
    <link href="${ctx}/static/js/select2/css/select2.css" rel="stylesheet"/>
    <link href="${ctx}/static/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet"
          type="text/css"/>
    <link href="${ctx}/static/js/icheck/skins/all.css" rel="stylesheet"/>
    <!-- Extra CSS Libraries Start -->
    <link href="${ctx}/static/css/datatable/jquery.dataTables.css" rel="stylesheet" type="text/css"/>
    <style rel="stylesheet">
        .selected {
            background-color: whitesmoke !important;
        }

        .select2-selection--single {
            border: 1px solid #ccc !important;
            height: 34px !important;
        }

        table > tbody > tr > td {
            white-space: nowrap;
            word-break: nowrap
        }

        table > thead > tr > th {
            white-space: nowrap;
            word-break: nowrap
        }

        .sj-mark-rk {
            width: 0;
            height: 0;
            line-height: 0;
            border-width: 10px;
            border-style: solid;
            margin: 0 auto;
            margin-top: -20px;
        }

        .sjcolor-online {
            background-color: #59cc59;
        }

        .rkcolor-online {
            border-color: #59cc59 transparent transparent transparent;
        }

        .sjcolor-lixian {
            background-color: #428bca;
        }

        .rkcolor-lixian {
            border-color: #428bca transparent transparent transparent;
        }

        .sjcolor-manglu {
            background-color: #d9534f;
        }

        .rkcolor-manglu {
            border-color: #d9534f transparent transparent transparent;
        }

        .sj-mark-ma {
            padding: 2px 10px;
            border-radius: 20px;
            width: 80px;
            text-align: center;
            color: #FFFFFF;
        }

        .sj-mark {
            text-align: center;
            width: 80px;
        }

        span {
            cursor: pointer
        }

        .online {
            color: #5cb85c;
        }

        .offline {
            color: #428bca;
        }

        .busy {
            color: #d9534f;
        }

        body, button, input, select, textarea, h1, h2, h3, h4, h5, h6 {
            font-family: Microsoft YaHei, '宋体', Tahoma, Helvetica, Arial, "\5b8b\4f53", sans-serif;
        }

        .countSum {
            margin-right: 10px;
        }

        #orderList > tbody > tr {
            cursor: pointer;
        }
    </style>
    <!-- Extra CSS Libraries End -->
</head>
<body role="document" style="padding: 10px;overflow-x: hidden;">
<div class="panel panel-default" style="margin-bottom: 0px;">
    <div class="panel-heading" style="padding:0 0;">
        <nav class="navbar navbar-default" role="navigation" style="margin-bottom: 0px;">
            <div class="nav navbar-nav navbar-left">
                <button type="button" id="doing" class="btn btn-link btn-lg">
                    <span class="glyphicon glyphicon-tasks"></span>
                </button>
            </div>
            <div class="container-fluid">
                <input type="hidden" id="orderId"/>
                <input type="hidden" id="orderCompany"/>
                <input type="hidden" id="orderType"/>
                <input type="hidden" id="lat"/>
                <input type="hidden" id="lon"/>
                <form id="searchForm_">
                    <div class="navbar-header" style="padding-top: 7px;min-width: 600px;">
                    <#if hasRole("super_admin")||hasPermission("select-company")>
                        <label for="real_name" class="control-label">公司选择:</label>
                        <select class="select2 select-company" style="width: 200px;"
                                id="companySelect">
                            <option value="">请选择</option>
                        </select>
                    </#if>

                        <div data-type="1" class="countSum label label-success"
                             data-url="${ctx}/admin/total/zaixiansiji"
                             data-filter="true">
                            <span>在线：</span>
                            <span id="OnLineStatus">...</span>
                            <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                            <span></span>
                        </div>
                        <div id="" data-type="2" class="countSum label offline" data-url="${ctx}/admin/total/lixiansiji"
                             data-filter="true">
                            <span>离线：</span>
                            <span id="OffLineStatus">...</span>
                            <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                            <span></span>
                        </div>
                        <div id="" data-type="3" class="countSum label busy" data-url="${ctx}/admin/total/manglusiji"
                             data-filter="true">
                            <span>忙碌：</span>
                            <span id="WorkingLineStatus">...</span>
                            <img src="${ctx}/static/images/loading.gif" style="height: 20px;width: 20px;"/>
                            <span></span>
                        </div>
                        <div data-type="4" class="countSum label offline" data-url=""
                             data-filter="true">
                            <span class="time"></span>
                            <span></span>
                        </div>
                    </div>
                    <div class="nav navbar-nav navbar-right">
                        <button type="button" id="newOrder" class="btn btn-link btn-lg">
                            <span class="glyphicon glyphicon-tasks"></span>
                        </button>
                    </div>
                </form>
            </div>
        </nav>
    </div>
    <div class="panel-body" style="padding: 0 0;min-height: 400px;">
        <div style="border-right: 1px solid #ddd;background-color: white;position:absolute; left:11px; width: 300px;height: 90%;z-index: 1000;"
             id="divDoing">
            <div class="panel-body">
                <ul class="list-group">
                    <div class="table-responsive">
                        <table id="zhixingzhong" style="font-size: 6pt;"
                               class="hover table table-striped table-bordered">
                            <thead>
                            <tr>
                                <th>
                                    忙碌司机
                                </th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </ul>
            </div>
        </div>
        <div id="admap" style="width: 100%;"></div>
        <div style="border-left: 1px solid #ddd;background-color: white; position:absolute;right:11px;top: 62px;width: 350px;height: 90%;z-index: 1000;overflow-y: scroll;"
             id="divNewOrder">
            <div class="panel-body" style="overflow-y: scroll;height: 50%;">
                <ul class="list-group">
                    <div class="table-responsive">
                        <table id="orderList" style="font-size: 6pt;" class="hover table table-striped table-bordered">
                            <thead>
                            <tr>
                                <th>
                                    订单信息
                                </th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </ul>
            </div>
            <div class="panel-body" style="overflow-y: scroll;height: 50%;">
                <ul class="list-group">
                    <li class="list-group-item">
                        <input type="text" placeholder="司机姓名、手机号" id="driverName" style="width: 100%;"/>
                    </li>
                    <div class="table-responsive">
                        <table id="driverList" style="font-size: 6pt;" class="hover table table-striped table-bordered">
                            <thead>
                            <tr>
                                <th width="20%"></th>
                                <th>姓名</th>
                                <th>距离(千米)</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </ul>
            </div>
        </div>
    </div>
</div>
<div id="hidden" style="display: none;"></div>
<script src="${ctx}/static/js/jquery-1.9.1.min.js"></script>
<script src="${ctx}/static/js/bootstrap.js"></script>
<!--select2 -->
<script src="${ctx}/static/js/select2/js/select2.full.js"></script>
<script src="${ctx}/static/js/select2/js/i18n/zh-CN.js"></script>
<!--datepicker -->
<script src="${ctx}/static/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script src="${ctx}/static/js/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<!--layerTMP模板引擎 -->
<script src="${ctx}/static/js/laytpl.js"></script>
<script src="${ctx}/static/js/dict.js"></script>
<!--icheck -->
<script src="${ctx}/static/js/icheck/icheck.js"></script>
<!--datatable -->
<script src="${ctx}/static/js/datatable/jquery.dataTables.js"></script>
<script src="${ctx}/static/js/datatable/dataTables.bootstrap.js"></script>

<script src="${ctx}/static/js/layer/layer.js"></script>
<script type="text/javascript"
        src="http://webapi.amap.com/maps?v=1.3&key=3847e08227b5479aa1596a7a8b944026&plugin=AMap.Autocomplete,AMap.PlaceSearch,AMap.Geocoder,AMap.CitySearch,AMap.Driving"></script>
<script type="text/javascript">
    var g = {
        ctx: '${ctx}',
        websocket: '${websocket}',
        user: '${_USER_.id}'
    }
    var menu;
</script>
<script type="text/html" id="markerHTML">
    <div class="sj-mark-ma {{d.color}}"><span>{{d.realName}}</span></div>
    <div class="sj-mark-rk {{d.rcolor}}"></div>
</script>
<script type="text/html" id="infoHTML">
    <div style="width: 150px;height: 200px;">
        <#--<div style="width: 150px;height: 200px; float: left;">-->
            <#--<img style="width: 150px;height: 200px;" src="${ctx}/{{d.img}}">-->
        <#--</div>-->
        <div style="width: 150px;height: 200px; float: left; font-size: 13px;">
            <p><span style="font-weight: bolder">姓名:</span>{{d.realName}}</p>
            <p><span style="font-weight: bolder">电话:</span>{{d.phone}}</p>
            <p><span style="font-weight: bolder">所属公司:</span>{{d.company}}</p>
            <p><span style="font-weight: bolder">今日:</span>({{d.countDay}})<span
                    style="font-weight: bolder">单</span>,<span
                    style="font-weight: bolder">本月</span>:({{d.countMonth}})<span style="font-weight: bolder">单</span></p>
            <p><span style="font-weight: bolder">定位时间:</span>{{d.locationDate}}</p>
        </div>
    </div>
</script>
<script type="text/html" id="infoTipHTML">
    <div style="height: 130px;">
        <div style="height: 130px; float: left; font-size: 13px;">
            <p><span style="font-weight: bolder">姓名:</span>{{d.realName}}</p>
            <p><span style="font-weight: bolder">电话:</span>{{d.phone}}</p>
            <p><span style="font-weight: bolder">所属公司:</span>{{d.company}}</p>
            <p><span style="font-weight: bolder">今日:</span>({{d.countDay}})<span
                    style="font-weight: bolder">单</span>,<span
                    style="font-weight: bolder">本月</span>:({{d.countMonth}})<span style="font-weight: bolder">单</span></p>
        </div>
    </div>
</script>
<script type="text/html" id="orderHtml">
    <div style="height: 130px;">
        <div style="width: 150px;height: 130px; float: left; font-size: 13px;">
            <p><span style="font-weight: bolder">起点:</span>{{d.start}}</p>
            <p><span style="font-weight: bolder">终点:</span>{{d.end}}</p>
        </div>
    </div>
</script>
<script src="${ctx}/static/js/app/admin/driver_map.js?_${.now?string("HHmmss")}"></script>
<script type="text/javascript">

    countDown();
</script>
</body>
</html>