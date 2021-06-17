package application;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public abstract class ParentController implements Initializable {
	
	@FXML ImageView iut_logo;
	
	MenuBar menuBar = new MenuBar();
	Menu fichier = new Menu("Fichier");
		MenuItem nouveau = new MenuItem("Nouveau");
		MenuItem ouvrir = new MenuItem("Ouvrir");
		MenuItem sauvegarder = new MenuItem("Sauvegarder");
		MenuItem fermer = new MenuItem("Fermer");
		
	Menu parametres = new Menu("Paramètres");
		CheckMenuItem pleinEcran = new CheckMenuItem("Plein écran");
		CheckMenuItem modeSombre = new CheckMenuItem("Mode sombre");
		
	Menu aPropos = new Menu("A propos");
		MenuItem qui = new MenuItem("Qui sommes-nous ?");
		MenuItem aide = new MenuItem("(?) Aide");
		MenuItem contacter = new MenuItem("Nous contacter");
	
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
	
	public void fullScreen() {
		Scene scene = Main.getScene();
		Stage stage = (Stage) scene.getWindow();
		if (pleinEcran.isSelected()) {
			stage.setMaximized(true);
		}
		else {
			stage.setMaximized(false);
		}
	}
	
	public MenuBar menuBar() {
			fichier.getItems().addAll(nouveau, ouvrir, sauvegarder, fermer);
			parametres.getItems().addAll(pleinEcran,modeSombre);
			aPropos.getItems().addAll(qui,aide,contacter);
		menuBar.getMenus().addAll(fichier, parametres, aPropos);
		
		modeSombre.setOnAction(Event -> darkMode());
		pleinEcran.setOnAction(Event -> fullScreen());
		fermer.setOnAction(Event -> closeExercise());
		
		contacter.setOnAction(Event -> {
			try {
				Desktop.getDesktop().browse(new URL("http://iut-laval.univ-lemans.fr/fr/l-iut-de-laval/contact.html").toURI());
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		});
		
		return menuBar;
	}
	
	public void closeExercise() {
		Main.setScreen(0);
	}
	
	public abstract void setMenuBar();

}
