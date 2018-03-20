package base.shiro;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by BOGONm on 16/8/10.
 */
public class SelfShiroHttpServletResponse extends org.apache.shiro.web.servlet.ShiroHttpServletResponse {
    public SelfShiroHttpServletResponse(HttpServletResponse wrapped, ServletContext context, ShiroHttpServletRequest request) {
        super(wrapped, context, request);
    }

    @Override
    public String encodeRedirectURL(String url) {
        // return super.encodeRedirectURL(url);
        return url;
    }

    @Override
    public String encodeURL(String url) {
        return url;
    }
}
