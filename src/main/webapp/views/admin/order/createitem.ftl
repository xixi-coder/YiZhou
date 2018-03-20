<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>创建订单</title>
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
                      id="createOrder_form">
                    <input type="hidden" value=""
                           id="memberId">
                    <input type="hidden" value="" name="order.distance"
                           id="distance">
                    <input type="hidden" value=""
                           id="time">
                    <table data-sortable="" style="border:1px solid #ddd;" class="table table-hover table-striped"
                           data-sortable-initialized="true">
                    <#if hasRole("super_admin")||hasPermission("select-company")>
                        <tr>
                            <td><label for="real_name" class="control-label">公司选择:</label></td>
                            <td>
                                <select class="select2" name="company" id="companySelect" style="width: 100%;">
                                    <#list companies as item>
                                        <option value="${(item.id)!}">${(item.name)!}</option>
                                    </#list>
                                </select>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                    </#if>
                        <tr>
                            <td width="25%"><label for="phone" class="control-label">联系号码:</label></td>
                            <td width="40%"><input type="text" name="memberInfo.phone" value=""
                                                   class="form-control n-invalid"
                                                   id="phone"
                                                   placeholder=""></td>
                            <td width="25%" id="member_level">
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                        <tr>
                            <td><label for="real_name" class="control-label">联系名称:</label></td>
                            <td><input type="text" name="memberInfo.nick_name" value="${(memberInfo.real_name)!}"
                                       class="form-control n-invalid"
                                       id="real_name"
                                       placeholder=" "></td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                        <tr style="display: none;" id="show_member_info_pancel">
                        </tr>
                    <#--<tr>
                        <td><label for="real_name" class="control-label">推荐人:</label></td>
                        <td><input type="text" name="inst_phone" value="${(memberInfo.inst_phone)!}"
                                   class="form-control n-invalid"
                                   id="inst_phone"
                                   placeholder="如：18123456789"></td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    </tr>-->
                        <tr>
                            <td><label for="reservation_address" class="control-label">预约地点:</label></td>
                            <td>
                                <div class="input-group">
                                    <input type="hidden" id="reservation_lat" name="trip.start_latitude"/>
                                    <input type="hidden" id="reservation_lng" name="trip.start_longitude"/>
                                    <input type="text" name="order.reservation_address" value=""
                                           class="form-control n-invalid"
                                           id="reservation_address"
                                           placeholder="">
                                    <div class="input-group-btn">
                                        <button class="btn btn-default" type="button" id="start_address">定位</button>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                        <tr>
                            <td><label for="destination" class="control-label">预约终点:</label></td>
                            <td>
                                <div class="input-group">
                                    <input type="hidden" id="destination_lat" name="trip.end_latitude"/>
                                    <input type="hidden" id="destination_lng" name="trip.end_longitude"/>
                                    <input type="text" name="order.destination" value=""
                                           class="form-control n-invalid"
                                           id="destination"
                                           placeholder="可不选">
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
                            <td><label for="setoutime" class="control-label">预约时间:</label></td>
                            <td>
                                <input type="text" name="setouttime" readonly="readonly"
                                       class="datetimepicker-input form-control n-invalid"
                                       id="setoutime" placeholder="可不选"></td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
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
                        <tr>
                            <td><label for="real_name" class="control-label">备注:</label></td>
                            <td>
                                <textarea style="width: 99%;" rows="10" name="order.remark"></textarea>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                        <tr id="show_mill" style="display: none;">
                        </tr>
                        <tr>
                            <td colspan="3">
                                <button type="button" id="createOrder" class="btn btn-primary btn-lg btn-block">创建订单
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
<script src="${ctx}/static/js/app/admin/create_order.js?_${.now?string("HHmmss")}"></script>
</body>
</html>