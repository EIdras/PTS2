package traitement_fichier;
import java.io.*;

public class EcritureFichier {

	public static void main(String[] args) {
		
		//Initialisation variable
        DataOutputStream out;
        String texte = "test";
         
        try {
            //Nouveau flot sortant
            out = new DataOutputStream(
                    new BufferedOutputStream(
                        new FileOutputStream("Fichier.dat"))); //ouverture fichier
            texte=Clavier.lireLigne(); // A adapter pour lire le texte du prof
            out.writeUTF( texte ); //ecriture dans le fichier
            out.close();
        } catch (IOException ioe) {
            System.err.println(ioe);
            System.exit(1);
        }
	}

}
