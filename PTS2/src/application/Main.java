package application;
	
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static FXMLLoader homeLoader, makerLoader, saveFileLoader;
	private static List<Pane> screens = new ArrayList<>(); // Liste des écrans
	private static Scene scene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			homeLoader = new FXMLLoader(getClass().getResource("home_page_refait.fxml"));					// Instancie des FXMLLoader
			makerLoader = new FXMLLoader(getClass().getResource("exercice_maker_refait.fxml"));
			saveFileLoader = new FXMLLoader(getClass().getResource("save_file_popup.fxml"));
			
			screens.add((BorderPane) homeLoader.load());											// Les ajoute à la liste des écrans
			screens.add((BorderPane) makerLoader.load());
			screens.add((BorderPane) saveFileLoader.load());
			
			scene = new Scene(screens.get(0));														// L'écran affiché sur la scene est le premier écran (d'index 0)
			loadCSS("application.css");
			primaryStage.getIcons().add(new Image(("file:src\\ressources\\img\\LOGO_IUT_ICON.png")));
			primaryStage.setTitle("Reconstitution - Application ENSEIGNANT");
			primaryStage.setMaximized(true); 														// Fenêtre maximisée (Plein écran)
			primaryStage.setMinWidth(1300);
			primaryStage.setMinHeight(760);
			primaryStage.setResizable(true);														// Redimensionnable
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void loadCSS(String cssPath) {
		scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
	}
	
	public void unloadCSS(String cssPath) {
		scene.getStylesheets().remove(getClass().getResource(cssPath).toExternalForm());
	}
	
	public static FXMLLoader getMakerLoader() {
		return makerLoader;
	}
	public static FXMLLoader getHomeLoader() {
		return homeLoader;
	}
	public static FXMLLoader getSaveFileLoader() {
		return saveFileLoader;
	}
	
	public static Scene getScene() {
		return scene;
	}

	
	public static Pane getScreen(int index) {
		return screens.get(index);
	}
	public static void setScreen(int index) {
		scene.setRoot(screens.get(index));
	}
}
