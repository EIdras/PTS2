package application;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SaveFileController extends ParentController implements Initializable{

	@FXML BorderPane bPane;
	@FXML Label nomExo_lbl, consigne_lbl, script_lbl, aide_lbl, media_lbl, occult_lbl, incomplet_lbl, affichageMots_lbl, solution_lbl, sauvegarde_lbl, mode_lbl, tempsLimite_lbl;
	private String nomExo, consigne, script, aide, media, sauvegarde, mode;
	private int incomplet, tempsLimite;
	private char occult;
	private boolean solution, affichageMots;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
	}
	
	@Override
	public void setMenuBar() {
		// Pas de menu bar ici
	}
	
	
	/* consigne 	: Chaîne de caractères qui correspond à la consigne saisie
	 * script 		: Chaîne de caractères qui correspond au script du média
	 * aide 		: Chaîne de caractères qui correspond à l'aide saisie
	 * media		: Chemin vers le fichier audio ou vidéo chargé par l'enseignant
	 * occult		: Caractère d'occultation choisi
	 * sauvegarde	: Chemin indiquant l'emplacement de sauvegarde du .exo
	 * mode			: Mode choisi, 0 si pas de mode choisi, 1 pour entrainement et 2 pour évaluation
	 * ------------------ Mode ENTRAINEMENT ------------------
	 * affichageMots: Valeur booléenne de l'option d'affichage du nombre de mots découverts
	 * incomplet	: Nombre de lettres autorisées avec l'option mot incomplet, vaut 0 si l'option n'est pas activée, sinon 2 ou 3
	 * solution		: Valeur booléenne de l'option d'affichage de la solution
	 *  ------------------ Mode  EVALUATION ------------------
	 * tempsLimite  : Valeur en secondes du temps limite de l'exercice
	 */
	
	
	public void fillRecap(String nomExo, String consigne, String script, String aide, String media, char occult, String sauvegarde, String mode, boolean affichageMots, int incomplet, boolean solution, int tempsLimite) {
		/*
		 * 
		 * 
		 *  - Autoriser mot incomplet (oui ou non)
    		- Nombre de lettres autorisées (2 ou 3)
  			- Autoriser affichage du nombre de mots découverts (oui ou non)
  			- Autoriser affichage de la solution (oui ou non)
  
		 */
		
		this.nomExo = nomExo;
		this.consigne = consigne;
		this.script = script;
		this.aide = aide;
		this.media = media;
		this.occult = occult;
		this.sauvegarde = sauvegarde;
		this.mode = mode;
		this.affichageMots = affichageMots;
		this.incomplet = incomplet;
		this.solution = solution;
		this.tempsLimite = tempsLimite;
		
		
		this.nomExo_lbl.setText(nomExo);
		this.consigne_lbl.setText(consigne);
		this.script_lbl.setText(script);
		this.aide_lbl.setText(aide);
		this.media_lbl.setText(media);
		this.occult_lbl.setText(String.valueOf(occult));
		this.sauvegarde_lbl.setText(sauvegarde);
		this.mode_lbl.setText(mode);
		this.affichageMots_lbl.setText(String.valueOf(affichageMots));
		this.incomplet_lbl.setText(String.valueOf(incomplet));
		this.solution_lbl.setText(String.valueOf(solution));
		this.tempsLimite_lbl.setText(String.valueOf(tempsLimite)+"s");
		
	}
	
	@FXML 
	public void SaveFile(){
		List<String> aideFinal = Arrays.asList(aide.split(" +"));
		Stage stage = (Stage) bPane.getScene().getWindow();
		if(nomExo != null						&& 
				sauvegarde != null				&& 
				consigne != null				&& 
				script != null					&&
				aideFinal != null				&&
				media != null					&&
				String.valueOf(occult) != null	&&
				mode != null					&&
				incomplet != 0) {
			
			try {
				new FileManager().sauvegarderFichier(nomExo, sauvegarde, consigne, script, aideFinal, media, String.valueOf(occult), mode, affichageMots ? 1 : 0, incomplet, 1 ,solution  ? 1 : 0, tempsLimite);
				
				createValidationAlert(sauvegarde,nomExo);
				stage.close();
				Main.setScreen(0);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Erreur inconnue");
				
				stage.close();
				
				Main.setScreen(0);
			}
		}
		else {
			System.err.println("Erreur dans la sauvegarde");
			createErrorAlert();
			stage.close();
		}

	}
	
	public void createErrorAlert() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION,
				"Impossible de sauvegarder votre fichier, des champs sont manquants.\n"
				+"Veuillez renseigner tous les champs.", ButtonType.OK);

		alert.setHeaderText("La sauvegarde a échouée");
		alert.setTitle("Erreur de sauvegarde");

		alert.showAndWait();
	}
	public void createValidationAlert(String pathExo, String nomExo) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION,
				"Vous pouvez retrouver le fichier de l'exercice sur votre ordinateur à cet emplacement : \n"
				+ pathExo+"/"+nomExo+".exo", ButtonType.OK);

		alert.setHeaderText("Votre fichier a été sauvegardé avec succès");
		alert.setTitle("Succès");

		alert.showAndWait();
	}


}
