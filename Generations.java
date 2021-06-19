import java.util.ArrayList;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Generations extends ArrayList<Player> {
    public Generations evolve(){
        // write down generational information in text file
        for(int x = 0; x < this.size(); x++) {
            try {
                Files.write(Paths.get("GenerationInformation.txt"), ("\n" + x + "  Right: " +
                        (this.get(x).sc1.right + this.get(x).sc2.right) + "   Left: "
                                + (this.get(x).sc1.left + this.get(x).sc2.left) + "   Shoot: "
                        + (this.get(x).sc1.shoot + this.get(x).sc2.shoot)).getBytes(), StandardOpenOption.APPEND);
            }
            catch(Exception ex){}
        }
        Random rand = new Random();
        // find highest fitness levels
        int apex = 0;
        for(int x = 1; x < this.size(); x++) {
            if(this.get(apex).fitness < this.get(x).fitness) {
                apex = x;
            }
        }
        // using dna of apex, create new generation
        Generations temp = new Generations();
        // add one version of the previous generation
        this.get(apex).enemies.main = true;
        temp.add(this.get(apex));
        for(int x = 1; x < this.size(); x++) {
            // so the mutation is going to randomly select whether or not each tendancy
            // goes down or up, it will then select a random number within the two bounds
            // since a specimen supposedly has superior fitness, we will make it more
            // difficult for too much straying from the norm
            // there are six variables that need to be changed
            int r1Mut = rand.nextInt(1);
            int r2Mut = rand.nextInt(1);
            int l1Mut = rand.nextInt(1);
            int l2Mut = rand.nextInt(1);
            int s1Mut = rand.nextInt(1);
            int s2Mut = rand.nextInt(1);
            switch(r1Mut) {
                case 0:
                    r1Mut = (this.get(apex).sc1.right) + rand.nextInt(250);
                    if(r1Mut > 1000) {
                        r1Mut = 1000;
                    }
                    break;
                case 1:
                    r1Mut = Math.abs((this.get(apex).sc1.right) - rand.nextInt(250));
                    if(r1Mut < 0) {
                        r1Mut = 1;
                    }
                    break;
            }
            
            switch(r2Mut) {
                case 0:
                    r2Mut = (this.get(apex).sc2.right) + rand.nextInt(500);
                    if(r2Mut > 1000) {
                        r2Mut = 1000;
                    }
                    break;
                case 1:
                    r2Mut = Math.abs((this.get(apex).sc2.right) - rand.nextInt(500));
                    if(r2Mut < 0) {
                        r2Mut = 1;
                    }
                    break;
            }
            switch(l1Mut) {
                case 0:
                    l1Mut = (this.get(apex).sc1.left) + rand.nextInt(500);
                    if(l1Mut > 1000) {
                        l1Mut = 1000;
                    }
                    break;
                case 1:
                    l1Mut = Math.abs((this.get(apex).sc1.left) - rand.nextInt(500));
                    if(l1Mut < 0) {
                        l1Mut = 1;
                    }
                    break;
            }
            switch(l2Mut) {
                case 0:
                    l2Mut = (this.get(apex).sc2.left) + rand.nextInt(500);
                    if(l2Mut > 1000) {
                        l2Mut = 1000;
                    }
                    break;
                case 1:
                    l2Mut = Math.abs((this.get(apex).sc2.left) - rand.nextInt(500));
                    if(l2Mut < 0) {
                        l2Mut = 1;
                    }
                    break;
            }
            switch(s1Mut) {
                case 0:
                    s1Mut = (this.get(apex).sc1.shoot) + rand.nextInt(500);
                    if(s1Mut > 1000) {
                        s1Mut = 1000;
                    }
                    break;
                case 1:
                    s1Mut = Math.abs((this.get(apex).sc1.shoot) - rand.nextInt(500));
                    if(s1Mut < 0) {
                        s1Mut = 1;
                    }
                    break;
            }
            switch(s2Mut) {
                case 0:
                    s2Mut = (this.get(apex).sc2.shoot) + rand.nextInt(500);
                    if(s2Mut > 1000) {
                        s2Mut = 1000;
                    }
                    break;
                case 1:
                    s2Mut = Math.abs((this.get(apex).sc2.shoot) - rand.nextInt(500));
                    if(s2Mut < 0) {
                        s2Mut = 1;
                    }
                    break;
            }
            temp.add(new Player(new PlayerSituations(r1Mut, l1Mut, s1Mut), new PlayerSituations(r2Mut, l2Mut, s2Mut), false));
        }
        return temp;
    }
}
