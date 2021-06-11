package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ParentController implements Initializable {
	
	@FXML ImageView iut_logo;
	
	public void setIcons() {
		iut_logo.setImage(new Image("ressources/img/LOGO_IUT_FULL2.png"));
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setIcons();
	}
	
	public void darkMode() {
		// Mode sombre en chargeant un CSS
		Main main = new Main();
		Scene scene = Main.getScene();
		if(scene.getStylesheets().stream().filter(value -> value.endsWith("darkmode.css")).collect(Collectors.toList()).size() > 0) {
			main.unloadCSS("darkmode.css");
		}
		else {
			main.loadCSS("darkmode.css");
		}
	}
	
	public MenuBar menuBar() {
		MenuBar menuBar = new MenuBar();
		Menu fichier = new Menu("Fichier");
			MenuItem nouveau = new MenuItem("Nouveau");
			MenuItem ouvrir = new MenuItem("Ouvrir");
			MenuItem sauvegarder = new MenuItem("Sauvegarder");
			MenuItem fermer = new MenuItem("Fermer");
			fichier.getItems().addAll(nouveau, ouvrir, sauvegarder, fermer);
		Menu parametres = new Menu("Paramètres");
			MenuItem pleinEcran = new MenuItem("Plein écran");
			MenuItem modeSombre = new MenuItem("Mode sombre");
			parametres.getItems().addAll(pleinEcran,modeSombre);
		Menu aPropos = new Menu("A propos");
			MenuItem qui = new MenuItem("Qui sommes-nous ?");
			MenuItem aide = new MenuItem("(?) Aide");
			MenuItem contacter = new MenuItem("Nous contacter");
			aPropos.getItems().addAll(qui,aide,contacter);
		menuBar.getMenus().addAll(fichier, parametres, aPropos);
		
		modeSombre.setOnAction(Event -> darkMode());
		
		return menuBar;
	}
	
	public abstract void setMenuBar();

}
