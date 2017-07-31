package Interface;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainFX extends Application {
	public MainController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("/Interface/main.fxml"));
		primaryStage.getIcons().add(new Image("/Interface/ss.png"));
		primaryStage.setTitle("Smartio & Renomax");
		primaryStage.setMinHeight(410);
		primaryStage.setMinWidth(606);
		primaryStage.setMaxHeight(410);
		primaryStage.setMaxWidth(606);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(0, "/Interface/MainWindow.css");
		primaryStage.setScene(scene);
		if (MainController.netIsAvailable() == true) {
			primaryStage.show();
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle(" перевірте підключення до інтернету");
					alert.setHeaderText("ЗЄДНАННЯ З ІНТЕРНЕТОМ ВІДСУТНЄ");
					alert.showAndWait();

				}
			});
		}
	}

	public static void main(String[] args)
			throws UnsupportedEncodingException, ClientProtocolException, URISyntaxException, IOException {
		launch(args);
	}
}