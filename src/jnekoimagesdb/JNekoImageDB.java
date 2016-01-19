package jnekoimagesdb;

import imgfs.ImgFS;
import imgfsgui.PagedImageList;
import imgfsgui.tabs.TabAlbumImageList;
import imgfsgui.tabs.TabAllImages;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import jnekoimagesdb.GUITools.DragDelta;
import menulist.MenuGroupItem;
import menulist.MenuGroupItemActionListener;
import menulist.MenuLabel;
import menulist.MenuList;

public class JNekoImageDB extends Application {
    private final DragDelta 
            DRD = new DragDelta();
    
    public static final StringBuilder
            LOG = new StringBuilder();
    
    private final VBox 
            mvbox           = new VBox(),
            toolbarvbox     = new VBox(),
            basevbox        = new VBox(), 
            base2vbox       = new VBox(),
            basesp          = new VBox();
            
    private final HBox 
            basehbox        = new HBox(),
            headerbox       = new HBox(), 
            toolbox         = new HBox(),
            logobox         = new HBox(),
            paginator_1     = new HBox();
    
    private final TextArea
            taLOG = new TextArea();
    
    private final TabAlbumImageList
            tabAlbumImageList = new TabAlbumImageList(toolbox, paginator_1);
    
    private final TabAllImages
            tabAllImages = new TabAllImages();

    private String 
            databaseName    = "default";

    private final MenuList 
            ml              = new MenuList();
    
    private final StartSplashScreen 
            splash = new StartSplashScreen();

    private final Timeline TMRLOG = new Timeline(new KeyFrame(Duration.millis(150), ae -> {
       if (taLOG.getText().length() < LOG.length()) {
           taLOG.setText(Lang.NullString);
           taLOG.appendText(LOG.toString());
           taLOG.setScrollTop(Double.MIN_VALUE);
       }
    }));
    
    private final MenuGroupItemActionListener
            menuAL = new MenuGroupItemActionListener() {
                @Override
                public void OnExpandGroup(boolean expanded, MenuGroupItem item) { }

                @Override
                public void OnItemHover(MenuLabel l) { }

                @Override
                public void OnItemClicked(MenuLabel l) {
                    clearAll();

                    if (l.getID().contentEquals("M01-01")) showAllImages(PagedImageList.IMAGES_ALL);
                    if (l.getID().contentEquals("M01-02")) showAllImages(PagedImageList.IMAGES_NOTAGGED);
                    if (l.getID().contentEquals("M01-06")) showAllImages(PagedImageList.IMAGES_NOT_IN_ALBUM);
                    
                    if (l.getID().contentEquals("M01-05")) showFileDialog();
                    if (l.getID().contentEquals("M01-04")) showAlbCats();
                    
                    if (l.getID().contentEquals("M03-03")) showLog();
                    if (l.getID().contentEquals("M03-02")) showSettings();
                    if (l.getID().contentEquals("M03-04")) {
                        
                    }
                }
            };

    private void clearAll() {
        basesp.getChildren().clear();
        toolbox.getChildren().clear();
        paginator_1.getChildren().clear();
    }
    
    private void showAllImages(long aID) {
        basesp.getChildren().add(tabAllImages);
        toolbox.getChildren().add(tabAllImages.getPanel());
        paginator_1.getChildren().add(tabAllImages.getPaginator());
        tabAllImages.setAlbumID(aID);
        tabAllImages.regenerate();
        tabAllImages.refresh();
    }
    
    private void showSettings() {

    }

    private void showFileDialog() {
        basesp.getChildren().add(ImgFS.getAddImagesTab().getList());
        toolbox.getChildren().add(ImgFS.getAddImagesTab().getTopPanel());
        paginator_1.getChildren().add(ImgFS.getAddImagesTab().getBottomPanel());
    }
    
    private void showAlbCats() {
        basesp.getChildren().add(tabAlbumImageList);
        tabAlbumImageList.refresh();
    }
    
    private void showLog() {
        basesp.getChildren().add(taLOG);
        taLOG.setScrollTop(65535);
    }
    
    @Override
    public void start(Stage primaryStage) {
        splash.show();
        
        try {
            
            ImgFS.init(databaseName);
//            cryptoEx.init(databaseName);
//            addNewImagesTab = new TabAddImagesToDB(cryptoEx, databaseName);
        } catch (Exception ex) {
            Logger.getLogger(JNekoImageDB.class.getName()).log(Level.SEVERE, null, ex);
            Platform.exit(); 
            return;
        }

        TMRLOG.setCycleCount(Animation.INDEFINITE);
        TMRLOG.play();
        
        final Image logoImage = new Image(new File("./icons/logo6.png").toURI().toString());
        final ImageView imgLogoV = new ImageView(logoImage);

        tabAlbumImageList.initDB();

        taLOG.setMaxSize(9999, 9999);
        taLOG.setPrefSize(9999, 9999);
        taLOG.setWrapText(true);
        
        GUITools.setStyle(taLOG, "JNekoImageDB", "taLOG");
        taLOG.textProperty().addListener((ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
            taLOG.setScrollTop(Double.MAX_VALUE);
        });
        
        StackPane root = new StackPane();
        final Scene scene;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            StackPane root3m = GUITools.getWinGUI(this, primaryStage, DRD, root, mvbox, "", GUITools.CLOSE_EXIT, false);
            scene = new Scene(root3m, 950, 650);
            scene.setFill(Color.TRANSPARENT);
             
        } else {
            scene = new Scene(root, 1336, 778);
        }
        
        mvbox.getChildren().add(toolbarvbox);
        mvbox.getChildren().add(basevbox);
        root.getChildren().add(mvbox);

        GUITools.setStyle(toolbox, "JNekoImageDB", "toolbox");
        toolbox.setMaxWidth(9999);
        toolbox.setPrefWidth(9999);
        toolbox.setAlignment(Pos.BOTTOM_LEFT);

        GUITools.setStyle(headerbox, "JNekoImageDB", "headerbox");
        headerbox.setMaxWidth(9999);
        
        GUITools.setStyle(logobox, "JNekoImageDB", "headerbox");
        logobox.setMaxSize(240, 64);
        logobox.setMinSize(240, 64);
        logobox.setPrefSize(240, 64);
        logobox.setAlignment(Pos.CENTER_RIGHT);
        logobox.getChildren().add(imgLogoV);
        
        headerbox.getChildren().add(toolbox);
        headerbox.getChildren().add(logobox);
        
        toolbarvbox.setPrefSize(9999, 70);
        toolbarvbox.setMaxSize(9999, 70);
        toolbarvbox.setMinSize(64, 70);
        
        toolbarvbox.getChildren().add(headerbox);

        GUITools.setStyle(paginator_1, "JNekoImageDB", "paginator_1");
        paginator_1.setMaxSize(9999, 32);
        paginator_1.setPrefSize(9999, 32);
        paginator_1.setMinSize(32, 32);
        paginator_1.setAlignment(Pos.CENTER);

        basevbox.getChildren().add(basehbox);

        GUITools.setStyle(basesp, "JNekoImageDB", "basesp");
        basesp.setPrefSize(9999, 9999);
        basesp.setMaxSize(9999, 9999);
        
        ml.getMenu().setActionListener(menuAL);
        
        ml.getMenu().addGroup("M01", Lang.JNekoImageDB_menu_title_main, null, "113311");
        ml.getMenu().addItem("M01", "M01-01", Lang.JNekoImageDB_menu_main_all_images);
        ml.getMenu().addItem("M01", "M01-02", Lang.JNekoImageDB_menu_main_all_images_wo_tags);
        ml.getMenu().addItem("M01", "M01-06", Lang.JNekoImageDB_menu_main_all_images_wo_groups);
        ml.getMenu().addItem("M01", "M01-04", Lang.JNekoImageDB_menu_title_albums);
        ml.getMenu().addItem("M01", "M01-03", Lang.JNekoImageDB_menu_main_tagcloud);
        ml.getMenu().addItem("M01", "M01-07", Lang.JNekoImageDB_menu_main_fav_tags);
        ml.getMenu().addItem("M01", "M01-05", Lang.JNekoImageDB_menu_main_add_images);
        
        ml.getMenu().addGroup("M03", Lang.JNekoImageDB_menu_title_settings, null, "111133");
        //ml.getMenu().addItem("M03", "M03-01", Lang.JNekoImageDB_menu_settings_album_roots);
        //ml.getMenu().addItem("M03", "M03-02", Lang.JNekoImageDB_menu_settings_main);
        ml.getMenu().addItem("M03", "M03-03", Lang.JNekoImageDB_menu_settings_logs);
        ml.getMenu().addItem("M03", "M03-04", "For test");

        ml.setPrefSize(240, 9999);
        ml.setMaxSize(240, 9999);
        ml.setMinSize(240, 300);
        
        GUITools.setStyle(ml, "JNekoImageDB", "MenuList");
        
        base2vbox.getChildren().add(basesp);
        base2vbox.getChildren().add(paginator_1);
        
        basehbox.getChildren().add(base2vbox);
        basehbox.getChildren().add(ml);

        primaryStage.getIcons().add(new Image(new File("./icons/icon128.png").toURI().toString()));
        primaryStage.getIcons().add(new Image(new File("./icons/icon64.png").toURI().toString()));
        primaryStage.getIcons().add(new Image(new File("./icons/icon32.png").toURI().toString()));
        
        primaryStage.setOnHiding((WindowEvent event) -> {
            ImgFS.dispose();
            Platform.exit(); 
        });
        
        primaryStage.setMinWidth(840);
        primaryStage.setMinHeight(480);
        primaryStage.setTitle(Lang.JNekoImageDB_title);
        primaryStage.setScene(scene);
        
        splash.hide();
        primaryStage.show();

        if (System.getProperty("os.name").toLowerCase().contains("win")) ResizeHelper.addResizeListener(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        launch(args);
    }
    
    public static final void L(String s) {
        final SimpleDateFormat DF = new SimpleDateFormat(Lang.DateTimeFormat);
        LOG.append("[").append(DF.format(new Date())).append("]\t");
        LOG.append(s).append("\n");
    }
}
