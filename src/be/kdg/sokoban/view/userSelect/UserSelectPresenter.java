package be.kdg.sokoban.view.userSelect;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.SokobanModel;
import be.kdg.sokoban.view.menu.MenuPresenter;
import be.kdg.sokoban.view.menu.MenuView;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.io.IOException;

/**
 * @author Lies Van der Haegen
 * @version 1.0 3/7/2017 9:51 AM
 */
public class UserSelectPresenter {
    private SokobanModel model;
    private UserSelectView view;
    @SuppressWarnings("unused")
    private MenuPresenter mPresenter;
    private MenuView mView;

    public UserSelectPresenter(SokobanModel model, UserSelectView view) {
        long time = System.currentTimeMillis();
        this.model = model;
        this.view = view;
        model.loadUsers();
        view.getUserView().setUsers(model.getUsers());
        addStyleSheets();
        addEventHandlers();

        if (SokobanMain.DEBUG)
            System.out.println("LoadTime LevelSelect: " + (System.currentTimeMillis() - time) + " milliseconds");
    }

    private void addEventHandlers() {
        for (int i = 0; i < view.getUserView().getBtnUser().length; i++) {
            final int j = i;
            view.getUserView().getBtnUser()[i].setOnAction(event -> {
                if (model.getUsers()[j] != null) {
                    model.setCurrentUserIndex(j);
                    mView = new MenuView();
                    mPresenter = new MenuPresenter(model, mView);
                    view.getScene().setRoot(mView);
                } else {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Create User");
                    dialog.setContentText("Give your character a name (only letters):");
                    String name = null;
                    while (name == null) {
                        name = dialog.showAndWait().get();
                        if (!name.trim().matches("^[A-Za-z' éèçáàêâëä]+$")) {
                            name = null;
                            new Alert(Alert.AlertType.ERROR, "Only letters and spaces are allowed:\n(A-Za-z' éèçáàêâëä)").showAndWait();
                        }

                    }
                    try {
                        model.addUser(j, name.trim());
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "can't save file\n" + e.getMessage());
                        alert.showAndWait();
                    }
                    updateView();
                }
            });
        }

        for (int i = 0; i < view.getUserView().getBtnDeleteUser().length; i++) {
            final int j = i;
            view.getUserView().getBtnDeleteUser()[i].setOnAction(event -> {
                try {
                    model.deleteUser(j);
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "can't save file\n" + e.getMessage());
                    alert.showAndWait();
                }
                updateView();
            });
        }
    }

    private void updateView() {
        view.getUserView().updateUsers(model.getUsers());
    }

    private void addStyleSheets() {
        view.getStylesheets().add("/be/kdg/sokoban/view/userSelect/css/userSelect.css");
        for (int i = 0; i < view.getUserView().getBtnUser().length; i++) {
            view.getUserView().getBtnUser()[i].getStyleClass().add("userButton");
        }
        for (int i = 0; i < view.getUserView().getBtnDeleteUser().length; i++) {
            view.getUserView().getBtnDeleteUser()[i].getStyleClass().add("userButton");
            view.getUserView().getBtnDeleteUser()[i].getStyleClass().add("deleteButton");
        }
        view.getLblTitle().getStyleClass().add("title");
        view.getStyleClass().add("borderPane");
        if (SokobanMain.DEBUG) System.out.println("StyleSheets loaded!");
    }
}
