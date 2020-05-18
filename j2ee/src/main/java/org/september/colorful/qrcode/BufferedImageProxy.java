package org.september.colorful.qrcode;

import java.awt.image.BufferedImage;

import org.september.colorful.qrcode.ColoredImage;

public class BufferedImageProxy implements ColoredImage{

	private BufferedImage source;

	public BufferedImageProxy(BufferedImage source) {
		super();
		this.source = source;
	}

	@Override
	public int getRGB(int x, int y) {
		return source.getRGB(x, y);
	}

	@Override
	public void setRGB(int x, int y, int rgb) {
		source.setRGB(x, y, rgb);
	}

	@Override
	public void reset(int width, int height) {
		source = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

	@Override
	public int getHeight() {
		return source.getHeight();
	}

	@Override
	public int getWidth() {
		return source.getWidth();
	}

	public BufferedImage getSource() {
		return source;
	}
	
}
