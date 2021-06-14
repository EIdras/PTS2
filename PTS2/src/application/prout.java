package application;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class prout {

	public static void ouvrirfichier() {
		// Initialisation variable
		Scanner scanner = new Scanner(System.in);
		final int BUFFERSIZE = 4 * 1024;
		int bytesRead = 0;

		// Choix fichier d'entrée et sortie
		System.out.println("Entrez le nom du fichier de l'exercice");
		String sourceFilePath = scanner.nextLine();
		String outputFilePath = "video.mp4";

		try ( // Ecriture de la vidéo dans un fichier tampon
				FileInputStream fin = new FileInputStream(new File(sourceFilePath + ".exo"));
				FileOutputStream fout = new FileOutputStream(new File(outputFilePath));) {

			byte[] buffer = new byte[BUFFERSIZE];

			Integer videoLength = null, textLength = null;
			ArrayList<Byte> tempByteList = new ArrayList<Byte>();

			int inbuff;
			byte bit;
			while ((inbuff = fin.read()) != -1) {
				bit = (byte) inbuff;
				if (bit != 0x00) {
					tempByteList.add(bit);
				} else {
					if (videoLength == null) {
						videoLength = getIntValueOfByteList(tempByteList);
						System.out.println(getIntValueOfByteList(tempByteList));
						tempByteList.clear();
					} else if (textLength == null) {
						textLength = getIntValueOfByteList(tempByteList);
						System.out.println(getIntValueOfByteList(tempByteList));
						tempByteList.clear();
					}
				}
			}

			System.out.println(videoLength);
			System.out.println(textLength);
		} catch (Exception e) {
			System.out.println("Something went wrong! Reason: " + e.getMessage());

		}

	}

	private static int getIntValueOfByteList(ArrayList<Byte> array) {
		int n = array.size();
		byte[] out = new byte[n];
		for (int i = 0; i < n; i++) {
			out[i] = array.get(i);
		}

		return Integer.parseInt(new String(out, StandardCharsets.UTF_8));
	}

	public static void main(String[] args) {
		ouvrirfichier();
	}
}
