package layoutLogic;

import domain.Pojazd;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class WindowSingleton {

    private static WindowSingleton instance;
    private Stage primaryStage;
    private Scene scene;

    private WindowSingleton() {
    }

    public static WindowSingleton getInstance() {
        if (instance == null) {
            instance = new WindowSingleton();
            return instance;
        } else return instance;

    }

    public static void alert(String error) {
        final Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Error");
        window.setMinWidth(356);
        window.setMinHeight(220);

        Label label = new Label(error);
        Button closeButton = new Button("Zamknij");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                window.close();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void showList(String title, String _order, String[] elements) {

        Button button = new Button("Wybierz");
        final Stage window = new Stage();
        window.setTitle(title);
        window.setMinWidth(600);
        final String[] choice = new String[1];
        int id;
        Label order = new Label(_order);
        final ListView<String> list = new ListView<String>();

        list.getItems().addAll(elements);

        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                choice[0] = list.getSelectionModel().getSelectedItem();
                System.out.println(choice[0]);
                window.close();

            }
        });

        VBox layout = new VBox(5);
        layout.getChildren().addAll(order, list, button);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }

    public static void showVehicleTable(String type){

        Stage window = new Stage();
        window.setTitle("Lista pojazdów typu \"" + type +"\"");
        final TableView<Pojazd> table;
//        final Button select = new Button("Select");
//        String choice = "";

        // id
        TableColumn<Pojazd, Long> idColumn = new TableColumn<Pojazd, Long>("ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<Pojazd, Long>("id"));

        // marka
        TableColumn<Pojazd, String> markaColumn = new TableColumn<Pojazd, String>("Marka");
        markaColumn.setMinWidth(100);
        markaColumn.setCellValueFactory(new PropertyValueFactory<Pojazd, String>("marka"));

        // model
        TableColumn<Pojazd, String> modelColumn = new TableColumn<Pojazd, String>("Model");
        modelColumn.setMinWidth(100);
        modelColumn.setCellValueFactory(new PropertyValueFactory<Pojazd, String>("model"));

        // id ubezpieczenia
        TableColumn<Pojazd, String> idUbezpieczeniaColumn = new TableColumn<Pojazd, String>("ID ubezpieczenia");
        idUbezpieczeniaColumn.setMinWidth(100);
        idUbezpieczeniaColumn.setCellValueFactory(new PropertyValueFactory<Pojazd, String>("id_ubezpieczenia"));

        // stan pojazdu
        TableColumn<Pojazd, String> stanPojazduColumn = new TableColumn<Pojazd, String>("Stan pojazdu");
        stanPojazduColumn.setMinWidth(100);
        stanPojazduColumn.setCellValueFactory(new PropertyValueFactory<Pojazd, String>("stan_pojazdu"));

        // dostepnosc
        TableColumn<Pojazd, String> dostepnosColumn = new TableColumn<Pojazd, String>("Dostępność");
        dostepnosColumn.setMinWidth(100);
        dostepnosColumn.setCellValueFactory(new PropertyValueFactory<Pojazd, String>("dostepnosc"));

        table = new TableView<Pojazd>();
        table.setItems(WindowSingleton.getVehiclesObservableList(type));
        table.getColumns().addAll(idColumn, markaColumn, modelColumn, idUbezpieczeniaColumn, stanPojazduColumn, dostepnosColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
//        select.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent e) {
//                Pojazd selection;
//                selection = table.getSelectionModel().getSelectedItem();
//                //System.out.println(selection.getId());
//
//            }
//        });


    }

    private static ObservableList<Pojazd> getVehiclesObservableList(String type){
        ObservableList<Pojazd> vehicles = FXCollections.observableArrayList();
        List<Pojazd> list = DBConnector.getInstance().entityManager.createQuery("SELECT a FROM Pojazd a WHERE typ='Samochód'", Pojazd.class).getResultList();

        for (Pojazd pojazd : list) {
            vehicles.add(pojazd);
        }
        return vehicles;
    }

    public void setLayout(String pathToFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource(pathToFXML));

        BorderPane borderPane = null;
        try {
            borderPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error");
        }

        scene = new Scene(borderPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("KCK2");
        primaryStage.show();
    }

    public void startApp(Stage stage) throws IOException, InterruptedException {
        primaryStage = stage;
        setLayout("/layout/WelcomeScreen.fxml");
        //WelcomeScreen.ProgressMax();


    }
}