package jnekoimagesdb.ui.md.toppanel;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TopPanel extends HBox {
    public TopPanel() {
        super();
        this.setAlignment(Pos.CENTER_LEFT);
        this.getStyleClass().addAll("panel_max_width", "panel_max_height", "panel_root_pane");
    }
    
    public void addSeparator() {
        final VBox sep1 = new VBox();
        sep1.getStyleClass().addAll("panel_max_width", "panel_max_height", "panel_null_pane");
        this.getChildren().add(sep1);
    }
    
    public void addFixedSeparator() {
        final VBox sep1 = new VBox();
        sep1.getStyleClass().addAll("panel_fixed_separator_width", "panel_max_height", "panel_null_pane");
        this.getChildren().add(sep1);
    }
    
    public void addNode(Node element) {
        this.getChildren().add(element);
    }
    
    public void addNodes(Node ... element) {
        this.getChildren().addAll(element);
    }
}
