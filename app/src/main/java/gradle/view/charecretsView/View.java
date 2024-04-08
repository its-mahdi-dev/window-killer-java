package gradle.view.charecretsView;

import java.awt.Graphics;
import java.util.*;

public abstract class View {
    public static final List<View> items = new ArrayList<>();

    public abstract void draw(Graphics g);
}
