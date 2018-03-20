package base.shiro;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * Created by BOGONm on 16/8/9.
 */
public class HasAnyRolesFreeMarkerMethod implements TemplateMethodModel {

    @SuppressWarnings("rawtypes")
    @Override
    public Object exec(List list) throws TemplateModelException {
        //参数不合法直接返回false
        if (null == list || list.isEmpty()) {
            return false;
        }
        // 循环判断当前用用户是否拥有其中的某一个角色
        boolean hasAny = false;
        for (Object obj : list) {
            System.out.println(obj);
            if (getSubject().hasRole((String) obj)) {
                hasAny = true;
                break;
            }
        }
        return hasAny;
    }

    private static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    private HasAnyRolesFreeMarkerMethod() {
    }

    private static class HasAnyRolesFreeMarkerMethodHolder {
        static HasAnyRolesFreeMarkerMethod instance = new HasAnyRolesFreeMarkerMethod();
    }

    public static HasAnyRolesFreeMarkerMethod getInstance() {
        return HasAnyRolesFreeMarkerMethodHolder.instance;
    }
}