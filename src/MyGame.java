import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import java.util.ArrayList;


public class MyGame extends Application {
    // JavaFX related objects
    Group root;
    Canvas canvas;
    GraphicsContext graphicsContext;
    AnimationTimer timer;
    EventHandler<KeyEvent> keyPressed;
    EventHandler<KeyEvent> keyReleased;
    ArrayList<String> input = new ArrayList<>(); // key events stay in this list as long as they're active


    // Your game objects
    Player player;
    static ArrayList<Enemy> obstacles = new ArrayList<>();
    static double accelerationTime = 0;
    static int level = 0;
    int scoreCounter = 0;
    int health = 6;
    static boolean gameOver = false;
    boolean startOver = true;


    // Your game parameters
    final int GAME_WIDTH = 1000;
    final int GAME_HEIGHT = 400;
    final static long startNanoTime = System.nanoTime();

    /**
     * Launch a JavaFX application.
     * @param args
     */
    public static void main (String[] args) {
        launch(args);
    }

    /** Start the game scene with defined GAME_WIDTH and GAME_HEIGHT.
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) throws CloneNotSupportedException {
        startOver = false;
        root = new Group();
        root.getChildren().removeAll();

        Scene jScene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);
        primaryStage.setTitle("HUBBM-Dino");
        primaryStage.setScene(jScene);

        player = new Player("samurai_idle", 1, 50, 248, 105, 108);
        player.setVelocityX(5);
        initGame();
        primaryStage.show();
    }

    /** Init game objects and parameters like key event listeners, timers etc.
     */
    public void initGame() throws CloneNotSupportedException {
        // Generate a game canvas where all your objects, texts etc. will be drawn.
        root.getChildren().clear();
        canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        graphicsContext = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        // TODO: Init game objects and parameters like key event listeners, timers etc.

        initKeyEventListeners();
        initGameOver();
        initTimer();
        timer.start();

    }


    /**
     * keyPressed and keyReleased are the two main keyboard event listener objects. You can check which keyboard
     * keys are pressed or released by means of this two objects and make appropriate changes in your game.
     */
    void initKeyEventListeners() {
        canvas.setFocusTraversable(true);
        keyPressed = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String code = event.getCode().toString();
                if ( !input.contains(code))
                    input.add(code);
            }
        };

        keyReleased = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String code = event.getCode().toString();
                input.remove(code);
            }
        };

        canvas.setOnKeyPressed(keyPressed);
        canvas.setOnKeyReleased(keyReleased);
    }

    /**
     * This timer object will call the handle() method at every frame. So, in this method's body, you can
     * redraw your objects to make a movement effect, check whether any of your objects collided or not,
     * and update your game score etc. This is the main lifecycle of your game.
     */
    void initTimer() throws CloneNotSupportedException {
        PlayBackground.render(graphicsContext);
        EnemySpawn.spawn(level);
        EnemySpawn.move(obstacles);

        AudioStream gameScreenAudio = PlayAudio.play("res/gameScreen.wav");
        AudioStream gameOverAudio = PlayAudio.play("res/gameOverScreen.wav");
        AudioPlayer.player.start(gameScreenAudio);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double time = (now - startNanoTime) / 1_000_000_000.0;
                accelerationTime++;
                Image healthSprite = new Image("res/health" + health + ".png");
                graphicsContext.drawImage(healthSprite, 870, 20);
                /* In this method's body, you can  redraw your objects to make a movement effect,
                   check whether any of your objects collided or not,
                   and update your game score etc. This is the main lifecycle of your game.*/

                graphicsContext.setFill( Color.GREENYELLOW);
                graphicsContext.setStroke(Color.INDIANRED);
                Font font = Font.font("Monospaced Bold", FontWeight.BOLD, 25);
                graphicsContext.setFont(font);

                graphicsContext.fillText(("score: " + scoreCounter), 20, 30);
                graphicsContext.strokeText(("score: " + scoreCounter), 20, 30);
                graphicsContext.fillText(("level: " + level), 20, 60);
                graphicsContext.strokeText(("level: " + level), 20, 60);

                graphicsContext.drawImage(player.sprite.getFrame(time), player.getX(), player.getY());
                EnemySpawn.delete(obstacles);

                level = (int) scoreCounter/10;
                for (Enemy enemy: obstacles) {
                    if (!enemy.intersects(player) && !enemy.isCount) {
                        if ((int)(enemy.getX()) < (int)(player.getX())) {
                            enemy.isCount = true;
                            if (!enemy.isDead) {
                                AudioStream scoreAudio = PlayAudio.play("res/score.wav");
                                AudioPlayer.player.start(scoreAudio);
                                scoreCounter++;
                            }
                        }
                    }
                }

                for (Enemy enemy: obstacles) {
                    enemy.setX(enemy.getX() + enemy.getVelocityX());
                    if (enemy.getX() > 1000 && enemy.getX()< 1100) {
                        if (accelerationTime % 50 == 0) {
                            enemy.setX(1100);
                        }
                    }
                }

                for (Enemy enemy: obstacles) {
                    graphicsContext.drawImage(enemy.sprite.getFrame(time), enemy.getX(), enemy.getY());
                }

                for (Enemy enemy: obstacles) {
                    if (enemy.intersects(player)) {
                        enemy.setSprite(enemy.deathSprite);
                        if (!enemy.isDead) {
                            AudioStream hitAudio = PlayAudio.play("res/hit.wav");
                            AudioPlayer.player.start(hitAudio);
                            health--;
                            enemy.isDead = true;
                        }
                        if (health == 0) {
                            gameOver = true;
                        }
                    }
                }

                if (obstacles.size() == 0) {
                    try {
                        EnemySpawn.spawn(level);
                        EnemySpawn.move(obstacles);
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                }


                if (player.isJumping) {
                    double gravity = 1.5;
                    player.setVelocityY(player.getVelocityY() - gravity);
                    player.setY(player.getY() - player.getVelocityY());

                    if (player.getY() > 248) {
                        player.setY(248);
                        player.setVelocityY(0);
                        player.isJumping = false;
                    }
                }

                player.setVelocityX(player.getVelocityX() + accelerationTime/1000000);
                if (input.contains("RIGHT")) {
                    player.setSprite("samurai_run", 4);
                    player.setX(player.getX() + player.getVelocityX());
                    if (player.getX()>925) {
                        player.setX(925);
                    }
                }
                if (input.contains("LEFT")) {
                    player.setX(player.getX() - player.getVelocityX());
                    if (player.getX()<-25) {
                        player.setX(-25);
                    }
                }

                if (input.contains("UP")) {
                    if (!player.isJumping) {
                        player.isJumping = true;
                        player.setVelocityY(30);
                    }
                }
                if (gameOver) {
                    AudioPlayer.player.stop(gameScreenAudio);
                    AudioPlayer.player.start(gameOverAudio);
                    super.stop();
                }
            }
        };
    }

    public void initGameOver() throws CloneNotSupportedException {
        Image playerIdle = new Image("res/samurai_idle1.png");
        Image bg = new Image("res/bg.png");
        Image platform = new Image("res/platform1.png");

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                double time = (now - startNanoTime) / 1_000_000_000.0;
                graphicsContext.drawImage(bg, 0, 0);
                graphicsContext.drawImage(platform, 0, 350);

                for (Enemy enemy: obstacles) {
                    graphicsContext.drawImage(enemy.sprite.getFrame(time), enemy.getX(), enemy.getY());
                }
                graphicsContext.drawImage(playerIdle, player.getX(), player.getY());

                graphicsContext.setFill( Color.GREENYELLOW);
                graphicsContext.setStroke(Color.INDIANRED);
                Font font = Font.font("Monospaced Bold", FontWeight.BOLD, 70);
                Font fontSmall = Font.font("Monospaced Bold", FontWeight.BOLD, 35);
                graphicsContext.setFont(font);
                graphicsContext.fillText("GAME OVER!", 305, 130);
                graphicsContext.strokeText("GAME OVER!", 305, 130);

                graphicsContext.setFont(fontSmall);
                graphicsContext.fillText("(Press ENTER to START over.)", 270, 180);
                graphicsContext.strokeText("(Press ENTER to START over.)", 270, 180);

                if (input.contains("ENTER")) {
                    gameOver = false;
                    startOver = true;
                    super.stop();
                }
            }
        }.start();
        if (startOver) {
            timer.stop();
            initGame();
        }
    }
}

