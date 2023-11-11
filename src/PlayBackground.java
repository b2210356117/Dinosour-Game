import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;


public class PlayBackground {
    static GameObject platform = new GameObject("platform", 1, 0, 350, 1000, 140);
    static GameObject background = new GameObject("background", 1, 0, 0, 1000, 400);
    static GameObject backTrees = new GameObject("backTrees", 1, 0, 0, 1000, 400);
    static GameObject midTrees = new GameObject("midTrees", 1, 0, 0, 1000, 400);
    static GameObject frontTrees = new GameObject("frontTrees", 1, 0, 0, 1000, 400);


    public static void render(GraphicsContext graphicsContext) throws CloneNotSupportedException {
        loop(background, graphicsContext, 0);
        loop(backTrees, graphicsContext, -1);
        loop(midTrees, graphicsContext, -3);
        loop(frontTrees, graphicsContext, -5);
        loop(platform, graphicsContext, -7);
    }

    public static void loop(GameObject gameObject1, GraphicsContext graphicsContext, double velocity) throws CloneNotSupportedException {
        gameObject1.setVelocityX(velocity);
        GameObject gameObject2 = (GameObject) gameObject1.clone();
        gameObject2.setX(gameObject1.getX() + gameObject1.getWidth());
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                double time = (now - MyGame.startNanoTime) / 1_000_000_000.0;
                if (MyGame.gameOver) {
                    super.stop();
                }

                gameObject2.setX(gameObject2.getX() + (gameObject2.getVelocityX()));
                gameObject1.setX(gameObject1.getX() + (gameObject1.getVelocityX()));

                if (gameObject1.getX()<= -(gameObject1.getWidth())) {
                    gameObject1.setX(gameObject2.getX()+gameObject2.getWidth()+ velocity);
                }
                if (gameObject2.getX()<= -(gameObject2.getWidth())) {
                    gameObject2.setX(gameObject1.getX() + gameObject1.getWidth()+ velocity);
                }

                graphicsContext.drawImage(gameObject1.sprite.getFrame(time), gameObject1.getX(), gameObject1.getY());
                graphicsContext.drawImage(gameObject2.sprite.getFrame(time), gameObject2.getX(), gameObject2.getY());

            }
        }.start();
    }
}
