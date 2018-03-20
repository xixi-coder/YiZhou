<html>
<head>
    <title>
    <#if provision.provision??&&provision.provision==1>软件使用协议和政策</#if>
    <#if provision.provision??&&provision.provision==2>代驾服务协议</#if>
    <#if provision.provision??&&provision.provision==3>司机服务合作协议</#if>
    <#if provision.provision??&&provision.provision==4>关于我们</#if>
    <#if provision.provision??&&provision.provision==5>出租车用户协议</#if>
    <#if provision.provision??&&provision.provision==6 >专车使用条款</#if>
    </title>
    <meta name="viewport" id="viewport" content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
</head>
<body>
${(provision.content)!}
</body>
</html>