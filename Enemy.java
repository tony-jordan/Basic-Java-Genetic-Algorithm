import javafx.scene.image.*;
public class Enemy extends ImageView{
    // this class just makes sure enemies have the correct images associated with them
    public Image i;
    public boolean alive = true;
    public Enemy(Image i) {
        super(i);
        this.i = i;
    }
}
