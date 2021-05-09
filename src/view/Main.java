package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import viewModel.ViewModelController;

public class Main extends Application {


//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
//    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //added
        Model m = new Model();
        ViewModelController vmc = new ViewModelController(m);

        FXMLLoader fxl=new FXMLLoader();
        Parent root = fxl.load(getClass().getResource("sample.fxml").openStream());
        primaryStage.setTitle("Hello World");
        ControllerView controllerView=fxl.getController();
        controllerView.init(vmc);
        controllerView.paint();


        //m.ConnectToServer("127.0.0.1",5402);
        primaryStage.setScene(new Scene(root, 800, 550));
        primaryStage.show();

    }




    public static void main(String[] args) {
        launch(args);
    }
}
