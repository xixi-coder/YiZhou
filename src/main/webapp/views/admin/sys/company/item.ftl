<#include "../../base/_base.ftl">
<#macro title>
公司管理
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/sys/company">公司列表</a></li>
    <li class="active"><a>公司<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">公司管理</h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="company_form">
            <input type="hidden" name="company.pk_id" value="${(company.pk_id)!}" id="pk_id">
            <input type="hidden" name="company.id" value="${(company.id)!}" id="id">
            <input type="hidden" name="area" id="area_value">
            <table class="table table-bordered table-striped">
                <tr>
                    <td>
                        <label for="name" class="control-label">公司名称:</label>
                    </td>
                    <td><input type="text" name="company.name" value="${(company.name)!}" class="form-control n-invalid"
                               id="name"
                               placeholder="公司名称"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td>
                        <label for="name" class="control-label">联系电话:</label>
                    </td>
                    <td><input type="text" name="company.phone" value="${(company.phone)!}"
                               class="form-control n-invalid"
                               id="phone"
                               placeholder="联系电话"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="name" class="control-label">详细地址:</label>
                    </td>
                    <td><textarea id="address" style="height:100px;" class="form-control address"
                                  readonly="readonly" name="company.address">${(company.address)!}</textarea>
                        <input type="hidden" value="${(company.latitude?c)!}" id="latitude" name="company.latitude"/>
                        <input type="hidden" value="${(company.longitude?c)!}" id="longitude" name="company.longitude"/>
                        <button type="button" class="btn btn-lg btn-link address">打开地图</button>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td>
                        <label for="name" class="control-label">公司介绍:</label>
                    </td>
                    <td><textarea style="height:100px;" class="form-control"
                                  name="company.description">${(company.description)!}</textarea></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <#if areas??&&areas?size gt 0>
                    <#list areas as item>
                        <tr class="area">
                            <td>
                                <label for="name" class="control-label">所属地区:</label>
                            </td>
                            <td colspan="4">
                                <select style="width: 25%;" class="select2 province">
                                    <option value="0">请选择</option>
                                    <#if province??&&province?size gt 0>
                                        <#list province as citem>
                                            <option value="${(citem.adcode)!}"
                                                    <#if item.province??&&item.province==citem.adcode>selected="selected"</#if>>${(citem.name)!}</option>
                                        </#list>
                                    </#if>
                                </select>
                                <select style="width: 25%;" class="select2 city">
                                    <option value="0">请选择</option>
                                    <#if city??&&city?size gt 0>
                                        <#list city as citem>
                                            <option value="${(citem.adcode)!}"
                                                    <#if item.city??&&item.city==citem.adcode>selected="selected"</#if>>${(citem.name)!}</option>
                                        </#list>
                                    </#if>
                                </select>
                                <select style="width: 25%;" class="select2 county">
                                    <option value="0">请选择</option>
                                    <#if county??&&county?size gt 0>
                                        <#list county as citem>
                                            <option value="${(item.adcode)!}"
                                                    <#if item.county??&&item.county==citem.adcode>selected="selected"</#if>>${(citem.name)!}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </td>
                            <td>
                                <button type="button" class="btn btn-sm btn-info add-area">添加地区</button>
                                <#if item_index!=0>
                                    <button type="button" class="btn btn-sm btn-info dele-area">删除</button>
                                </#if>
                            </td>
                        </tr>
                    </#list>
                <#else>
                    <tr class="area">
                        <td>
                            <label for="name" class="control-label">所属地区:</label>
                        </td>
                        <td colspan="4">
                            <select style="width: 25%;" class="select2 province" name="province">
                                <option value="0">请选择</option>
                                <#if province??&&province?size gt 0>
                                    <#list province as citem>
                                        <option value="${(citem.adcode)!}">${(citem.name)!}</option>
                                    </#list>
                                </#if>
                            </select>
                            <select style="width: 25%;" class="select2 city" name="city">
                                <option value="0">请选择</option>
                                <#if city??&&city?size gt 0>
                                    <#list city as citem>
                                        <option value="${(citem.adcode)!}">${(citem.name)!}</option>
                                    </#list>
                                </#if>
                            </select>
                            <select style="width: 25%;" class="select2 county" name="county">
                                <option value="0">请选择</option>
                                <#if county??&&county?size gt 0>
                                    <#list county as item>
                                        <option value="${(item.adcode)!}">${(item.name)!}</option>
                                    </#list>
                                </#if>
                            </select>
                        </td>
                        <td>
                            <button type="button" class="btn btn-sm btn-info add-area">添加地区</button>
                        </td>
                    </tr>
                </#if>
                <tr>
                    <td>
                        <label for="name" class="control-label">网址:</label>
                    </td>
                    <td>
                        <input type="text" name="company.website" value="${(company.website)!}"
                               class="form-control n-invalid"
                               id="website"
                               placeholder="如:http://www.baidu.com">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td>
                        <label for="name" class="control-label">公司QQ:</label>
                    </td>
                    <td><input type="text" name="company.qq" value="${(company.qq)!}" class="form-control n-invalid"
                               id="qq"
                               placeholder="如:1000001">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="name" class="control-label">公司图片:</label>
                    </td>
                    <td colspan="4">
                        <input type="hidden" name="company.picture" value="${(company.picture)!}" id="picture"/>
                        <img id="picture_show" style="width: 200px; height: 100px;"
                             src="<#if company.picture??>${(company.picture)!}<#else>${ctx}/static/images/default-user.jpg</#if>"/>
                        <div id="uploader">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker">选择文件</div>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*(600*400)</div>
                    </td>

                </tr>
            <#-- -->
                <tr>
                    <td>
                        <label for="name" class="control-label">服务类型:</label>
                    </td>
                    <td colspan="4">

                      <#if serviceTypesAll??&&serviceTypesAll?size gt 0>
                          <#list serviceTypesAll as type>
                              <span onclick="update_service_type(${(type.id)!},${(company.id)!})"><input class="serviceType"
                                                                                        id="serviceType_${(type.id)!}"
                                                                                        name="serviceType"
                                                                                        type="checkbox"
                              />${(type.name)!}</span>
                          </#list>
                      </#if>
                        </br>
                           <#if serviceTypes??&&serviceTypes?size gt 0>
                               <#list serviceTypes as type>
                              <script>
                                  document.getElementById("serviceType_${(type.id)!}").setAttribute("checked", "checked");
                              </script>
                               </#list>
                           </#if>

                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*不勾选就禁用</div>
                    </td>
                </tr>
            <#---->
                <tr>
                    <td colspan="6" style="text-align: center;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                        <button class="btn btn-success" type="submit">提交</button>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript"
        src="http://webapi.amap.com/maps?v=1.3&key=3847e08227b5479aa1596a7a8b944026&plugin=AMap.Autocomplete,AMap.PlaceSearch,AMap.Geocoder,AMap.CitySearch"></script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/company_item.js?_${.now?string("hhmmss")}"></script>
<script id="address_map" type="text/html">
    <div class="input-group">
        <input type="text" id="search_input" class="form-control"/>
        <span class="input-group-btn">
										<button class="btn btn-info" id="search_btn" type="button">搜索</button>
									  </span>
    </div>
    <div id="container" style="width:100%;height:80%; margin-top: 20px;"></div>

</script>
<script id="area_tmp" type="text/html">
    <tr class="area">
        <td>
            <label for="name" class="control-label">所属地区:</label>
        </td>
        <td colspan="4">
            <select style="width: 25%;" class="select2 province" name="province">
                <option value="0">请选择</option>
                <#if province??&&province?size gt 0>
                    <#list province as citem>
                        <option value="${(citem.adcode)!}">${(citem.name)!}</option>
                    </#list>
                </#if>
            </select>
            <select style="width: 25%;" class="select2 city" name="city">
                <option value="0">请选择</option>
                <#if city??&&city?size gt 0>
                    <#list city as citem>
                        <option value="${(citem.adcode)!}">${(citem.name)!}</option>
                    </#list>
                </#if>
            </select>
            <select style="width: 25%;" class="select2 county" name="county">
                <option value="0">请选择</option>
                <#if county??&&county?size gt 0>
                    <#list county as item>
                        <option value="${(item.adcode)!}">${(item.name)!}</option>
                    </#list>
                </#if>
            </select>
        </td>
        <td>
            <button type="button" class="btn btn-sm btn-info dele-area">删除</button>
        </td>
    </tr>
</script>
</#macro>