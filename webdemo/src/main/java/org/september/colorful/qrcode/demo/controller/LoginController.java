package org.september.colorful.qrcode.demo.controller;
import org.september.simpleweb.auth.PublicMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "")
@PublicMethod
public class LoginController {

	@RequestMapping("/login")
	public ModelAndView login(){
		ModelAndView mv = new ModelAndView();
//		SystemSetting setting = new SystemSetting();
//		setting.setCfgKey("enable_login_verify_code");
//		setting.setCfgVal("1");
//		SystemSetting settingPo = commonDao.getByExample(setting);
//		if(settingPo!=null) {
//			mv.addObject("enableLoginVerifyCode", 1);
//		}else{
//			mv.addObject("enableLoginVerifyCode", 0);
//		}
		mv.addObject("enableLoginVerifyCode", 0);
		return mv;
	}
}
