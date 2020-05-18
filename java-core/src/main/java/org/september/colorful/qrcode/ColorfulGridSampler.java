package org.september.colorful.qrcode;

import java.util.ArrayList;
import java.util.List;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.PerspectiveTransform;

public class ColorfulGridSampler extends GridSampler{

	private ColoredImage originalBI;
	
	public ColorfulGridSampler(ColoredImage originalBI) {
		super();
		this.originalBI = originalBI;
	}

	@Override
	public BitMatrix sampleGrid(BitMatrix image,
	                              int dimensionX,
	                              int dimensionY,
	                              float p1ToX, float p1ToY,
	                              float p2ToX, float p2ToY,
	                              float p3ToX, float p3ToY,
	                              float p4ToX, float p4ToY,
	                              float p1FromX, float p1FromY,
	                              float p2FromX, float p2FromY,
	                              float p3FromX, float p3FromY,
	                              float p4FromX, float p4FromY) throws NotFoundException {
	
		throw NotFoundException.getNotFoundInstance();
	}
	
	  public List<BitMatrix> sampleColorfulGrid(BitMatrix image,
	                              int dimensionX,
	                              int dimensionY,
	                              PerspectiveTransform transform) throws NotFoundException {
	    if (dimensionX <= 0 || dimensionY <= 0) {
	      throw NotFoundException.getNotFoundInstance();
	    }
	    List<BitMatrix> bitsList = new ArrayList<>();
	    BitMatrix bits = null;
	    BitMatrix redBits = null;
	    BitMatrix greenBits = null;
	    BitMatrix blueBits = null;
	    boolean isColorful = isColorful(transform, dimensionY);
	    if(isColorful) {
	    	redBits = new BitMatrix(dimensionX, dimensionY);
		    greenBits = new BitMatrix(dimensionX, dimensionY);
		    blueBits = new BitMatrix(dimensionX, dimensionY);
		    bitsList.add(redBits);
		    bitsList.add(greenBits);
		    bitsList.add(blueBits);
	    }else {
	    	bits = new BitMatrix(dimensionX, dimensionY);
	    	bitsList.add(bits);
	    }
	    
	    float[] points = new float[2 * dimensionX];
	    for (int y = 0; y < dimensionY; y++) {
	      int max = points.length;
	      float iValue = y + 0.5f;
	      for (int x = 0; x < max; x += 2) {
	        points[x] = (float) (x / 2) + 0.5f;
	        points[x + 1] = iValue;
	      }
	      transform.transformPoints(points);
	      // Quick check to see if points transformed to something inside the image;
	      // sufficient to check the endpoints
	      checkAndNudgePoints(image, points);
	      try {
	        for (int x = 0; x < max; x += 2) {
	          
	          if(isColorful) {
	        	  //检查原图的rgb值
	        	  RGBColor c = new RGBColor(originalBI.getRGB((int) points[x], (int) points[x + 1]));
		          //黑白互换 
		          if(c.getRed()<127 && c.getGreen()<127 && c.getBlue()<127) {
		        	  c = RGBColor.WHITE;
		          }else if(c.getRed()>=127 && c.getGreen()>=127 && c.getBlue()>=127) {
		        	  c = RGBColor.BLACK;
		          }
//		          System.out.println("r="+c.getRed()+",g="+c.getGreen()+",b="+c.getBlue()+",at x="+points[x]+",y="+points[x+1]);
		          if(c.getRed()>=127) {
		        	  redBits.set(x / 2, y);
		          }
		          if(c.getGreen()>=127) {
		        	  greenBits.set(x / 2, y);
		          }
		          if(c.getBlue()>=127) {
		        	  blueBits.set(x / 2, y);
		          }
	          }else {
		          if (image.get((int) points[x], (int) points[x + 1])) {
		            // Black(-ish) pixel
		            bits.set(x / 2, y);
		          }
	          }

	        }
	      } catch (ArrayIndexOutOfBoundsException aioobe) {
	        // This feels wrong, but, sometimes if the finder patterns are misidentified, the resulting
	        // transform gets "twisted" such that it maps a straight line of points to a set of points
	        // whose endpoints are in bounds, but others are not. There is probably some mathematical
	        // way to detect this about the transformation that I don't know yet.
	        // This results in an ugly runtime exception despite our clever checks above -- can't have
	        // that. We could check each point's coordinates but that feels duplicative. We settle for
	        // catching and wrapping ArrayIndexOutOfBoundsException.
	        throw NotFoundException.getNotFoundInstance();
	      }
	    }
	    return bitsList;
	  }

	@Override
	public BitMatrix sampleGrid(BitMatrix image, int dimensionX, int dimensionY, PerspectiveTransform transform)
			throws NotFoundException {
		throw NotFoundException.getNotFoundInstance();
	}

	private boolean isColorful(PerspectiveTransform transform , int dimensionY) {
		float[] points = new float[2 * dimensionY];
		List<Integer> colors = new ArrayList<>();
		for (int y = 0; y < dimensionY; y++) {
		      int max = points.length;
		      float iValue = y + 0.5f;
		      for (int x = 0; x < max; x += 2) {
		        points[x] = (float) (x / 2) + 0.5f;
		        points[x + 1] = iValue;
		      }
		      transform.transformPoints(points);
//		      checkAndNudgePoints(image, points);
		      for (int x = 0; x < max; x += 2) {
		    	//检查原图的rgb值
		    	 RGBColor c = new RGBColor(originalBI.getRGB((int) points[x], (int) points[x + 1]));
	          	int r = 0,g=0,b=0;
				if(c.getRed()<127) {
					r = 0;
				}else {
					r = 255;
				}
				if(c.getGreen()<127) {
					g = 0;
				}else {
					g = 255;
				}
				
				if(c.getBlue()<127) {
					b = 0;
				}else {
					b = 255;
				}
				originalBI.setRGB(x, y, new RGBColor(r,g,b).getRGB());
				if(!colors.contains(originalBI.getRGB(x, y))) {
					colors.add(originalBI.getRGB(x, y));
				}
		      }
		    }
		if(colors.size()>2) {
			return true;
		}else {
			return false;
		}
	}
}
