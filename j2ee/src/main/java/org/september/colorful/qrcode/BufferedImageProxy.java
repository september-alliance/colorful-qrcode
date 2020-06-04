package org.september.colorful.qrcode;

import java.awt.image.BufferedImage;

import org.september.colorful.qrcode.ColoredImage;

public class BufferedImageProxy implements ColoredImage{

	private BufferedImage target;
	
	private QRCodeBitMatrix QRCodeBitMatrix;

	public BufferedImageProxy(BufferedImage source) {
		super();
		this.target = source;
	}

	@Override
	public int getRGB(int x, int y) {
		return target.getRGB(x, y);
	}

	@Override
	public void setRGB(int x, int y, int rgb) {
		target.setRGB(x, y, rgb);
	}

	@Override
	public void reset(int width, int height) {
		target = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

	@Override
	public int getHeight() {
		return target.getHeight();
	}

	@Override
	public int getWidth() {
		return target.getWidth();
	}

	public BufferedImage getTarget() {
		return target;
	}

	@Override
	public QRCodeBitMatrix getQRCodeBitMatrix() {
		return QRCodeBitMatrix;
	}

	public void setQRCodeBitMatrix(QRCodeBitMatrix qRCodeBitMatrix) {
		QRCodeBitMatrix = qRCodeBitMatrix;
	}
	
	
}
