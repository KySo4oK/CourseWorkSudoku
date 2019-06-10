import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main extends Application{
    static Sudoku sudoku;
    static GridPane gridPane;
    static TextField[][] textFields;
    static String fileName = "save.txt";

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            textFields = new TextField[9][9];
            for(int i = 0; i < textFields.length; i++){
                for (int j = 0; j < textFields.length; j++){
                        textFields[i][j] = new TextField();
                }
            }

            gridPane = new GridPane();
            gridPane.setPrefWidth(250);
            gridPane.setPrefHeight(250);
            for(int i = 0; i < textFields.length; i++){
                for (int j = 0; j < textFields.length; j++){
                    textFields[i][j].setPrefWidth(gridPane.getPrefWidth());
                    textFields[i][j].setPrefHeight(gridPane.getPrefHeight());
                    textFields[i][j].setAlignment(Pos.CENTER);
                    gridPane.add(textFields[i][j], j, i);
                }
            }

            gridPane.setAlignment(Pos.CENTER);
            gridPane.setStyle("-fx-font: 12 arial; -fx-base: #fafafa; -fx-grid-lines-visible: true");

            Button checkResultButton = new Button("Перевірити");
            checkResultButton.setMinWidth(10);
            checkResultButton.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
            checkResultButton.setLayoutX(0);
            checkResultButton.setLayoutY(gridPane.getPrefWidth());
            checkResultButton.setPrefWidth(100);

            Button quitButton = new Button("Вихід");
            quitButton.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
            quitButton.setLayoutX(checkResultButton.getPrefWidth());
            quitButton.setLayoutY(gridPane.getPrefWidth());
            quitButton.setMinWidth(10);
            quitButton.setPrefWidth(60);

            Button saveButton = new Button("Зберегти");
            saveButton.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
            saveButton.setLayoutX(160);
            saveButton.setLayoutY(gridPane.getPrefWidth());
            saveButton.setMinWidth(10);
            saveButton.setPrefWidth(90);

            Group group = new Group(gridPane, checkResultButton, quitButton, saveButton);

            Scene scene = new Scene(group, 250,270);


            Label label = new Label("Ви виграли");
            label.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;" +
                    "-fx-background-position: center center;");
            label.setLayoutX(80);
            label.setLayoutY(20);
            label.setAlignment(Pos.CENTER);
            Button anotherOneButton = new Button("Ще одну");
            anotherOneButton.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
            anotherOneButton.setLayoutX(80);
            anotherOneButton.setLayoutY(80);
            anotherOneButton.setPrefWidth(100);
            anotherOneButton.setOnAction(event -> {
                sudoku = new Sudoku();
                initTextFields();
                primaryStage.setScene(scene);
            });

            Button quitWonSceneButton = new Button("Вихід");
            quitWonSceneButton.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
            quitWonSceneButton.setLayoutX(80);
            quitWonSceneButton.setLayoutY(50);
            quitWonSceneButton.setMinWidth(10);
            quitWonSceneButton.setPrefWidth(100);
            quitWonSceneButton.setOnAction(event ->
                    primaryStage.close());

            Group wonSceneGroup = new Group(label,anotherOneButton,quitWonSceneButton);
            Scene wonScene = new Scene(wonSceneGroup, 250, 270);

            quitButton.setOnAction(event ->
                    primaryStage.close());
            checkResultButton.setOnAction(event ->{
                if(checkResult()){
                    primaryStage.setScene(wonScene);
                }
            });
            saveButton.setOnAction(event -> {
                sudoku.setToFile();
                saveResults();});
            Button continueButton = new Button("Продовжити");
            continueButton.setLayoutX(80);
            continueButton.setLayoutY(100);
            continueButton.setPrefWidth(100);
            continueButton.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
            continueButton.setOnAction(event -> {
                try {
                    sudoku = new Sudoku(fileName);
                    initTextFields();
                    setResults();
                    primaryStage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Button newGameButton = new Button("Нова гра");
            newGameButton.setOnAction(event -> {
                sudoku = new Sudoku();
                initTextFields();
                primaryStage.setScene(scene);
            });
            newGameButton.setLayoutY(130);
            newGameButton.setLayoutX(80);
            newGameButton.setPrefWidth(100);
            newGameButton.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");

            Button startQuitButton = new Button("Вихід");
            startQuitButton.setLayoutY(160);
            startQuitButton.setLayoutX(80);
            startQuitButton.setPrefWidth(100);
            startQuitButton.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
            startQuitButton.setOnAction(event ->
                primaryStage.close());
            Group startGroup = new Group(continueButton,newGameButton,startQuitButton);
            Scene startScene = new Scene(startGroup,250,270);

            primaryStage.setScene(startScene);
            primaryStage.setTitle("Судоку");
            primaryStage.show();



        } catch (Exception e){
            System.out.println("error");
        }
    }

    public static void initTextFields(){
        for(int i = 0; i < textFields.length; i++){
            for (int j = 0; j < textFields.length; j++){
                if(sudoku.matrix[i][j].getConst()) {
                    textFields[i][j].setText("" + sudoku.matrix[i][j].getValue());
                    textFields[i][j].setStyle("-fx-font: 12 arial;" +
                            "-fx-background-position: center center;" +
                            "-fx-background-repeat: stretch;" +
                            "-fx-background-color: #b6e7c9;");
                    textFields[i][j].setDisable(true);
                } else {
                    textFields[i][j].setText("");
                    textFields[i][j].setStyle("-fx-font: 12 arial;" +
                            "-fx-background-color: #fafafa;");
                }
            }
        }
    }


    public static void main(String[] args) throws Exception{

        launch(args);
    }

    public static boolean checkResult(){
        for(int i = 0 ; i < sudoku.matrix.length ; i++){
            for(int j = 0 ; j < sudoku.matrix.length; j++){
                if(!sudoku.matrix[i][j].getConst()){
                        try {
                        if (sudoku.matrix[i][j].getValue() !=
                                        Integer.parseInt(textFields[i][j].getText())) {
                            return false;
                        }
                    } catch (NumberFormatException e){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void saveResults(){
        try(FileWriter fileWriter = new FileWriter(fileName,true)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    try {
                        Integer.parseInt(textFields[i][j].getText());
                        fileWriter.write(textFields[i][j].getText() + ",");
                    } catch (NumberFormatException e){
                        fileWriter.write("-,");
                    }
                }
                fileWriter.write("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setResults(){
        try(FileReader reader = new FileReader(fileName)){
            Scanner scan = new Scanner(reader);
            ArrayList<String> lines = new ArrayList<>();
            while (scan.hasNextLine()){
                lines.add(scan.nextLine());
            }
            for(int i=0; i < 9; i++){
                String[] line = lines.get(i + 18).split(",");
                for(int j = 0; j < 9; j++){
                    if (!line[j].equals("-")){
                        textFields[i][j].setText(line[j]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
