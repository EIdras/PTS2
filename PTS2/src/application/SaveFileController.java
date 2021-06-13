package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class SaveFileController extends ParentController implements Initializable{

	@FXML BorderPane bPane;
	@FXML Label consigne_lbl, script_lbl, aide_lbl, media_lbl, occult_lbl, incomplet_lbl, solution_lbl, sauvegarde_lbl;
	private static String consigne, script, aide, media, occult, incomplet, solution, sauvegarde;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
	}
	
	@Override
	public void setMenuBar() {
	}
	
	
	public void fillRecap(String consigne, String script, String aide, String media, String occult, String incomplet, String solution, String sauvegarde) {
		this.consigne = consigne;
		this.script = script;
		this.aide = aide;
		this.occult = occult;
		this.media = media;
		this.incomplet = incomplet;
		this.solution = solution;
		this.sauvegarde = sauvegarde;
		
		this.consigne_lbl.setText(consigne);
		this.script_lbl.setText(script);
		this.aide_lbl.setText(aide);
		this.media_lbl.setText(media);
		this.occult_lbl.setText(occult);
		this.incomplet_lbl.setText(incomplet);
		this.solution_lbl.setText(solution);
		this.sauvegarde_lbl.setText(sauvegarde);
	}

}
