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
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
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

public class MakerController extends ParentController implements Initializable {
	String path;

	private boolean atEndOfMedia = false;

	Image soundIcon = new Image("ressources/img/buttons/soundButton.png");
	Image playIcon = new Image("ressources/img/buttons/playButton.png");
	Image pauseIcon = new Image("ressources/img/buttons/pauseButton.png");

	@FXML
	MediaView mediaView;
	Media media;
	MediaPlayer mediaPlayer;

	@FXML Button btn_play;
	@FXML TextField area_filePath, occultChar;
	@FXML Text txt_wordCount;
	@FXML TextArea field_transcription;
	@FXML ImageView mp3_picture, soundButton, playPauseButton;
	@FXML Slider time_slider, volume_slider;
	@FXML CheckBox enableIncompleteWord, enableDisplayNbWordFound, enableAnswerDisplay;
	@FXML RadioButton trainningModeRadioButton,evaluationModeRadioButton , twoLettersMinRadioButton, threeLettersMinRadioButton;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		playPauseButton.setImage(pauseIcon);
		soundButton.setImage(soundIcon);
		addTextLimiter(occultChar, 1);
	}
	
	
	@FXML
	public void chooseMediaPath() throws MalformedURLException {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = new File("");
		selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile == null)
			return;
		path = selectedFile.toURI().toURL().toExternalForm();

		launchMedia();
		printFilePath();
		if (path.contains(".mp3"))
			afficheImage();
	}

	@FXML
	public void launchMedia() {
		media = new Media(path);
		mediaPlayer = new MediaPlayer(media);
		mediaView.setMediaPlayer(mediaPlayer);
		mediaView.autosize();
		mediaPlayer.setAutoPlay(true); // le média est lancé automatiquement
		mediaPlayer.setVolume(0.25);
		System.out.println("Media lancé !");
		playPauseButton.setDisable(false);
		timeSliderUpdate();
		volumeSliderUpdate();
	}

	@FXML
	public void timeSliderUpdate() {
		mediaPlayer.setOnReady(new Runnable() {

			@Override
			public void run() {
				time_slider.setMax(mediaPlayer.getTotalDuration().toSeconds());
			}
		});

		// Ecoute sur le slider. Quand il est modifié, modifie le temps du media player.
		InvalidationListener sliderChangeListener = o -> {
			Duration seekTo = Duration.seconds(time_slider.getValue());
			mediaPlayer.seek(seekTo);
		};
		time_slider.valueProperty().addListener(sliderChangeListener);

		// Lie le temps du media player au slider
		mediaPlayer.currentTimeProperty().addListener(l -> {
			// Supression temporaire de l'écoute sur le slider, pour qu'il ne réponde pas
			// aux changements du temps de lecture
			time_slider.valueProperty().removeListener(sliderChangeListener);

			// Met a jour la valeur de temps du média avec la position du slider.
			Duration currentTime = mediaPlayer.getCurrentTime();
			time_slider.setValue(currentTime.toSeconds());

			// Réactivation de l'écoute du slider
			time_slider.valueProperty().addListener(sliderChangeListener);
		});
	}

	@FXML
	public void volumeSliderUpdate() {
		volume_slider.valueProperty().addListener((o -> {
			mediaPlayer.setVolume(volume_slider.getValue() / 100.0); // Change le volume sonore selon la valeur du
																		// slider vertical
		}));
	}

	@FXML
	public void printFilePath() {
		area_filePath.setPromptText(path);
	}

	@FXML
	public void afficheImage() {
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
		if (key.equals("image"))
			mp3_picture.setImage((Image) valueAdded);
	}

	@FXML
	public void showVolumeSlider() {
		if (volume_slider.isVisible()) {
			volume_slider.setVisible(false);
		} else {
			volume_slider.setVisible(true);
		}
	}

	@FXML
	public void playBtn() {
		Status status = mediaPlayer.getStatus();

		Duration ct = mediaPlayer.getCurrentTime();
		Duration td = mediaPlayer.getTotalDuration();

		if (ct.equals(td)) {
			System.out.println("-----FIN MEDIA-----");
			atEndOfMedia = true;
		}

		if (status == Status.UNKNOWN || status == Status.HALTED) {
			// On ne fait rien dans ces états
			return;
		}

		if (status == Status.PAUSED || status == Status.READY || status == Status.STOPPED) {
			// On relance le média si il est arrivé à sa fin
			if (atEndOfMedia) {
				mediaPlayer.seek(mediaPlayer.getStartTime());
				atEndOfMedia = false;
			}
			// Quand le media est en pause, un clic sur l'icone 'play' le met en play et
			// change cet icone.
			mediaPlayer.play();
			playPauseButton.setImage(pauseIcon);
			System.out.println(status);

		} else {
			// Quand le media n'est pas en pause, un clic sur l'icone 'pause' le met en
			// pause et change cet icone.
			mediaPlayer.pause();
			playPauseButton.setImage(playIcon);
			System.out.println(status);
		}
	}

	@FXML
	public void darkMode() {

	}

	@FXML
	public void wordCount() {	// Ne fonctionne pas pour l'instant, génère simplement des erreurs :X
		if ((field_transcription.getText() != null && !field_transcription.getText().isEmpty())) {
			int value = field_transcription.getText().length();
			txt_wordCount.setText(String.valueOf(value));
		}

	}

	public static void addTextLimiter(final TextField tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.setText(s);
				}
			}
		});
	}

	@FXML
	public void disableTrainningButtons() {
		// Désactive les checkbox de l'entrainnement
		enableAnswerDisplay.setDisable(true);
		enableDisplayNbWordFound.setDisable(true);
		enableIncompleteWord.setDisable(true);

		// Active les checkbox de l'évaluation
		// TODO (pas d'options)
	}

	@FXML
	public void disabletEvaluationButtons() {
		// Active les checkbox de l'évaluation
		// TODO (pas d'options)

		// Désactive les checkbox de l'entrainnement
		enableAnswerDisplay.setDisable(false);
		enableDisplayNbWordFound.setDisable(false);
		enableIncompleteWord.setDisable(false);
	}

}