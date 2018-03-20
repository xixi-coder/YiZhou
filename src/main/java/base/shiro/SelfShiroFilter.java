package base.shiro;


import org.apache.shiro.web.servlet.ShiroHttpServletRequest;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class SelfShiroFilter extends org.apache.shiro.web.servlet.ShiroFilter {
    @Override
    protected ServletResponse wrapServletResponse(HttpServletResponse orig, ShiroHttpServletRequest request) {
        // TODO Auto-generated method stub
        // return super.wrapServletResponse(orig, request);
        return new SelfShiroHttpServletResponse(orig, getServletContext(), request);
    }
}
