<#include "../../base/_base.ftl">
<#macro title>
司机信息
</#macro>
<#macro content>
<ol class="breadcrumb" style="padding-left: 5px; background: rgba(0,0,0,0.05)">
    <li><a href="${ctx}/admin/member/driver">司机列表</a></li>
    <li class="active"><a>信息<#if action==1>编辑<#else>新增</#if></a></li>
</ol>
<div class="panel panel-default" id="menu" data-menu="sys-smstmp">
    <div class="panel-heading">
        <h4 class="panel-title">司机信息设置 </h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="smstmp_form">
            <input type="hidden" name="memberLogin.id" value="${(memberLogin.id)!}">
            <input type="hidden" name="driverInfo.id" value="${(driverInfo.id)!}">
            <input type="hidden" name="action" value="${action}">
            <table class="table table-bordered table-striped">
                <tr>
                    <td><label for="real_name" class="control-label">姓名:</label></td>
                    <td><input type="text" name="driverInfo.real_name" value="${(driverInfo.real_name)!}"
                               class="form-control n-invalid"
                               id="real_name"
                               placeholder="如：张三"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="address" class="control-label">家庭地址:</label></td>
                    <td><input type="text" name="driverInfo.address" value="${(driverInfo.address)!}"
                               class="form-control n-invalid"
                               id="address"
                               placeholder="如：安徽省合肥市蜀山区.."></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>


                </tr>

                <tr>
                    <td><label for="email" class="control-label">电子邮箱:</label></td>
                    <td><input type="text" name="driverInfo.email" value="${(driverInfo.email)!}"
                               class="form-control n-invalid"
                               id="email"
                               placeholder="如：888@163.com"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="user_name" class="control-label">用户名:</label></td>
                    <td><input type="text" name="memberLogin.user_name" value="${(memberLogin.user_name)!}"
                               class="form-control n-invalid"
                               id="username"
                               placeholder="如:18000000000"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="email" class="control-label">昵称:</label></td>
                    <td><input type="text" name="driverInfo.nick_name" value="${(driverInfo.nick_name)!}"
                               class="form-control n-invalid"
                               id="email"
                               placeholder="如：小猪"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>


                    <td><label for="job" class="control-label">工作类型:</label></td>
                    <td>
                        <div class="col-sm-7">
                            <select name="driverInfo.job_type" class="select2" style="width: 100%;" id="job">
                                <option value="0">请选择</option>
                                <option value="1" <#if driverInfo.job_type??&&driverInfo.job_type==1>selected</#if>>兼职
                                </option>
                                <option value="2" <#if driverInfo.job_type??&&driverInfo.job_type==2>selected</#if>>全职
                                </option>
                            </select>
                        </div>
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="address" class="control-label">电话:</label></td>
                    <td><input type="text" name="driverInfo.phone" value="${(driverInfo.phone)!}"
                               class="form-control n-invalid"
                               id="phone"
                               placeholder="如:18000000000"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="head_portrait" class="control-label">头像:</label></td>
                    <td><span style="height: 40px;width: 40px;" class="rounded-image topbar-profile-image">
                            <img id="portrait_show" style="height: 40px;"
                                 src="<#if driverInfo.head_portrait??>${(driverInfo.head_portrait)!}<#else>${ctx}/static/images/default-user.jpg</#if>"/></span>
                        <div id="uploader">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker">选择文件</div>
                            </div>
                        </div>
                        <input type="hidden" name="driverInfo.head_portrait" class="form-control n-invalid"
                               id="head_portrait" value="${(driverInfo.head_portrait)!}">
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>
                </tr>

                <tr>
                    <td><label for="postcode" class="control-label">生日:</label></td>
                    <td><input type="text" name="driverInfo.post_code" readonly data-date-format="yyyy-mm-dd"
                               class="form-control datepicker-input" id="postcode" value="${(driverInfo.post_code)!}"
                               placeholder="如:1999-9-19"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>

                    <td><label for="gender" class="control-label">性别:</label></td>
                    <td><input type="radio" <#if (driverInfo.gender??)&&(driverInfo.gender)==1>checked="checked"</#if>
                               name="driverInfo.gender" value="1" class="form-control n-invalid" id="man">&nbsp;男
                        <input type="radio" <#if (driverInfo.gender??)&&(driverInfo.gender)==0>checked="checked"</#if>
                               name="driverInfo.gender" value="0" class="form-control n-invalid" id="waman">&nbsp;女
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>
                </tr>


                <tr>
                    <td><label for="job_type" class="control-label">司机类型:</label></td>
                    <td>
                        <#list serviceTypes as item>
                            <label><input type="checkbox" value="${item.id}" name="c1"
                                <#list types as i ><#if i=="${item.id}">checked</#if></#list>>${item.name}</label>
                        </#list>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <#if hasRole("super_admin")||hasPermission("select-company")>
                        <td><label for="company" class="control-label">所属公司:</label></td>
                        <td>
                            <div class="col-sm-7">
                                <select id="company" class="select2 select-company" name="driverInfo.company"
                                        style="width: 100%;"
                                        data-value="${(driverInfo.company)!}">
                                    <option value="0">请选择</option>
                                </select>
                            </div>
                        </td>
                        <td>
                            <div style="padding-top: 10px;color: red;">*</div>
                        </td>
                    <#else>
                        <td colspan="3"></td>
                    </#if>
                </tr>
                <tr>
                    <td><label for="company" class="control-label">提成设置</label></td>
                    <td colspan="5">
                        <div class="row">
                            <#list serviceTypes as item>
                            <#--专车-->
                                <#if "${item.id}"=="1">
                                    <div id="zc_sushi_re" class="col-sm-4"><label for="company"
                                                                                  class="control-label">专车舒适型提成:</label>
                                        <select class="select2" name="driverInfo.zc_sushi_re"
                                                data-value="${(driverInfo.zc_sushi_re)!}" style="width: 150px;">
                                            <option value="0">请选择</option>
                                            <#list roya as item>
                                                <option value="${(item.id)!}">${(item.name)!}</option>
                                            </#list>
                                        </select>
                                    </div>
                                    <div id="zc_haohua_re" class="col-sm-4"><label for="company"
                                                                                   class="control-label">专车豪华型提成:</label>
                                        <select class="select2" name="driverInfo.zc_haohua_re"
                                                data-value="${(driverInfo.zc_haohua_re)!}"
                                                style="width: 150px;">
                                            <option value="0">请选择</option>
                                            <#list roya as item>
                                                <option value="${(item.id)!}">${(item.name)!}</option>
                                            </#list>
                                        </select>
                                    </div>
                                    <div id="zc_shangwu_re" class="col-sm-4">
                                        <label for="company"
                                               class="control-label">专车商务型提成:</label>
                                        <select class="select2" name="driverInfo.zc_shangwu_re"
                                                data-value="${(driverInfo.zc_shangwu_re)!}"
                                                style="width: 150px;">
                                            <option value="0">请选择</option>
                                            <#list roya as item>
                                                <option value="${(item.id)!}">${(item.name)!}</option>
                                            </#list>
                                        </select>
                                    </div>
                                </#if>
                            <#--代驾-->
                                <#if "${item.id}"=="2">
                                    <div id="dj_re" class="col-sm-4"><label for="company" class="control-label">代驾提成:</label>
                                        <select class="select2" name="driverInfo.dj_re" data-value="${(driverInfo.dj_re)!}"
                                                style="width: 150px;">
                                            <option value="0">请选择</option>
                                            <#list roya as item>
                                                <option value="${(item.id)!}">${(item.name)!}</option>
                                            </#list>
                                        </select>
                                    </div>
                                </#if>
                            <#--出租车-->
                                <#if "${item.id}"=="3">
                                    <div id="texi_re" class="col-sm-4"><label for="company" class="control-label">出租车提成:</label>
                                        <select class="select2" name="driverInfo.texi_re" data-value="${(driverInfo.texi_re)!}"
                                                style="width: 150px;">
                                            <option value="0">请选择</option>
                                            <#list roya as item>
                                                <option value="${(item.id)!}">${(item.name)!}</option>
                                            </#list>
                                        </select>
                                    </div>
                                </#if>
                            <#--快车-->
                                <#if "${item.id}"=="4">
                                    <div id="kc_re" class="col-sm-4"><label for="company"
                                                                            class="control-label">快车提成:</label>
                                        <select class="select2" name="driverInfo.kc_re"
                                                data-value="${(driverInfo.kc_re)!}"
                                                style="width: 150px;">
                                            <option value="0">请选择</option>
                                            <#list roya as item>
                                                <option value="${(item.id)!}">${(item.name)!}</option>
                                            </#list>
                                        </select>
                                    </div>
                                </#if>
                            <#--顺风车-->
                                <#if "${item.id}"=="5">
                                    <div id="shunfengche_re" class="col-sm-4"><label for="company"
                                                                                     class="control-label">顺风车提成:</label>
                                        <select class="select2" name="driverInfo.shunfengche_re"
                                                data-value="${(driverInfo.shunfengche_re)!}"
                                                style="width: 150px;">
                                            <option value="0">请选择</option>
                                            <#list roya as item>
                                                <option value="${(item.id)!}">${(item.name)!}</option>
                                            </#list>
                                        </select>
                                    </div>
                                </#if>
                            <#--城际专线-->
                                <#if "${item.id}"=="6">
                                    <div id="zhuanxian_re" class="col-sm-4"><label for="company"
                                                                                   class="control-label">城际专线提成:</label>
                                        <select class="select2" name="driverInfo.zhuanxian_re"
                                                data-value="${(driverInfo.zhuanxian_re)!}"
                                                style="width: 150px;">
                                            <option value="0">请选择</option>
                                            <#list roya as item>
                                                <option value="${(item.id)!}">${(item.name)!}</option>
                                            </#list>
                                        </select>
                                    </div>
                                </#if>
                            <#--航空专线-->
                                <#if "${item.id}"=="7">
                                    <div id="hkzhuanxian_re" class="col-sm-4"><label for="company"
                                                                                   class="control-label">航空专线提成:</label>
                                        <select class="select2" name="driverInfo.hkzhuanxian_re"
                                                data-value="${(driverInfo.hkzhuanxian_re)!}"
                                                style="width: 150px;">
                                            <option value="0">请选择</option>
                                            <#list roya as item>
                                                <option value="${(item.id)!}">${(item.name)!}</option>
                                            </#list>
                                        </select>
                                    </div>
                                </#if>
                            </#list>
                        </div>
                    </td>
                </tr>

                <tr>
                    <td><label for="DriverNationAlity" class="control-label">国籍:</label></td>
                    <td><input type="text" name="driverInfo.DriverNationAlity"
                               class="form-control" id="DriverNationAlity" value="${(driverInfo.DriverNationAlity)!}"
                               placeholder="如:中国"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                    <td><label for="DriverNation" class="control-label">驾驶员名族:</label></td>
                    <td><input type="text" name="driverInfo.DriverNation"
                               class="form-control" id="DriverNation" value="${(driverInfo.DriverNation)!}"
                               placeholder="如:汉族"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="DriverMaritalStatus" class="control-label">驾驶员婚姻状况:</label></td>
                    <td><input type="text" name="driverInfo.DriverMaritalStatus"
                               class="form-control" id="DriverMaritalStatus" value="${(driverInfo.DriverMaritalStatus)!}"
                               placeholder="未婚；已婚；离异"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="DriverLanguageLevel" class="control-label">驾驶员外语能力:</label></td>
                    <td><input type="text" name="driverInfo.DriverLanguageLevel"
                               class="form-control" id="DriverLanguageLevel" value="${(driverInfo.DriverLanguageLevel)!}"
                               placeholder="如：CET4"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>
                <tr>
                    <td><label for="postcode" class="control-label">身份证号:</label></td>
                    <td><input type="text" name="driverInfo.certificate_no"
                               class="form-control" id="certificate_no" value="${(driverInfo.certificate_no)!}"
                               placeholder=""></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="postcode" class="control-label">机动车驾驶证号:</label></td>
                    <td><input type="text" name="driverInfo.LicenseId"
                               class="form-control" id="LicenseId" value="${(driverInfo.LicenseId)!}"
                               placeholder="请仔细检查号码"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="DriverEducation" class="control-label">驾驶员学历:</label></td>
                    <td><input type="text" name="driverInfo.DriverEducation"
                               class="form-control" id="DriverEducation" value="${(driverInfo.DriverEducation)!}"
                               placeholder="填写代码"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="DriverCensus" class="control-label">户口登记机关名称:</label></td>
                    <td><input type="text" name="driverInfo.DriverCensus"
                               class="form-control" id="DriverCensus" value="${(driverInfo.DriverCensus)!}"
                               placeholder="户口登记机关名称"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>


                <tr>
                    <td><label for="DriverAddress" class="control-label">户口住址或长住地址:</label></td>
                    <td><input type="text" name="driverInfo.DriverAddress"
                               class="form-control" id="DriverAddress" value="${(driverInfo.DriverAddress)!}"
                               placeholder=""></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="DriverContactAddress" class="control-label">驾驶员通信地址:</label></td>
                    <td><input type="text" name="driverInfo.DriverContactAddress"
                               class="form-control" id="DriverContactAddress" value="${(driverInfo.DriverContactAddress)!}"
                               placeholder=""></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>


                <tr>
                    <td><label for="GetDriverLicenseDate" class="control-label">初次领取驾驶证日期:</label></td>
                    <td><input type="text" name="driverInfo.GetDriverLicenseDate" readonly data-date-format="yyyy-mm-dd"
                               class="form-control datepicker-input" id="GetDriverLicenseDate" value="${(driverInfo.GetDriverLicenseDate)!}"
                               placeholder="如:1999-9-19"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="DriverLicenseOn" class="control-label">驾驶证有效期限起:</label></td>
                    <td><input type="text" name="driverInfo.DriverLicenseOn"  readonly data-date-format="yyyy-mm-dd"
                               class="form-control datepicker-input" id="DriverLicenseOn" value="${(driverInfo.DriverLicenseOn)!}"
                               placeholder="如:1999-9-19"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                </tr>



                <tr>
                    <td><label for="DriverLicenseOff" class="control-label">驾驶证有效期限止:</label></td>
                    <td><input type="text" name="driverInfo.DriverLicenseOff" readonly data-date-format="yyyy-mm-dd"
                               class="form-control datepicker-input" id="DriverLicenseOff" value="${(driverInfo.DriverLicenseOff)!}"
                               placeholder="如:1999-9-19"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="TaxiDriver" class="control-label">是否巡游出租车汽车驾驶员:</label></td>
                    <td><input type="radio" <#if (driverInfo.TaxiDriver??)&&(driverInfo.TaxiDriver)==1>checked="checked"</#if>
                               name="driverInfo.TaxiDriver" value="1" class="form-control n-invalid" id="TaxiDriver">&nbsp;是
                        <input type="radio" <#if (driverInfo.TaxiDriver??)&&(driverInfo.TaxiDriver)==0>checked="checked"</#if>
                               name="driverInfo.TaxiDriver" value="0" class="form-control n-invalid" id="TaxiDriver1">&nbsp;否
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>
                </tr>
                <tr>
                    <td><label for="DriverType" class="control-label">准驾车型:</label></td>
                    <td><input type="text" name="driverInfo.DriverType"
                               class="form-control" id="DriverType" value="${(driverInfo.DriverType)!}"
                               placeholder="填写代码"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="DriverCensus" class="control-label">户口登记机关名称:</label></td>
                    <td><input type="text" name="driverInfo.DriverCensus"
                               class="form-control" id="DriverCensus" value="${(driverInfo.DriverCensus)!}"
                               placeholder="户口登记机关名称"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="CertificateNo2" class="control-label">网络预约出租汽车驾驶员资格证号:</label></td>
                    <td><input type="text" name="driverInfo.CertificateNo2"
                               class="form-control" id="CertificateNo2" value="${(driverInfo.CertificateNo2)!}"
                               placeholder=""></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="NetworkCarIssueOrganization" class="control-label">网络预约出租汽车驾驶员证发证机构:</label></td>
                    <td><input type="text" name="driverInfo.NetworkCarIssueOrganization"
                               class="form-control" id="NetworkCarIssueOrganization" value="${(driverInfo.NetworkCarIssueOrganization)!}"
                               placeholder="全称"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="GetNetworkCarProofDate" class="control-label">初次领取资格证日期:</label></td>
                    <td><input type="text" name="driverInfo.GetNetworkCarProofDate" readonly data-date-format="yyyy-mm-dd"
                               class="form-control datepicker-input" id="GetNetworkCarProofDate" value="${(driverInfo.GetNetworkCarProofDate)!}"
                               placeholder="如:1999-9-19"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="NetworkCarIssueDate" class="control-label">资格证发证日期:</label></td>
                    <td><input type="text" name="driverInfo.NetworkCarIssueDate"  readonly data-date-format="yyyy-mm-dd"
                               class="form-control datepicker-input" id="NetworkCarIssueDate" value="${(driverInfo.NetworkCarIssueDate)!}"
                               placeholder="如:1999-9-19"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>

                </tr>

                <tr>
                    <td><label for="NetworkCarProofOn" class="control-label">资格证有效起始日期:</label></td>
                    <td><input type="text" name="driverInfo.NetworkCarProofOn" readonly data-date-format="yyyy-mm-dd"
                               class="form-control datepicker-input" id="NetworkCarProofOn" value="${(driverInfo.NetworkCarProofOn)!}"
                               placeholder="如:1999-9-19"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="NetworkCarProofOff" class="control-label">资格证有效截止日期:</label></td>
                    <td><input type="text" name="driverInfo.NetworkCarProofOff"  readonly data-date-format="yyyy-mm-dd"
                               class="form-control datepicker-input" id="NetworkCarProofOff" value="${(driverInfo.NetworkCarProofOff)!}"
                               placeholder="如:1999-9-19"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="RegisterDate" class="control-label">报备日期:</label></td>
                    <td><input type="text" name="driverInfo.RegisterDate" readonly data-date-format="yyyy-mm-dd"
                               class="form-control datepicker-input" id="RegisterDate" value="${(driverInfo.RegisterDate)!}"
                               placeholder="驾驶员信息向服务所在地出租汽车行政主管部门报备日期"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="InDriverBlacklist" class="control-label">是否在驾驶员黑名单内:</label></td>
                    <td><input type="radio" <#if (driverInfo.InDriverBlacklist??)&&(driverInfo.InDriverBlacklist)==1>checked="checked"</#if>
                               name="driverInfo.InDriverBlacklist" value="1" class="form-control n-invalid" id="InDriverBlacklist">&nbsp;是
                        <input type="radio" <#if (driverInfo.InDriverBlacklist??)&&(driverInfo.InDriverBlacklist)==0>checked="checked"</#if>
                               name="driverInfo.InDriverBlacklist" value="0" class="form-control n-invalid" id="InDriverBlacklist">&nbsp;否
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>
                </tr>

                <tr>
                    <td><label for="ContractCompany" class="control-label">驾驶员合同签署公司:</label></td>
                    <td><input type="text" name="driverInfo.ContractCompany"
                               class="form-control" id="ContractCompany" value="${(driverInfo.ContractCompany)!}"
                               placeholder="全称"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="CommercialType" class="control-label">服务类型:</label></td>
                    <td><input type="radio" <#if (driverInfo.CommercialType??)&&(driverInfo.CommercialType)==1>checked="checked"</#if>
                               name="driverInfo.CommercialType" value="1" class="form-control n-invalid" id="CommercialType">&nbsp;网络预约出租汽车
                        <input type="radio" <#if (driverInfo.CommercialType??)&&(driverInfo.CommercialType)==2>checked="checked"</#if>
                               name="driverInfo.CommercialType" value="2" class="form-control n-invalid" id="CommercialType1">&nbsp;巡游出租汽车
                        <input type="radio" <#if (driverInfo.CommercialType??)&&(driverInfo.CommercialType)==3>checked="checked"</#if>
                               name="driverInfo.CommercialType" value="3" class="form-control n-invalid" id="CommercialType2">&nbsp;私人小客车合乘
                    </td>
                    <td>
                        <div style="padding-top: 10px;color: red;"></div>
                    </td>
                </tr>

                <tr>
                    <td><label for="ContractOn" class="control-label">合同（或协议）有效期起:</label></td>
                    <td><input type="text" name="driverInfo.ContractOn" readonly data-date-format="yyyy-mm-dd"
                               class="form-control datepicker-input" id="ContractOn" value="${(driverInfo.ContractOn)!}"
                               placeholder="如:1999-9-19"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="ContractOff" class="control-label">合同（或协议）有效期止:</label></td>
                    <td><input type="text" name="driverInfo.ContractOff"  readonly data-date-format="yyyy-mm-dd"
                               class="form-control datepicker-input" id="ContractOff" value="${(driverInfo.ContractOff)!}"
                               placeholder="如:1999-9-19"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="EmergencyContact" class="control-label">紧急情况联系人:</label></td>
                    <td><input type="text" name="driverInfo.EmergencyContact"
                               class="form-control" id="EmergencyContact" value="${(driverInfo.EmergencyContact)!}"
                               placeholder="姓名"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="EmergencyContactPhone" class="control-label">紧急联系人电话:</label></td>
                    <td><input type="text" name="driverInfo.EmergencyContactPhone"
                               class="form-control" id="EmergencyContactPhone" value="${(driverInfo.EmergencyContactPhone)!}"
                               placeholder="手机号"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

                <tr>
                    <td><label for="EmergencyContactAddress" class="control-label">紧急情况联系人通信地址:</label></td>
                    <td><input type="text" name="driverInfo.EmergencyContactAddress"
                               class="form-control" id="EmergencyContactAddress" value="${(driverInfo.EmergencyContactAddress)!}"
                               placeholder="地址"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                    <td><label for="birthday" class="control-label">司机出生日期:</label></td>
                    <td><input type="text" name="driverInfo.birthday" readonly data-date-format="yyyy-mm-dd"
                               class="form-control datepicker-input" id="birthday" value="${(driverInfo.birthday)!}"
                               placeholder="YYYYMMDD"></td>
                    <td>
                        <div style="padding-top: 10px;color: red;">*</div>
                    </td>
                </tr>

            </table>
            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <a class="btn btn-default" href="javascript:void(0);" onclick="window.history.go(-1)">取消</a>
                    </div>
                    <#if hasPermission("sijibianji")>
                        <div class="col-sm-2" style="float: right;">
                            <button id="tijiao" class="btn btn-success" type="submit">提交</button>
                        </div></#if>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/driver_item.js?_${.now?string("hhmmss")}"></script>
</#macro>