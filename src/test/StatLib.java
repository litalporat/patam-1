package test;
import java.lang.Math;

public class StatLib {

	

	// simple average
	public static float avg(float[] x){
		float sum=0;
		for(int i=0; i<x.length; i++)
			sum+=x[i];
		return (sum/x.length);
	}

	// returns the variance of X and Y
	public static float var(float[] x){
		float u = avg(x);
		float sumSquare=0, sum=0;

		for(int i = 0; i<x.length ;i++){
			sumSquare+= x[i] * x[i];
		}

		return sumSquare / x.length - (u * u);
	}

	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y){
		//float comboAvg=(avg(x)+avg(y))/2;
		int size = x.length+ y.length;
		float[] combo = new float[size];
		for (int i=0; i<size; i++){
			if (i>=x.length)
				combo[i]=y[i-x.length];
			else
				combo[i]=x[i];
		}
		float comboAvg = avg(combo);
		float result=comboAvg-avg(x)*avg(y);
		return result;
	}

	// a test

	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		float above =cov(x,y);
		double below =Math.sqrt(var(x))* Math.sqrt(var(y));
		return above/(float)below;
	}

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		float[] x = new float[points.length];
		float[] y = new float[points.length];
		for (int i=0; i< points.length;i++){
			x[i] = points[i].x;
			y[i] = points[i].y;

		}
		float a = cov(x,y);
		float avgX = avg(x), avgY=avg(y);
		Line myL = new Line(a,avgY -a*avgX);
		return myL;
	}

	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p,Point[] points){
		Line myL = linear_reg(points);
		return dev(p,myL);
	}

	// returns the deviation between point p and the line
	public static float dev(Point p,Line l){
		float fx = l.f(p.x);
		float result = fx - p.y;
		if (result < 0)
			result*=-1;
		return result;
	}
	
}
