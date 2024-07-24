package privategpt.process;

public class SimilarityCalculationProcess {
	public static double calculateSimilarity(String str1, String str2) {
		// Setzt beide Strings in Kleinbuchstaben
		str1 = str1.toLowerCase();
		str2 = str2.toLowerCase();

		// Berechnet die maximale Länge der beiden Strings
		int maxLength = Math.max(str1.length(), str2.length());
		if (maxLength == 0) {
			return 1.0; // Beide Strings sind leer
		}

		// Berechnet die Levenshtein-Distanz zwischen den beiden Strings
		int levenshteinDistance = calculateLevenshteinDistance(str1, str2);

		// Berechnet den Ähnlichkeitswert basierend auf der Levenshtein-Distanz
		return 1.0 - (double) levenshteinDistance / maxLength;
	}

	private static int calculateLevenshteinDistance(String str1, String str2) {
		int len1 = str1.length();
		int len2 = str2.length();

		// Erstellen der Abstandsmatrix
		int[][] distance = new int[len1 + 1][len2 + 1];

		// Initialisieren der ersten Spalte und der ersten Zeile
		for (int i = 0; i <= len1; i++) {
			distance[i][0] = i;
		}
		for (int j = 0; j <= len2; j++) {
			distance[0][j] = j;
		}

		// Füllen der Abstandsmatrix
		for (int i = 1; i <= len1; i++) {
			for (int j = 1; j <= len2; j++) {
				// Kosten berechnen: 0 wenn die Zeichen gleich sind, 1 wenn sie unterschiedlich sind
				int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
				distance[i][j] = Math.min(Math.min(distance[i - 1][j] + 1, // Löschen
						distance[i][j - 1] + 1), // Einfügen
						distance[i - 1][j - 1] + cost); // Ersetzen
			}
		}

		// Rückgabe der berechneten Levenshtein-Distanz
		return distance[len1][len2];
	}

}
