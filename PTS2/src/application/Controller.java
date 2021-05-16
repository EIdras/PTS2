package application;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class Controller implements Initializable{
	String path;
	
	private boolean atEndOfMedia = false;
	
	String soundButtonpath = "./soundButton.png";
	String playButtonpath = "./playButton.png";
	String pauseButtonpath = "./pauseButton.png";
	Image soundIcon = new Image(new File(soundButtonpath).toURI().toString());
	Image playIcon = new Image(new File(playButtonpath).toURI().toString());
	Image pauseIcon = new Image(new File(pauseButtonpath).toURI().toString());
	
	@FXML 
	MediaView mediaView;
	Media media;
	MediaPlayer mediaPlayer;
	
	@FXML Button btn_play;
	@FXML TextField area_filePath;
	@FXML TextField occultChar;
	@FXML Text txt_wordCount;
	@FXML TextArea field_transcription;
	
	@FXML ImageView mp3_picture;
	@FXML ImageView soundButton;
	@FXML ImageView playPauseButton;
	
	@FXML Slider time_slider;
	@FXML Slider volume_slider;
	
	@FXML public void chooseMediaPath() throws MalformedURLException {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = new File("");
		selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile == null) return;
		path = selectedFile.toURI().toURL().toExternalForm();
		
		launchMedia();
		printFilePath();
		if(path.contains(".mp3")) afficheImage();
	}
	
	@FXML public void launchMedia() {
		media = new Media(path);   
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);  
        mediaView.autosize();
        mediaPlayer.setAutoPlay(true);											// le média est lancé automatiquement
        mediaPlayer.setVolume(0.25);
        System.out.println("Media lancé !");
        playPauseButton.setDisable(false);
        timeSliderUpdate();
        volumeSliderUpdate();
	}
	
	@FXML public void dispose() {
		mediaPlayer.stop();
		mediaPlayer.dispose();
	}
	/*
	@FXML public void seekOnSlider() {
		Duration duration = mediaPlayer.getMedia().getDuration();
		media_slider.valueProperty().addListener((obs -> {
			mediaPlayer.seek(duration.multiply(media_slider.getValue() / 100.0));
	    }));
		
		Duration duration = mediaPlayer.getMedia().getDuration();
		mediaPlayer.seek(duration.multiply(media_slider.getValue() / 100.0));	// Calcule le rapport entre la valeur du slider et le temps correspondant dans le média
		
	}
	*/
	
	@FXML public void timeSliderUpdate() {
		mediaPlayer.setOnReady(new Runnable() {
			
			@Override
			public void run() {
				time_slider.setMax(mediaPlayer.getTotalDuration().toSeconds());
			}
		});
		
		// Listen to the slider. When it changes, seek with the player.
		InvalidationListener sliderChangeListener = o-> {
		    Duration seekTo = Duration.seconds(time_slider.getValue());
		    mediaPlayer.seek(seekTo);
		};
		time_slider.valueProperty().addListener(sliderChangeListener);

		// Link the player's time to the slider
		mediaPlayer.currentTimeProperty().addListener(l-> {
		    // Temporarily remove the listener on the slider, so it doesn't respond to the change in playback time
			time_slider.valueProperty().removeListener(sliderChangeListener);

		    // Keep timeText's text up to date with the slider position.
		    Duration currentTime = mediaPlayer.getCurrentTime();
		    time_slider.setValue(currentTime.toSeconds());    

		    // Re-add the slider listener
		    time_slider.valueProperty().addListener(sliderChangeListener);
		});
	}
	
	@FXML public void volumeSliderUpdate() {
		volume_slider.valueProperty().addListener((o-> {
			mediaPlayer.setVolume(volume_slider.getValue() / 100.0);					// Change le volume sonore selon la valeur du slider vertical
	    }));
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
		if(key.equals("image")) mp3_picture.setImage((Image) valueAdded);
	}
	
	@FXML public void showVolumeSlider() {
		if(volume_slider.isVisible()) {
			volume_slider.setVisible(false);
		}
		else {
			volume_slider.setVisible(true);
		}
	}

	@FXML public void playBtn() {
			Status status = mediaPlayer.getStatus();
		 
			if (status == Status.UNKNOWN  || status == Status.HALTED)
			{
				return;
			}
 
        		if ( status == Status.PAUSED
        				|| status == Status.READY
        				|| status == Status.STOPPED)
        		{
        			// rewind the movie if we're sitting at the end
        			if (atEndOfMedia) {
            		mediaPlayer.seek(mediaPlayer.getStartTime());
            		atEndOfMedia = false;
        			}
        			mediaPlayer.play();
        			playPauseButton.setImage(pauseIcon);         	 
        		} 
        		else {
        			mediaPlayer.pause();
        			playPauseButton.setImage(playIcon);
        		}
		} 

	@FXML public void darkMode() {
		
	}
	
	@FXML public void wordCount() {
		if ((field_transcription.getText() != null && !field_transcription.getText().isEmpty())) {
			int value = field_transcription.getText().length();
			txt_wordCount.setText(String.valueOf(value));
		}
		
		
	}
	
	public static void addTextLimiter(final TextField tf, final int maxLength) {
	    tf.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (tf.getText().length() > maxLength) {
	                String s = tf.getText().substring(0, maxLength);
	                tf.setText(s);
	            }
	        }
	    });
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		playPauseButton.setImage(pauseIcon);
		soundButton.setImage(soundIcon);
		addTextLimiter(occultChar, 1);
	}
	
}
