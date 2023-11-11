import javafx.scene.image.Image;

public class Player extends GameObject {
    public int score;
    public boolean isJumping;


    public Player(String name, int frameNum, double x, double y, double width, double height) {
        super(name, frameNum, x, y, width, height);
    }

    public void jump() {
            int gravity = -5;
            this.setVelocityY(this.getVelocityY() - gravity);
            this.setY(this.getY() - this.getVelocityY());
        System.out.println(this.getY());

            if (this.getY() > 202) {
                this.setY(202);
                this.setVelocityY(0);
                this.isJumping = false;
            }
    }

}
