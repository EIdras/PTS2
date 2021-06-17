package application;

import java.io.*;

public class FileManager {
	public void sauvegarderFichier(String exoName, String path, String consigne, String script, String aide,
			String pathToMedia, String occultationChar, String mode, int authorizeIncompleteBooleanWord,
			int numberOfLetterThatCanBeDiscover, int displayFoundWords, int displayFinalAnswer, int TimeLimitInMinute) {
		// Initialisation variable
		final int BUFFERSIZE = 4 * 1024;
		int bytesRead;

		String sourceFilePath = pathToMedia.replace("%20", " ").substring(6);
		System.out.println(sourceFilePath);
		String outputFilePath = path + "\\" + exoName;
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
				FileOutputStream fout = new FileOutputStream(new File(outputFilePath + ".exo"));) {

			byte[] buffer = new byte[BUFFERSIZE];
			if (exoName.equals("")) {
				exoName = "Exo";
			}
			if (consigne.equals("")) {
				consigne = "consigne";
			}
			if (script.equals("")) {
				script = "script";
			}

			fout.write((sourceFilePath.charAt(sourceFilePath.length() - 1) + "").getBytes());
			fout.write(String.valueOf(tailleVideo).getBytes()); // Ecriture taille video
			fout.write(0x00);
			fout.write(String.valueOf(exoName.length()).getBytes()); // Ecriture taille texte
			fout.write(0x00);
			fout.write(String.valueOf(consigne.length()).getBytes()); // Ecriture taille consigne
			fout.write(0x00);
			fout.write(String.valueOf(script.length()).getBytes()); // Ecriture taille script
			fout.write(0x00);
			fout.write(String.valueOf(aide.length()).getBytes()); // Ecriture taille aide
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
			fout.write(0x00);

			while (fin.available() != 0) { // Ecriture de la vidéo depuis le fichier tampon
				bytesRead = fin.read(buffer);
				fout.write(buffer, 0, bytesRead);
			}
			fout.write(exoName.getBytes());
			fout.write(consigne.getBytes());
			fout.write(script.getBytes());
			fout.write(aide.getBytes());
			fout.write(occultationChar.getBytes());
			fout.write(mode.getBytes());
			fout.write(authorizeIncompleteBooleanWord);
			fout.write(numberOfLetterThatCanBeDiscover);
			fout.write(displayFoundWords);
			fout.write(displayFinalAnswer);
			fout.write(TimeLimitInMinute);

//			System.out.println("Fichier écrit sans problème : ");
		} catch (Exception e) {
			System.out.println("Something went wrong! Reason: " + e.getMessage());
		}

	}

}