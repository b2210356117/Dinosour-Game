import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;



/**
 * With this object, you can store information of position like x, y, width, height of your object.
 * Also you can store image of your object and render it.
 */
public class GameObject implements Cloneable {
    AnimatedImage sprite; // instead of one image, we save an imageArray to the gameObject
    private double x, y;
    private double width, height;
    private double velocityX, velocityY;
    private final double gravity = -10;


    public GameObject(String name, int frameNumber, double x, double y, double width, double height) {
        setSprite(name, frameNumber);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    public void render(GraphicsContext graphicsContext) {
        // Hint: You can take a GraphicsContext object as a parameter and call drawImage() method to draw an image on your game canvas.
    }

    public void setSprite(String name, int frameNumber) {
        double duration = 0.200 + MyGame.accelerationTime/100000; // may change
        Image[] imageArray = new Image[frameNumber];
        for (int i=1; i<=frameNumber; i++) {
            imageArray[i-1] = new Image("res/" + name + i + ".png");
        }
        sprite = new AnimatedImage(imageArray, duration);
    }
    public void setSprite(AnimatedImage sprite) { this.sprite= sprite;}

    public AnimatedImage getSprite() {
        return sprite;
    }
    public void setX(double x) {
        this.x = x;
    }

    // TODO: this implementation adds exception to all over MyGame, change it if you have time.
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Rectangle2D getHitbox() {
        return new Rectangle2D(x, y, width-20, height-20);
    }
    public boolean intersects (GameObject gameObject) {
        return gameObject.getHitbox().intersects(this.getHitbox());
    }

    public void setY(double y) {
        this.y = y;
    }
    public void setWidth(double width) {
        this.width = width;
    }
    public void setHeight(double height) { this.height = height; }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getVelocityX() {
        return velocityX;
    }
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }
    public double getVelocityY() {
        return velocityY;
    }
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }
}
