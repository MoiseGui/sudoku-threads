package application;
	
import java.io.IOException;

import application.model.Sudoku;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	private static Stage primaryStage;
	private static BorderPane rootLayout;
	private Sudoku sudoku = new Sudoku();
	
	
	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void setPrimaryStage(Stage primaryStage) {
		Main.primaryStage = primaryStage;
	}

	public static BorderPane getRootLayout() {
		return rootLayout;
	}

	public static void setRootLayout(BorderPane rootLayout) {
		Main.rootLayout = rootLayout;
	}

	public Sudoku getSudoku() {
		return sudoku;
	}

	public void setSudoku(Sudoku sudoku) {
		this.sudoku = sudoku;
	}

	@Override
	public void start(Stage primaryStage) {
		Main.primaryStage = primaryStage;
		Main.primaryStage.setTitle("Sudoku Solver");
		Main.primaryStage.getIcons().add(new Image("file:resources/img/logo.png"));
		initRootLayout();
	}
	
	private void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Main.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(getClass().getResource("stylesheet/application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			primaryStage.show();
			MainController controller = loader.getController();
			controller.SwitchScene("view/9x9.fxml");
			controller.getSudoku().setSize(9);
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unused")
	private void SwitchScene(String fxml) throws IOException {
		AnchorPane anchorpane = null;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource(fxml));
		anchorpane = (AnchorPane) loader.load();
//		borderpan.setCenter(anchorpane);
		Main.getPrimaryStage().setHeight(717);
		Main.getPrimaryStage().setWidth(768);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
