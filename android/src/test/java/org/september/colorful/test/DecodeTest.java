package org.september.colorful.test;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;
import org.september.colorful.qrcode.BitMapUtil;
import org.september.colorful.qrcode.BitmapProxy;
import org.september.colorful.qrcode.ColorfulQRCodeReader;
import org.september.colorful.qrcode.ColorfulResult;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DecodeTest {
	

	@Test
	public void decodeQRCode() throws Exception {
		Bitmap bitmap = BitmapFactory.decodeFile("D:\\qrcode\\code9-improved.png");
        bitmap = bitmap.copy(bitmap.getConfig(),true);
        bitmap = BitMapUtil.improveImage(bitmap);
        
		if(bitmap.getWidth()>450 && bitmap.getHeight()>450) {
			bitmap = BitMapUtil.scaleBitmap(bitmap , 450,450);
		}
		
        HashMap<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        
        String result = "";
        result = decode(bitmap , hints);
        
        System.out.println("result is "+result);
    }
	
	private String decode(Bitmap bitmap , HashMap<DecodeHintType, Object> hints) throws NotFoundException, ChecksumException, FormatException, IOException {
		int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
//        bitmap.setPremultiplied(false);
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
		BinaryBitmap binmap = new BinaryBitmap(new HybridBinarizer(source));
		ColorfulResult result = new ColorfulQRCodeReader().decode(binmap, hints , new BitmapProxy(bitmap));
		String resultStr = result.getMergedResult();
		return resultStr;
	}
	
}
