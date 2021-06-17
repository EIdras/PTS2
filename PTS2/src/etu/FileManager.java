package etu;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileManager {
	public static HashMap<String, Object> ouvrirfichier(String path) {
		// Initialisation variable

		final int BUFFERSIZE = 4 * 1024;
		int bytesRead = 0, extensionMedia = 0;
		File /* fichierTampon = null, */ videoFile = null;

		try ( // Ecriture de la vidéo dans un fichier tampon
				FileInputStream fin = new FileInputStream(new File(path));) {

			byte[] buffer = new byte[BUFFERSIZE];
			fin.read(buffer, 0, 1);
			ArrayList<Byte> tempArrayList = new ArrayList<Byte>();
			tempArrayList.add(buffer[0]);
			extensionMedia = getIntValueOfByteList(tempArrayList);
		} catch (Exception e) {
			System.out.println("Something went wrong! Reason: " + e.getMessage());

		}
		
		try {
//			fichierTampon = File.createTempFile("tampon", ".tmp"); // Créé un fichier temporaire dans le
			// AppData/Local/Temp/
			videoFile = File.createTempFile("videoExo", ".mp"+extensionMedia);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String sourceFilePath = path;
		String outputFilePath = videoFile.getAbsolutePath();

		try ( // Ecriture de la vidéo dans un fichier tampon
				FileInputStream fin = new FileInputStream(new File(sourceFilePath));
				FileOutputStream fout = new FileOutputStream(new File(outputFilePath));) {

			byte[] buffer = new byte[BUFFERSIZE];

			Integer videoLength = null, exoNameLength = null, consigneLength = null, scriptLength = null,
					aideLength = null, occultationCharLength = null, modeLength = null, incompleteWordsLength = null,
					letterNumberInteger = null, foundWordsLength = null, finalAnswerLength = null,
					timeLimitLength = null;
			ArrayList<Byte> tempByteList = new ArrayList<Byte>();
			String exoName, consigne, script, aide, occultationChar, mode;
			int incompleteWords, letterNumber, foundWords, finalAnswer, timeLimit, extensionVideo;

			int inbuff;
			byte bit;
			int i = 0;
			fin.read(buffer, 0, 1); //permets de skip le premier bit (celui qui détermine si c'est un mp3 ou un mp4.
			while (i != 12) {
				inbuff = fin.read();
				bit = (byte) inbuff;
				if (bit != 0x00) {
					tempByteList.add(bit);
				} else {
					if (videoLength == null) {
						videoLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (exoNameLength == null) {
						exoNameLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (consigneLength == null) {
						consigneLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (scriptLength == null) {
						scriptLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (aideLength == null) {
						aideLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (occultationCharLength == null) {
						occultationCharLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (modeLength == null) {
						modeLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (incompleteWordsLength == null) {
						incompleteWordsLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (letterNumberInteger == null) {
						letterNumberInteger = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (foundWordsLength == null) {
						foundWordsLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (finalAnswerLength == null) {
						finalAnswerLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (timeLimitLength == null) {
						timeLimitLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					}

				}
			}

			for (i = 0; i < videoLength; i++) {
				bytesRead = fin.read(buffer, 0, Math.min(videoLength - i, BUFFERSIZE));
				i += BUFFERSIZE - 1;
				fout.write(buffer, 0, bytesRead);
			}

			tempByteList.clear();

			inbuff = 0;
			bit = 0x00;

			for (i = 0; i < exoNameLength; i++) {
				fin.read(buffer, 0, 1);
				bit = (byte) buffer[0];
				tempByteList.add(bit);
			}

			exoName = beautifyToStringArrays(tempByteList);
			tempByteList.clear();

			for (i = 0; i < consigneLength; i++) {
				fin.read(buffer, 0, 1);
				bit = (byte) buffer[0];
				tempByteList.add(bit);
			}

			consigne = beautifyToStringArrays(tempByteList);
			tempByteList.clear();

			for (i = 0; i < scriptLength; i++) {
				fin.read(buffer, 0, 1);
				bit = (byte) buffer[0];
				tempByteList.add(bit);
			}

			script = beautifyToStringArrays(tempByteList);
			tempByteList.clear();

			for (i = 0; i < aideLength; i++) {
				fin.read(buffer, 0, 1);
				bit = (byte) buffer[0];
				tempByteList.add(bit);
			}

			aide = beautifyToStringArrays(tempByteList);
			tempByteList.clear();

			for (i = 0; i < occultationCharLength; i++) {
				fin.read(buffer, 0, 1);
				bit = (byte) buffer[0];
				tempByteList.add(bit);
			}

			occultationChar = beautifyToStringArrays(tempByteList);
			tempByteList.clear();

			for (i = 0; i < modeLength; i++) {
				fin.read(buffer, 0, 1);
				bit = (byte) buffer[0];
				tempByteList.add(bit);
			}

			mode = beautifyToStringArrays(tempByteList);
			tempByteList.clear();

			// int incompleteWords, letterNumber, foundWords, finalAnswer, timeLimit;

			fin.read(buffer, 0, 1);
			incompleteWords = buffer[0];
			tempByteList.clear();

			fin.read(buffer, 0, 1);
			letterNumber = buffer[0];
			tempByteList.clear();

			fin.read(buffer, 0, 1);
			foundWords = buffer[0];
			tempByteList.clear();

			fin.read(buffer, 0, 1);
			finalAnswer = buffer[0];
			tempByteList.clear();

			if (timeLimitLength == 1) {
				fin.read(buffer, 0, 1);
				timeLimit = buffer[0];
			} else if (timeLimitLength > 1) {

				for (i = 0; i < timeLimitLength; i++) {
					bit = (byte) buffer[0];
					tempByteList.add(bit);
				}

				timeLimit = getIntValueOfByteList(tempByteList);
			} else {
				timeLimit = -1;
			}

			System.out.println("nom exo : " + exoName);
			System.out.println("consigne : " + consigne);
			System.out.println("script : " + script);
			System.out.println("aide : " + aide);
			System.out.println("caractère d'occultation : " + occultationChar);
			System.out.println("mode : " + mode);
			System.out.println("Autoriser les mots incomplets : " + (incompleteWords == 1 ? "oui" : "non"));
			System.out.println("Nombre de lettres pour découvrir un mot : " + letterNumber);
			System.out.println("Afficher les mots trouvés : " + (foundWords == 1 ? "oui" : "non"));
			System.out.println("Afficher la solution : " + (finalAnswer == 1 ? "oui" : "non"));
			System.out.println("TimeLimit : " + timeLimit + " minutes ");
			HashMap<String, Object> toReturn = new HashMap<String, Object>() {
				{
					put("exoName", exoName);
					put("consigne", consigne);
					put("script", script);
					put("aide", aide);
					put("occultChar", occultationChar);
					put("mode", mode);
					put("incompleteWords", incompleteWords);
					put("letterNumber", letterNumber);
					put("foundWords", foundWords);
					put("finalAnswer", finalAnswer);
					put("timeLimit", timeLimit);

				}
			};
			toReturn.put("mediaPath", videoFile.toURI().toString());
			return toReturn;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Something went wrong! Reason: " + e.getMessage());

		}
		return null;
	}

	private static String beautifyToStringArrays(ArrayList<Byte> array) {
		String toReturn = "";
		for (Byte temp : array) {
			toReturn += String.valueOf((char) (byte) temp);
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

	public static void sauvegarderFichier() {
		String texte = "prout putain de merde";
		// Initialisation variable
		final int BUFFERSIZE = 4 * 1024;
		int bytesRead;

		// Choix fichier d'entrée et sortie
		String sourceFilePath = "C:\\Users\\Maxime\\Downloads\\antoto.mp4";

		String outputFilePath = "C:\\Users\\Maxime\\Desktop\\.exo";
		int tailleVideo = 0;

		try ( // Ecriture de la vidéo dans un fichier tampon
				FileInputStream fin = new FileInputStream(new File(sourceFilePath));
				FileOutputStream fout = new FileOutputStream(new File("fichierTampon.bin"));) {

			byte[] buffer = new byte[BUFFERSIZE];

			while (fin.available() != 0) {
				bytesRead = fin.read(buffer);
				tailleVideo += bytesRead;
				fout.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			System.out.println("Something went wrong! Reason: " + e.getMessage());

		}

		try ( // Ecriture du fichier exo
				FileInputStream fin = new FileInputStream(new File("fichierTampon.bin"));
				FileOutputStream fout = new FileOutputStream(new File(outputFilePath));) {

			byte[] buffer = new byte[BUFFERSIZE];

			fout.write(String.valueOf(tailleVideo).getBytes()); // Ecriture taille video
			fout.write(0x00);
			int tailleTexte = texte.length();
			fout.write(String.valueOf(tailleTexte).getBytes()); // Ecriture taille texte
			fout.write(0x00);

			while (fin.available() != 0) { // Ecriture de la vidéo depuis le fichier tampon
				bytesRead = fin.read(buffer);
				fout.write(buffer, 0, bytesRead);
			}
			fout.write(texte.getBytes()); // Ecriture du texte
		} catch (Exception e) {
			System.out.println("Something went wrong! Reason: " + e.getMessage());
		}

	}

}
