package org.september.colorful.qrcode;

public interface ColoredImage {
	
	/**
	 * 重置一张图片,java用BufferedImage（颜色类型为BufferedImage.TYPE_INT_RGB）
	 */
	public void reset(int width , int height);
	
	public int getRGB(int x , int y);
	
	public void setRGB(int x , int y, int rgb);
	
	public int getHeight();
	
	public int getWidth();
}
