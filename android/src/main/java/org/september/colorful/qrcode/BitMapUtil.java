package org.september.colorful.qrcode;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitMapUtil {

	public static Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }
	
	public static Bitmap improveImage(Bitmap image) {
		List<RGBColor> colors = new ArrayList<>();
		colors.add(new RGBColor(0,0,0));
		colors.add(new RGBColor(255,255,255));
		colors.add(new RGBColor(0,0,255));
		colors.add(new RGBColor(0,255,255));
		colors.add(new RGBColor(255,0,0));
		colors.add(new RGBColor(255,255,0));
		colors.add(new RGBColor(0,255,0));
		colors.add(new RGBColor(255,0,255));
		
		for(int x=0;x<image.getWidth();x++) {
			for(int y=0;y<image.getHeight();y++) {
				RGBColor c1 = new RGBColor(image.getPixel(x, y));
				double dis = Double.MAX_VALUE;
				RGBColor nearestColor = null;
				for(RGBColor c2 : colors) {
					double tmpDis = cacuColorDistance(c1 , c2);
					if(tmpDis<dis) {
						dis = tmpDis;
						nearestColor = c2;
					}
				}
				image.setPixel(x, y, nearestColor.getRGB());
			}
		}
		return image;
	}
	
	private static double cacuColorDistance(RGBColor c1 ,RGBColor c2) {
		double dis = Math.sqrt(Math.pow(c2.getRed()-c1.getRed(),2) + Math.pow(c2.getGreen()-c1.getGreen(),2) + Math.pow(c2.getBlue()-c1.getBlue(),2));
		return dis;
	}
}
