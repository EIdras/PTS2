package etu;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileManager {
	public static void ouvrirfichier(String path) {
		System.out.println(path);
		// Initialisation variable
		final int BUFFERSIZE = 4 * 1024;
		int bytesRead = 0;

		// Choix fichier d'entrée et sortie
		String sourceFilePath = path;
		String outputFilePath = "videoExo.mp4";

		try ( // Ecriture de la vidéo dans un fichier tampon
				FileInputStream fin = new FileInputStream(new File(sourceFilePath));
				FileOutputStream fout = new FileOutputStream(new File(outputFilePath));) {
			/*
			 * exoName consigne script aide occultationChar mode authorizeIncompleteWord,
			 * numberOfLetterThatCanBeDiscover displayFoundWords displayFinalAnswer
			 * TimeLimitInMinute
			 */
			byte[] buffer = new byte[BUFFERSIZE];

			Integer videoLength = null, exoNameLength = null, consigneLength = null, scriptLength = null,
					aideLength = null, occultationCharLength = null, modeLength = null, incompleteWordLength = null,
					numberofLetterLengthInteger = null, foundWordsLengthInteger = null, finalAnswer = null,
					timelimitLength = null, offset = 0;
			ArrayList<Byte> tempByteList = new ArrayList<Byte>();

			int inbuff;
			byte bit;
			int i = 0;
			while (i != 12) {
				inbuff = fin.read();
				bit = (byte) inbuff;
				if (bit != 0x00) {
					tempByteList.add(bit);
				} else {
					if (videoLength == null) {
						videoLength = getIntValueOfByteList(tempByteList);
						System.out.println("longeur de la vidéo : " + getIntValueOfByteList(tempByteList));
						offset += tempByteList.size();
						tempByteList.clear();
						++i;
					} else if (exoNameLength == null) {
						System.out.println("exoNameLength trouvé");
						exoNameLength = getIntValueOfByteList(tempByteList);
						offset += tempByteList.size();
						tempByteList.clear();
						++i;

					} else if (consigneLength == null) {
						System.out.println("consigneLength trouvé");
						consigneLength = getIntValueOfByteList(tempByteList);
						offset += tempByteList.size();
						tempByteList.clear();
						++i;

					} else if (scriptLength == null) {
						scriptLength = getIntValueOfByteList(tempByteList);
						System.out.println("longeur du texte : " + getIntValueOfByteList(tempByteList));
						offset += tempByteList.size();
						tempByteList.clear();
						++i;

					} else if (aideLength == null) {
						aideLength = getIntValueOfByteList(tempByteList);
						offset += tempByteList.size();
						tempByteList.clear();
						++i;

					} else if (occultationCharLength == null) {
						occultationCharLength = getIntValueOfByteList(tempByteList);
						offset += tempByteList.size();
						tempByteList.clear();
						++i;
					} else if (modeLength == null) {
						modeLength = getIntValueOfByteList(tempByteList);
						offset += tempByteList.size();
						tempByteList.clear();
						++i;
					} else if (incompleteWordLength == null) {
						incompleteWordLength = getIntValueOfByteList(tempByteList);
						offset += tempByteList.size();
						tempByteList.clear();
						++i;
					} else if (numberofLetterLengthInteger == null) {
						numberofLetterLengthInteger = getIntValueOfByteList(tempByteList);
						offset += tempByteList.size();
						tempByteList.clear();
						++i;
					} else if (foundWordsLengthInteger == null) {
						foundWordsLengthInteger = getIntValueOfByteList(tempByteList);
						offset += tempByteList.size();
						tempByteList.clear();
						++i;
					} else if (finalAnswer == null) {
						finalAnswer = getIntValueOfByteList(tempByteList);
						offset += tempByteList.size();
						tempByteList.clear();
						++i;
					} else if (timelimitLength == null) {
						timelimitLength = getIntValueOfByteList(tempByteList);
						offset += tempByteList.size();
						tempByteList.clear();
						++i;
					}
				}
			}

			System.out.println(videoLength);
			System.out.println(scriptLength);
			for (i = 0; i < videoLength; ++i) {
				bytesRead = fin.read(buffer);
				fout.write(buffer, 0, bytesRead);
			}

			ArrayList<Byte> texteByteList = new ArrayList<Byte>();
			inbuff = 0;
			bit = '\0';

			for (i = 0; i < scriptLength; i++) {
				inbuff = fin.read(buffer);
				bit = (byte) inbuff;
				System.out.println(bit);
				texteByteList.add(bit);
			}
			
			System.out.println(texteByteList.toString());

			String texte = beautifyToStringArrays(texteByteList);
			System.out.println(texte);

		} catch (Exception e) {
			System.out.println("Something went wrong! Reason: " + e.getMessage());
			e.printStackTrace();

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
			int numberOfLetterThatCanBeDiscover, int displayFoundWords, int displayFinalAnswer, int TimeLimitInMinute) {
		// Initialisation variable
		Scanner scanner = new Scanner(System.in);
		final int BUFFERSIZE = 4 * 1024;
		int bytesRead;

		String sourceFilePath = pathToMedia.replace("%20", " ").substring(6);
		String outputFilePath = path + "\\" + exoName;
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
			if (mode == "entrainement") {
				fout.write(authorizeIncompleteBooleanWord); // Ecriture du texte
				fout.write(numberOfLetterThatCanBeDiscover); // Ecriture du texte
				fout.write(displayFoundWords); // Ecriture du texte
				fout.write(displayFinalAnswer); // Ecriture du texte
			} else {
				fout.write(TimeLimitInMinute); // Ecriture du texte
			}

//			System.out.println("Fichier écrit sans problème : ");
		} catch (Exception e) {
			System.out.println("Something went wrong! Reason: " + e.getMessage());
		}

	}

	public static void main(String[] args) {
		ouvrirfichier("C:\\Users\\Maxime\\Desktop\\exercice.exo");
	}
}
