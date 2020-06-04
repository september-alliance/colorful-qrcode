package org.september.colorful.test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import net.coobird.thumbnailator.Thumbnails;

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
        
        ImageIO.write(result.getTarget(), "png", new File("D:\\qrcode\\code.png"));
	}
	
	
	@Test
	public void decodeQRCode() throws Exception {
//		BufferedImage image= ImageIO.read(new File("D:\\qrcode\\code9-improved.png"));
		BufferedImage image = improveImage(ImageIO.read(new File("D:\\qrcode\\code11.jpg")));
		ImageIO.write(image, "png", new File("D:\\qrcode\\code11-improved.png"));
//		image = improveImage(image);
		if(image.getWidth()>450 && image.getHeight()>450) {
			image = Thumbnails.of(image).size(450, 450).asBufferedImage();
			ImageIO.write(image, "png", new File("D:\\qrcode\\code11-improved-resize.png"));
		}
		
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
	
	@Test
	public void testImproveImage() throws Exception {
		
		BufferedImage image = improveImage(ImageIO.read(new File("D:\\qrcode\\code10.jpg")));
		ImageIO.write(image, "png", new File("D:\\qrcode\\code9-improved.png"));
		
	}
	
	private BufferedImage improveImage(BufferedImage image) {
		List<Color> colors = new ArrayList<>();
		colors.add(new Color(0,0,0));
		colors.add(new Color(255,255,255));
		colors.add(new Color(0,0,255));
		colors.add(new Color(0,255,255));
		colors.add(new Color(255,0,0));
		colors.add(new Color(255,255,0));
		colors.add(new Color(0,255,0));
		colors.add(new Color(255,0,255));
		
		for(int x=0;x<image.getWidth();x++) {
			for(int y=0;y<image.getHeight();y++) {
				Color c1 = new Color(image.getRGB(x, y));
				double dis = Double.MAX_VALUE;
				Color nearestColor = null;
				for(Color c2 : colors) {
					double tmpDis = cacuColorDistance(c1 , c2);
					if(tmpDis<dis) {
						dis = tmpDis;
						nearestColor = c2;
					}
				}
				image.setRGB(x, y, nearestColor.getRGB());
			}
		}
		return image;
	}
	
	private void adjust(BufferedImage image) {
		for(int x=0;x<image.getWidth();x++) {
			for(int y=0;y<image.getHeight();y++) {
				if(x-1<0 || y-1<0) {
					continue;
				}
				if(x+1>=image.getWidth() || y+1>=image.getHeight()) {
					continue;
				}
//				System.out.println("adjust x = "+x +",y="+y);
//				Color color = new Color(image.getRGB(x, y));
				Map<Integer,Integer> colorCount = new HashMap<>();
				List<Integer> aroundColors = new ArrayList<>();
				//从左上角开始，顺时针绕一圈
				int c1 = image.getRGB(x-1, y-1);
				int c2 = image.getRGB(x, y-1);
				int c3 = image.getRGB(x+1, y-1);
				int c4 = image.getRGB(x+1, y);
				int c5 = image.getRGB(x+1, y+1);
				int c6 = image.getRGB(x, y+1);
				int c7 = image.getRGB(x-1, y+1);
				int c8 = image.getRGB(x-1, y);
				aroundColors.add(c1);
				aroundColors.add(c2);
				aroundColors.add(c3);
				aroundColors.add(c4);
				aroundColors.add(c5);
				aroundColors.add(c6);
				aroundColors.add(c7);
				aroundColors.add(c8);
				for(Integer c : aroundColors) {
					if(colorCount.containsKey(c)) {
						colorCount.put(c, colorCount.get(c)+1);
					}else {
						colorCount.put(c, 1);
					}
				}
				for(Integer c : colorCount.keySet()) {
					if(colorCount.get(c)>=6) {
						image.setRGB(x, y, c);
						break;
					}
				}
			}
		}
	}
	
	public double cacuColorDistance(Color c1 ,Color c2) {
		double dis = Math.sqrt(Math.pow(c2.getRed()-c1.getRed(),2) + Math.pow(c2.getGreen()-c1.getGreen(),2) + Math.pow(c2.getBlue()-c1.getBlue(),2));
		return dis;
	}
}
