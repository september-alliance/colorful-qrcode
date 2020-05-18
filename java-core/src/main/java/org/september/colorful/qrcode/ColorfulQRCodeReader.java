package org.september.colorful.qrcode;

import java.util.List;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.decoder.QRCodeDecoderMetaData;

public class ColorfulQRCodeReader extends QRCodeReader{

	public ColorfulResult decode(BinaryBitmap image, Map<DecodeHintType,?> hints , ColoredImage sourceBI)
		      throws NotFoundException, ChecksumException, FormatException {
	    
	    ColorfulDetectorResult detectorResult = new ColorfulDetector(image.getBlackMatrix() , sourceBI).detectColorful(hints);
	    ColorfulResult colorfulResult = new ColorfulResult();
	    for(BitMatrix bits : detectorResult.getBitsList()) {
	    	DecoderResult decoderResult = getDecoder().decode(bits, hints);
		    ResultPoint[] points = detectorResult.getPoints();
		    // If the code was mirrored: swap the bottom-left and the top-right points.
		    if (decoderResult.getOther() instanceof QRCodeDecoderMetaData) {
		      ((QRCodeDecoderMetaData) decoderResult.getOther()).applyMirroredCorrection(points);
		    }

		    Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points, BarcodeFormat.QR_CODE);
		    List<byte[]> byteSegments = decoderResult.getByteSegments();
		    if (byteSegments != null) {
		      result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
		    }
		    String ecLevel = decoderResult.getECLevel();
		    if (ecLevel != null) {
		      result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
		    }
		    if (decoderResult.hasStructuredAppend()) {
		      result.putMetadata(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE,
		                         decoderResult.getStructuredAppendSequenceNumber());
		      result.putMetadata(ResultMetadataType.STRUCTURED_APPEND_PARITY,
		                         decoderResult.getStructuredAppendParity());
		    }
		    
		    colorfulResult.getInnerResults().add(result);
	    }

	    return colorfulResult;
	  }
	
}
