package controller;

public abstract class Reader {
	protected static boolean checkDirection(String str) {
		str = str.replace("Directed=", "");

		if (str.toLowerCase().equals("true")) {
			return true;
		}

		return false;
	}

	protected static boolean checkValue(String str) {
		str = str.replace("Valued=", "");

		if (str.toLowerCase().equals("true")) {
			return true;
		}

		return false;
	}
}
