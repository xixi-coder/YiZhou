<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>改派订单</title>
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
        div.circle{
            padding: 2px 10px;
            border-radius: 20px;
            width: 80px;
            text-align: center;
            color: #FFFFFF;
        }
    </style>
    <!-- Extra CSS Libraries End -->
</head>
<body role="document" style="padding: 10px;overflow-x: hidden;">
<div class="panel panel-default">
    <div class="panel-body">
        <div class="row">
            <div class="col-sm-6">
                <div id="admap" style="width: 100%;"></div>
            </div>
            <div class="col-sm-6">
                <form class="form-horizontal" method="post" role="form" autocomplete="off"
                      id="changeOrder_form">
                    <input type="hidden" value=""
                           id="memberId">
                    <input type="hidden" value=""
                           id="distance">
                    <input type="hidden" value=""
                           id="time">
                    <input type="hidden" value="${(order.id)!}"
                           id="orderId" name="orderId">
                    <table data-sortable="" style="border:1px solid #ddd;" class="table table-hover table-striped"
                           data-sortable-initialized="true">
                        <tr>
                            <td width="25%"><label for="phone" class="control-label">客户手机号:</label></td>
                            <td width="40%"><input type="text" name="memberInfo.phone" value="${(order.phone)!}" readonly="readonly"
                                                   class="form-control n-invalid"
                                                   id="phone"
                                                   placeholder="如：18012345678"></td>
                            <td width="25%" id="member_level">
                            </td>
                        </tr>
                        <tr>
                            <td><label for="real_name" class="control-label">客户称呼:</label></td>
                            <td><input type="text" name="memberInfo.nick_name" value="${(memberInfo.nick_name)!}"
                                       class="form-control n-invalid"
                                       id="real_name"
                                       placeholder="如：张三"></td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                        <tr style="display: none;" id="show_member_info_pancel">
                        </tr>
                        <tr>
                            <td><label for="real_name" class="control-label">推荐人:</label></td>
                            <td><input type="text" name="inst_phone" value="${(memberInfo.inst_phone)!}" readonly="readonly"
                                       class="form-control n-invalid"
                                       id="inst_phone"
                                       placeholder="如：18123456789"></td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                        <tr>
                            <td><label for="reservation_address" class="control-label">客户地点:</label></td>
                            <td>
                                <div class="input-group">
                                    <input type="text" name="order.reservation_address" value="${(order.reservation_address)!}" readonly="readonly"
                                           class="form-control n-invalid"
                                           id="reservation_address"
                                           placeholder="如：安徽省合肥市临滨苑">
                                    <div class="input-group-btn">
                                        <button class="btn btn-default" type="button" id="start_address">定位</button>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                    <#--起点经纬度-->
                        <input type="hidden" id="reservation_lat" name="trip.start_latitude" value="${(trip.start_latitude)!}"/>
                        <input type="hidden" id="reservation_lng" name="trip.start_longitude" value="${(trip.start_longitude)!}"/>
                    <#--终点经纬度-->
                        <input type="hidden" id="destination_lat" name="trip.end_latitude" value="${(trip.end_latitude)!}"/>
                        <input type="hidden" id="destination_lng" name="trip.end_longitude" value="${(trip.end_longitude)!}"/>
                        <tr>
                            <td><label for="destination" class="control-label">预约终点:</label></td>
                            <td>
                                <div class="input-group">

                                    <input type="text" name="order.destination" value="${(order.destination)!}" readonly="readonly"
                                           class="form-control n-invalid"
                                           id="destination"
                                           placeholder="如：安徽省合肥市临滨苑">
                                    <div class="input-group-btn">
                                        <button class="btn btn-default" type="button" id="end_address">定位</button>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                        <tr>
                            <td><label for="setoutime" class="control-label" >请重新预约时间:</label></td>
                            <td>
                                <input type="text" name="setouttime" readonly="readonly"
                                       class="datetimepicker-input form-control n-invalid"
                                       id="setoutime" value="${.now?string("yyyy-MM-dd HH:mm")}"></td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                        <tr>
                            <td><label for="real_name" class="control-label">订单所属公司:</label></td>
                            <td>
                                <input type="text" name="companyname" readonly="readonly"
                                       class="form-control n-invalid"
                                       id="companyName" value="${(company.name)!}"></td>

                            </td>
                            <input type="text" name="company" hidden="hidden" id="companyId" value="${(order.company)!}"
                        </tr>
                    <#--<tr>-->
                    <#--<td><input type="checkbox" value="1" id="orderAmount">&nbsp;&nbsp;<label for="real_name"-->
                    <#--class="control-label">定价格:</label>-->
                    <#--</td>-->
                    <#--<td>-->
                    <#--<div class="input-group" style="display: none;">-->
                    <#--<input type="text" name="order.amount" value="" class="form-control n-invalid"-->
                    <#--id="amount">-->
                    <#--<div class="input-group-addon">元</div>-->
                    <#--</div>-->
                    <#--</td>-->
                    <#--<td>-->
                    <#--<div style="padding-top: 10px;color: red;">*</div>-->
                    <#--</td>-->
                    <#--</tr>-->
                        <tr>
                            <td><label for="real_name" class="control-label">预算价格:</label></td>
                            <td>
                                <div class="input-group">
                                    <input type="text" name="order.yg_amount" value="${(order.yg_amount)!}"
                                           class="form-control n-invalid"
                                           id="yg_amount">
                                    <div class="input-group-addon">元</div>
                                </div>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                        <#--<tr>
                            <td><label for="real_name" class="control-label">备注:</label></td>
                            <td>
                                <textarea style="width: 99%;" rows="10" name="order.remark"></textarea>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>-->
                        <tr id="show_mill" style="display: none;">
                        </tr>
                        <tr>
                            <td colspan="3">
                                <button type="button" id="changeOrder" class="btn btn-primary btn-lg btn-block">改派订单
                                </button>
                            </td>
                        </tr>
                    </table>
                    <input type="hidden" name="driverIds" id="driverIds"/>
                    <div class="table-responsive">
                        <table id="driverList" class="hover table table-striped table-bordered">
                            <thead>
                            <tr>
                                <th width="10px">
                                    <button type="button" id="referes" class="btn btn-link">刷新</button>
                                </th>
                                <th>姓名</th>
                                <th>距离(千米)</th>
                                <th>本日接单</th>
                                <th>本月接单</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div id="hidden" style="display: none;"></div>


<script type="text/html" id="markerHTML">
    <div class="sj-mark-ma {{d.color}}"><span>{{d.realName}}</span></div>
    <div class="sj-mark-rk {{d.rcolor}}"></div>
</script>
<script type="text/html" id="infoHTML">
    <div style="width: 300px;height: 200px;">
        <div style="width: 150px;height: 200px; float: left;">
            <img style="width: 150px;height: 200px;" src="${ctx}/{{d.img}}">
        </div>
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


<script src="${ctx}/static/js/jquery-1.9.1.min.js"></script>
<script src="${ctx}/static/js/bootstrap.js"></script>
<!--datatable -->
<script src="${ctx}/static/js/datatable/jquery.dataTables.js"></script>
<script src="${ctx}/static/js/datatable/dataTables.bootstrap.js"></script>
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
<script id="member_info_tmp" type="text/html">
    <td colspan="3" class="control-label" style="text-align: left;">
        <div class="row">
            <div class="col-sm-4">总订单数</div>
            <div class="col-sm-2">{{d.totalOrderCount}}</div>
            <div class="col-sm-4">实际订单数</div>
            <div class="col-sm-2">{{d.realTotalOrderCount}}</div>
        </div>
        <div class="row">
            <div class="col-sm-4">本月总订单数</div>
            <div class="col-sm-2">{{d.totalOrderCountOfThisMonth}}</div>
            <div class="col-sm-4">本月实际订单数</div>
            <div class="col-sm-2">{{d.realTotalOrderCountOfThisMonth}}</div>
        </div>
    </td>
</script>
<script id="order_distance" type="text/html">
    <td colspan="3" class="control-label" style="text-align: left;">
        <div class="row">
            <div class="col-sm-2">距离</div>
            <div class="col-sm-3">{{d.distance}}公里</div>
            <div class="col-sm-2">时间</div>
            <div class="col-sm-3">{{d.times}}分钟</div>
        </div>
    </td>
</script>
<script src="${ctx}/static/js/app/admin/change_order.js?_${.now?string("HHmmss")}"></script>
</body>
</html>