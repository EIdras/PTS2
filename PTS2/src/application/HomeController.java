package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class HomeController extends ParentController implements Initializable {

	@FXML ImageView iut_logo;
	@FXML BorderPane bPane;
	@FXML ImageView tradLogo_view;
	File f;
	
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
    public void Drop(DragEvent event) throws IOException{
        List<File> files = event.getDragboard().getFiles();
        f = files.get(0);
        System.out.println("Sélection effectuée");
        loadFile(f);
    }
	
	
	@FXML
	private void openFile() {
		FileChooser fileChooser = new FileChooser();
		
		File filePath = fileChooser.showOpenDialog(null);
		System.out.println(filePath.toString());
		loadFile(filePath);
		
	}
	
	private void loadFile(File file) {
		//TODO : Récupérer le travail de Claude pour lire le fichier et récupérer les informations nécessaires
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
	
	
	public void setMenuBar() {
		bPane.setTop(super.menuBar());
	}
}