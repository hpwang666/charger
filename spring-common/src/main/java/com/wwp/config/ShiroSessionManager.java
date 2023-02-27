package com.wwp.config;

import com.wwp.common.util.oConvertUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

public class ShiroSessionManager extends DefaultWebSessionManager {

    public final static String HEADER_TOKEN_NAME = "X-Access-Token";

    public ShiroSessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String id = WebUtils.toHttp(request).getHeader(HEADER_TOKEN_NAME);
        if (oConvertUtils.isEmpty(id)) {
            // 按照默认规则从cookie中获取SessionId
            //System.out.println("按照默认规则从cookie中获取SessionId");
            return super.getSessionId(request, response);
        } else {
            // 从Header头中获取sessionId
            //System.out.println("从Header头中获取sessionId"+id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        }
    }

    @Override
    public void setTimeout(SessionKey key, long maxIdleTimeInMillis) throws InvalidSessionException {
        super.setTimeout(key, 3600000);
    }
}

