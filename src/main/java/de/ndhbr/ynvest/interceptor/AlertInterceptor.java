package de.ndhbr.ynvest.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AlertInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        if (modelAndView == null) {
            return;
        }

        if (request.isUserInRole("ROLE_NOT_VERIFIED")) {
            modelAndView.addObject("primary", "Du bist noch nicht verifiziert! " +
                    "Vervollständige deine Daten, um deinen ersten Wertpapierhandel tätigen zu können.");
        }
    }
}
