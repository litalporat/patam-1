package test;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Set;

public class TimeSeries {
	private final HashMap<String,ArrayList<Float>> map;

	public TimeSeries(String csvFileName) {
		map = new HashMap<>();
		initMap(csvFileName);
	}

	public String[] getAllFeatures() {
		Set<String> keys = map.keySet();
		return keys.toArray(new String[0]);
	}

	public float[] getFeatureData(String key) {
		float[] arr = new float[map.get(key).size()];
		for (int i = 0; i < map.get(key).size(); i++) {
			arr[i] = map.get(key).get(i);
		}
		return arr;
	}

	private void initMap(String csvFileName) {
		Scanner s = null;
		try {
			s = new Scanner(new FileReader(csvFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (s != null) {
			String line = s.nextLine();
			String[] headers = line.split(",");
			for (String header: headers)
				map.put(header, new ArrayList<>());

			while (s.hasNextLine()) {
				line = s.nextLine();
				String[] row = line.split(",");
				int i=0;
				for (String h : map.keySet())  {
					map.get(h).add(Float.parseFloat(row[i]));
					i++;
				}
			}
		}
	}

	public Point[] createPoints(String f1, String f2) {
		float[] arr1 = getFeatureData(f1);
		float[] arr2 = getFeatureData(f2);
		Point[] myPoints = new Point[arr1.length];
		for (int i =0; i < arr1.length; i++)
			myPoints[i] = new Point(arr1[i],arr2[i]);
		return myPoints;
	}
}
