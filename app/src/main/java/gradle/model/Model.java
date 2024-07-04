package gradle.model;

import java.awt.geom.Point2D;
import java.util.*;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.Timer;

import gradle.controller.Constants;
import gradle.controller.Utils;
import gradle.interfaces.Rotation;
import gradle.view.GamePanel;

public abstract class Model {
    private String Id;
    public Point2D anchor;
    public int w;
    public int h;
    public double angle;
    public ArrayList<GamePanel> currentPanels = new ArrayList<>();
    


    public Model() {
        Id = UUID.randomUUID().toString();
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public static Model findModel(String Id, List<? extends Model> items) {
        for (Model item : items) {
            if (item.getId().equals(Id)) {
                return item;
            }
        }
        return null;
    }

    public void addItem(Model item) {
        List<Model> items = getItems();
        if (items != null) {
            items.add(item);
        }
    }



   

    public Point2D getPanelAnchor() {
        if (currentPanels.size() > 0)
            return Utils.getRelatedPoint(anchor, currentPanels.get(0));
        else
            return anchor;
    }

    

    

    protected abstract List<Model> getItems();

    protected abstract List<Model> getRemovedItems();
}
