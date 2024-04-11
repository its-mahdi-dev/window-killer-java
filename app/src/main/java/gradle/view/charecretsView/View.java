package gradle.view.charecretsView;

import java.awt.Graphics;
import java.util.*;

import gradle.model.Model;

public abstract class View {
    
    public int x;
    public int y;
    public int w;
    public int h;

    public abstract void draw(Graphics g);
    public abstract void setUtil(Model model);
    public abstract List<View> getItems();
}
