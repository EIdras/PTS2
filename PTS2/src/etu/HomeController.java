package etu;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HomeController extends ParentController implements Initializable {

	@FXML
	BorderPane bPane;
	@FXML
	ImageView iut_logo;
	File f;
	String path;
	@FXML
	TextField filePath;
	@FXML
	Label errorLabel;
	@FXML
	Label errorFileNameLabel;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		setMenuBar();
	}

	@FXML
	public void onDragOver(DragEvent event) {
		if (event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.ANY);
		}
	}

	@FXML
	public void Drop(DragEvent event) throws IOException {
		List<File> files = event.getDragboard().getFiles();
		f = files.get(0);
		if (f.getAbsolutePath().endsWith(".exo")) {
			errorLabel.setVisible(false);
			errorFileNameLabel.setVisible(false);
			path = f.getAbsolutePath();
			printFilePath();
			openFile(f);
		} else {
			errorLabel.setVisible(true);
			path = f.getAbsolutePath().replace("\\", "/");
			errorFileNameLabel
					.setText("Le fichier " + path.split("/")[path.split("/").length - 1] + " n'est pas valide");
			errorFileNameLabel.setVisible(true);
			path = null;
		}
	}

	@FXML
	public void importFile() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("EXO files", "*.exo");
		fileChooser.getExtensionFilters().add(extFilter);
		File selectedFile = new File("");
		selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile == null)
			return;
		path = selectedFile.getAbsolutePath().replace("\\", "/");
//		try {
//			path = selectedFile.toURI().toURL().toExternalForm();
//			path = selectedFile.getAbsolutePath();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
		printFilePath();
		openFile(new File(path));
	}

	@FXML
	public void printFilePath() {
		filePath.setText(path);
	}

	private void openFile(File f2) {
		// TODO
		FXMLLoader loader = Main.getEtuLoader();
		EtuController etuController = loader.getController();
		HashMap<String, Object> map = new FileManager().ouvrirfichier(f2.getAbsolutePath());
		
		Main.setScreen(1);
		etuController.setParamaters((String) map.get("exoName"), f2.getAbsolutePath(), (String) map.get("consigne"),
				(String) map.get("script"), (String) map.get("aide"),  (String) map.get("mediaPath"), (String) map.get("occultChar"),
				(String) map.get("mode"), (int) map.get("incompleteWords"), (int) map.get("letterNumber"), (int) map.get("foundWords"), (int) map.get("finalAnswer"), (int) map.get("timeLimit"));
	}

	@FXML
	public void changePage() {
		Main.setScreen(1);
	}

	@Override
	public void setMenuBar() {
		bPane.setTop(super.menuBar());
	}
}