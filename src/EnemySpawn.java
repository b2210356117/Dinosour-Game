import java.util.ArrayList;
import java.util.Random;

public class EnemySpawn {


    public static void spawn(int level) throws CloneNotSupportedException {
        Random random = new Random();
        int spawnCoordinates = random.nextInt(2000) + 1000;

        ArrayList<Enemy> enemies = new ArrayList<>();
        Enemy bird = new Enemy("bird", 2, spawnCoordinates, 130, 93, 82);
        Enemy cactus = new Enemy("cactus", 1, spawnCoordinates, 251, 49, 101);
        Enemy bigCactus = new Enemy("cactus_big", 1, spawnCoordinates, 251, 102, 104);

        if (level == 0) {
            int bigCactusAmount = 2;
            int cactusAmount = 8;

            for (int i = 0; i < cactusAmount; i++) {
                enemies.add((Enemy) cactus.clone());
            }
            for (int i = 0; i < bigCactusAmount; i++) {
                enemies.add((Enemy) bigCactus.clone());
            }
        } else if (level>0 && level<3) {
            int bigCactusAmount = level*2;
            int cactusAmount = 10 - bigCactusAmount;

            for (int i = 0; i < cactusAmount; i++) {
                enemies.add((Enemy) cactus.clone());
            }
            for (int i = 0; i < bigCactusAmount; i++) {
                enemies.add((Enemy) bigCactus.clone());
            }
        } else {
            int cactusAmount = 5;
            int bigCactusAmount = 3;
            int birdAmount = 2;

            for (int i=0; i<cactusAmount; i++) {
                enemies.add((Enemy) cactus.clone());
            }
            for (int i=0; i<bigCactusAmount; i++) {
                enemies.add((Enemy) bigCactus.clone());
            }
            for (int i=0; i<birdAmount; i++) {
                enemies.add((Enemy) bird.clone());
            }
        }
        for (int i=0; i<enemies.size(); i++) {
            if (i%2 ==0) {
                enemies.get(i).setX( enemies.get(i).getX() + 1000);
            }
        }
        MyGame.obstacles = enemies;
    }

    public static void move(ArrayList<Enemy> enemies) {
        ArrayList<Enemy> outOfScreen = new ArrayList<>();
        Random random = new Random();

        for (Enemy enemy: enemies) {
            double velocity = -(random.nextInt(10) + 2 + MyGame.level/2);
            enemy.setVelocityX(velocity);

            if (enemy.getX() <= -30) {
                outOfScreen.add(enemy);
            }
        }
        MyGame.obstacles.removeAll(outOfScreen);
    }

    public static void delete(ArrayList<Enemy> enemies) {
        ArrayList<Enemy> outOfScreen = new ArrayList<>();
        for (Enemy enemy: enemies) {
            if (enemy.getX() <= -50) {
                outOfScreen.add(enemy);
            }
        }
        MyGame.obstacles.removeAll(outOfScreen);
    }
}
