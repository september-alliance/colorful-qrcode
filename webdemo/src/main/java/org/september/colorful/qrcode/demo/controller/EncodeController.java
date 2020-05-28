package org.september.colorful.qrcode.demo.controller;
import org.september.simpleweb.auth.PublicMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "")
@PublicMethod
public class EncodeController {

	@RequestMapping("/encode")
	public ModelAndView encode(){
		ModelAndView mv = new ModelAndView();
		return mv;
	}
}
