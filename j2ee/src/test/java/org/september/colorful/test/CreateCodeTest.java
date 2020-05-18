package org.september.colorful.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.september.colorful.qrcode.BufferedImageProxy;
import org.september.colorful.qrcode.ColorfulQRCodeReader;
import org.september.colorful.qrcode.ColorfulQRCodeWriter;
import org.september.colorful.qrcode.ColorfulResult;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class CreateCodeTest {
	
	public void createQRCodeNormal() throws WriterException, IOException {
		String content = "当你看到这条消息时，我知道你已经开始使用的多彩码APP，感谢你的使用。多彩码将二维码的色彩从2色上升到8色甚至32色，色彩再多的话，识别效果可能不好。因此可以提升二维码的信息容量，希望这能对大家有所帮助。";
		
//        ImageIO.write(image, "png", new File("D:\\code-normal.png"));
	}

	@Test
	public void createQRCode() throws WriterException, IOException {
//		String content = FileUtils.readFileToString(new File("C:\\Users\\Administrator\\Desktop\\问题.txt"));
//		String content = "当你看到这条消息时，我知道你已经开始使用的多彩码APP，感谢你的使用。多彩码将二维码的色彩从2色上升到8色深圳32色，色彩再多的话，识别效果可能不好。因此可以提升二维码的信息容量，希望这能对大家有所帮助。";
//		String content = "这是世界上第一张多彩码\r\nThis is the first colorful QR code in the world";
		String content = "一2c一2c一2c一2c";
		
		HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  // 纠错等级
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        
        BufferedImageProxy result = new BufferedImageProxy(null);
        ColorfulQRCodeWriter writer = new ColorfulQRCodeWriter().withLocationColor(0x629755).withMultiple(4);
        writer.encode(content, hints, result);
        
        ImageIO.write(result.getSource(), "png", new File("D:\\qrcode\\code.png"));
	}
	
	
	@Test
	public void decodeQRCode() throws Exception {
		BufferedImage image= ImageIO.read(new File("D:\\qrcode\\code.png"));
        
        HashMap<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        
        String result = "";
        result = decode(image , hints);
        
        System.out.println("result is "+result);
    }
	
	private String decode(BufferedImage image , HashMap<DecodeHintType, Object> hints) throws NotFoundException, ChecksumException, FormatException, IOException {
      BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
      BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
      ColorfulResult result = new ColorfulQRCodeReader().decode(bitmap, hints , new BufferedImageProxy(image));
      String resultStr = result.getMergedResult();
      return resultStr;
	}
}
