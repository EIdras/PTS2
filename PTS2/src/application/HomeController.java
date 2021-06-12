package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

public class HomeController extends ParentController implements Initializable {

	@FXML ImageView iut_logo;
	File f;
	
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
    public void Drop(DragEvent event) throws IOException{
        List<File> files = event.getDragboard().getFiles();
        f = files.get(0);
        System.out.println("Sélection effectuée");
        openFile(f);;
    }
	
	
	private void openFile(File f2) {
		// TODO Fo fer sa
		
	}


	@FXML 
	public void changePage() {
		Main.setScreen(1);
	}
}