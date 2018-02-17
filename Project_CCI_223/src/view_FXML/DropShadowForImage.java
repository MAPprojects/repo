package view_FXML;

import javafx.scene.effect.DropShadow;

public class DropShadowForImage {

    public static DropShadow dropShadow(Double width,Double height,Double offsetX,Double offsetY,Double radius){
        DropShadow dropShadow=new DropShadow();
        dropShadow.setWidth(width);
        dropShadow.setHeight(height);
        dropShadow.setOffsetX(offsetX);
        dropShadow.setOffsetY(offsetY);
        dropShadow.setRadius(radius);
        return dropShadow;
    }
}
