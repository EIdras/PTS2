package application;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileManager {
	public void ouvrirfichier() {
		// Initialisation variable
		Scanner scanner = new Scanner(System.in);
		final int BUFFERSIZE = 4 * 1024;
		int bytesRead = 0;

		// Choix fichier d'entrée et sortie
		System.out.println("Entrez le nom du fichier de l'exercice");
		String sourceFilePath = scanner.nextLine();
		String outputFilePath = "videoExo.mp4";

		try ( // Ecriture de la vidéo dans un fichier tampon
				FileInputStream fin = new FileInputStream(new File(sourceFilePath + ".exo"));
				FileOutputStream fout = new FileOutputStream(new File(outputFilePath));) {

			byte[] buffer = new byte[BUFFERSIZE];

			Integer videoLength = null, textLength = null;
			ArrayList<Byte> tempByteList = new ArrayList<Byte>();

			int inbuff;
			byte bit;
			int i = 0;
			while (i != 2) {
				inbuff = fin.read();
				bit = (byte) inbuff;
				if (bit != 0x00) {
					tempByteList.add(bit);
				} else {
					if (videoLength == null) {
						videoLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (textLength == null) {
						textLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					}
				}
			}

			System.out.println(videoLength);
			System.out.println(textLength);

			for (i = 0; i < videoLength; ++i) {
				bytesRead = fin.read(buffer);
				fout.write(buffer, 0, bytesRead);
			}

			ArrayList<Byte> texteByteList = new ArrayList<Byte>();
			inbuff = 0;
			bit = '\0';

			for (i = 0; i < textLength; i++) {
				inbuff = fin.read();
				bit = (byte) inbuff;
				texteByteList.add(bit);
			}

			String texte = beautifyToStringArrays(texteByteList);
			System.out.println(texte);

		} catch (Exception e) {
			System.out.println("Something went wrong! Reason: " + e.getMessage());

		}
	}

	private static String beautifyToStringArrays(ArrayList<Byte> array) {
		String toReturn = "";
		for (Byte temp : array) {
			toReturn += String.valueOf(temp);
		}
		return toReturn;
	}

	private static int getIntValueOfByteList(ArrayList<Byte> array) {
		int n = array.size();
		byte[] out = new byte[n];
		for (int i = 0; i < n; i++) {
			out[i] = array.get(i);
		}

		return Integer.parseInt(new String(out, StandardCharsets.UTF_8));
	}

	public void sauvegarderFichier(String exoName, String path, String consigne, String script, ArrayList<String> aide,
			String pathToMedia, String occultationChar, String mode, int authorizeIncompleteBooleanWord,
			int numberOfLetterThatCanBeDiscover, int displayFoundWords, int displayFinalAnswer, int TimeLimitInMinute) {
		// Initialisation variable
		Scanner scanner = new Scanner(System.in);
		final int BUFFERSIZE = 4 * 1024;
		int bytesRead;

		String sourceFilePath = pathToMedia;
		String outputFilePath = exoName;
		int tailleVideo = 0;

		try ( // Ecriture de la vidéo dans un fichier tampon
				FileInputStream fin = new FileInputStream(new File(sourceFilePath));
				FileOutputStream fout = new FileOutputStream(new File("fichierTampon.bin"));) {

			byte[] buffer = new byte[BUFFERSIZE];

			while (fin.available() != 0) {
				bytesRead = fin.read(buffer);
				++tailleVideo;
				fout.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			System.out.println("Something went wrong! Reason: " + e.getMessage());

		}

		try ( // Ecriture du fichier exo
				FileInputStream fin = new FileInputStream(new File("fichierTampon.bin"));
				FileOutputStream fout = new FileOutputStream(new File(outputFilePath + ".exo"));) {

			byte[] buffer = new byte[BUFFERSIZE];

			fout.write(String.valueOf(tailleVideo).getBytes()); // Ecriture taille video
			fout.write(0x00);
			fout.write(String.valueOf(exoName.length()).getBytes()); // Ecriture taille texte
			fout.write(0x00);
			fout.write(String.valueOf(consigne.length()).getBytes()); // Ecriture taille consigne
			fout.write(0x00);
			fout.write(String.valueOf(script.length()).getBytes()); // Ecriture taille script
			fout.write(0x00);
			fout.write(String.valueOf(aide.toString().length()).getBytes()); // Ecriture taille aide
			fout.write(0x00);
			fout.write(String.valueOf(occultationChar.length()).getBytes()); // Ecriture taille char d'occultation
			fout.write(0x00);
			fout.write(String.valueOf(mode.length()).getBytes()); // Ecriture taille mode
			fout.write(0x00);
			fout.write(String.valueOf(authorizeIncompleteBooleanWord).getBytes()); // Ecriture taille autoriser mot
																					// incomplet
			fout.write(0x00);
			fout.write(String.valueOf(numberOfLetterThatCanBeDiscover).getBytes()); // Ecriture taille nombre lettres
																					// nécéssaire pour découvrir mot
			fout.write(0x00);
			fout.write(String.valueOf(displayFoundWords).getBytes()); // Ecriture taille displayFoundWord
			fout.write(0x00);
			fout.write(String.valueOf(displayFinalAnswer).getBytes()); // Ecriture taille displayAnswer
			fout.write(0x00);
			fout.write(String.valueOf(TimeLimitInMinute).getBytes()); // Ecriture taille TimeLimit

			while (fin.available() != 0) { // Ecriture de la vidéo depuis le fichier tampon
				bytesRead = fin.read(buffer);
				fout.write(buffer, 0, bytesRead);
			}
			fout.write(exoName.getBytes()); // Ecriture du texte
			fout.write(consigne.getBytes()); // Ecriture du texte
			fout.write(script.getBytes()); // Ecriture du texte
			fout.write(aide.toString().getBytes()); // Ecriture du texte
			fout.write(occultationChar.getBytes()); // Ecriture du texte
			fout.write(mode.getBytes()); // Ecriture du texte
			fout.write(authorizeIncompleteBooleanWord); // Ecriture du texte
			fout.write(numberOfLetterThatCanBeDiscover); // Ecriture du texte
			fout.write(displayFoundWords); // Ecriture du texte
			fout.write(displayFinalAnswer); // Ecriture du texte
			fout.write(TimeLimitInMinute); // Ecriture du texte
		} catch (Exception e) {
			System.out.println("Something went wrong! Reason: " + e.getMessage());
		}

	}

}
