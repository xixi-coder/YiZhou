<#include "../../base/_base.ftl">
<#macro title>
微信公众号设置
</#macro>
<#macro content>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">微信公众号设置</h4>
    </div>
    <div class="panel-body">
        <form class="form-horizontal" method="post" role="form" autocomplete="off"
              id="wechat_ccount_form">
            <input type="hidden" name="wechatAccount.id" value="${(wechatAccount.id)!}" id="id">
            <div class="row ">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="name" class="col-sm-4 control-label">APPID:</label>
                    <div class="col-sm-7">
                        <input type="text" name="wechatAccount.app_id" value="${(wechatAccount.app_id)!}"
                               class="form-control n-invalid"
                               id="name" data-rule="required"
                               placeholder="APPID">
                    </div>
                    <div style="padding-top: 10px;color: red;">*</div>
                </div>
            </div>
            <div class="row ">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="name" class="col-sm-4 control-label">AppSecret:</label>
                    <div class="col-sm-7">
                        <input type="text" name="wechatAccount.app_secret" value="${(wechatAccount.app_secret)!}"
                               class="form-control n-invalid"
                               id="name" data-rule="required"
                               placeholder="AppSecret">
                    </div>
                    <div style="padding-top: 10px;color: red;">*</div>
                </div>
            </div>
            <div class="row ">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="name" class="col-sm-4 control-label">TOKEN:</label>
                    <div class="col-sm-7">
                        <input type="text" name="wechatAccount.token" value="${(wechatAccount.token)!}"
                               class="form-control n-invalid"
                               id="name" data-rule="required"
                               placeholder="APPID">
                    </div>
                    <div style="padding-top: 10px;color: red;">*</div>
                </div>
            </div>
            <div class="row ">
                <div class="form-group col-xs-12 col-md-12">
                    <label for="description" class="col-sm-4 control-label">原始ID:</label>
                    <div class="col-sm-7">
                        <input type="text" name="wechatAccount.account" value="${(wechatAccount.account)!}"
                               class="form-control n-invalid"
                               id="name" data-rule="required"
                               placeholder="AppSecret">
                    </div>
                    <div style="padding-top: 10px;color: red;">*</div>
                </div>
            </div>
            <div class="row" style="margin-top:20px;">
                <div class="col-sm-12">
                    <div class="col-sm-6" style="float: right;">
                        <button class="btn btn-success" type="submit">提交</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</#macro>
<#macro javascript>
<script type="text/javascript" src="${ctx}/static/js/app/admin/wechat_account.js?_${.now?string("hhmmss")}"></script>
</#macro>