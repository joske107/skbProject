package be.kdg.sokoban.view.option;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.SokobanModel;
import be.kdg.sokoban.view.menu.MenuPresenter;
import be.kdg.sokoban.view.menu.MenuView;
import javafx.scene.control.Alert;

import java.io.IOException;


/**
 * @author Lies Van der Haegen
 * @version 1.0 3/10/2017 6:04 PM
 */
public class OptionPresenter {
    private SokobanModel model;
    private OptionView view;
    @SuppressWarnings("unused")
    private MenuPresenter mPresenter;
    private MenuView mView;

    public OptionPresenter(SokobanModel model, OptionView view) {
        long time = System.currentTimeMillis();
        this.model = model;
        this.view = view;

        try {
            view.setConfig(model.loadConfig());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "can't save configFile\n" + e.getMessage());
            alert.showAndWait();
        }
        addStyleSheets();
        addEventHandlers();
        if (SokobanMain.DEBUG)
            System.out.println("LoadTime LevelSelect: " + (System.currentTimeMillis() - time) + " milliseconds");
    }

    private void addEventHandlers() {
        view.getBtnBack().setOnAction(event -> {
            try {
                model.saveConfig(view.getConfig());
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "can't save configFile\n" + e.getMessage());
                alert.showAndWait();
            }
            mView = new MenuView();
            mPresenter = new MenuPresenter(model, mView);
            view.getScene().setRoot(mView);
        });
    }

    private void addStyleSheets() {
        view.getStylesheets().add("/be/kdg/sokoban/view/option/css/option.css");
        view.getLblTitle().getStyleClass().add("title");
        view.getGridPane().getStyleClass().add("gridPane");
        view.getBtnBack().getStyleClass().add("btnBack");
        view.getLblAnimation().getStyleClass().add("option");
        view.getBtnEnable().getStyleClass().add("setting");
        view.getStyleClass().add("borderPane");
    }
}
