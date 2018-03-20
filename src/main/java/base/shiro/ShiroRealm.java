package base.shiro;

import base.Constant;
import kits.Md5Kit;
import models.sys.Resources;
import models.sys.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.joda.time.DateTime;

import java.util.List;


/**
 * Created by BOGONm on 16/8/9.
 */
public class ShiroRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.fromRealm(getName()).iterator().next();
        User user = User.dao.findByUserName(username);
        SimpleAuthorizationInfo simpleAuthenticationInfo = new SimpleAuthorizationInfo();
        List<Resources> resources = Resources.dao.findCodeByUser(user.getId());
        for (Resources resource : resources) {
            simpleAuthenticationInfo.addStringPermission(resource.getCode());//添加权限
            simpleAuthenticationInfo.addRole(resource.getStr("role"));//添加角色
        }
        return simpleAuthenticationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //令牌中可以取出用户名密码
        User user = User.dao.findByUserName(token.getUsername());
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }
        if (user.getStatus() == Constant.DataAuditStatus.AUDITFAIL) {
            throw new UnknownAccountException("用户被禁用");
        }
        String md5Str = Md5Kit.MD5(String.valueOf(token.getPassword()) + user.getSalt());
        if (StringUtils.equals(md5Str, user.getPassword())) {
            user.setLastLoginTime(DateTime.now().toDate());
            user.update();
            return new SimpleAuthenticationInfo(token.getUsername(), token.getPassword(), getName());
        } else {
            throw new UnknownAccountException("密码错误");
        }
    }
}
