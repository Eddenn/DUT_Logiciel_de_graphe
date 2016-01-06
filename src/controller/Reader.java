package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Graph;

public abstract class Reader {
	public static Graph read(String strFile) {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(strFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Scanner sc = new Scanner(br);
		
		ArrayList<String> alStr = new ArrayList<String>();
		
		while (sc.hasNextLine()) {
			alStr.add(sc.nextLine());
		}

		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String strFirstLine = alStr.get(0);
		alStr.remove(0);
		
		if (isMatrix(strFirstLine)) {
			return ReaderMatrix.readMatrix(alStr);
		}
				
		return ReaderAdjacencyList.ReadAdjacencyList(alStr);
	}
	
	private static boolean isMatrix(String str) {
		str = str.toLowerCase().replaceAll("ismatrix=", "");

		return (str.toLowerCase().equals("true"));
	}
	
	protected static boolean checkDirection(String str) {
		str = str.toLowerCase().replace("directed=", "");
		
		return (str.toLowerCase().equals("true"));
	}

	protected static boolean checkValue(String str) {
		str = str.toLowerCase().replace("valued=", "");

		return (str.toLowerCase().equals("true"));
	}
}
