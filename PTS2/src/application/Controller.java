package application;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;

public class Controller implements Initializable{
	String path;
	
	@FXML 
	MediaView mediaView;
	Media media;
	MediaPlayer mediaPlayer;
	
	@FXML Button btn_play;
	@FXML ImageView mp3_picture;
	@FXML TextField area_filePath;
	
	@FXML public void choosePath() throws MalformedURLException {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = new File("");
		selectedFile = fileChooser.showOpenDialog(null);
		path = selectedFile.toURI().toURL().toExternalForm();
		System.out.println(path);
		launchMedia();
		printFilePath();
		if(path.contains(".mp3")) afficheImage();
	}
	
	@FXML public void launchMedia() {
		media = new Media(path);   
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);  
        mediaPlayer.setAutoPlay(true);
        System.out.println("Media lancé !");
	}
	
	@FXML public void printFilePath() {
		area_filePath.setPromptText(path);
	}
	
	@FXML public void afficheImage() {
		media.getMetadata().addListener(new MapChangeListener<String, Object>() {
            @Override
            public void onChanged(Change<? extends String, ? extends Object> ch) {
              if (ch.wasAdded()) {
                handleMetadata(ch.getKey(), ch.getValueAdded());
              }
            }
          });
	}
	
	protected void handleMetadata(String key, Object valueAdded) {
		System.out.println("Modification Handle");
		if(key.equals("image")) mp3_picture.setImage((Image) valueAdded);
	}

	@FXML public void playBtn() {
		if(mediaPlayer.getStatus() == Status.PAUSED) {
        	mediaPlayer.play();
        	btn_play.setText("Pause");
        	System.out.println("PLAY");
		}
      	else{
        	mediaPlayer.pause();
        	btn_play.setText("Play");
        	System.out.println("PAUSE");
        }
	} 

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
}
