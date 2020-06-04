package org.september.colorful.qrcode;


import android.graphics.Bitmap;

public class BitmapProxy implements ColoredImage {

	private Bitmap bm;
	
	private QRCodeBitMatrix QRCodeBitMatrix;

	public BitmapProxy(Bitmap bm) {
		super();
		this.bm = bm;
	}

	@Override
	public void reset(int width, int height) {
		this.bm = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);
	}

	@Override
	public int getRGB(int x, int y) {
		return bm.getPixel(x, y);
	}

	@Override
	public void setRGB(int x, int y, int rgb) {
		bm.setPixel(x, y, rgb);
	}

	@Override
	public int getHeight() {
		return bm.getHeight();
	}

	@Override
	public int getWidth() {
		return bm.getWidth();
	}

	public Bitmap getBm() {
		return bm;
	}

	public QRCodeBitMatrix getQRCodeBitMatrix() {
		return QRCodeBitMatrix;
	}

	public void setQRCodeBitMatrix(QRCodeBitMatrix qRCodeBitMatrix) {
		QRCodeBitMatrix = qRCodeBitMatrix;
	}
}
