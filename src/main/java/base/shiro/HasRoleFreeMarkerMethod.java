package base.shiro;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * Created by BOGONm on 16/8/9.
 */
public class HasRoleFreeMarkerMethod implements TemplateMethodModel {
    @Override
    public Object exec(List list) throws TemplateModelException {
        if (null == list || 1 != list.size()) {
            throw new TemplateModelException("Wrong arguments: only one argument is allowed");
        }

        String roleName = (String) list.get(0);
        return getSubject() != null && roleName != null
                && roleName.length() > 0 && getSubject().hasRole(roleName);
    }

    private static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    private HasRoleFreeMarkerMethod() {

    }

    private static class HasRoleFreeMarkerMethodHolder {
        static HasRoleFreeMarkerMethod instance = new HasRoleFreeMarkerMethod();
    }

    public static HasRoleFreeMarkerMethod getInstance() {
        return HasRoleFreeMarkerMethodHolder.instance;
    }

}
