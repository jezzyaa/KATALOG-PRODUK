package util;

import java.net.URL;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class GambarProduk {

    // Nama foto yang tersedia di src/main/resources/image. Tambahkan nama baru di sini.
    public static final String[] DAFTAR = {"kopi.jpg", "roti.jpg", "susu.jpg", "pudding.jpg","dumpling.jpg" };

    // Membuat bingkai berukuran tetap berisi foto produk (atau tulisan jika foto tidak ditemukan).
    public static StackPane buatBingkai(String namaFoto, double lebar, double tinggi) {
        StackPane bingkai = new StackPane();
        bingkai.setPrefSize(lebar, tinggi);
        bingkai.setMinSize(lebar, tinggi);
        bingkai.setMaxSize(lebar, tinggi);
        bingkai.getStyleClass().add("bingkai-foto");

        ImageView fotoView = new ImageView();
        fotoView.setFitWidth(lebar - 10);
        fotoView.setFitHeight(tinggi - 10);
        fotoView.setPreserveRatio(true);

        URL lokasi = GambarProduk.class.getResource("/image/" + namaFoto);
        if (lokasi != null) {
            Image gambar = new Image(lokasi.toExternalForm(),
                    lebar - 10, tinggi - 10, true, true);
            if (!gambar.isError()) {
                fotoView.setImage(gambar);
                bingkai.getChildren().add(fotoView);
            } else {
                bingkai.getChildren().add(new Label("Gambar tidak terbaca"));
            }
        } else {
            bingkai.getChildren().add(new Label("Gambar tidak tersedia"));
        }
        return bingkai;
    }
}
