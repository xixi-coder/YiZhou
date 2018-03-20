<#include "../../base/_base.ftl">
<#macro title>
车辆信息
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/member/driver">司机信息</a></li>
    <li class="active"><a>信息<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-smstmp">
    <div class="panel-heading">
        <h4 class="panel-title">车辆信息设置 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="carinfo_form">
            <input type="hidden" name="carInfo.car_id" value="${(carInfo.car_id)!}">
            <input type="hidden" name="car.id" value="${(car.id)!}">
            <input type="hidden" name="action" value="${action}">
            <input type="hidden" name="id" value="${id!0}">
            <table class="table table-bordered table-striped">
            <#--第一个tr-->
                <tr>
                <#--汽车品牌-->
                    <td><label for="name" class="control-label">汽车品牌:</label></td>
                    <td>
                        <select id="brand" class="select2" name="car.brand" style="width: 100%;">
                            <option value="0">请选择</option>
                            <#if carBrands??&&carBrands?size gt 0>
                                <#list carBrands as item>
                                    <option value="${(item.id)!}"
                                            <#if car.brand??&&car.brand==item.id>selected="selected"</#if>>${(item.name)!}</option>
                                </#list>
                            </#if>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--汽车型号-->
                    <td><label for="name" class="control-label">汽车型号:</label></td>
                    <td>
                        <select id="model" class="select2" name="car.model_type" style="width: 100%;">
                            <option value="0">请选择</option>
                            <#if models??&&models?size gt 0>
                                <#list models as item>
                                    <option value="${(item.id)!}"
                                            <#if car.model_type??&&car.model_type==item.id>selected="selected"</#if>>${(item.name)!}</option>
                                </#list>
                            </#if>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第二个tr-->
                <tr>
                <#--车牌号-->
                    <td><label for="plate_no" class="control-label">车牌号:</label></td>
                    <td><input type="text" name="car.plate_no" value="${(car.plate_no)!}" class="form-control n-invalid"
                               placeholder="如:皖A88888">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--注册车辆类型-->
                    <td><label for="plate_no" class="control-label">注册车辆类型:</label></td>
                    <td>
                        <select name="car.type" class="select2" style="width: 100%;">
                            <#list serviceTypes as item>
                            <#--专车-->
                                <#if "${item}"=="1">
                                    <option value="37" <#if car.type??&&car.type==37>selected="selected"</#if>>专车-豪华型
                                    </option>
                                    <option value="43" <#if car.type??&&car.type==43>selected="selected"</#if>>专车-舒适型
                                    </option>
                                    <option value="45" <#if car.type??&&car.type==45>selected="selected"</#if>>专车-商务型
                                    </option>
                                </#if>
                            <#--出租车-->
                                <#if "${item}"=="3">
                                    <option value="42" <#if car.type??&&car.type==42>selected="selected"</#if>>出租车
                                    </option>
                                </#if>
                            <#--快车-->
                                <#if "${item}"=="4">
                                    <option value="44" <#if car.type??&&car.type==44>selected="selected"</#if>>快车
                                    </option>
                                </#if>
                            <#--顺风车-->
                                <#if "${item}"=="5">
                                    <option value="55" <#if car.type??&&car.type==55>selected="selected"</#if>>顺风车
                                    </option>
                                </#if>
                            <#--城际专线-->
                                <#if "${item}"=="6">
                                    <option value="59" <#if car.type??&&car.type==59>selected="selected"</#if>>城际专线
                                    </option>
                                </#if>
                            <#--航空专线-->
                                <#if "${item}"=="7">
                                    <option value="58" <#if car.type??&&car.type==58>selected="selected"</#if>>航空专线
                                    </option>
                                </#if>
                            </#list>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第三个tr-->
                <tr>
                <#--识别码-->
                    <td><label for="vin" class="control-label">识别码vin:</label></td>
                    <td><input type="text" name="car.vin" value="${(car.vin)!}"
                               class="form-control n-invalid"
                               placeholder="以机动车行驶证为准">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--行驶里程-->
                    <td><label for="postcode" class="control-label">行驶里程:</label></td>
                    <td><input type="text" name="carInfo.distance"
                               class="form-control n-invalid" value="${(carInfo.distance)!}"
                               placeholder="如:10000">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第四个tr-->
                <tr>
                <#--所属地区-->
                    <td>
                        <label for="name" class="control-label">所属地区:</label>
                    </td>
                    <td colspan="4">
                        <select id="province" style="width: 25%;" class="select2" name="carInfo.province">
                            <option value="0">请选择</option>
                            <#if province??&&province?size gt 0>
                                <#list province as item>
                                    <option value="${(item.adcode)!}"
                                            <#if carInfo.province??&&carInfo.province==item.adcode>selected="selected"</#if>>${(item.name)!}</option>
                                </#list>
                            </#if>
                        </select>
                        <select id="city" style="width: 25%;" class="select2" name="carInfo.city">
                            <option value="0">请选择</option>
                            <#if city??&&city?size gt 0>
                                <#list city as item>
                                    <option value="${(item.adcode)!}"
                                            <#if carInfo.city??&&carInfo.city==item.adcode>selected="selected"</#if>>${(item.name)!}</option>
                                </#list>
                            </#if>
                        </select>
                        <select id="county" style="width: 25%;" class="select2" name="carInfo.county">
                            <option value="0">请选择</option>
                            <#if county??&&county?size gt 0>
                                <#list county as item>
                                    <option value="${(item.adcode)!}"
                                            <#if carInfo.county??&&carInfo.county==item.adcode>selected="selected"</#if>>${(item.name)!}</option>
                                </#list>
                            </#if>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第五个tr-->
                <tr>
                <#--车辆颜色-->
                    <td><label for="postcode" class="control-label">车辆颜色:</label></td>
                    <td><input type="text" name="car.color"
                               class="form-control n-invalid" value="${(car.color)!}"
                               placeholder="如:黑色">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                <#--车牌颜色-->
                    <td><label for="plate_color" class="control-label">车牌颜色:</label></td>
                    <td>
                        <select name="car.plate_color" style="height: 26px;" value="${car.plate_color!}">
                            <option value="">请选择</option>
                            <option value="1" <#if "${car.plate_color!}"=="1">selected</#if>>蓝色</option>
                            <option value="2" <#if "${car.plate_color!}"=="2">selected</#if>>黄色</option>
                            <option value="3" <#if "${car.plate_color!}"=="3">selected</#if>>黑色</option>
                            <option value="4" <#if "${car.plate_color!}"=="4">selected</#if>>白色</option>
                            <option value="5" <#if "${car.plate_color!}"=="5">selected</#if>>绿色</option>
                            <option value="6" <#if "${car.plate_color!}"=="6">selected</#if>>其他</option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第六个tr-->
                <tr>
                <#--核定载客人数-->
                    <td><label for="Seats" class="control-label">核定载客人数:</label></td>
                    <td><input type="text" name="car.Seats"
                               class="form-control n-invalid" value="${(car.Seats)!}"
                               placeholder="如:4">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--机动车车辆类型-->
                    <td><label for="VehicleType" class="control-label">机动车车辆类型:</label></td>
                    <td><input type="text" name="car.VehicleType"
                               class="form-control n-invalid" value="${(car.VehicleType)!}"
                               placeholder="以机动车行驶为准">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第七个tr-->
                <tr>
                <#--车辆所有人-->
                    <td><label for="OwnerName" class="control-label">车辆所有人:</label></td>
                    <td><input type="text" name="car.OwnerName"
                               class="form-control n-invalid" value="${(car.OwnerName)!}"
                               placeholder="应与《机动车登记证书》注明的车所有人一致">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--发动机号-->
                    <td><label for="EngineId" class="control-label">发动机号:</label></td>
                    <td><input type="text" name="car.EngineId"
                               class="form-control n-invalid" value="${(car.EngineId)!}"
                               placeholder="以机动车行驶为准">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第八个tr-->
                <tr>
                <#--车辆注册日期-->
                    <td><label for="CertifyDateA" class="control-label">车辆注册日期:</label></td>
                    <td><input type="text" name="car.CertifyDateA" data-date-format="yyyy-mm-dd" readonly
                               class="form-control datepicker-input" id="CertifyDateA" value="${(car.CertifyDateA)!}"
                               placeholder="应与机动车驾驶证一致">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--车辆燃油类型-->
                    <td><label for="FuelType" class="control-label">车辆燃油类型:</label></td>


                    <td>
                        <select name="car.FuelType" style="height: 26px;" value="${car.FuelType!}">
                            <option value="">请选择</option>
                            <option value="A" <#if "${car.FuelType!}"=="A">selected</#if>>汽油</option>
                            <option value="B" <#if "${car.FuelType!}"=="B">selected</#if>>柴油</option>
                            <option value="C" <#if "${car.FuelType!}"=="C">selected</#if>>电</option>
                            <option value="D" <#if "${car.FuelType!}"=="D">selected</#if>>混合油</option>
                            <option value="E" <#if "${car.FuelType!}"=="E">selected</#if>>天然气</option>
                            <option value="F" <#if "${car.FuelType!}"=="F">selected</#if>>液化石油气</option>
                            <option value="L" <#if "${car.FuelType!}"=="L">selected</#if>>甲醇</option>
                            <option value="M" <#if "${car.FuelType!}"=="M">selected</#if>>乙醇</option>
                            <option value="N" <#if "${car.FuelType!}"=="N">selected</#if>>太阳能</option>
                            <option value="O" <#if "${car.FuelType!}"=="O">selected</#if>>混合动力</option>
                            <option value="Y" <#if "${car.FuelType!}"=="Y">selected</#if>>无</option>
                            <option value="Z" <#if "${car.FuelType!}"=="Z">selected</#if>>其他</option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第九个tr-->
                <tr>
                <#--发动机排量-->
                    <td><label for="EngineDisplace" class="control-label">发动机排量:</label></td>
                    <td><input type="text" name="car.EngineDisplace"
                               class="form-control n-invalid" value="${(car.EngineDisplace)!}"
                               placeholder="毫升">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--运输证字号-->
                    <td><label for="Certificate" class="control-label">运输证字号:</label></td>
                    <td><input type="text" name="car.Certificate"
                               class="form-control n-invalid" value="${(car.Certificate)!}"
                               placeholder="">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第十个tr-->
                <tr>
                <#--车辆运输证发证机构-->
                    <td><label for="TransAgency" class="control-label">车辆运输证发证机构:</label></td>
                    <td><input type="text" name="car.TransAgency"
                               class="form-control n-invalid" value="${(car.TransAgency)!}"
                               placeholder="全称">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--车辆经营区域-->
                    <td><label for="TransArea" class="control-label">车辆经营区域:</label></td>
                    <td><input type="text" name="car.TransArea"
                               class="form-control n-invalid" value="${(car.TransArea)!}"
                               placeholder="">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第十一个tr-->
                <tr>
                <#--车辆运输证有效期起-->
                    <td><label for="TransDateStart" class="control-label">车辆运输证有效期起:</label></td>
                    <td><input type="text" name="car.TransDateStart" data-date-format="yyyy-mm-dd" readonly
                               class="form-control datepicker-input" id="TransDateStart"
                               value="${(car.TransDateStart)!}"
                               placeholder="">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--车辆运输证有效期止-->
                    <td><label for="TransDateStop" class="control-label">车辆运输证有效期止:</label></td>
                    <td><input type="text" name="car.TransDateStop" data-date-format="yyyy-mm-dd" readonly
                               class="form-control datepicker-input" id="TransDateStop" value="${(car.TransDateStop)!}"
                               placeholder="YYYYMMDD">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第十二个tr-->
                <tr>
                <#--车辆初次登记日期-->
                    <td><label for="CertifyDateB" class="control-label">车辆初次登记日期:</label></td>
                    <td><input type="text" name="car.CertifyDateB" data-date-format="yyyy-mm-dd" readonly
                               class="form-control datepicker-input" id="CertifyDateB" value="${(car.CertifyDateB)!}"
                               placeholder="YYYYMMDD">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--车辆检修状态-->
                    <td><label for="FixState" class="control-label">车辆检修状态:</label></td>
                    <td>
                        <select name="car.FixState" style="height: 26px;" value="${car.FixState!}">
                            <option value="">请选择</option>
                            <option value="0" <#if "${car.CommercialType!}"=="1">selected</#if>>未检修</option>
                            <option value="1" <#if "${car.CommercialType!}"=="2">selected</#if>>已检修</option>
                            <option value="2" <#if "${car.CommercialType!}"=="3">selected</#if>>未知</option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第十三个tr-->
                <tr>
                <#--车辆下次年检时间-->
                    <td><label for="NextFixDate" class="control-label">车辆下次年检时间:</label></td>
                    <td><input type="text" name="car.NextFixDate"
                               class="form-control n-invalid" value="${(car.NextFixDate)!}"
                               placeholder="YYYYMMDD">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--车辆年度审验状态-->
                    <td><label for="CheckState" class="control-label">车辆年度审验状态:</label></td>
                    <td>
                        <select name="car.CheckState" style="height: 26px;" value="${car.CheckState!}">
                            <option value="">请选择</option>
                            <option value="0" <#if "${car.CommercialType!}"=="0">selected</#if>>未年审</option>
                            <option value="1" <#if "${car.CommercialType!}"=="1">selected</#if>>年审合格</option>
                            <option value="2" <#if "${car.CommercialType!}"=="2">selected</#if>>不合格</option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第十五个tr-->
                <tr>
                <#--发票打印设备序列号-->
                    <td><label for="FeePrintId" class="control-label">发票打印设备序列号:</label></td>
                    <td><input type="text" name="car.FeePrintId"
                               class="form-control n-invalid" value="${(car.FeePrintId)!}"
                               placeholder="">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--卫星定位装置品牌-->
                    <td><label for="GPSBrand" class="control-label">卫星定位装置品牌:</label></td>
                    <td><input type="text" name="car.GPSBrand"
                               class="form-control n-invalid" value="${(car.GPSBrand)!}"
                               placeholder="">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第十六个tr-->
                <tr>
                <#--卫星定位装置型号-->
                    <td><label for="GPSModel" class="control-label">卫星定位装置型号:</label></td>
                    <td><input type="text" name="car.GPSModel"
                               class="form-control n-invalid" value="${(car.GPSModel)!}"
                               placeholder="">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--卫星定位装置IMEI号-->
                    <td><label for="GPSIMEI" class="control-label">卫星定位装置IMEI号:</label></td>
                    <td><input type="text" name="car.GPSIMEI"
                               class="form-control n-invalid" value="${(car.GPSIMEI)!}"
                               placeholder="">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第十七个tr-->
                <tr>
                <#--卫星定位设备安装日期-->
                    <td><label for="GPSinstallDate" class="control-label">卫星定位设备安装日期:</label></td>
                    <td><input type="text" name="car.GPSinstallDate" data-date-format="yyyy-mm-dd" readonly
                               class="form-control datepicker-input" id="GPSinstallDate"
                               value="${(car.GPSinstallDate)!}"
                               placeholder="">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--报备日期-->
                    <td><label for="RegisterDate" class="control-label">报备日期:</label></td>
                    <td><input type="text" name="car.RegisterDate" data-date-format="yyyy-mm-dd" readonly
                               class="form-control datepicker-input" id="RegisterDate" value="${(car.RegisterDate)!}"
                               placeholder="">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第十八个tr-->
                <tr>
                <#--服务类型-->
                    <td><label for="CommercialType" class="control-label">服务类型:</label></td>
                    <td>
                        <select name="car.CommercialType" style="height: 26px;" value="${car.CommercialType!}">
                            <option value="">请选择</option>
                            <option value="1" <#if "${car.CommercialType!}"=="1">selected</#if>>网络预约出租汽车</option>
                            <option value="2" <#if "${car.CommercialType!}"=="2">selected</#if>>巡游出租车汽车</option>
                            <option value="3" <#if "${car.CommercialType!}"=="3">selected</#if>>私人小客车合称</option>
                        </select>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                <#--运价类型编码-->
                    <td><label for="FareType" class="control-label">运价类型编码:</label></td>
                    <td><input type="text" name="car.FareType"
                               class="form-control n-invalid" value="${(car.FareType)!}"
                               placeholder="">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            <#--第十九个tr-->
                <tr>
                    <td><label for="head_portrait">车辆图片:</label></td>
                    <td>
                        <img id="portrait_show" style="height: 200px;width: 200px;"
                             src="<#if car.picture??>${(car.picture)!}<#else>${ctx}/static/images/default-user.jpg</#if>"/></span>
                        <div id="uploader">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker">选择文件</div>
                            </div>
                        </div>
                        <input type="hidden" name="car.picture" class="form-control n-invalid"
                               id="picture" value="${(car.picture)!}">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="Certificate">运输证:</label></td>
                    <td>
                        <img id="Certificate" style="height: 200px;width: 200px;"
                             src="<#if car.Certificate??>${(car.Certificate)!}<#else>${ctx}/static/images/default-user.jpg</#if>"/></span>
                        <div id="uploader">
                            <!--用来存放文件信息-->
                            <div id="thelist2" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker2">选择文件</div>
                            </div>
                        </div>
                        <input type="hidden" name="car.Certificate" class="form-control n-invalid"
                               id="Certificate" value="${(car.Certificate)!}">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
            </table>

            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">返回</a>
                    </div>
                    <div class="col-sm-2" style="float: right;">
                        <button class="btn btn-success" type="submit">提交</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script>
    var id = ${id};
</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/carinfo.js?_${.now?string("hhmmss")}"></script>
</#macro>