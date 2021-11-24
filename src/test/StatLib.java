package test;


public class StatLib {

	// simple average
	public static float avg(float[] x){
		float sum = 0;
		for (float number: x)
			sum += number;

		return sum / x.length;
	}

	// returns the variance of X and Y
	public static float var(float[] x){
		float u = avg(x);
		float result = 0;
		for (float number: x)
			result += Math.pow(number,2);
		result = (result / x.length) - (float)(Math.pow(u, 2));

		return result;
	}

	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y){
		float avgX = avg(x);
		float avgY = avg(y);
		float count = 0;
		for( int i = 0; i < x.length; i++)
			count += ((x[i] - avgX) * (y[i] - avgY));

		return (count / x.length);
	}


	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		float Sx = (float)Math.sqrt(var(x));
		float Sy = (float)Math.sqrt(var(y));

		return cov(x,y) / (Sx * Sy);
	}

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		float a, b, avgX, avgY;
		float[] arrX = new float[points.length];
		float[] arrY = new float[points.length];

		for (int i = 0; i < points.length; i++) {
			arrX[i] = points[i].x;
			arrY[i] = points[i].y;
		}

		a = cov(arrX, arrY) / var(arrX);
		avgX = avg(arrX);
		avgY = avg(arrY);
		b = avgY - (a * avgX);
		return new Line(a,b);
	}

	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p, Point[] points){
		float deviation;
		Line temp = linear_reg(points);
		deviation = dev(p,temp);

		return deviation;
	}

	// returns the deviation between point p and the line
	public static float dev(Point p, Line l){
		return Math.abs(l.f(p.x)-p.y);
	}
	
}
