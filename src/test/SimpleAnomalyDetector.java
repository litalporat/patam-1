package test;

import java.util.ArrayList;
import java.util.List;
import static test.StatLib.*;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	final double THRESHOLD = 0.9;
	final float OFFSET = (float)1.1;
	public List<CorrelatedFeatures> corList;

	public SimpleAnomalyDetector() {
		this.corList = new ArrayList<>();
	}

	@Override
	public void learnNormal(TimeSeries ts) {
		String[] feature_keys = ts.getAllFeatures();
		for (int i = 0; i < feature_keys.length; i++) {
			float m = 0;
			int c = -1;
			for (int j = i + 1; j < feature_keys.length; j++) {
				float p = Math.abs(pearson(ts.getFeatureData(feature_keys[i]),ts.getFeatureData(feature_keys[j])));
				if (p > m) {
					m = p;
					c = j;
				}
			}
			if (c != -1 && m > THRESHOLD) {
				String f1 = feature_keys[i];
				String f2 = feature_keys[c];
				Point[] myPoints = ts.createPoints(feature_keys[i],feature_keys[c]);
				Line linearReg = linear_reg(myPoints);
				float maxDev = MaxDev(myPoints, linearReg);
				corList.add( new CorrelatedFeatures(f1, f2, m, linearReg,maxDev * OFFSET));
			}
		}
	}

	private float MaxDev(Point[] ps,Line l){
		float max = 0;
		for (Point p : ps){
			float d = dev(p,l);
			if (d > max)
				max = d;
		}
		return max;
	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		List<AnomalyReport> report = new ArrayList<>();
		for (CorrelatedFeatures cor: corList) {
			String fr1 = cor.feature1;
			String fr2 = cor.feature2;
			float[] xPoints = ts.getFeatureData(fr1);
			float[] yPoints = ts.getFeatureData(fr2);

			for (int i = 0; i < xPoints.length; i++) {
				Point p = new Point(xPoints[i], yPoints[i]);
				float dev = dev(p, cor.lin_reg);
				if (dev > cor.threshold) {
					report.add(new AnomalyReport(fr1 + "-" + fr2, i + 1));
				}
			}
		}
		return report;
	}
	
	public List<CorrelatedFeatures> getNormalModel(){
		return corList;
	}
}
