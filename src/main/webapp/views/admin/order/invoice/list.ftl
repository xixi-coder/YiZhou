<#include "../../base/_base.ftl">
<#macro title>
发票管理
</#macro>
<#macro content>
<!--suppress ALL -->
<input type="hidden" value="${(type)!}" id="orderType"/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4>发票管理</h4>
        <div class="additional-btn">
            <a href="#" class="hidden reload"><i class="icon-ccw-1"></i></a>
        </div>
    </div>
    <div class="panel-body">
        <div class="table-responsive" style="overflow:auto;">
            <form id="searchForm_">
                <table class="hover table table-striped table-bordered" cellspacing="0" width="200%">
                    <tr>
                        <td colspan="2">
                            <label for="s-dir.addressee_phone-LIKE" class="control-label">收件人手机号码:</label>
                            <input type="hidden" name="type" value="${(type)!}"/>
                            <input name="s-dir.addressee_phone-LIKE"/>
                            <label for="s-name-LIKE" class="control-label">申请时间:</label>
                            <input type="hidden" name="s-dir.create_time-BETWEEN" value="" id="timeSearch">
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="start"
                                   placeholder="如:1992-11-11">至
                            <input type="text" data-date-format="yyyy-MM-dd"
                                   class="datepicker-input" id="end"
                                   placeholder="如:1992-11-11">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="control-label">状态:</label>
                            <select name="s-dir.status-EQ" style="height: 26px;">
                                <option value="" selected="selected">全部</option>
                                <option value="0">新申请</option>
                                <option value="1">已开票</option>
                            </select>

                            <#if hasRole("super_admin")||hasPermission("select-company")>
                                <label class="control-label">所属公司:</label>
                                <select class="select2 select-company" name="s-dmi.company-EQ"
                                        style="width: 100px;">
                                    <option value="">请选择</option>
                                </select>
                            </#if>
                        </td>
                        <td>
                            <button class="btn btn-info search" type="button">搜索</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="table-responsive" style="overflow:auto;">

            <table id="invoice-list" cellspacing="0" class="hover table table-striped table-bordered"
                   width="200%">
                <thead>
                <tr>
                    <th width="10px"></th>
                    <th>发票抬头</th>
                    <th>发票内容</th>
                    <th>开票人手机号</th>
                    <th>金额</th>
                    <th>收件人姓名</th>
                    <th>收件人手机号</th>
                    <th>邮编</th>
                    <th>收件地址</th>
                    <th>状态</th>
                    <th>所属公司</th>
                    <th>申请日期</th>
                    <th>发票号</th>
                    <th>开票时间</th>
                    <th width="100px;">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>


</#macro>
<#macro javascript>
<script id="action_tpl" type="text/html">
    <div class="btn-group" style="width:64px;">
        <button type="button" class="btn btn-success btn-xs ">操作</button>
        <button type="button" class="btn btn-success btn-xs  dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only"></span></button>
        <ul class="dropdown-menu success btn-xs " role="menu">
            {{# if(d.status==0){ }}
            <#if hasPermission("huiyuankaipiao")>
                <li><a class="addInvoice" href="javascript:void(0);" data-id="{{d.id}}">开票</a></li>
            </#if>
            {{# } }}
            <li><a  class="findInvoice" href="javascript:void(0);" data-id="{{d.id}}">查看明细</a></li>
        </ul>
    </div>
</script>
<script id="invoice_no" type="text/x-handlebars-template">
    <form id="addinvoice_form" action="" autocomplete="off">
        <table style="border-collapse:separate;border-spacing:10px;">
            <tr>
                <td>
                    <label for="amount" class="col-sm-4 control-label">发票号:</label>
                </td>
                <td>
                    <input type="hidden" name="invoiceRec.id" value="{{d.id}}">
                    <input type="text" name="invoiceRec.invoice_no"
                           placeholder="">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="amount" class="col-sm-4 control-label">快递公司:</label>
                </td>
                <td>
                    <input type="text" name="invoiceRec.express_company"
                           placeholder="">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="amount" class="col-sm-4 control-label">快递单号:</label>
                </td>
                <td>
                    <input type="text" name="invoiceRec.express_no"
                           placeholder="">
                </td>
            </tr>
        </table>
    </form>
</script>

<#--   -->
<div id="invoice_no2" hidden="hidden">

    <script>
        $("#tableExcel").remove();
        detailed.forEach(function(date){
            var table=$("#tableExcel");
            var tbody=$("<tbody> </tbody>");
            var tr1 = $("<tr class='tr'> </tr>");
            var tr2 = $("<tr class='tr'> </tr>");
            var tr3 = $("<tr class='tr'> </tr>");
            var tr4 = $("<tr class='tr'> </tr>");
            var tr5 = $("<tr class='tr'> </tr>");
            var tr6 = $("<tr class='tr'> </tr>");
            $("<td colspan='1'  class='active' style='border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;border-collapse: collapse;WORD-WRAP: break-word;'>下单方式：</td>").appendTo(tr1);
            $("<td colspan='5' class='active' style='border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;border-collapse: collapse;WORD-WRAP: break-word;'>"+date.remark+"</td>").appendTo(tr1);
            $("<td colspan='1' class='active1' style=' height: 40px;border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;'> 起止地点:</td>").appendTo(tr2);
            $("<td colspan='3' class='active1' style=' height: 40px;border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;'>  <span class='sp1'>"+date.destination+" </span></br>    至    </br>  <span class='sp2'> "+date.reservation_address+ "  </span>  </td>").appendTo(tr2);
            $("<td>实际公里:</td>").appendTo(tr2);
            $("<td colspan='1' class='active1' style=' height: 40px;border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;'>"+date.distance +" 公  里</td>").appendTo(tr2);
            $("<td colspan='1'  class='active1' style=' height: 40px;border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;'>起止时间:</td>").appendTo(tr3);
            $("<td colspan='3' class='active1' style=' height: 40px;border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;'> <span class='sp3'> "+date.create_time+"</span>  至 <span class='sp4'>"+date.pay_time+"</span> </td>").appendTo(tr3);
            $("<td colspan='1' class='active1' style=' height: 40px;border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;' >时  长:</td>").appendTo(tr3);
            var date1 = new Date(date.create_time);
            var date2 = new Date(date.pay_time);
            var s1 = date1.getTime(), s2 = date2.getTime();
            var total = (s2 - s1)/1000/60;
            debugger;
            $("<td colspan='1'  class='active1' style=' height: 40px;border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;'>"+total.toFixed(0)+" 分钟</td>").appendTo(tr3);
            $("<td colspan='1'  class='active1' style=' height: 40px;border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;'>金  额:</td>").appendTo(tr4);
            var ma;
            var fen;
            var jiao;
            var yuan;
            var shi;
            var bai;
            var qian;
            if(date.amount!=null){
                ma =date.amount.toFixed(2);
                if(ma.indexOf(".")>0){
                var  qm =  ma.split(".")[0];
                var  hm =  ma.split(".")[1];
                    fen= hm.charAt(hm.length-1);
                    jiao=hm.charAt(hm.length-2);

                    yuan=qm.charAt(qm.length-1);
                    shi=qm.charAt(qm.length-2);
                    bai=qm.charAt(qm.length-3);
                    qian=qm.charAt(qm.length-4);


                }else{
                    yuan=ma.charAt(ma.length-1);
                    shi=ma.charAt(ma.length-2);
                    bai=ma.charAt(ma.length-3);
                    qian=ma.charAt(ma.length-4);

                }
            }
            $("<td colspan='4' class='active1' style=' height: 40px;border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;万&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+(Arabia_To_Chinese(qian)==undefined?'':Arabia_To_Chinese(qian))+"仟&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+(Arabia_To_Chinese(bai)==undefined ? '':Arabia_To_Chinese(bai)) +"佰&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+Arabia_To_Chinese(shi==null?0:shi)+" 拾&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+ Arabia_To_Chinese(yuan==null?0:yuan)+"元&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+Arabia_To_Chinese(jiao==null?0:jiao) +"角&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+Arabia_To_Chinese(fen==null?0:fen) +"分&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; </td>").appendTo(tr4);
            $("<td colspan='1'  class='active1' style=' height: 40px;border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;'>"+date.amount+"元 </td>").appendTo(tr4);
            $("<td colspan='1'  class='active1' style=' height: 40px;border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;'>备注单号:</td>").appendTo(tr5);
            $("<td colspan='5' class='active1' style=' height: 40px;border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;'>"+date.no+"</td>").appendTo(tr5);
            $("<td colspan='6' class='active' style=' height: 40px;border-right: black 2px solid;border-top: black 2px solid;border-left: black 2px solid;border-bottom: black 2px solid;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; {{  getNowFormatDate()}}</td> ").appendTo(tr6);
            tr1.appendTo(tbody).appendTo(table);
            tr2.appendTo(tbody).appendTo(table);
            tr3.appendTo(tbody).appendTo(table);
            tr4.appendTo(tbody).appendTo(table);
            tr5.appendTo(tbody).appendTo(table);
            tr6.appendTo(tbody).appendTo(table);

        })
       </script>

    <table id="tableExcel" cellspacing="0" cellpadding="0" width="98%" align="center" border="0" style="border-right: black 1px solid; border-top: black 1px solid; border-left: black 1px solid; border-bottom: black 1px solid; border-collapse: collapse;">

   </table> <p>&nbsp;</p>

    <button type="button" onclick="method5('tableExcel')">导出</button>

</div>

<style>

    .tr{
        border-right: black 2px solid;
        border-top: black 2px solid;
        border-left: black 2px solid;
        border-bottom: black 2px solid;
        border-collapse: collapse
    }
    .sp1{
        color:#F00
    }
    .sp2{
        color:#00F
    }
    .sp3{
        color:#00cc33
    }
    .sp4{
        color:#0099ff
    }


</style>

<script language="JavaScript" type="text/javascript">
    //第五种方法
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
    function method5(tableid) {
        if(getExplorer()=='ie')
        {
            var curTbl = document.getElementById(tableid);
            var oXL = new ActiveXObject("Excel.Application");
            var oWB = oXL.Workbooks.Add();
            var xlsheet = oWB.Worksheets(1);
            var sel = document.body.createTextRange();
            sel.moveToElementText(curTbl);
            sel.select();
            sel.execCommand("Copy");
            xlsheet.Paste();
            oXL.Visible = true;
            try {
                var fname = oXL.Application.GetSaveAsFilename("Excel.xls", "Excel Spreadsheets (*.xls), *.xls");
            }
            catch (e) {
                print("Nested catch caught " + e);
            }
            finally {
                oWB.SaveAs(fname);
                oWB.Close(savechanges = false);
                oXL.Quit();
                oXL = null;
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
                template = '<html><head><meta charset="UTF-8"></head><body><table>{table}</table></body></html>',
                base64 = function(s) {
                    return window.btoa(unescape(encodeURIComponent(s))) }
                ,
                format = function(s, c) {
                    return s.replace(/{(\w+)}/g,
                            function(m, p) {
                                return c[p];
                            }
                    ) }
        return function(table, name) {
            if (!table.nodeType) table = document.getElementById(table)
            var ctx = {
                worksheet: name || 'Worksheet', table: table.innerHTML}
            window.location.href = uri + base64(format(template, ctx))
        }
    }
    )()


    function Arabia_To_Chinese(Num) {
        switch (Num) {
            case "0":  return  "零" ;
            case "1": return   "壹"  ;
            case "2": return   "贰" ;
            case "3": return   "叁"  ;
            case "4": return   "肆"  ;
            case "5": return   "伍"  ;
            case "6": return   "陆" ;
            case "7": return   "柒"  ;
            case "8": return  "捌"  ;
            case "9": return  "玖" ;
        }
    }


</script>

<#--   -->

<script type="text/javascript" src="${ctx}/static/js/app/admin/invoice.js?_${.now?string("hhmmss")}"></script>
</#macro>