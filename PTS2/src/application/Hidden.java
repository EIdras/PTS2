package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Hidden {

	static String original = "theorically this thing should work, but i'm not sure of that. You're, test 9999 # e-e";
	static String foundString = "";
	static String hiddenChar = "#";
	static String mode = "entrainement";
	static int NumberOfLettersRequiredToRevealWord = 3;
	static int words = 0;
	static int answers = 0;
	static boolean block = false;
	static Scanner scanner = new Scanner(System.in);
	static List<String> helpWords = new ArrayList<String>();

	/**
	 * Transform an Array as a beautiful text
	 * 
	 * @param array
	 * @return Array but as text
	 */
	private static String beautifyToStringArrays(String[] array) {
		String toReturn = "";
		for (String temp : array) {
			toReturn += temp + " ";
		}
		return toReturn;
	}

	private static String reformatSentence(String original, String other) {

		if (original.length() != other.length()) {
			System.out.println("\n");
			System.out.println(original);
			System.out.println(other);
			System.out.println("\n");
			throw new Error("Strings don't have the same size");
		} else {
			String[] splittedOriginal = original.split("");
			String[] splittedOther = other.split("");
			for (int i = 0; i < splittedOriginal.length; i++) {
				// System.out.println(splittedOriginal[i] + " | " + splittedOther[i]);
				if (splittedOriginal[i] != splittedOther[i] && !splittedOther[i].equals(hiddenChar)) {
					splittedOther[i] = splittedOriginal[i];
				}
			}
			String toReturn = "";
			for (String temp : splittedOther) {
				toReturn += temp;
			}
			return toReturn;
		}
	}

	public static void main(String[] args) {
		helpWords.add("theorically");
		helpWords.add("this");
		helpWords.add("that");
		helpWords.add("but");
		foundString = hideWord(original);
		System.out.println(foundString);
		while (!original.equals(foundString)) {
			String answer = scanner.next();
			answer = cleanWord(answer);
			foundString = tryUnmaskWord(answer);
			System.out.println(foundString);
		}
	}

	/**
	 * Remove everything that is not a letter or a '
	 * 
	 * @param word
	 * @return
	 */
	public static String cleanWord(String word) {
		return word.replaceAll("[^A-Za-z0-9'-]|\\.", "");
	}

	/**
	 * Transform everything that is a letter/number into an hidden char
	 * 
	 * @param sentence
	 * @return
	 */
	public static String hideWord(String sentence) {
		return sentence.replaceAll("[A-Za-z0-9]", hiddenChar);
	}

	public static String tryUnmaskWord(String word) {
		String[] splittedOriginal = original.split(" +|\n|,");
		String[] splittedFoundString = foundString.split(" +|\n|,");
		for (int i = 0; i < splittedOriginal.length; i++) {
			String subString = splittedOriginal[i];
			// System.out.println(word + " : " + subString);
			if ((cleanWord(subString).equalsIgnoreCase(word))) {
				int indexOfWordInOriginalString = original.indexOf(subString);
				char[] originalLetters = original.toCharArray();
				char[] foundLetters = foundString.toCharArray();
				for (int j = 0; j < original.length(); j++) {
					if (j >= indexOfWordInOriginalString && j < indexOfWordInOriginalString + subString.length()) {
						foundLetters[j] = originalLetters[j];
					}

				}

				foundString = String.copyValueOf(foundLetters);
			} else if (mode.equals("entrainement") && helpWords.contains(cleanWord(subString))) {

				if (word.length() == 2 && NumberOfLettersRequiredToRevealWord == 2
						&& cleanWord(subString).startsWith(word)) {
					char[] tempFoundLetters = splittedFoundString[i].toCharArray();
					// ";"
					for (int j = 0; j < 2; j++) {
						tempFoundLetters[j] = subString.toCharArray()[j];
					}
					splittedFoundString[i] = String.copyValueOf(tempFoundLetters);
//					foundString = beautifyToStringArrays(splittedFoundString);
					foundString = reformatSentence(original, beautifyToStringArrays(splittedFoundString).trim());

				} else if (word.length() == 3 && NumberOfLettersRequiredToRevealWord == 3
						&& cleanWord(subString).startsWith(word)) {
					char[] tempFoundLetters = splittedFoundString[i].toCharArray();
					// ";"
					for (int j = 0; j < 3; j++) {
						tempFoundLetters[j] = subString.toCharArray()[j];
					}
					splittedFoundString[i] = String.copyValueOf(tempFoundLetters);
					foundString = reformatSentence(original, beautifyToStringArrays(splittedFoundString).trim());
				}
			}
		}

		return foundString;
	}
}