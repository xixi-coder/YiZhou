package base.shiro;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * Created by BOGONm on 16/8/9.
 */
public class HasPermissionFreeMarkerMethod implements TemplateMethodModel {
    @Override
    public Object exec(List list) throws TemplateModelException {
        if (null == list || 1 != list.size()) {
            throw new TemplateModelException("Wrong arguments: only one argument is allowed");
        }

        String permissionName = (String) list.get(0);
        return getSubject() != null && permissionName != null
                && permissionName.length() > 0 && getSubject().isPermitted(permissionName);
    }

    private static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    private HasPermissionFreeMarkerMethod() {
    }

    private static class HasPermissionFreeMarkerMethodHolder {
        static HasPermissionFreeMarkerMethod instance = new HasPermissionFreeMarkerMethod();
    }

    public static HasPermissionFreeMarkerMethod getInstance() {
        return HasPermissionFreeMarkerMethodHolder.instance;
    }
}
