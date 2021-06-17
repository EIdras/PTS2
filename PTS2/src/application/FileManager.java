package application;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class FileManager {
	@SuppressWarnings("resource")
	public void sauvegarderFichier(String exoName, String path, String consigne, String script, String aide,
			String pathToMedia, String occultationChar, String mode, int authorizeIncompleteBooleanWord,
			int numberOfLetterThatCanBeDiscover, int displayFoundWords, int displayFinalAnswer, int TimeLimitInMinute)
			throws IOException {
		final int BUFFERSIZE = 4 * 1024;
		int bytesRead;
		File tamponFile = File.createTempFile(exoName, ".bin");

		String sourceFilePath = pathToMedia.replace("%20", " ").substring(6);
		System.out.println(sourceFilePath);
		String outputFilePath = path + "\\" + exoName;
		int tailleVideo = 0;

		// Écriture de la vidéo dans un fichier tampon
		FileInputStream fin = new FileInputStream(new File(sourceFilePath));
		FileOutputStream fout = new FileOutputStream(tamponFile);

		byte[] buffer = new byte[BUFFERSIZE];

		while (fin.available() != 0) {
			bytesRead = fin.read(buffer);
			tailleVideo += bytesRead;
			fout.write(buffer, 0, bytesRead);
		}

		// Ecriture du fichier exo
		FileInputStream finF = new FileInputStream(tamponFile);
		FileOutputStream foutF = new FileOutputStream(new File(outputFilePath + ".exo"));

		byte[] bufferF = new byte[BUFFERSIZE];

		foutF.write(String.valueOf(pathToMedia.charAt(pathToMedia.length() - 1)).getBytes());
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
																					// nï¿½cï¿½ssaire pour dï¿½couvrir
																					// mot
		foutF.write(0x00);
		foutF.write(String.valueOf(displayFoundWords).getBytes()); // Ecriture taille displayFoundWord
		foutF.write(0x00);
		foutF.write(String.valueOf(displayFinalAnswer).getBytes()); // Ecriture taille displayAnswer
		foutF.write(0x00);
		foutF.write(String.valueOf(TimeLimitInMinute).getBytes()); // Ecriture taille TimeLimit
		foutF.write(0x00);// ï¿½a c'est traitre
		while (finF.available() != 0) { // Ecriture de la vidï¿½o depuis le fichier tampon
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
		foutF.write(numberOfLetterThatCanBeDiscover);// Ecriture du texte
		foutF.write(displayFoundWords); // Ecriture du texte
		foutF.write(displayFinalAnswer);// Ecriture du texte
		foutF.write(TimeLimitInMinute); // Ecriture du texte

//			System.out.println("Fichier ï¿½crit sans problï¿½me : ");

	}

	@SuppressWarnings("serial")
	public HashMap<String, Object> ouvrirfichier(String path) {
		// Initialisation variable

		final int BUFFERSIZE = 4 * 1024;
		int bytesRead = 0, extensionMedia = 0;
		File videoFile = null;

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
			videoFile = File.createTempFile("videoExo", ".mp" + extensionMedia);
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
					foundScriptLength = null, aideLength = null, modeLength = null;
			ArrayList<Byte> tempByteList = new ArrayList<Byte>();
			String exoName, consigne, script, foundScript, aide, mode;

			int inbuff;
			byte bit;
			int i = 0;
			fin.read(buffer, 0, 1); // permets de skip le premier bit (celui qui détermine si c'est un mp3 ou un
									// mp4.
			while (i != 7) {
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
					} else if (foundScriptLength == null) {
						foundScriptLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (aideLength == null) {
						aideLength = getIntValueOfByteList(tempByteList);
						tempByteList.clear();
						++i;
					} else if (modeLength == null) {
						modeLength = getIntValueOfByteList(tempByteList);
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
			
			for (i = 0; i < foundScriptLength; i++) {
				fin.read(buffer, 0, 1);
				bit = (byte) buffer[0];
				tempByteList.add(bit);
			}

			foundScript = beautifyToStringArrays(tempByteList);
			tempByteList.clear();

			for (i = 0; i < aideLength; i++) {
				fin.read(buffer, 0, 1);
				bit = (byte) buffer[0];
				tempByteList.add(bit);
			}

			aide = beautifyToStringArrays(tempByteList);
			tempByteList.clear();

			for (i = 0; i < modeLength; i++) {
				fin.read(buffer, 0, 1);
				bit = (byte) buffer[0];
				tempByteList.add(bit);
			}

			mode = beautifyToStringArrays(tempByteList);
			tempByteList.clear();


			System.out.println("nom exo : " + exoName);
			System.out.println("consigne : " + consigne);
			System.out.println("script : " + script);
			System.out.println("aide : " + aide);
			System.out.println("mode : " + mode);
			
			HashMap<String, Object> toReturn = new HashMap<String, Object>() {
				{
					put("exoName", exoName);
					put("consigne", consigne);
					put("script", script);
					put("foundScript", foundScript);
					put("aide", aide);
					put("mode", mode);
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
}