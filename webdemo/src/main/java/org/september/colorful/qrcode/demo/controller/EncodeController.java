package org.september.colorful.qrcode.demo.controller;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.september.colorful.qrcode.BufferedImageProxy;
import org.september.colorful.qrcode.ColorfulQRCodeWriter;
import org.september.colorful.qrcode.demo.vo.EncodeResultVo;
import org.september.simpleweb.auth.PublicMethod;
import org.september.simpleweb.model.ResponseVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


@Controller
@RequestMapping(value = "")
@PublicMethod
public class EncodeController {

	@RequestMapping("/encode")
	public ModelAndView encode(){
		ModelAndView mv = new ModelAndView();
		return mv;
	}
	
    @ResponseBody
    @RequestMapping("/doEncode")
    public ResponseVo<EncodeResultVo> doEncode(String content , int multiple , int positionColor,String errorCorrection) throws Exception  {
    	HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.valueOf(errorCorrection));  // 纠错等级
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        EncodeResultVo encodeResult = new EncodeResultVo();
        BufferedImageProxy result = new BufferedImageProxy(null);
        ColorfulQRCodeWriter writer = new ColorfulQRCodeWriter().withLocationColor(positionColor).withMultiple(multiple);
        writer.encode(content, hints, result);
        result.getTarget();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ImageIO.write(result.getTarget(), "png", stream);
		String base64 = Base64.getEncoder().encodeToString(stream.toByteArray());
		encodeResult.setBase64Image(base64);
		encodeResult.setVersion(result.getQRCodeBitMatrix().getQrCode().getVersion().getVersionNumber());
		encodeResult.setHeight(result.getQRCodeBitMatrix().getBitMatrix().getHeight()/multiple);
		encodeResult.setWidth(result.getQRCodeBitMatrix().getBitMatrix().getWidth()/multiple);
		
		encodeResult.setOriginalHeight(result.getQRCodeBitMatrix().getBitMatrix().getHeight());
		encodeResult.setOriginalWidth(result.getQRCodeBitMatrix().getBitMatrix().getWidth());
		
        return ResponseVo.<EncodeResultVo>BUILDER().setData(encodeResult).setCode(ResponseVo.BUSINESS_CODE.SUCCESS);
    }
}
