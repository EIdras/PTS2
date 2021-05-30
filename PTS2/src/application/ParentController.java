package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ParentController implements Initializable {
	
	@FXML ImageView iut_logo;
	
	public void setIcons() {
		iut_logo.setImage(new Image("ressources/img/LOGO_IUT_FULL.png"));
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setIcons();
	}

}
