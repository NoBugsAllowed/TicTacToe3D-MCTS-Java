package sample;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main extends Application implements TicTacToe3D.OnMoveMadeListener {
    private static final int N = 4;
    private static final int Big = 10000;
    private static final double STICK_RADIUS = 0.2;
    private static final double STICK_DIST = 6;
    private static final double BALL_RADIUS = 1;
    private static final double STICK_H = N*2*BALL_RADIUS;
    private static final double MIN_CAMERA_DIST = 30;
    private static final double MAX_CAMERA_DIST = 60;
    private Color COLOR_1;
    private Color COLOR_2;
    private Color currentColor;
    private Stage window;
    private Group root;
    private Label gameFinishedInfo;
    private Rotate yCameraRotate;
    private Rotate xCameraRotate;
    private Translate cameraTranslate;
    private double lastMouseX;
    private double lastMouseY;
    private double yCameraAngle = 20;
    private double xCameraAngle = -30;
    private GameMode gameMode;
    private int player = 1;
    private boolean isGameFinished = false;
    private TicTacToe3D game;

    @Override
    public void moveMade(int player, int x, int y, int z) {
        Sphere ball = new Sphere(BALL_RADIUS);
        ball.setMaterial(new PhongMaterial(player==1?COLOR_1:COLOR_2));
        ball.setDrawMode(DrawMode.FILL);
        ball.setTranslateX(x * STICK_DIST);
        ball.setTranslateZ(y *STICK_DIST);
        ball.setTranslateY(z * (-2) -1);
        root.getChildren().add(ball);
    }

    @Override
    public void gameFinished(int winner,String name) {
        if(gameMode==GameMode.PvP) {
            gameFinishedInfo.setText(name + " wins!");
            gameFinishedInfo.setTextFill(winner==1?COLOR_1:COLOR_2);
        }
        else if(winner==1) {
            gameFinishedInfo.setText("You win!");
        }
        else {
            gameFinishedInfo.setText("AI wins!");
        }
    }

    private Scene createGameArea() {
        // Sticks
        AnchorPane anchorPane = new AnchorPane();
        Scene scene = new Scene(anchorPane);
        root = new Group();
        Cylinder[] sticks = new Cylinder[N*N];
        int[] count = new int[N*N];
        int[][][] board = new int[N][N][N];
        for(int i=0; i<N; i++)
            for(int j=0; j<N; j++) Arrays.fill(board[i][j], 0);

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            {
                Cylinder stick = new Cylinder(STICK_RADIUS,STICK_H);
                stick.setMaterial(new PhongMaterial(Color.GRAY));
                stick.setDrawMode(DrawMode.FILL);
                stick.setTranslateX(i * STICK_DIST);
                stick.setTranslateY(-STICK_H/2);
                stick.setTranslateZ((j * STICK_DIST));
                stick.setOnMouseClicked(event->{
                    if(game.getGameState() != TicTacToe3D.GameState.STARTED) {
                        return;
                    }
                    double tx = stick.getTranslateX();
                    double tz = stick.getTranslateZ();
                    int X = (int)(tx / STICK_DIST);
                    int Y = (int)(tz / STICK_DIST);
                    int Z = game.getStickElementsCount(X,Y);

                    if(!game.makeMove(player,X,Y)) return;

                    if(gameMode==GameMode.PvP) {
                        currentColor = currentColor==COLOR_1?COLOR_2:COLOR_1;
                        player=player==1?2:1;
                    }
                    else {
                        // ruch AI
                        // Tutaj wywolac game.makeMove()
                        game.makeMove(2,0,0);
                    }
                });
                sticks[i + j * N] = stick;

            }
        // Podstawka planszy
        Box surface = new Box(N * STICK_DIST, 0.5, N * STICK_DIST);
        surface.setTranslateX((N - 1) * STICK_DIST / 2);
        surface.setTranslateZ((N - 1) * STICK_DIST / 2);
        surface.setMaterial(new PhongMaterial(Color.BROWN));
        surface.setDrawMode(DrawMode.FILL);
        // Przesuniecie kamery
        Translate pivot = new Translate((N - 1) * STICK_DIST / 2,0,(N - 1) * STICK_DIST / 2);
        yCameraRotate = new Rotate(yCameraAngle, Rotate.Y_AXIS);
        xCameraRotate = new Rotate(xCameraAngle, Rotate.X_AXIS);
        cameraTranslate = new Translate(0, 0, -48);
        // Create and position camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(
                pivot,
                yCameraRotate,
                xCameraRotate,
                cameraTranslate);

        // Build the Scene Graph
        root.getChildren().add(camera);
        root.getChildren().addAll(sticks);
        root.getChildren().addAll(surface);

        // Use a SubScene
        SubScene subScene = new SubScene(root, 800, 500, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.LIGHTYELLOW);
        subScene.setCamera(camera);
        subScene.setOnMousePressed(event -> {
            lastMouseX = event.getSceneX();
            lastMouseY = event.getSceneY();
            yCameraAngle = yCameraRotate.getAngle();
            xCameraAngle = xCameraRotate.getAngle();
        });

        subScene.setOnMouseReleased(event -> {
            yCameraAngle = yCameraRotate.getAngle();
            xCameraAngle = xCameraRotate.getAngle();
        });
        subScene.setOnMouseDragged(event -> {
            double dx = (event.getSceneX() - lastMouseX) * 180 / subScene.getWidth();
            double dy = (event.getSceneY() - lastMouseY) * 70 / subScene.getHeight();
            yCameraRotate.setAngle(yCameraAngle + dx);
            if (xCameraAngle - dy < 0 && xCameraAngle - dy > -90)
                xCameraRotate.setAngle(xCameraAngle - dy);
        });
        subScene.setOnScroll(event -> {
            if (event.getDeltaY() > 0) {
                if (cameraTranslate.getZ() < -MIN_CAMERA_DIST)
                    cameraTranslate.setZ(cameraTranslate.getZ() + 1);
            } else if (cameraTranslate.getZ() > -MAX_CAMERA_DIST)
                cameraTranslate.setZ(cameraTranslate.getZ() - 1);
        });
        window.addEventHandler(KeyEvent.KEY_RELEASED,escPressedHandler);
        anchorPane.getChildren().add(subScene);

        gameFinishedInfo = new Label();
        gameFinishedInfo.setFont(Font.font ("Verdana", 20));
        gameFinishedInfo.setMinWidth(100);

        AnchorPane.setTopAnchor(gameFinishedInfo,10.0);
        AnchorPane.setLeftAnchor(gameFinishedInfo,20.0);
        anchorPane.getChildren().add(gameFinishedInfo);

        return scene;
    }

    private Scene createStartMenu() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("start_menu.fxml"));

        // Prepare comboBoxes for choosing colors
        List<Color> colorList = Arrays.asList(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.PINK, Color.WHITE);

        ComboBox<Color> cbFirst = (ComboBox<Color>) root.lookup("#cbFirstColor");
        ComboBox<Color> cbSecond = (ComboBox<Color>) root.lookup("#cbSecondColor");
        Callback<ListView<Color>, ListCell<Color>> cellFactory = new Callback<ListView<Color>, ListCell<Color>>() {
            @Override
            public ListCell<Color> call(ListView<Color> l) {
                return new ListCell<Color>() {
                    @Override
                    protected void updateItem(Color item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setStyle("-fx-background-color: #" + item.toString().substring(2, 8));
                        }
                    }
                };
            }
        };
        cbFirst.setItems(FXCollections.observableArrayList(colorList));
        cbFirst.setButtonCell(cellFactory.call(null));
        cbFirst.setCellFactory(cellFactory);
        cbFirst.valueProperty().addListener((ov, oldCol, newCol) -> {
            if (newCol == cbSecond.getSelectionModel().getSelectedItem()) {
                cbSecond.getSelectionModel().select(oldCol);
            }
        });
        cbSecond.setItems(FXCollections.observableArrayList(colorList));
        cbSecond.setButtonCell(cellFactory.call(null));
        cbSecond.setCellFactory(cellFactory);
        cbSecond.valueProperty().addListener((ov, oldCol, newCol) -> {
            if (newCol == cbFirst.getSelectionModel().getSelectedItem()) {
                cbFirst.getSelectionModel().select(oldCol);
            }
        });
        cbFirst.getSelectionModel().select(0);
        cbSecond.getSelectionModel().select(1);

        // Add handlers to buttons
        Button btnPvP = (Button) root.lookup("#btnPvP");
        btnPvP.setOnAction(e -> {
            COLOR_1 = cbFirst.getSelectionModel().getSelectedItem();
            COLOR_2 = cbSecond.getSelectionModel().getSelectedItem();
            currentColor = COLOR_1;
            game = new TicTacToe3D(N);
            game.attachOnMoveListener(this);
            try {
                game.registerPlayer("Player 1");
                game.registerPlayer("Player 2");
            }
            catch (TicTacToe3D.GameStartedException ex) {
                ex.printStackTrace();
            }
            gameMode = GameMode.PvP;
            switchScene(createGameArea());
        });
        Button btnPvAIEasy = (Button) root.lookup("#btnPvAIEasy");
        btnPvAIEasy.setOnAction(e -> {
            COLOR_1 = cbFirst.getSelectionModel().getSelectedItem();
            COLOR_2 = cbSecond.getSelectionModel().getSelectedItem();
            currentColor = COLOR_1;
            game = new TicTacToe3D(N);
            game.attachOnMoveListener(this);
            try {
                game.registerPlayer("Player 1");
                game.registerPlayer("AI");
            }
            catch (TicTacToe3D.GameStartedException ex) {
                ex.printStackTrace();
            }
            gameMode = GameMode.PvAI;
            switchScene(createGameArea());
        });
        Button btnPvAIHard = (Button) root.lookup("#btnPvAIHard");
        btnPvAIHard.setOnAction(e -> {
            COLOR_1 = cbFirst.getSelectionModel().getSelectedItem();
            COLOR_2 = cbSecond.getSelectionModel().getSelectedItem();
            currentColor = COLOR_1;
            game = new TicTacToe3D(N);
            game.attachOnMoveListener(this);
            gameMode = GameMode.PvAI;
            switchScene(createGameArea());
        });
        return new Scene(root, 400, 600);
    }

    private void displayMenu(int winner) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("game_menu.fxml"));
        double centerXPosition = window.getX() + window.getWidth() / 2d;
        double centerYPosition = window.getY() + window.getHeight() / 2d;

        Button btnPlayAgain = (Button) root.lookup("#btnPlayAgain");
        Button btnStartMenu = (Button) root.lookup("#btnStartMenu");
        Button btnExit = (Button) root.lookup("#btnExit");

        btnPlayAgain.setOnAction(e -> {
            currentColor = COLOR_1;
            isGameFinished = false;
            window.removeEventHandler(KeyEvent.KEY_RELEASED,escPressedHandler);
            switchScene(createGameArea());
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        });
        btnStartMenu.setOnAction(e -> {
            try {
                isGameFinished = false;
                window.removeEventHandler(KeyEvent.KEY_RELEASED,escPressedHandler);
                switchScene(createStartMenu());
                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        btnExit.setOnAction(e -> {
            window.close();
        });

        Stage stage = new Stage();
        stage.setScene(new Scene(root, 300, 400));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(window);
        stage.setOnShown(ev -> {
            stage.setX(centerXPosition - stage.getWidth() / 2d);
            stage.setY(centerYPosition - stage.getHeight() / 2d);
            stage.show();
        });
        stage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                stage.close();
            }
        });
        stage.show();
    }

    private void switchScene(Scene scene) {
        window.setScene(scene);
        centerStage(window);
    }

    private void centerStage(Stage stage) {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    private EventHandler escPressedHandler = (EventHandler<KeyEvent>) event -> {
        if (KeyCode.ESCAPE == event.getCode()) {
            try {
                displayMenu(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void start(Stage primaryStage) throws IOException {
        window = primaryStage;

        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setMinWidth(320);
        primaryStage.setMinHeight(400);
        primaryStage.setResizable(false);
        primaryStage.setScene(createStartMenu());

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
