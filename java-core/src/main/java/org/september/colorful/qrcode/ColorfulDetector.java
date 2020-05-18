package org.september.colorful.qrcode;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.PerspectiveTransform;
import com.google.zxing.common.detector.MathUtils;
import com.google.zxing.qrcode.decoder.Version;
import com.google.zxing.qrcode.detector.AlignmentPattern;
import com.google.zxing.qrcode.detector.Detector;
import com.google.zxing.qrcode.detector.FinderPattern;
import com.google.zxing.qrcode.detector.FinderPatternFinder;
import com.google.zxing.qrcode.detector.FinderPatternInfo;

public class ColorfulDetector extends Detector{

	private ColoredImage originalBI;
	
	public ColorfulDetector(BitMatrix image ,ColoredImage originalBI) {
		super(image);
		this.originalBI = originalBI;
	}
	
	public ColorfulDetectorResult detectColorful(Map<DecodeHintType,?> hints) throws NotFoundException, FormatException {
		ResultPointCallback resultPointCallback = getResultPointCallback();
		resultPointCallback = hints == null ? null :
        (ResultPointCallback) hints.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);

    	FinderPatternFinder finder = new FinderPatternFinder(getImage(), resultPointCallback);
    	FinderPatternInfo info = null;
    	try {
    		Method findMethod = FinderPatternFinder.class.getDeclaredMethod("find", Map.class);
        	findMethod.setAccessible(true);
        	info = (FinderPatternInfo) findMethod.invoke(finder, hints);
    	}catch(Exception ex) {
    		ex.printStackTrace();
    		throw NotFoundException.getNotFoundInstance();
    	}
    	
    	return processColorfulFinderPatternInfo(info);
    }
	
	protected static int computeDimension(ResultPoint topLeft,
            ResultPoint topRight,
            ResultPoint bottomLeft,
            float moduleSize) throws NotFoundException {
		int tltrCentersDimension = MathUtils.round(ResultPoint.distance(topLeft, topRight) / moduleSize);
		int tlblCentersDimension = MathUtils.round(ResultPoint.distance(topLeft, bottomLeft) / moduleSize);
		int dimension = ((tltrCentersDimension + tlblCentersDimension) / 2) + 7;
		switch (dimension & 0x03) { // mod 4
		case 0:
			dimension++;
		break;
		// 1? do nothing
		case 2:
			dimension--;
		break;
		case 3:
			dimension-=2;
			// why throw exception，这里不知道为什么，源码要抛出异常，我发现改成 dimension-=2;也可以，原理没有看懂
			//throw NotFoundException.getNotFoundInstance();
		}
		return dimension;
	}

	protected ColorfulDetectorResult processColorfulFinderPatternInfo(FinderPatternInfo info)
		      throws NotFoundException, FormatException {

	    FinderPattern topLeft = info.getTopLeft();
	    FinderPattern topRight = info.getTopRight();
	    FinderPattern bottomLeft = info.getBottomLeft();
	
	    float moduleSize = calculateModuleSize(topLeft, topRight, bottomLeft);
	    if (moduleSize < 1.0f) {
	      throw NotFoundException.getNotFoundInstance();
	    }
	    int dimension = computeDimension(topLeft, topRight, bottomLeft, moduleSize);
	    Version provisionalVersion = Version.getProvisionalVersionForDimension(dimension);
	    int modulesBetweenFPCenters = provisionalVersion.getDimensionForVersion() - 7;
	
	    AlignmentPattern alignmentPattern = null;
	    // Anything above version 1 has an alignment pattern
	    if (provisionalVersion.getAlignmentPatternCenters().length > 0) {
	
	      // Guess where a "bottom right" finder pattern would have been
	      float bottomRightX = topRight.getX() - topLeft.getX() + bottomLeft.getX();
	      float bottomRightY = topRight.getY() - topLeft.getY() + bottomLeft.getY();
	
	      // Estimate that alignment pattern is closer by 3 modules
	      // from "bottom right" to known top left location
	      float correctionToTopLeft = 1.0f - 3.0f / modulesBetweenFPCenters;
	      int estAlignmentX = (int) (topLeft.getX() + correctionToTopLeft * (bottomRightX - topLeft.getX()));
	      int estAlignmentY = (int) (topLeft.getY() + correctionToTopLeft * (bottomRightY - topLeft.getY()));
	
	      // Kind of arbitrary -- expand search radius before giving up
	      for (int i = 4; i <= 16; i <<= 1) {
	        try {
	          alignmentPattern = findAlignmentInRegion(moduleSize,
	              estAlignmentX,
	              estAlignmentY,
	              i);
	          break;
	        } catch (NotFoundException re) {
	          // try next round
	        }
	      }
	      // If we didn't find alignment pattern... well try anyway without it
	    }
	
	    PerspectiveTransform transform = null;
	    try {
		    Method method = Detector.class.getDeclaredMethod("createTransform", ResultPoint.class,ResultPoint.class,ResultPoint.class,ResultPoint.class,int.class);
		    method.setAccessible(true);
		    transform = (PerspectiveTransform) method.invoke(this, topLeft, topRight, bottomLeft, alignmentPattern, dimension);
//		    PerspectiveTransform transform = createTransform(topLeft, topRight, bottomLeft, alignmentPattern, dimension);
	    }catch(Exception ex) {
	    	ex.printStackTrace();
	    	throw NotFoundException.getNotFoundInstance();
	    }
	    
	    // 生成3个BitMatrix
	    List<BitMatrix> bits = colorfulSampleGrid(getImage(), transform, dimension);
	    
	    ResultPoint[] points;
	    if (alignmentPattern == null) {
	      points = new ResultPoint[]{bottomLeft, topLeft, topRight};
	    } else {
	      points = new ResultPoint[]{bottomLeft, topLeft, topRight, alignmentPattern};
	    }
	    return new ColorfulDetectorResult(bits, points);
	  }
	
	protected List<BitMatrix> colorfulSampleGrid(BitMatrix image,
            PerspectiveTransform transform,
            int dimension) throws NotFoundException {

	ColorfulGridSampler sampler = new ColorfulGridSampler(originalBI);
	return sampler.sampleColorfulGrid(image, dimension, dimension, transform);
	}
}
