package etu;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

public class HomeController extends ParentController implements Initializable {

	@FXML
	ImageView iut_logo;
	File f;
	String path;
	@FXML
	TextField filePath;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
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
		System.out.println("Sélection effectuée");
		openFile(f);
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
		try {
			path = selectedFile.toURI().toURL().toExternalForm();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printFilePath();
		openFile(new File(path));
	}

	@FXML
	public void printFilePath() {
		filePath.setText(path);
	}

	private void openFile(File f2) {
		// TODO
	}

	@FXML
	public void changePage() {
		Main.setScreen(1);
	}
}