package org.september.colorful.qrcode.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * @author yexinzhou
 */
@Component
public class MvInterceptor implements HandlerInterceptor {

	private BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
     private TemplateHashModel staticModels = wrapper.getStaticModels();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof ResourceHttpRequestHandler) {
            return true;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView==null) {
            return;
        }

    }

    private void addEnumToModel(Class<?> clazz , ModelAndView mv) throws TemplateModelException {
    	TemplateHashModel optionValueTypeStatics = (TemplateHashModel) staticModels.get(clazz.getName());
        mv.addObject(clazz.getSimpleName(), optionValueTypeStatics);
    }
}
