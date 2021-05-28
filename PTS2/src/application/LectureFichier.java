package traitement_fichier;

import java.io.*;

public class LectureFichier {

	public static void main(String[] args) {

		//initialisation variable
        DataInputStream in;
        String texte=null;
        
        try {
            //Nouveau flot entrant
            in = new DataInputStream(
                    new BufferedInputStream(
                        new FileInputStream("Fichier.dat"))); //ouverture fichier + Ajouter le choix nom du fichier
            texte=in.readUTF(); //Remplissage variable du texte
            System.out.println(texte); // A adapter pour afficher le texte dans le cadre voulu et crypté
        } catch (IOException ioe) {
            System.err.println(ioe);
            System.exit(1);
        }
		
	}

}
