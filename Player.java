import javafx.scene.image.*;
import javafx.scene.shape.Line;
import java.util.ArrayList;

public class Player extends Thread {
    // points for fitness
    public int fitness = 0;
    public ArrayList<Line> activeLazers;
    public boolean shotReady = true;
    public boolean dead = false;
    public Enemies enemies;
    public PlayerSituations sc1, sc2;
    public long time;
    // init image
    public ImageView playerImage = new ImageView(new Image("p1.png"));
    // add player constructor
    // so we need to create some doubles between 0 and 3 that represent the player's actions based 
    // on certain situations. So if the AI recognizes a specific situation, it will
    // make it's decision based it's random tendancy number
    public Player(PlayerSituations sc1, PlayerSituations sc2, boolean main) {
        this.activeLazers = new ArrayList<>();
        this.sc1 = sc1; 
        this.sc2 = sc2;
        //SpaceInvaders.pane.getChildren().add(this.playerImage);
        this.playerImage.setFitHeight(90);
        this.playerImage.setFitWidth(150);
        this.playerImage.setX(400);
        this.playerImage.setY(700);
        // so each enemy has to be specific to each player because 
        // they can't destroy each other's enemies in order
        // for my genetic algorithm to work
        this.enemies = new Enemies(this, main);
    }
    
    @Override
    public void run() {
        // start time to record how long each player stays alive
        long startTime = System.currentTimeMillis();
        // this is essentially our fitness method. It calculates and 
        // remembers each player's fitness.
        // It will only stop when the player is dead,
        // The fitnesses between the players will then be compared, and the players with the higherst
        // fitness will pass on their genes
        if(this.dead) {
            this.time = System.currentTimeMillis() - startTime;
            fitness += (time/1000);
        }
        
    }
}
