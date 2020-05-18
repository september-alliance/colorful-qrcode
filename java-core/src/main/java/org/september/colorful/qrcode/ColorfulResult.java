package org.september.colorful.qrcode;

import java.util.ArrayList;
import java.util.List;

import com.google.zxing.Result;

public class ColorfulResult {

	private List<Result> innerResults = new ArrayList<>();

	public List<Result> getInnerResults() {
		return innerResults;
	}

	public void setInnerResults(List<Result> innerResults) {
		this.innerResults = innerResults;
	}
	
	
	public String getMergedResult() {
		if(innerResults.size()==1) {
			return innerResults.get(0).getText();
		}
		String resultR = innerResults.get(0).getText();
		String resultG = innerResults.get(1).getText();
		String resultB = innerResults.get(2).getText();
		if(resultR.equals(resultG) && resultG.equals(resultB)) {
			return resultR;
		}
		StringBuilder sb = new StringBuilder();
		int rIndex = 0;
		int gIndex = 0;
		int bIndex = 0;
		while(rIndex<resultR.length() || gIndex<resultG.length() || bIndex<resultB.length()) {
			if(rIndex<resultR.length()) {
				sb.append(resultR.charAt(rIndex));
				rIndex++;
			}
			if(gIndex<resultG.length()) {
				sb.append(resultG.charAt(gIndex));
				gIndex++;
			}
			if(bIndex<resultB.length()) {
				sb.append(resultB.charAt(bIndex));
				bIndex++;
			}
		}
		return sb.toString();
	}
}
