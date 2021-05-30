package application;
	
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static FXMLLoader homeLoader, makerLoader;
	private static List<Pane> screens = new ArrayList<>(); // Liste des écrans
	private static Scene scene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			homeLoader = new FXMLLoader(getClass().getResource("home_page.fxml"));					// Instancie des FXMLLoader
			makerLoader = new FXMLLoader(getClass().getResource("exercice_maker.fxml"));
			
			screens.add((AnchorPane) homeLoader.load());											// Les ajoute à la liste des écrans
			screens.add((AnchorPane) makerLoader.load());
			
			scene = new Scene(screens.get(0));														// L'écran affiché sur la scene est le premier écran (d'index 0)
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());	// Lie le fichier CSS à la page
			primaryStage.getIcons().add(new Image(("file:src\\ressources\\img\\LOGO_IUT_ICON.png")));
			primaryStage.setTitle("Reconstitution - APPLICATION ENSEIGNANT");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	public static FXMLLoader getMakerLoader() {
		return makerLoader;
	}
	public static FXMLLoader getHomeLoader() {
		return homeLoader;
	}

	
	public static Pane getScreen(int index) {
		return screens.get(index);
	}
	public static void setScreen(int index) {
		scene.setRoot(screens.get(index));
	}
}
