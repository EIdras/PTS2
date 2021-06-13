package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class SaveFileController extends ParentController implements Initializable{

	@FXML BorderPane bPane;
	@FXML Label consigne, script, aide, media, occult, incomplet, solution, sauvegarde;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
	}
	
	@Override
	public void setMenuBar() {
	}
	
	
	public void fillRecap(String consigne, String script, String aide, String media, String occult, String incomplet, String solution, String sauvegarde) {
		this.consigne.setText(consigne);
		this.script.setText(script);
		this.aide.setText(aide);
		this.media.setText(media);
		this.occult.setText(occult);
		this.incomplet.setText(incomplet);
		this.solution.setText(solution);
		this.sauvegarde.setText(sauvegarde);
	}

}
