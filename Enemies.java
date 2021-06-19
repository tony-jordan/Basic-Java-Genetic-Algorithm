import javafx.scene.image.*;
import java.util.Random;
import java.util.ArrayList;

public class Enemies extends Thread{
    public Enemy[] enemies = new Enemy[55];
    public Player p;
    public boolean shotReady = true;
    public boolean right = true;
    public boolean main;
    // create a constructor that takes in what level 
    // the AI is on so it can match that difficulty
    public Enemies(Player p, boolean main) {
        this.p = p;
        this.main = main;
        
        Random rand = new Random();
        // I need to create all of the enemies using an array, and then
        // I also need to figure out how I'm gonna get them to move.
        double xPos = 150;
        double yPos = 60;
        for(int x = 0; x < this.enemies.length; x++) {
            // I'm gonna use random numbers to figure out what form each enemy will take
            // since there are multiple variations
            int test = rand.nextInt(3);
            if(test == 0) {
                this.enemies[x] = new Enemy(new Image("e1.png"));
                this.enemies[x].setFitHeight(55);
                this.enemies[x].setFitWidth(55);
                //SpaceInvaders.pane.getChildren().add(this.enemies[x]);
                this.enemies[x].setX(xPos);
                this.enemies[x].setY(yPos);
            }
            else if(test == 1) {
                this.enemies[x] = new Enemy(new Image("e2.png"));
                this.enemies[x].setFitHeight(55);
                this.enemies[x].setFitWidth(55);
                //SpaceInvaders.pane.getChildren().add(this.enemies[x]);
                this.enemies[x].setX(xPos);
                this.enemies[x].setY(yPos);
            }
            else {
                this.enemies[x] = new Enemy(new Image("e3.png"));
                this.enemies[x].setFitHeight(55);
                this.enemies[x].setFitWidth(55);
                //SpaceInvaders.pane.getChildren().add(this.enemies[x]);
                this.enemies[x].setX(xPos);
                this.enemies[x].setY(yPos);
            }
            xPos += 60;
            if((x + 1) % 11 == 0 && x != 0) {
                yPos += 50;
                xPos = 150;
            }
            if(!main) {
                this.enemies[x].setOpacity(.3);
                this.p.playerImage.setOpacity(.3);
            }
        }
    }
    // big ol animation thread
    @Override
    public void run() {
        /*
        boolean right = true;
        while(true) {
            // search all images for the leftmost or rightmost image
            if(right) {
                for(int q = 0; q < this.enemies.length; q++) {
                    if(this.enemies[q].getX() >= 910 && this.enemies[q].alive) {
                        right = false;
                        for(int x = 0; x < this.enemies.length; x++) {
                            this.enemies[x].setY(this.enemies[x].getY() + 10);
                        }
                        break;
                    }
                }
            }
            else {
                for(int q = 0; q < this.enemies.length; q++) {
                    if(this.enemies[q].getX() <= 0 && this.enemies[q].alive) {
                        right = true;
                        for(int x = 0; x < this.enemies.length; x++) {
                            this.enemies[x].setY(this.enemies[x].getY() + 10);
                        }
                        break;
                    }
                }
            }
            if(right) {
                for(int x = 0; x < this.enemies.length; x++) {
                    this.enemies[x].setX(this.enemies[x].getX() + 10);
                    if((x + 1) % 11 == 0) {
                        try {
                            this.sleep(50);
                        }
                        catch(Exception ex) {};
                    }
                }
                try {
                        this.sleep(1000);
                    }
                    catch(Exception ex) {};
            }
            else {
                for(int x = 0; x < this.enemies.length; x++) {
                    this.enemies[x].setX(this.enemies[x].getX() - 10);
                    if((x + 1) % 11 == 0) {
                        try {
                            this.sleep(50);
                        }
                        catch(Exception ex) {};
                    }
                }
                try {
                        this.sleep(1000);
                    }
                    catch(Exception ex) {};
            }
        }
*/
    }
    
    // method for getting all of the alive enemies on the bottom row
    public ArrayList<Integer> getAlive() {
        ArrayList<Integer> alive = new ArrayList<>();
        try {
            for(int x = 0; x < 11; x++) {
                for(int y = 4; y >= 0; y--) {
                    if(this.enemies[x + (11*y)].alive) {
                        alive.add(x + (11 * y));
                        break;
                    }
                }
            }
        }
        catch(Exception ex) {}
        return alive;
    }
    public ArrayList<Integer> aliveNum() {
        ArrayList<Integer> alive = new ArrayList<>();
        for(int x = 0; x < 55; x++) {
            if(enemies[x].alive) {
                alive.add(x);
            }
        }
        return alive;
    }
}
