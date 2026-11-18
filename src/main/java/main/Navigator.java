package main;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.EditController;
import controller.KatalogController;
import model.Produk;

public class Navigator {

    private final Stage stage;
    private Scene scene;

    public Navigator(Stage stage) {
        this.stage = stage;
    }

    private FXMLLoader loader(String nama) {
        return new FXMLLoader(getClass().getResource("/view/" + nama + ".fxml"));
    }

    private void tampil(Parent root) {
        if (scene == null) {
            scene = new Scene(root, 1000, 760);
            stage.setTitle("Katalog Produk UPI Store");
            stage.setMinWidth(680);
            stage.setMinHeight(500);
            stage.setScene(scene);
        } else {
            scene.setRoot(root);
        }
        stage.show();
    }

    public void bukaKatalog() throws IOException {
        bukaKatalog("");
    }

    // Membuka katalog, dengan pesan opsional dari halaman sebelumnya.
    public void bukaKatalog(String pesan) throws IOException {
        FXMLLoader l = loader("katalog");
        Parent root = l.load();
        KatalogController c = l.getController();
        c.setNavigator(this);
        c.setPesan(pesan);
        tampil(root);
    }

    // Halaman edit menerima objek Produk yang dipilih.
    public void bukaEdit(Produk produk) throws IOException {
        FXMLLoader l = loader("edit");
        Parent root = l.load();
        EditController c = l.getController();
        c.setData(this, produk);
        tampil(root);
    }
}
