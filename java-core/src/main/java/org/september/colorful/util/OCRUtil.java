package org.september.colorful.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class OCRUtil {

	/**
	 * 反色
	 */
	public static final BufferedImage inverse(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage inverseImage = new BufferedImage(width, height, image.getType());
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = image.getRGB(j, i);
				inverseImage.setRGB(j, i, 0xFFFFFF - pixel);
			}
		}
		return inverseImage;
	}
	
	public static boolean isWhite(int color) {
		int red = (color >> 16) & 0xFF;
		int green = (color >> 8) & 0xFF;
		int blue = color & 0xFF;
		if(red==255 && green==255 && blue==255) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isBlack(int color) {
		int red = (color >> 16) & 0xFF;
		int green = (color >> 8) & 0xFF;
		int blue = color & 0xFF;
		if(red==0 && green==0 && blue==0) {
			return true;
		}else {
			return false;
		}
	}
}
