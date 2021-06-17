package application;

import java.io.*;

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
		String outputFilePath = path + "\\" + exoName;
		int tailleVideo = 0;

		// Ecriture de la vidéo dans un fichier tampon
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
																					// nécéssaire pour découvrir mot
		foutF.write(0x00);
		foutF.write(String.valueOf(displayFoundWords).getBytes()); // Ecriture taille displayFoundWord
		foutF.write(0x00);
		foutF.write(String.valueOf(displayFinalAnswer).getBytes()); // Ecriture taille displayAnswer
		foutF.write(0x00);
		foutF.write(String.valueOf(TimeLimitInMinute).getBytes()); // Ecriture taille TimeLimit
		foutF.write(0x00);//ça c'est traitre
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
		foutF.write(numberOfLetterThatCanBeDiscover);// Ecriture du texte
		foutF.write(displayFoundWords); // Ecriture du texte
		foutF.write(displayFinalAnswer);// Ecriture du texte
		foutF.write(TimeLimitInMinute); // Ecriture du texte

//			System.out.println("Fichier écrit sans problème : ");

	}

}
