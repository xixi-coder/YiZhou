/**
 * Created by admin on 2016/12/9.
 */
//第五种方法
var idTmr;
var excelExport = {
    ieExport: function (tableId,name) {
        var curTbl = document.getElementById(tableId);
        var oXL;
        try {
            oXL = new ActiveXObject("Excel.Application"); //创建AX对象excel
        } catch (e) {
            alert("无法启动Excel!\n\n如果您确信您的电脑中已经安装了Excel，" + "那么请调整IE的安全级别。\n\n具体操作：\n\n" + "工具 → Internet选项 → 安全 → 自定义级别 → 对没有标记为安全的ActiveX进行初始化和脚本运行 → 启用");
            return false;
        }
        var winname = window.open('', '_blank', 'top=10000');
        var strHTML = curTbl.innerHTML;
        winname.document.open('text/html', 'replace');
        winname.document.writeln(strHTML);
        winname.document.execCommand('saveas','',name+'.xls');
        winname.close();
        // var oWB = oXL.Workbooks.Add(); //获取workbook对象
        // var oSheet = oWB.ActiveSheet;//激活当前sheet
        // var sel = document.body.createTextRange();
        // sel.moveToElementText(curTbl); //把表格中的内容移到TextRange中
        // sel.select(); //全选TextRange中内容
        // sel.execCommand("Copy");//复制TextRange中内容
        // oSheet.Paste();//粘贴到活动的EXCEL中
        // oXL.Visible = true; //设置excel可见属性
        // var fname = oXL.Application.GetSaveAsFilename(name+"name.xls", "Excel Spreadsheets (*.xls), *.xls");
        // oWB.SaveAs(fname);
        // oWB.Close();
        // oXL.Quit();
    },
    Export: function (tableId, name,filename) {
        if (excelExport.getExplorer() == 'ie') {
            excelExport.ieExport(tableId, name);
        } else {
            excelExport.tableToExcel(tableId, name,filename)
        }
    },
    tableToExcel: function (table, name,filename) {
        var uri = 'data:application/vnd.ms-excel;base64,',
            template = '<html><head><meta charset="UTF-8"></head><body><table>{table}</table></body></html>',
            base64 = function (s) {
                return window.btoa(unescape(encodeURIComponent(s)))
            },
            format = function (s, c) {
                return s.replace(/{(\w+)}/g,
                    function (m, p) {
                        return c[p];
                    })
            }
        if (!table.nodeType) table = document.getElementById(table)
        var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML};
        document.getElementById("exporHiden").href = uri + base64(format(template, ctx));
        document.getElementById("exporHiden").download = filename;//这里是关键所在,当点击之后,设置a标签的属性,这样就可以更改标签的标题了
        document.getElementById("exporHiden").click();
        // window.location.href = uri + base64(format(template, ctx));
    }, Cleanup: function () {
        window.clearInterval(idTmr);
        CollectGarbage();
    }, getExplorer: function () {
        var explorer = window.navigator.userAgent;
        console.log(explorer);
        //ie
        if (explorer.indexOf("MSIE") >= 0||explorer.indexOf("Mozilla") >= 0) {
            return 'ie';
        }
        //firefox
        else if (explorer.indexOf("Firefox") >= 0) {
            return 'Firefox';
        }
        //Chrome
        else if (explorer.indexOf("Chrome") >= 0) {
            return 'Chrome';
        }
        //Opera
        else if (explorer.indexOf("Opera") >= 0) {
            return 'Opera';
        }
        //Safari
        else if (explorer.indexOf("Safari") >= 0) {
            return 'Safari';
        }
    }
}