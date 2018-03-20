<!DOCTYPE html>
<html lang="en">
<head>
    <title>收费标准明细</title>
    <meta name="viewport" charset="utf-8" content="width=device-width,
    initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=1.0">
    <link rel="stylesheet" href="${ctx}/static/www/css/app.css">
    <script src="${ctx}/static/www/js/app.js"></script>
    <style type="text/css" rel="stylesheet">
        .viewport {
            width: ${chargeStandard?size+1}00%;
        }
    </style>
</head>
<body>
<div id="viewport" class="viewport">

<#list chargeStandard as item>
    <div class="pageview" style="max-width: ">
        <img id="img${item_index}" src="${ctx}/static/www/images/bg.png" width="100%" height="100%" style="position: absolute;z-index: -1000;"/>
    ${item.desc}
    </div>
</#list>
</div>
<div class="pagenumber">
<#list chargeStandard as item>
    <div></div>
</#list>
</div>
</body>
<script type="text/javascript">
    var a = document.getElementsByClassName("pageview");
    for (var i = 0; i < a.length; i++) {
        a[i].style['max-width'] = window.innerWidth + 'px';
        var img = document.getElementById("img"+i);
        img.style['width'] = window.innerWidth + 'px';
    }
</script>
</html>