package etu;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

public class EtuController extends ParentController implements Initializable {
	String pathToMedia, pathToExo, nomExo, consigne, script, mode, aide, occultationChar;
	Hidden hidden;

	private boolean atEndOfMedia = false;

	Image soundIcon = new Image("ressources/img/buttons/soundButton.png");
	Image playIcon = new Image("ressources/img/buttons/playButton.png");
	Image pauseIcon = new Image("ressources/img/buttons/pauseButton.png");

	@FXML
	BorderPane bPane;
	@FXML
	MediaView mediaView;
	@FXML
	StackPane media_pane;
	Media media;
	MediaPlayer mediaPlayer;

	@FXML
	Button btn_play;
	@FXML
	Text txt_wordCount;
	@FXML
	TextArea consigne_area, script_area;
	@FXML
	TextField input_field;
	@FXML
	ImageView mp3_picture, soundButton, playPauseButton;
	@FXML
	Slider time_slider, volume_slider;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		playPauseButton.setImage(pauseIcon);
		soundButton.setImage(soundIcon);
		setMenuBar();
	}

	public void setParamaters(String nomExo, String pathToExo, String consigne, String script, String aide,
			String pathToMedia, String occultationChar, String mode, int incompleteWords, int letterNumber,
			int foundWords, int displayAnswer, int timeLimit) {
		this.pathToExo = pathToExo;
		this.pathToMedia = pathToMedia;
		this.script = script;
		this.nomExo = nomExo;
		this.consigne = consigne;
		this.mode = mode;
		this.aide = aide;
		this.occultationChar = occultationChar;
		this.hidden = new Hidden(script, occultationChar, mode, letterNumber);

		script_area.setText(hidden.hideWord(script));
		consigne_area.setText(consigne);
		launchMedia();
		if (pathToMedia.endsWith(".mp3"))
			afficheImage();

		Stage primaryStage = (Stage) mediaView.getScene().getWindow();
		primaryStage.setTitle("Reconstitution - Application �tudiante - " + nomExo);

	}

	@FXML
	public void tryToUnmaskWord() {
		String toUnmaskString = input_field.getText();
		input_field.clear();
		script_area.setText(hidden.tryUnmaskWord(toUnmaskString));
	}

	@FXML
	public void launchMedia() {
		System.out.println(pathToMedia);
		media = new Media(pathToMedia);
		mediaPlayer = new MediaPlayer(media);
		mediaView.setMediaPlayer(mediaPlayer);
		mediaView.autosize();
		mediaPlayer.setAutoPlay(true); // le m�dia est lanc� automatiquement
		mediaPlayer.setVolume(0.25);
		System.out.println("Media lanc� !");
		playPauseButton.setDisable(false);
		timeSliderUpdate();
		volumeSliderUpdate();
		mediaViewWidthListener();
		mediaViewHeightListener();
	}

	public void timeSliderUpdate() {

		mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
			@Override
			public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
				time_slider.setValue(newValue.toSeconds());
			}
		});

		time_slider.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mediaPlayer.seek(Duration.seconds(time_slider.getValue()));
			}
		});

		time_slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mediaPlayer.seek(Duration.seconds(time_slider.getValue()));
			}
		});

		mediaPlayer.setOnReady(new Runnable() {
			@Override
			public void run() {
				Duration total = media.getDuration();
				time_slider.setMax(total.toSeconds());
			}
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
			// On ne fait rien dans ces �tats
			return;
		}

		if (status == Status.PAUSED || status == Status.READY || status == Status.STOPPED) {
			// On relance le m�dia si il est arriv� � sa fin
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

		if (atEndOfMedia) {
			System.out.println("Retour au d�but...");
			mediaPlayer.seek(mediaPlayer.getStartTime());
			playPauseButton.setImage(pauseIcon);
			atEndOfMedia = false;
		}
	}

	@Override
	@FXML
	public void darkMode() {
		// Mode sombre en chargeant un CSS
		Main main = new Main();
		Scene scene = Main.getScene();
		// System.out.println(scene.getStylesheets());
		if (scene.getStylesheets().stream().filter(value -> value.endsWith("darkmode.css")).collect(Collectors.toList())
				.size() > 0) {
			main.unloadCSS("darkmode.css");
		} else {
			main.loadCSS("darkmode.css");
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

	public void mediaViewWidthListener() {

		media_pane.widthProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				System.out.print("LARGEUR " + media_pane.widthProperty().getValue());
				mediaView.setFitWidth(media_pane.widthProperty().getValue());
				System.out.println(" = " + mediaView.getFitWidth());
			}

		});

	}

	public void mediaViewHeightListener() {
		media_pane.heightProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				System.out.print("HAUTEUR " + media_pane.heightProperty().getValue());
				mediaView.setFitHeight(media_pane.heightProperty().getValue());
				System.out.println(" = " + mediaView.getFitHeight());
			}

		});
	}

	@Override
	public void setMenuBar() {
		javafx.scene.control.MenuBar menuBar = super.menuBar();
		MenuItem sauvegarder = new MenuItem("Sauvegarder");
		MenuItem fermer = new MenuItem("Fermer");
		menuBar.getMenus().get(0).getItems().addAll(sauvegarder, fermer);

		fermer.setOnAction(Event -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Fermeture");
			alert.setHeaderText(
					"Confirmez vous vouloir fermer l'exerice ?\nValider l'action reviendrait � perdre ton contenu non enregistr�");

			// option != null.
			Optional<ButtonType> option = alert.showAndWait();

			if (option.get() == ButtonType.OK) {
				mediaPlayer.stop();
				Main.setScreen(0);
			}
		});

		sauvegarder.setOnAction(Event -> {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Sauvegarde");
			try {
				new FileManager().sauvegarderFichier(nomExo, pathToExo.substring(0,pathToExo.lastIndexOf("\\"))+ "\\", consigne, script, hidden.getFoundString(), aide, pathToMedia, occultationChar, mode);
			
			alert.setHeaderText(
					"Votre progression a �t� enregistr� sous le nom de :\n\t" + pathToExo.replace(".exo", ".corr")
							+ "\nATTENTION : ce fichier ne permets pas de r�cup�rer votre progression !");
			} catch (IOException e) {
				e.printStackTrace();
				alert.setAlertType(AlertType.ERROR);
				alert.setHeaderText(
						"Une erreur est survenue lors de la sauvegarde du fichier !");
			}
			alert.showAndWait();

		});

		bPane.setTop(menuBar);
	}

}