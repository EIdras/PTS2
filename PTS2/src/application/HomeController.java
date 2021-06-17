package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class HomeController extends ParentController implements Initializable {

	@FXML
	ImageView iut_logo;
	@FXML
	BorderPane bPane;
	@FXML
	ImageView tradLogo_view;
	@FXML
	Label errorLabel;
	Image logo_trad = new Image("ressources/img/TRADUCTION_ICON.png");
	File f;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		tradLogo_view.setImage(logo_trad);
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
		if (f.getAbsolutePath().endsWith(".corr")) {
			errorLabel.setVisible(false);
			loadFile(f);
		} else {
			errorLabel.setVisible(true);
		}
	}

	@FXML
	private void openFile() {
		FileChooser fileChooser = new FileChooser();

		File filePath = fileChooser.showOpenDialog(null);
		System.out.println(filePath.toString());
		loadFile(filePath);

	}

	private void loadFile(File file) {
		FXMLLoader loader = Main.getCorrectionLoader();
		correctionController correctionController = loader.getController();
		System.out.println("load file");
		HashMap<String, Object> map = new FileManager().ouvrirfichier(file.getAbsolutePath());

		Main.setScreen(3);
		correctionController.setParamaters((String) map.get("exoName"), file.getAbsolutePath(),
				(String) map.get("consigne"), (String) map.get("script"), (String) map.get("foundScript"),
				(String) map.get("aide"), (String) map.get("mediaPath"), (String) map.get("mode"));
	}

	@FXML
	private void newFile() {
		DirectoryChooser dirChooser = new DirectoryChooser();

		File filePath = dirChooser.showDialog(null);
		System.out.println(filePath.toString());
		changePage(filePath.toString());
	}

	public void changePage(String filePath) {
		MakerController mkController = Main.getMakerLoader().getController();
		mkController.setSavePath(filePath);
		Main.setScreen(1);
	}

	@Override
	public void setMenuBar() {
		bPane.setTop(super.menuBar());
	}
}