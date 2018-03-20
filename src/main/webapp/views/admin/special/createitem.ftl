<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>创建路线</title>
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
                      id="createLine_form">
                    <input type="hidden" value="" name="special.distance"
                           id="distance">
                    <input type="hidden" value="" name="special.time"
                           id="time">
                    <#--<input type="hidden" value="" name="special.startcity"-->
                           <#--id="startcity">-->
                    <#--<input type="hidden" value="" name="special.endcity"-->
                           <#--id="endcity">-->
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
                            <td><label for="real_name" class="control-label">专线类型选择:</label></td>
                            <td>
                                <select class="select2" name="serviceTypeItems" id="serviceTypeItems" style="width: 100%;">
                                    <#list serviceTypeItems as item>
                                        <option value="${(item.id)!}">${(item.name)!}</option>
                                    </#list>
                                </select>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>

                        <tr>
                            <td><label for="reservation_address" class="control-label">专线具体起点位置:</label></td>
                            <td>
                                <div class="input-group">
                                    <input type="hidden" id="reservation_lat" name="special.start_latitude"/>
                                    <input type="hidden" id="reservation_lng" name="special.start_longitude"/>
                                    <input type="text" name="special.reservation_address" value=""
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
                        <tr>
                            <td><label for="destination" class="control-label">专线具体终点位置:</label></td>
                            <td>
                                <div class="input-group">
                                    <input type="hidden" id="destination_lat" name="special.end_latitude"/>
                                    <input type="hidden" id="destination_lng" name="special.end_longitude"/>
                                    <input type="text" name="special.destination" value=""
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



                                    <input type="hidden" name="special.startcity"
                                          
                                           id="startcity" value="" >


                                    <input type="hidden" name="special.endcity"

                                           id="endcity" value="" >

                                    <input type="hidden" name="special.startadcode"

                                           id="startadcode" value="" >


                                    <input type="hidden" name="special.endadcode"

                                           id="endadcode" value="" >


<#--                        <tr>
                            <td><label for="setoutime" class="control-label">最早出发时间:</label></td>
                            <td>
                                <input type="text" name="setouttime1" readonly="readonly"
                                       class="datetimepicker-input form-control n-invalid"
                                       id="setoutime1" value="${.now?string("yyyy-MM-dd HH:mm")}"></td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                        <tr>
                            <td><label for="setoutime" class="control-label">最迟出发时间:</label></td>
                            <td>
                                <input type="text" name="setouttime2" readonly="readonly"
                                       class="datetimepicker-input form-control n-invalid"
                                       id="setoutime2" value="${.now?string("yyyy-MM-dd HH:mm")}"></td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>-->

                        <tr>
                            <td><label for="real_name" class="control-label">拼车价格:</label></td>
                            <td>
                                <div class="input-group">
                                    <input type="text" name="special.sharing_price" value=""
                                           class="form-control n-invalid"
                                           id="sharing_price">
                                    <div class="input-group-addon">每人/元</div>
                                </div>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                        <tr>
                            <td><label for="real_name" class="control-label">不拼车价格:</label></td>
                            <td>
                                <div class="input-group">
                                    <input type="text" name="special.index_price" value=""
                                           class="form-control n-invalid"
                                           id="index_price">
                                    <div class="input-group-addon">元</div>
                                </div>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>

                        <tr>
                            <td><label>特殊时间段设置</label></td>
                            <td>
                                <select class="item_time" data-value="" name="special.setouttime1">
                                <#list 0..23 as item>
                                    <option value="${item?string("00")}">${item?string("00")}</option>
                                </#list>
                                </select>:
                                <select class="item_time" data-value="" name="special.setouttime3">
                                <#list 0..59 as item>
                                    <option value="${item?string("00")}">${item?string("00")}</option>
                                </#list>
                                </select>
                                <label>至</label>
                                <select class="item_time" data-value="" name="special.setouttime2">
                                <#list 0..23 as item>
                                    <option value="${item?string("00")}">${item?string("00")}</option>
                                </#list>
                                </select>:
                                <select class="item_time" data-value="" name="special.setouttime4">
                                <#list 0..59 as item>
                                    <option value="${item?string("00")}">${item?string("00")}</option>
                                </#list>
                                </select>
                            </td>
                        </tr>

                        <tr>
                            <td><label for="real_name" class="control-label">特殊时间段拼车价格:</label></td>
                            <td>
                                <div class="input-group">
                                    <input type="text" name="special.sharing_esyimated_price" value=""
                                           class="form-control n-invalid"
                                           id="sharing_price" placeholder="如不需按时间收费，段设置则价格设置与普通拼车相同">
                                    <div class="input-group-addon">每人/元</div>
                                </div>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                        <tr>
                            <td><label for="real_name" class="control-label">特殊时间段不拼车价格:</label></td>
                            <td>
                                <div class="input-group">
                                    <input type="text" name="special.estimated_price" value=""
                                           class="form-control n-invalid"
                                           id="index_price" placeholder="如不需按时间段收费，则价格设置与普通不拼车相同">
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
                                <textarea style="width: 99%;" rows="10" name="special.tag"></textarea>
                            </td>
                            <td>
                                <div style="padding-top: 10px;color: red;">*</div>
                            </td>
                        </tr>
                        <tr id="show_mill" style="display: none;">
                        </tr>
                        <tr>
                            <td colspan="3">
                                <button type="button" id="createLine" class="btn btn-primary btn-lg btn-block">创建路线
                                </button>
                            </td>
                        </tr>
                    </table>
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
<script src="${ctx}/static/js/app/admin/create_line.js?_${.now?string("HHmmss")}"></script>
</body>
</html>