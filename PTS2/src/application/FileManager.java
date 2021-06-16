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

	public void sauvegarderFichier(String exoName, String path, String consigne, String script, List<String> aide,
			String pathToMedia, String occultationChar, String mode, int authorizeIncompleteBooleanWord,
			int numberOfLetterThatCanBeDiscover, int displayFoundWords, int displayFinalAnswer, int TimeLimitInMinute) throws IOException {
		// Initialisation variable
		Scanner scanner = new Scanner(System.in);
		final int BUFFERSIZE = 4 * 1024;
		int bytesRead;

		String sourceFilePath = pathToMedia.replace("%20", " ").substring(6);
		String outputFilePath = path + "\\" + exoName;
		int tailleVideo = 0;

		// Ecriture de la vidéo dans un fichier tampon
				FileInputStream fin = new FileInputStream(new File(sourceFilePath));
				FileOutputStream fout = new FileOutputStream(new File("fichierTampon.bin"));

			byte[] buffer = new byte[BUFFERSIZE];

			while (fin.available() != 0) {
				bytesRead = fin.read(buffer);
				++tailleVideo;
				fout.write(buffer, 0, bytesRead);
			}


		// Ecriture du fichier exo
				FileInputStream finF = new FileInputStream(new File("fichierTampon.bin"));
				FileOutputStream foutF = new FileOutputStream(new File(outputFilePath + ".exo"));

			byte[] bufferF = new byte[BUFFERSIZE];

			foutF.write(String.valueOf(tailleVideo).getBytes()); // Ecriture taille video
			foutF.write(0x00);
			foutF.write(String.valueOf(exoName.length()).getBytes()); // Ecriture taille texte
			foutF.write(0x00);
			foutF.write(String.valueOf(consigne.length()).getBytes()); // Ecriture taille consigne
			foutF.write(0x00);
			foutF.write(String.valueOf(script.length()).getBytes()); // Ecriture taille script
			foutF.write(0x00);
			foutF.write(String.valueOf(aide.toString().length()).getBytes()); // Ecriture taille aide
			foutF.write(0x00);
			foutF.write(String.valueOf(occultationChar.length()).getBytes()); // Ecriture taille char d'occultation
			foutF.write(0x00);
			foutF.write(String.valueOf(mode.length()).getBytes()); // Ecriture taille mode
			foutF.write(0x00);
			foutF.write(String.valueOf(authorizeIncompleteBooleanWord).getBytes()); // Ecriture taille autoriser mot
																					// incomplet
			foutF.write(0x00);
			foutF.write(String.valueOf(numberOfLetterThatCanBeDiscover).getBytes()); // Ecriture taille nombre lettres
																					// nécéssaire pour découvrir mot
			foutF.write(0x00);
			foutF.write(String.valueOf(displayFoundWords).getBytes()); // Ecriture taille displayFoundWord
			foutF.write(0x00);
			foutF.write(String.valueOf(displayFinalAnswer).getBytes()); // Ecriture taille displayAnswer
			foutF.write(0x00);
			foutF.write(String.valueOf(TimeLimitInMinute).getBytes()); // Ecriture taille TimeLimit

			while (finF.available() != 0) { // Ecriture de la vidéo depuis le fichier tampon
				bytesRead = finF.read(bufferF);
				foutF.write(bufferF, 0, bytesRead);
			}
			foutF.write(exoName.getBytes()); // Ecriture du texte
			foutF.write(consigne.getBytes()); // Ecriture du texte
			foutF.write(script.getBytes()); // Ecriture du texte
			foutF.write(aide.toString().getBytes()); // Ecriture du texte
			foutF.write(occultationChar.getBytes()); // Ecriture du texte
			foutF.write(mode.getBytes()); // Ecriture du texte
			foutF.write(authorizeIncompleteBooleanWord); // Ecriture du texte
			foutF.write(numberOfLetterThatCanBeDiscover); // Ecriture du texte
			foutF.write(displayFoundWords); // Ecriture du texte
			foutF.write(displayFinalAnswer); // Ecriture du texte
			foutF.write(TimeLimitInMinute); // Ecriture du texte
			
//			System.out.println("Fichier écrit sans problème : ");


	}

}
