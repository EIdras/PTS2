package application;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Hidden {

	static String original = "I think the cat is there.You're 90 years old.";
	static String hiddenChar = "#";
	static int words = 0;
	static int answers = 0;
	static boolean block = false;
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		Set<String> revealed = new HashSet<>();
		System.out.println(buildHint(revealed));
		while (answers != words) {
			String answer = scanner.next();
			answer = cleanWord(answer);
			revealed.add(answer.toLowerCase());
			System.out.println(buildHint(revealed));
		}
	}

	public static String cleanWord(String word) {
		return word.replaceAll("[^A-Za-z0-9]", "");
	}

	public static String buildHint(Set<String> revealed) {
		final StringBuilder builder = new StringBuilder();
		answers = 0;

		// Iterate through each word of the original text
		for (final String w : original.split("\\s+")) {

			// remove any non-alphabetical characters
			// and convert to lower case
			final String clean = cleanWord(w).toLowerCase();

			// Add a space if there is already text in the output.
			// This ensures that words do not hang on each other
			if (builder.length() > 0) {
				builder.append(' ');
			}

			// Check if the clean word was already guessed
			if (revealed.contains(clean)) {

				// append original word
				builder.append(w);
				answers++;
			} else {

				// append '#' by replacing every alphabetical character to '#' using RegEx
				builder.append(w.replaceAll("[A-Za-z0-9]", hiddenChar));
				if (!block) {
					words++;
				}
			}
		}
		block = true;
		return builder.toString();
	}
}