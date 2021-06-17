package application;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MakerController extends ParentController implements Initializable {
	String mediaPath, nomExo;
	private static String saveFilePath;

	private boolean atEndOfMedia = false;

	Image soundIcon = new Image("ressources/img/buttons/soundButton.png");
	Image playIcon = new Image("ressources/img/buttons/playButton.png");
	Image pauseIcon = new Image("ressources/img/buttons/pauseButton.png");

	@FXML BorderPane bPane;
	
	@FXML MediaView mediaView;
	@FXML StackPane media_pane;
	Media media;
	MediaPlayer mediaPlayer;

	@FXML Button btn_play;
	@FXML TextField area_filePath, occultChar, timeMin_field, timeSec_field, exoName;
	@FXML Text txt_wordCount;
	@FXML Label timeLimit_lbl, min_lbl, s_lbl;
	@FXML TextArea consigne_area, script_area, aide_area;
	@FXML ImageView mp3_picture, soundButton, playPauseButton;
	@FXML Slider time_slider, volume_slider;
	@FXML CheckBox enableIncompleteWord, enableDisplayNbWordFound, enableAnswerDisplay;
	@FXML RadioButton trainingModeRadioButton ,evaluationModeRadioButton , twoLettersMinRadioButton, threeLettersMinRadioButton;
	@FXML ChoiceBox<String> incompleteWordNbLetters;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		setMenuBar();
		playPauseButton.setImage(pauseIcon);
		soundButton.setImage(soundIcon);
		addTextLimiter(occultChar, 1);
		incompleteWordNbLetters.getItems().addAll("2 caractères", "3 caractères");
		incompleteWordNbLetters.setValue("2 caractères");
	}
	
	
	@FXML
	public void chooseMediaPath() throws MalformedURLException {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("Media files (mp3, mp4)", "*.mp3", "*.mp4");
        fileChooser.getExtensionFilters().add(extFilter);
		File selectedFile = new File("");
		selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile == null)
			return;
		mediaPath = selectedFile.toURI().toURL().toExternalForm();

		launchMedia();
		printFilePath();
		if (mediaPath.endsWith(".mp3"))
			afficheImage();
	}

	@FXML
	public void launchMedia() {
		media = new Media(mediaPath);
		mediaPlayer = new MediaPlayer(media);
		mediaView.setMediaPlayer(mediaPlayer);
		mediaView.autosize();
		mediaPlayer.setAutoPlay(true); // le média est lancé automatiquement
		mediaPlayer.setVolume(0.25);
		System.out.println("Media lancé !");
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
		}
				);

		time_slider.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mediaPlayer.seek(Duration.seconds(time_slider.getValue()));
			}
		});
		
		time_slider.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				// TODO Auto-generated method stub
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
	public void printFilePath() {
		area_filePath.setPromptText(mediaPath);
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
			System.out.println("Retour au début...");
			mediaPlayer.seek(mediaPlayer.getStartTime());
			playPauseButton.setImage(pauseIcon);
			atEndOfMedia = false;
		}
	}

	@Override
	@FXML
	public void darkMode() {
		super.darkMode();
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
	public void disableTrainingButtons() {
		trainingModeRadioButton.setDisable(false);
		trainingModeRadioButton.setSelected(false);
		
		timeMin_field.setDisable(false);
		timeSec_field.setDisable(false);
		timeLimit_lbl.setDisable(false);
		min_lbl.setDisable(false);
		s_lbl.setDisable(false);
		
		enableAnswerDisplay.setDisable(true);
		incompleteWordNbLetters.setDisable(true);
		enableDisplayNbWordFound.setDisable(true);
		enableIncompleteWord.setDisable(true);
	}

	@FXML
	public void disabletEvaluationButtons() {
		enableIncompleteWord();
		evaluationModeRadioButton.setDisable(false);
		evaluationModeRadioButton.setSelected(false);
		
		timeMin_field.setDisable(true);
		timeSec_field.setDisable(true);
		timeLimit_lbl.setDisable(true);
		min_lbl.setDisable(true);
		s_lbl.setDisable(true);
		
		enableAnswerDisplay.setDisable(false);
		enableDisplayNbWordFound.setDisable(false);
		enableIncompleteWord.setDisable(false);
	}
	
	@FXML 
	public void enableIncompleteWord() {
		if (enableIncompleteWord.isSelected()) {
			incompleteWordNbLetters.setDisable(false);
		}
		else {
			incompleteWordNbLetters.setDisable(true);
		}
		
	}
	
	
	@FXML
	public void saveExercise() throws IOException {
		/* consigne 	: Chaîne de caractères qui correspond à la consigne saisie
		 * script 		: Chaîne de caractères qui correspond au script du média
		 * aide 		: Chaîne de caractères qui correspond à l'aide saisie
		 * media		: Chemin vers le fichier audio ou vidéo chargé par l'enseignant
		 * occult		: Caractère d'occultation choisi
		 * sauvegarde	: Chemin indiquant l'emplacement de sauvegarde du .exo
		 * mode			: Mode choisi, "entrainement" ou "evaluation"
		 * ------------------ Mode ENTRAINEMENT ------------------
		 * affichageMots: Valeur booléenne de l'option d'affichage du nombre de mots découverts
		 * incomplet	: Nombre de lettres autorisées avec l'option mot incomplet, vaut 0 si l'option n'est pas activée, sinon 2 ou 3
		 * solution		: Valeur booléenne de l'option d'affichage de la solution
		 *  ------------------ Mode  EVALUATION ------------------
		 * tempsLimite  : Valeur en secondes du temps limite de l'exercice
		 */
		

		FXMLLoader load = new FXMLLoader(getClass().getResource("save_file_popup.fxml"));
		Parent root = load.load();
		
		Stage popUpStage = new Stage();
		popUpStage.initModality(Modality.APPLICATION_MODAL);

		Scene popUpScene = new Scene(root, 600, 400);
		
		String consigne = consigne_area.getText();
		String script = script_area.getText();
		String aide = aide_area.getText();
		String media = mediaPath;
		nomExo = exoName.getText();
		String occultStr = occultChar.getText() + " ";
		char occult = occultStr.charAt(0);
		if (occult == ' ') {
			occult = '#';
		}
		String sauvegarde = saveFilePath;
		int mode = 0;
		if (trainingModeRadioButton.isSelected()) {
			mode = 1;
		}
		else if (evaluationModeRadioButton.isSelected()) {
			mode = 2;
		}
		boolean affichageMots = false;
		int incomplet = 0;
		boolean solution = false;
		int tempsLimite = 0;
		
		if (mode == 1) {
			affichageMots = enableDisplayNbWordFound.isSelected();
			if (enableIncompleteWord.isSelected()) {
				if (incompleteWordNbLetters.getValue() == "2 caractères") {
					incomplet = 2;
				}
				else {
					incomplet = 3;
				}
			solution = enableAnswerDisplay.isSelected();
			}
		}
		else {
			int tempsMinEnSec 	= Integer.parseInt(timeMin_field.getText()) * 60;
			int tempsSec		= Integer.parseInt(timeSec_field.getText());
			tempsLimite = tempsMinEnSec + tempsSec;
			if (tempsLimite < 10) {
				tempsLimite = 300;
			}
		}
		
		
		SaveFileController saveController = load.getController();
		saveController.fillRecap(nomExo, consigne, script, aide, media, occult, sauvegarde, mode == 1 ? "entrainement" : "evaluation", affichageMots, incomplet, solution, tempsLimite);
		popUpStage.setTitle("Sauvegarde de votre exercice - Résumé");
		popUpStage.setScene(popUpScene);
		popUpStage.show();

	}
	
	public void mediaViewWidthListener() {
		
		media_pane.widthProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				mediaView.setFitWidth(media_pane.widthProperty().getValue());
			}
			
		});

	}
	
	public void mediaViewHeightListener() {
		media_pane.heightProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				mediaView.setFitHeight(media_pane.heightProperty().getValue());
			}
			
		});
	}


	public void setSavePath(String filePath) {
		this.saveFilePath = filePath;
		System.out.println("Chemin du fichier à enregistrer : "+filePath);
	}


	@Override
	public void setMenuBar() {
		bPane.setTop(super.menuBar());
	}

}