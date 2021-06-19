import java.util.Random;
public class PlayerSituations {
    public int right;
    public int left;
    public int shoot;
    public Player player;
    // so there are two situations it the gameplay that can occur that are really important,
    // when the player is being shot at, and when the player is not being shot at.
    // so we're going to create a set of 100 moves the player can make, and base what it can
    // do off of how high each of its tendancy variables is, for example, if A player has tendancies
    // of 90, 5 and 3 (they're going to add up to 100, might make the
    // number different in the future though) 90% of the time, the player will preform
    // a specific movement, and this randomness is what the genetic algorithm will solve
    
    // creating a seperate class for tendencies also allows me to add tendencies as I see fit, to allow
    // for some solid optimization.
    public PlayerSituations() {
        Random rand = new Random();
        
        this.right = rand.nextInt(1000);
        this.left = rand.nextInt(1000);
        this.shoot = rand.nextInt(1000);
    }
    public PlayerSituations(int right, int left, int shoot) {
        this.right = right;
        this.left = left;
        this.shoot = shoot;
    }
    public double invoke() {
        Random rand = new Random();
        // basically roll random numbers between 0 and all of the individual doubles,
        // higher numbers will have a higher chance of being selected, so we can
        // count on higher numbers being selected more
        try {
            int rightTest = rand.nextInt(Math.abs(this.right));
            int leftTest = rand.nextInt(Math.abs(this.left));
            int shootTest = rand.nextInt(Math.abs(this.shoot));
            
            // return 0 1 or 2 based on the highest number
            if(rightTest > leftTest && rightTest > shootTest)
                return 0.0;
            else if((leftTest > rightTest && leftTest > shootTest))
                return 1.0;
            else if((shootTest > leftTest && shootTest > rightTest))
                return 2.0;
            else return 3.0;
        }
        catch(Exception ex) {
            int rightTest = rand.nextInt(1000);
            int leftTest = rand.nextInt(1000);
            int shootTest = rand.nextInt(1000);
        
            // return 0 1 or 2 based on the highest number
            if(rightTest > leftTest && rightTest > shootTest)
                return 0.0;
            else if((leftTest > rightTest && leftTest > shootTest))
                return 1.0;
            else if((shootTest > leftTest && shootTest > rightTest))
                return 2.0;
            else return 3.0;
        }
        // the hope is, while the AI is going through it's rounds, it will recognize differnt scenarios,
        // and make the best decision on what to do based on trial and error with this system.
    }
}
