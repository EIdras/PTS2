package etu;

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

	private static FXMLLoader homeLoader, etuLoader;
	private static List<Pane> screens = new ArrayList<>(); // Liste des �crans
	private static Scene scene;

	@Override
	public void start(Stage primaryStage) {

		try {
			homeLoader = new FXMLLoader(getClass().getResource("home_page.fxml")); // Instancie des FXMLLoader
			etuLoader = new FXMLLoader(getClass().getResource("etu.fxml"));

			screens.add((BorderPane) homeLoader.load()); // Les ajoute � la liste des �crans
			screens.add((BorderPane) etuLoader.load());

			scene = new Scene(screens.get(0)); // L'�cran affich� sur la scene est le premier �cran (d'index 0)
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// // Lie le fichier CSS � la page
			loadCSS("application.css");
			primaryStage.getIcons().add(new Image(("file:src\\ressources\\img\\LOGO_IUT_ICON.png")));
			primaryStage.setTitle("Reconstitution - Application �TUDIANT");
			primaryStage.setMaximized(true); // Fen�tre maximis�e (Plein �cran)
			primaryStage.setMinWidth(1300);
			primaryStage.setMinHeight(760);
			primaryStage.setResizable(true); // Redimensionnable
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
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

	public static FXMLLoader getEtuLoader() {
		return etuLoader;
	}

	public static FXMLLoader getHomeLoader() {
		return homeLoader;
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
