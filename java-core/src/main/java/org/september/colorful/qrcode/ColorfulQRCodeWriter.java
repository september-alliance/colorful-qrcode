/*
 * Copyright 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.september.colorful.qrcode;

import java.awt.Color;
import java.util.Map;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Version;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

/**
 * This object renders a QR Code as a BitMatrix 2D array of greyscale values.
 * 多彩码画二维码图片，只能按倍数扩大原始大小(二维码对应版本的真实像素)，无需设置二维码的最终框高。
 * 二维码的四边补白部分为数据部分的2%(单边)
 */
public final class ColorfulQRCodeWriter {

	/**
	 * 定位块颜色
	 */
	private int locationColor=0x000000;
	
	/**
	 * 放大倍数
	 */
	private int multiple=2;
	
	
	public ColoredImage encode(String content,Map<EncodeHintType,Object> hints ,ColoredImage result) throws WriterException {
		StringBuilder[] sbArr = new StringBuilder[] {new StringBuilder(), new StringBuilder(), new StringBuilder()};
		for(int i=0;i<content.length();i++) {
			sbArr[i%3].append(content.charAt(i));
		}
		QRCodeBitMatrix bitMatrix1 = encode(sbArr[0].toString(),multiple , hints);
		QRCodeBitMatrix bitMatrix2 = encode(sbArr[1].toString(),multiple,hints);
		QRCodeBitMatrix bitMatrix3 = encode(sbArr[2].toString(),multiple,hints);
		int version = bitMatrix1.getQrCode().getVersion().getVersionNumber();
		if(version< bitMatrix2.getQrCode().getVersion().getVersionNumber()) {
			version = bitMatrix2.getQrCode().getVersion().getVersionNumber();
		}
		if(version< bitMatrix3.getQrCode().getVersion().getVersionNumber()) {
			version = bitMatrix3.getQrCode().getVersion().getVersionNumber();
		}
		
		//使用最大version
		hints.put(EncodeHintType.QR_VERSION, String.valueOf(version));
		
		bitMatrix1 = encode(sbArr[0].toString(),multiple , hints);
		bitMatrix2 = encode(sbArr[1].toString(),multiple,hints);
		bitMatrix3 = encode(sbArr[2].toString(),multiple,hints);
		
		int height = bitMatrix1.getBitMatrix().getHeight();
        int width = bitMatrix1.getBitMatrix().getWidth();
        result.reset(width, height);
        for (int x = 0; x < width; x++) {
        	for (int y = 0; y < height; y++) {
        		//初始化为黑色
        		result.setRGB(x, y , 0x000000);
        	}
        }
        
        drawImage(result,bitMatrix1,new Color(255,0,0));
        drawImage(result,bitMatrix2,new Color(0,255,0));
        drawImage(result,bitMatrix3,new Color(0,0,255));
        //
        for (int x = 0; x < width; x++) {
        	for (int y = 0; y < height; y++) {
        		//黑白互换
        		if(isWhite(result.getRGB(x, y))) {
        			result.setRGB(x, y, Color.BLACK.getRGB());
        		}else if(isBlack(result.getRGB(x, y))) {
        			result.setRGB(x, y, Color.WHITE.getRGB());
        		}
        	}
        }
        return result;
	}

  	private QRCodeBitMatrix encode(String contents,int multiple,
                          Map<EncodeHintType,?> hints) throws WriterException {

    if (contents.isEmpty()) {
      throw new IllegalArgumentException("Found empty contents");
    }

    ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
    if (hints != null) {
      if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
        errorCorrectionLevel = ErrorCorrectionLevel.valueOf(hints.get(EncodeHintType.ERROR_CORRECTION).toString());
      }
    }

    QRCode code = Encoder.encode(contents, errorCorrectionLevel, hints);
    QRCodeBitMatrix result = renderResult(code, multiple);
    result.setQrCode(code);
    return result;
  }

  // Note that the input matrix uses 0 == white, 1 == black, while the output matrix uses
  // 0 == black, 255 == white (i.e. an 8 bit greyscale bitmap).
  private static QRCodeBitMatrix renderResult(QRCode code,int multiple) {
	  QRCodeBitMatrix result = new QRCodeBitMatrix();
    ByteMatrix input = code.getMatrix();
    if (input == null) {
      throw new IllegalStateException();
    }
    if(multiple<1) {
    	multiple = 1;
    }
    int quietZone = (int) (input.getWidth()*0.02);
    if(quietZone==0) {
    	quietZone = 1;
    }
    int inputWidth = input.getWidth();
    int inputHeight = input.getHeight();
    int qrWidth = inputWidth + (quietZone * 2);
    int qrHeight = inputHeight + (quietZone * 2);
    int outputWidth = qrWidth*multiple;
    int outputHeight = qrHeight * multiple;

//    int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
    // Padding includes both the quiet zone and the extra white pixels to accommodate the requested
    // dimensions. For example, if input is 25x25 the QR will be 33x33 including the quiet zone.
    // If the requested size is 200x160, the multiple will be 4, for a QR of 132x132. These will
    // handle all the padding from 100x100 (the actual QR) up to 200x160.
    int leftPadding = quietZone*multiple;
    int topPadding = quietZone*multiple;

    BitMatrix output = new BitMatrix(outputWidth, outputHeight);

    for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
      // Write the contents of this row of the barcode
      for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
        if (input.get(inputX, inputY) == 1) {
          output.setRegion(outputX, outputY, multiple, multiple);
        }
      }
    }
    result.setBitMatrix(output);
    return result;
  }

  
  private void drawImage(ColoredImage image , QRCodeBitMatrix bitMatrix , Color color) {
		 Version version = bitMatrix.getQrCode().getVersion();
      int[] tl = bitMatrix.getBitMatrix().getTopLeftOnBit();
      int totalModelNum = (version.getVersionNumber() - 1) * 4 + 5 + 16;    //获取单边模块数
      int resultWidth = bitMatrix.getBitMatrix().getWidth() - 2 * (tl[0]);
      int modelWidth = resultWidth / totalModelNum;   //得到每个模块长度
      //得到三个基准点的起始与终点
      int startModel = 0;
      int endModel = 7;
      int topEndX = tl[0] + modelWidth * endModel;
      int topStartX = tl[0] + modelWidth * startModel;
      int topStartY = tl[0] + modelWidth * startModel;
      int topEndY = tl[0] + modelWidth * endModel;
      int rightStartX = (totalModelNum - endModel) * modelWidth + tl[0];
      int rightEndX = bitMatrix.getBitMatrix().getWidth() - modelWidth * startModel - tl[0];
      int leftStartY = bitMatrix.getBitMatrix().getHeight() - modelWidth * endModel - tl[1];
      int leftEndY = bitMatrix.getBitMatrix().getHeight() - modelWidth * startModel - tl[1];
	
		for(int x=0;x<image.getWidth();x++) {
			for(int y=0;y<image.getHeight();y++) {
				if(bitMatrix.getBitMatrix().get(x, y)) {
					if (x >= topStartX && x < topEndX && y >= topStartY && y < topEndY) {
                     //左上角颜色
						image.setRGB(x, y, locationColor);
                 } else if (x < rightEndX && x >= rightStartX && y >= topStartY && y < topEndY) {
                     //右上角颜色
                 	image.setRGB(x, y, locationColor);
                 } else if (x >= topStartX && x < topEndX && y >= leftStartY && y < leftEndY) {
                     //右下角颜色
                 	image.setRGB(x, y, locationColor);
                 } else {
                 	if(image.getRGB(x, y)==-1) {
         				image.setRGB(x, y, color.getRGB());
         			}else {
         				image.setRGB(x, y, image.getRGB(x, y) | color.getRGB());
         			}
                 }
					
				}
			}
		}
	}
  
  	private static boolean isWhite(int color) {
		int red = (color >> 16) & 0xFF;
		int green = (color >> 8) & 0xFF;
		int blue = color & 0xFF;
		if(red==255 && green==255 && blue==255) {
			return true;
		}else {
			return false;
		}
	}
	
  	private static boolean isBlack(int color) {
		int red = (color >> 16) & 0xFF;
		int green = (color >> 8) & 0xFF;
		int blue = color & 0xFF;
		if(red==0 && green==0 && blue==0) {
			return true;
		}else {
			return false;
		}
	}
  	
  	public ColorfulQRCodeWriter withLocationColor(int color) {
  		this.locationColor = color;
  		return this;
  	}
  	
  	public ColorfulQRCodeWriter withMultiple(int multiple) {
  		this.multiple = multiple;
  		return this;
  	}
}
