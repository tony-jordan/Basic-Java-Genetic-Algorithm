// Project: Final Project
// Name: Tony Jordan
// Date: 11/23/2020
// Description: Create a space invaders game, then create
// an AI to play it really well.

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SpaceInvaders extends Application {
    public static Pane pane = new Pane();
    public static boolean allDead = false;
    public static boolean going = false;
    public static Text scoreText, timeText;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // add in menu type stuff
        Rectangle btmMenu = new Rectangle(1000, 200);
        btmMenu.relocate(0, 820);
        Rectangle topMenu = new Rectangle(1000, 200);
        topMenu.relocate(0, -150);
        pane.getChildren().addAll(btmMenu, topMenu);
        
        scoreText = new Text("0");
        
        scoreText.setScaleX(3);
        scoreText.setScaleY(3);
        scoreText.relocate(900, 10);
        scoreText.setFill(Color.WHITE);
        
        Text score = new Text("Highest Score: ");
        score.setFill(Color.WHITE);
        score.relocate(700, 10);
        score.setScaleX(3);
        score.setScaleY(3);
        
        pane.getChildren().addAll(score, scoreText);
        
        
        Scene scene = new Scene(pane, 1000, 900);
        scene.setFill(Color.LIGHTSKYBLUE);
        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(scene);
        primaryStage.show();

        // start everything
        getItGoin(50);
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    private void getItGoin(int playerNum) {

        // make an arraylist of timelines because javafx does not work well with 
	// any changes made to the GUI in classes that do not have the main method
        ArrayList<Timeline> timelines = new ArrayList<>();
        Generations generation = new Generations();
        for(int q = 0; q < playerNum; q++) {
            Player player;
            if(q == 0) {
                player = new Player(new PlayerSituations(), new PlayerSituations(), true);
                player.start();
                generation.add(player);
                pane.getChildren().add(player.playerImage);
            }
            else {
                player = new Player(new PlayerSituations(), new PlayerSituations(), false);
                generation.add(player);
                pane.getChildren().add(player.playerImage);
            }
            for(int p = 0; p < player.enemies.enemies.length; p++) {
                pane.getChildren().add(player.enemies.enemies[p]);
            }

            // Make a timeline that continuously checks to see if every AI has failed,
            // it will then alter the genetic algorithm based on these changes
            EventHandler<ActionEvent> event = w -> {
                if(!allDead) {
                    boolean ded = true;
                    for(int x = 0; x < generation.size(); x++) {
                        if(!generation.get(x).dead) {
                            ded = false;
                        }
                    }
                    if(ded) {
                        allDead = true;
                        // restart everything
                        for(int x = 0; x < timelines.size()-1; x++) {
                            timelines.get(x).stop();
                        }
                        getItGoin(generation.evolve());
                        int largest = 0;
                        for(int x = 1; x < generation.size(); x++) {
                            if(generation.get(largest).fitness < generation.get(x).fitness) {
                                largest = x;
                            }
                        }
                        scoreText.setText(Integer.toString(generation.get(largest).fitness));
                        timelines.get(timelines.size()-1).stop();
                    }
                }
            };
            Timeline Tracker = new Timeline(new KeyFrame(Duration.millis(generation.size() * 40), event));
            Tracker.setCycleCount(1);
            
            EventHandler<ActionEvent> play = ex -> {
                Tracker.play();
                // check to see what move the player should make
                try {
                    if(!player.dead && player.activeLazers.size() > 0 && player.activeLazers.get(0).getStartX() == player.playerImage.getX()) {
                        double result = player.sc1.invoke();
                        if(result == 0.0) {
                            if(player.playerImage.getX() < 860) {
                                player.playerImage.setX(player.playerImage.getX() + 10);
                            }
                            else {
                                player.dead = true;
                                pane.getChildren().remove(player.playerImage);
                                pane.getChildren().removeAll(player.enemies.enemies);
                            }
                        }
                        else if(result == 1.0) {
                            if(player.playerImage.getX() > 20) {
                                player.playerImage.setX(player.playerImage.getX() - 10);
                            }
                            else {
                                player.dead = true;
                                pane.getChildren().remove(player.playerImage);
                                pane.getChildren().removeAll(player.enemies.enemies);
                            }
                        }
                        else if(result == 2.0 && player.shotReady) {
                            player.shotReady = false;
                            Line lazer = new Line(player.playerImage.getX()+75, player.playerImage.getY(), player.playerImage.getX()+75, player.playerImage.getY() - 10);
                            pane.getChildren().add(lazer);
                            EventHandler<ActionEvent> shoot = t -> {
                                lazer.setStartY(lazer.getStartY() - 1);
                                lazer.setEndY(lazer.getEndY() - 1);
                                for(int x = 0; x < player.enemies.enemies.length; x++) {
                                    if(lazer.getBoundsInParent().intersects(player.enemies.enemies[x].getBoundsInParent()) &&
                                     player.enemies.enemies[x].alive) {
                                        pane.getChildren().removeAll(player.enemies.enemies[x], lazer);
                                        player.enemies.enemies[x].alive = false;
                                        lazer.setStartX(0);
                                        lazer.setStartY(0);
                                        lazer.setEndX(0);
                                        lazer.setEndY(0);
                                        player.shotReady = true;
                                        player.fitness++;
                                    }
                                    if(lazer.getEndY() == 0) {
                                        player.shotReady = true;
                                        player.fitness--;
                                    }
                                }
                            };
                            Timeline pShoot = new Timeline(new KeyFrame(Duration.millis(3), shoot));
                            pShoot.setCycleCount(Timeline.INDEFINITE);
                            pShoot.play();
                            timelines.add(pShoot);
                        }
                    }
                    else if(!player.dead){
                        double result = player.sc2.invoke();
                        if(result == 0.0) {
                            if(player.playerImage.getX() < 860) {
                                player.playerImage.setX(player.playerImage.getX() + 10);
                            }
                            else {
                                player.dead = true;
                                pane.getChildren().remove(player.playerImage);
                                pane.getChildren().removeAll(player.enemies.enemies);
                            }
                        }
                        if(result == 1.0) {
                            if(player.playerImage.getX() >= 20) {
                                player.playerImage.setX(player.playerImage.getX() - 10);
                            }
                            else {
                                player.dead = true;
                                pane.getChildren().remove(player.playerImage);
                                pane.getChildren().removeAll(player.enemies.enemies);
                            }
                        }
                        if(result == 2.0 && player.shotReady) {
                            player.shotReady = false;
                            Line lazer = new Line(player.playerImage.getX()+75, player.playerImage.getY(), player.playerImage.getX()+75, player.playerImage.getY() - 10);
                            pane.getChildren().add(lazer);
                            EventHandler<ActionEvent> shoot = t -> {
                                lazer.setStartY(lazer.getStartY() - 1);
                                lazer.setEndY(lazer.getEndY() - 1);
                                for(int x = 0; x < player.enemies.enemies.length; x++) {
                                    if(lazer.getBoundsInParent().intersects(player.enemies.enemies[x].getBoundsInParent()) &&
                                            player.enemies.enemies[x].alive) {
                                        pane.getChildren().removeAll(player.enemies.enemies[x], lazer);
                                        player.enemies.enemies[x].alive = false;
                                        lazer.setStartX(0);
                                        lazer.setStartY(0);
                                        lazer.setEndX(0);
                                        lazer.setEndY(0);
                                        player.shotReady = true;
                                        player.fitness++;
                                        break;
                                    }
                                    if(lazer.getEndY() == 0) {
                                        player.shotReady = true;
                                        player.fitness--;
                                    }
                                }
                            };
                            Timeline pShoot = new Timeline(new KeyFrame(Duration.millis(3), shoot));
                            pShoot.setCycleCount(Timeline.INDEFINITE);
                            pShoot.play();
                            timelines.add(pShoot);
                        }
                    }
                }
                catch (Exception p) {
                    
                }
            };
            Timeline PlayerTL = new Timeline(new KeyFrame(Duration.millis(10), play));
            PlayerTL.setCycleCount(Timeline.INDEFINITE);
            PlayerTL.play();
            timelines.add(PlayerTL);
            EventHandler<ActionEvent> enemy = ep -> {
                // search all images for the leftmost or rightmost image
                if(player.enemies.right) {
                    for(int p = 0; p < player.enemies.enemies.length; p++) {
                        if(player.enemies.enemies[p].getX() >= 910 && player.enemies.enemies[p].alive) {
                            player.enemies.right = false;
                            for(int x = 0; x < player.enemies.enemies.length; x++) {
                                player.enemies.enemies[x].setY(player.enemies.enemies[x].getY() + 10);
                            }
                            break;
                        }
                    }
                }
                else {
                    for(int p = 0; p < player.enemies.enemies.length; p++) {
                        if(player.enemies.enemies[p].getX() <= 0 && player.enemies.enemies[p].alive) {
                            player.enemies.right = true;
                            for(int x = 0; x < player.enemies.enemies.length; x++) {
                                player.enemies.enemies[x].setY(player.enemies.enemies[x].getY() + 10);
                            }
                            break;
                        }
                    }
                }
                if(player.enemies.right) {
                    for(int x = 0; x < player.enemies.enemies.length; x++) {
                        player.enemies.enemies[x].setX(player.enemies.enemies[x].getX() + 10);
                    }
                }
                else {
                    for(int x = 0; x < player.enemies.enemies.length; x++) {
                        player.enemies.enemies[x].setX(player.enemies.enemies[x].getX() - 10);
                    }
                }
            };
            Timeline ETL = new Timeline(new KeyFrame(Duration.millis(player.enemies.aliveNum().size() * 20), enemy));
            ETL.setCycleCount(Timeline.INDEFINITE);
            ETL.play();
            timelines.add(ETL);
            try {
                EventHandler<ActionEvent> check = e -> {
                    ArrayList<Integer> alive = player.enemies.getAlive();
                    for(int x = 0; x < alive.size(); x++) {
                        if(player.enemies.enemies[alive.get(x)].getX() == player.playerImage.getX() + 20 && !player.dead) {

                            Line line = new Line(player.enemies.enemies[alive.get(x)].getX() +25, player.enemies.enemies[alive.get(x)].getY() +20, player.enemies.enemies[alive.get(x)].getX()+25, player.enemies.enemies[alive.get(x)].getY() + 30);
                            player.activeLazers.add(line);
                            pane.getChildren().add(line);
                            EventHandler<ActionEvent> shoot = ex -> {
                                line.setStartY(line.getStartY() + 1);
                                line.setEndY(line.getEndY() + 1);
                                if (line.getBoundsInParent().intersects(player.playerImage.getBoundsInParent())) {
                                    player.activeLazers.remove(line);
                                    pane.getChildren().remove(player.playerImage);
                                    pane.getChildren().remove(line);
                                    player.dead = true;
                                    pane.getChildren().removeAll(player.enemies.enemies);
                                    player.fitness--;
                                }
                                if(line.getEndY() >= 900) {
                                    player.activeLazers.remove(line);
                                    pane.getChildren().remove(line);
                                    player.fitness++;
                                }  
                            };
                            Timeline shootL = new Timeline(new KeyFrame(Duration.millis(3), shoot));
                            shootL.setCycleCount(Timeline.INDEFINITE);

                            shootL.play();
                            timelines.add(shootL);
                            
                        }
                    }
                };
                Timeline lazers = new Timeline(new KeyFrame(Duration.millis(2100), check));
                lazers.setCycleCount(Timeline.INDEFINITE);
                lazers.play();
                timelines.add(lazers);
            }
            catch(Exception p){}
            timelines.add(Tracker);
            
        }
    }
    
    // this method pretty much does the same thing as the previous one, except
    // this one is made specifically for after the first generation has ended.
    private Generations getItGoin(Generations generation) {
        pane.getChildren().removeIf(Line.class::isInstance);
        ArrayList<Timeline> timelines = new ArrayList<>();
        allDead = false;
        generation = generation.evolve();
        Generations tempGen = new Generations();
        for(int q = 0; q < generation.size(); q++) {
            Player player;
            if(q == 0) {
                player = new Player(new PlayerSituations(generation.get(q).sc1.right, generation.get(q).sc1.left, generation.get(q).sc1.shoot),
                        new PlayerSituations(generation.get(q).sc2.right, generation.get(q).sc2.left, generation.get(q).sc2.shoot), true);
                player.start();
                tempGen.add(player);
                pane.getChildren().add(player.playerImage);
            }
            else {
                player = new Player(new PlayerSituations(generation.get(q).sc1.right, generation.get(q).sc1.left, generation.get(q).sc1.shoot),
                        new PlayerSituations(generation.get(q).sc2.right, generation.get(q).sc2.left, generation.get(q).sc2.shoot), false);
                tempGen.add(player);
                pane.getChildren().add(player.playerImage);
            }
            for(int p = 0; p < player.enemies.enemies.length; p++) {
                pane.getChildren().add(player.enemies.enemies[p]);
            }

             EventHandler<ActionEvent> event = w -> {
                if(!allDead) {
                    boolean ded = true;
                    for(int x = 0; x < tempGen.size(); x++) {
                        if(!tempGen.get(x).dead) {
                            ded = false;
                        }
                    }
                    if(ded) {
                        allDead = true;
                        // restart everything
                        for(int x = 0; x < timelines.size()-1; x++) {
                            timelines.get(x).stop();
                        }
                        getItGoin(tempGen.evolve());
                        int largest = 0;
                        for(int x = 1; x < tempGen.size(); x++) {
                            if(tempGen.get(largest).fitness < tempGen.get(x).fitness) {
                                largest = x;
                            }
                        }
                        scoreText.setText(Integer.toString(tempGen.get(largest).fitness));
                        timelines.get(timelines.size()-1).stop();  
                    }
                }
            };
            Timeline Tracker = new Timeline(new KeyFrame(Duration.millis(40 * tempGen.size()), event));
            Tracker.setCycleCount(1);

            EventHandler<ActionEvent> play = ex -> {
                Tracker.play();
                // check to see what move the player should make
                try {
                    if(!player.dead && player.activeLazers.size() > 0 && player.activeLazers.get(0).getStartX() == player.playerImage.getX()) {
                        double result = player.sc1.invoke();
                        if(result == 0.0) {
                            if(player.playerImage.getX() < 860) {
                                player.playerImage.setX(player.playerImage.getX() + 10);
                            }
                            else {
                                player.dead = true;
                                pane.getChildren().remove(player.playerImage);
                                pane.getChildren().removeAll(player.enemies.enemies);
                            }
                        }
                        else if(result == 1.0) {
                            if(player.playerImage.getX() > 20) {
                                player.playerImage.setX(player.playerImage.getX() - 10);
                            }
                            else {
                                player.dead = true;
                                pane.getChildren().remove(player.playerImage);
                                pane.getChildren().removeAll(player.enemies.enemies);
                            }
                        }
                        else if(result == 2.0 && player.shotReady) {
                            player.shotReady = false;
                            Line lazer = new Line(player.playerImage.getX()+75, player.playerImage.getY(), player.playerImage.getX()+75, player.playerImage.getY() - 10);
                            pane.getChildren().add(lazer);
                            EventHandler<ActionEvent> shoot = t -> {
                                lazer.setStartY(lazer.getStartY() - 1);
                                lazer.setEndY(lazer.getEndY() - 1);
                                for(int x = 0; x < player.enemies.enemies.length; x++) {
                                    if(lazer.getBoundsInParent().intersects(player.enemies.enemies[x].getBoundsInParent()) &&
                                     player.enemies.enemies[x].alive) {
                                        pane.getChildren().removeAll(player.enemies.enemies[x], lazer);
                                        player.enemies.enemies[x].alive = false;
                                        lazer.setStartX(0);
                                        lazer.setStartY(0);
                                        lazer.setEndX(0);
                                        lazer.setEndY(0);
                                        player.shotReady = true;
                                        player.fitness++;
                                    }
                                    if(lazer.getEndY() == 0) {
                                        player.shotReady = true;
                                        player.fitness--;
                                    }
                                }
                            };
                            Timeline pShoot = new Timeline(new KeyFrame(Duration.millis(3), shoot));
                            pShoot.setCycleCount(Timeline.INDEFINITE);
                            pShoot.play();
                            timelines.add(pShoot);
                        }
                    }
                    else if(!player.dead){
                        double result = player.sc2.invoke();
                        if(result == 0.0) {
                            if(player.playerImage.getX() < 860) {
                                player.playerImage.setX(player.playerImage.getX() + 10);
                            }
                            else {
                                player.dead = true;
                                pane.getChildren().remove(player.playerImage);
                                pane.getChildren().removeAll(player.enemies.enemies);
                            }
                        }
                        if(result == 1.0) {
                            if(player.playerImage.getX() > 20) {
                                player.playerImage.setX(player.playerImage.getX() - 10);
                            }
                            else {
                                player.dead = true;
                                pane.getChildren().remove(player.playerImage);
                                pane.getChildren().removeAll(player.enemies.enemies);
                            }
                        }
                        if(result == 2.0 && player.shotReady) {
                            player.shotReady = false;
                            Line lazer = new Line(player.playerImage.getX()+75, player.playerImage.getY(), player.playerImage.getX()+75, player.playerImage.getY() - 10);
                            pane.getChildren().add(lazer);
                            EventHandler<ActionEvent> shoot = t -> {
                                lazer.setStartY(lazer.getStartY() - 1);
                                lazer.setEndY(lazer.getEndY() - 1);
                                for(int x = 0; x < player.enemies.enemies.length; x++) {
                                    if(lazer.getBoundsInParent().intersects(player.enemies.enemies[x].getBoundsInParent()) &&
                                            player.enemies.enemies[x].alive) {
                                        pane.getChildren().removeAll(player.enemies.enemies[x], lazer);
                                        player.enemies.enemies[x].alive = false;
                                        lazer.setStartX(0);
                                        lazer.setStartY(0);
                                        lazer.setEndX(0);
                                        lazer.setEndY(0);
                                        player.shotReady = true;
                                        player.fitness++;
                                        break;
                                    }
                                    if(lazer.getEndY() == 0) {
                                        player.shotReady = true;
                                        player.fitness--;
                                    }
                                }
                            };
                            Timeline pShoot = new Timeline(new KeyFrame(Duration.millis(3), shoot));
                            pShoot.setCycleCount(Timeline.INDEFINITE);
                            pShoot.play();
                            timelines.add(pShoot);
                        }
                    }
                }
                catch (Exception p) {

                }
            };
            Timeline PlayerTL = new Timeline(new KeyFrame(Duration.millis(10), play));
            PlayerTL.setCycleCount(Timeline.INDEFINITE);
            PlayerTL.play();
            timelines.add(PlayerTL);

            EventHandler<ActionEvent> enemy = ep -> {
                // search all images for the leftmost or rightmost image
                if(player.enemies.right) {
                    for(int p = 0; p < player.enemies.enemies.length; p++) {
                        if(player.enemies.enemies[p].getX() >= 910 && player.enemies.enemies[p].alive) {
                            player.enemies.right = false;
                            for(int x = 0; x < player.enemies.enemies.length; x++) {
                                player.enemies.enemies[x].setY(player.enemies.enemies[x].getY() + 10);
                            }
                            break;
                        }
                    }
                }
                else {
                    for(int p = 0; p < player.enemies.enemies.length; p++) {
                        if(player.enemies.enemies[p].getX() <= 0 && player.enemies.enemies[p].alive) {
                            player.enemies.right = true;
                            for(int x = 0; x < player.enemies.enemies.length; x++) {
                                player.enemies.enemies[x].setY(player.enemies.enemies[x].getY() + 10);
                            }
                            break;
                        }
                    }
                }
                if(player.enemies.right) {
                    for(int x = 0; x < player.enemies.enemies.length; x++) {
                        player.enemies.enemies[x].setX(player.enemies.enemies[x].getX() + 10);
                    }
                }
                else {
                    for(int x = 0; x < player.enemies.enemies.length; x++) {
                        player.enemies.enemies[x].setX(player.enemies.enemies[x].getX() - 10);
                    }
                }
            };
            Timeline ETL = new Timeline(new KeyFrame(Duration.millis(player.enemies.aliveNum().size() * 20), enemy));
            ETL.setCycleCount(Timeline.INDEFINITE);
            ETL.play();
            timelines.add(ETL);
            try {
                EventHandler<ActionEvent> check = e -> {
                    ArrayList<Integer> alive = player.enemies.getAlive();
                    for(int x = 0; x < alive.size(); x++) {
                        if(player.enemies.enemies[alive.get(x)].getX() == player.playerImage.getX() + 20 && !player.dead) {

                            Line line = new Line(player.enemies.enemies[alive.get(x)].getX() +25, player.enemies.enemies[alive.get(x)].getY() +20, player.enemies.enemies[alive.get(x)].getX()+25, player.enemies.enemies[alive.get(x)].getY() + 30);
                            player.activeLazers.add(line);
                            pane.getChildren().add(line);
                            EventHandler<ActionEvent> shoot = ex -> {
                                line.setStartY(line.getStartY() + 1);
                                line.setEndY(line.getEndY() + 1);
                                if (line.getBoundsInParent().intersects(player.playerImage.getBoundsInParent())) {
                                    player.activeLazers.remove(line);
                                    pane.getChildren().remove(player.playerImage);
                                    pane.getChildren().remove(line);
                                    player.dead = true;
                                    pane.getChildren().removeAll(player.enemies.enemies);
                                    player.fitness--;
                                }
                                if(line.getEndY() >= 900) {
                                    player.activeLazers.remove(line);
                                    pane.getChildren().remove(line);
                                    player.fitness++;
                                }  
                            };
                            Timeline shootL = new Timeline(new KeyFrame(Duration.millis(3), shoot));
                            shootL.setCycleCount(Timeline.INDEFINITE);

                            shootL.play();
                            timelines.add(shootL);
                        }
                    }
                };
                Timeline lazers = new Timeline(new KeyFrame(Duration.millis(2100), check));
                lazers.setCycleCount(Timeline.INDEFINITE);
                lazers.play();
                timelines.add(lazers);
            }
            catch(Exception p){}
            timelines.add(Tracker);
        }
        return tempGen;
    }

}
