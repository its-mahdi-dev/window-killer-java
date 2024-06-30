package gradle.controller;

import javax.swing.*;

import gradle.model.CollectibleModel;
import gradle.model.EnemyModel;
import gradle.model.EpsilonModel;
import gradle.model.EpsilonVertexModel;
import gradle.model.ShotModel;
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.Panels;
import gradle.view.StorePanel;
import gradle.view.charecretsView.CollectibleView;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.EpsilonVertexView;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.NavbarView;
import gradle.view.charecretsView.ShotView;

public class Update {

    public static int upsCount = 0;
    public static int fpsCount = 0;
    public static Timer timer1 = new Timer((int) Constants.FRAME_UPDATE_TIME, e -> updateView()) {
        {
            setCoalesce(true);
        }
    };
    public static Timer timer2 = new Timer((int) Constants.MODEL_UPDATE_TIME, e -> updateModel()) {
        {
            setCoalesce(true);
        }
    };

    public static void updateView() {
        
        
       
        
        

        
        fpsCount++;
    }

    public static void updateModel() {

        upsCount++;
    }

}
