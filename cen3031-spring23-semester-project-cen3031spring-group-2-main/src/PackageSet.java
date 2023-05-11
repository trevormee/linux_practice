/*
	PackageSet.java
	
	This class reads and stores ticket packages from a file.
	
	@author Reece Sellers
	@author Eli Turner
*/

package mainProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PackageSet {
	private String fileName;
	private static int size = 4;
	private static String[] packageNames = new String[size];
	private static double[] packagePrices = new double[size];
	private static String[] packageDescriptions = new String[size];
	BufferedReader br;

	public PackageSet() {
		fileName = "";
		int size = 0;
		packageNames = new String[size];
		packagePrices = new double[size];
		packageDescriptions = new String[size]; //
	}

	public PackageSet(String fileName, int size) {
		this.fileName = fileName;
		PackageSet.size = size;
		packageNames = new String[size];
		packagePrices = new double[size];
		packageDescriptions = new String[size];
	}

	public static int getSize() {
		return size;
	}

	public String getFileName() {
		return fileName;
	}

	public static String getPackageName(int index) {
		return packageNames[index];
	}

	public static double getPackagePrice(int index) {
		return packagePrices[index];
	}

	public static String getPackageDescription(int index) {
		return packageDescriptions[index];
	}

	public void LoadFile() throws IOException {
		br = new BufferedReader(new FileReader(fileName));
		String line;
		int i = 0;
		// Loop through and parse the file based off of ';' delimiter
		while ((line = br.readLine()) != null && i < size) {
			String[] parts = line.split(";");
			packageNames[i] = parts[0];
			packagePrices[i] = Double.parseDouble(parts[1]);
			packageDescriptions[i] = parts[2];
			i++;
		}
	}
}
