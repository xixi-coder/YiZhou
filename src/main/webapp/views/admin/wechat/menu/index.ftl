<#include "../../base/_base.ftl">
<#macro title>
自定义菜单设置
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">自定义菜单设置</h4>
        <button data-id="0" type="button" class="btn btn-link add">添加</button>
    </div>
    <div class="panel-body">
        <div id="content">
            <ul class="list-group">
                <#if menu??&&menu?size gt 0>
                    <#list menu as item>
                        <li class="list-group-item node-content">
                            <div style="width: 300px;float: left;">${(item.name)!}</div>
                            &nbsp;
                            <div style="width: 300px;float: left;margin-top: -10px;">
                                <button data-id="${(item.id)!}" type="button" class="btn btn-link add">添加</button>
                                &nbsp;
                                <button data-id="${(item.id)!}" type="button" class="btn btn-link edit">编辑</button>
                                &nbsp;&nbsp;
                                <button data-id="${(item.id)!}" type="button" class="btn btn-link del">删除</button>
                            </div>
                        </li>
                        <#if item.children??&&item.children?size gt 0>
                            <#list item.children as citem>
                                <li class="list-group-item node-content"
                                    style="padding-left: 40px;">
                                    <div style="width: 300px;float: left;">${(citem.name)!}</div>
                                    &nbsp;
                                    &nbsp;
                                    <div style="width: 300px;float: left;margin-top: -10px;">
                                        <button data-id="${(citem.id)!}" type="button" class="btn btn-link edit">
                                            编辑
                                        </button>
                                        &nbsp;&nbsp;
                                        <button data-id="${(citem.id)!}" type="button" class="btn btn-link del">删除
                                        </button>
                                    </div>
                                </li>
                            </#list>
                        </#if>
                    </#list>
                </#if>
            </ul>
        </div>
    </div>
</div>
</#macro>
<#macro javascript>
<script id="menuContent" type="text/html">
    <div class="panel-body">
        <form role="form">
            <div class="form-group">
                <input type="hidden" class="form-control" id="id" value="{{d.id}}"/>
                <input type="hidden" class="form-control" id="parent" value="{{d.parent}}"/>
                <label for="menuName">菜单名称</label>
                <input type="text" class="form-control" id="menuName" placeholder="菜单名称" value="{{d.menuName}}"/>
            </div>
            <div class="form-group">
                <label for="menuUrl">跳转地址</label>
                <input type="text" class="form-control" id="menuUrl" placeholder="跳转地址" value="{{d.menuUrl}}"/>
            </div>
        </form>
    </div>

</script>
<script type="text/javascript" src="${ctx}/static/js/app/admin/wechat_menu.js?_${.now?string("hhmmss")}"></script>
</#macro>