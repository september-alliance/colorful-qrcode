package org.september.colorful.qrcode.demo.auth;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.september.simpleweb.auth.DefaultMethod;
import org.september.simpleweb.auth.SimpleWebSecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class WebSecurityConfiguration extends SimpleWebSecurityConfiguration {

//	@Autowired
//	private UserDataService userDataService;
	
	private static List<String>	defualtUrls = new ArrayList<>();

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/**");
//	}
	
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		// find Mapping with no PublicMethod but DefaultMethod
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        
        for (Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {

            RequestMappingInfo requestMappingInfo = entry.getKey();
            String url = requestMappingInfo.getPatternsCondition().getPatterns().iterator().next();
            HandlerMethod mappingInfoValue = entry.getValue();
            Method method = mappingInfoValue.getMethod();
            DefaultMethod defualtAnno = method.getDeclaringClass().getAnnotation(DefaultMethod.class);
            if (defualtAnno == null) {
                defualtAnno = method.getAnnotation(DefaultMethod.class);
            }
            if (defualtAnno != null) {
                
                defualtUrls.add(url);
            }
        
        }
//		http.authorizeRequests().antMatchers(authorizeUrls.toArray(new String[] {})).hasRole("admin").anyRequest().authenticated();
		http.authorizeRequests().antMatchers("/**").hasRole("default").anyRequest().authenticated();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(new UserAuthenticationProvider(userDataService , systemUserService));
	}
	
	public static List<String> getDefaultUrls(){
		return Arrays.asList(defualtUrls.toArray(new String[] {}));
	}
	
}
