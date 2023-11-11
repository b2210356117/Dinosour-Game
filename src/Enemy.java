import javafx.scene.image.Image;

public class Enemy extends GameObject {
    boolean isCount;
    boolean isDead;
    AnimatedImage deathSprite;

    public Enemy(String name, int frameNumber, double x, double y, double width, double height) {
        super(name, frameNumber, x, y, width, height);
        isCount = false;
        isDead = false;
        setDeathSprite(name);
    }

    public void setDeathSprite(String name) {
        double duration = 0.200 + MyGame.accelerationTime/100000;
        Image[] imageArray = new Image[1];
        for (int i=1; i<=1; i++) {
            imageArray[i-1] = new Image("res/" + name + "_dead1.png");
        }
        deathSprite = new AnimatedImage(imageArray, duration);
    }
}
