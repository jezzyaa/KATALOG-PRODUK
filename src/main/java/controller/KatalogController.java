package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import data.ProdukRepository;
import main.Navigator;
import model.Produk;
import util.GambarProduk;
import java.text.NumberFormat;
import java.util.Locale;

public class KatalogController {

    @FXML
    private TextField namaField;
    @FXML
    private TextField hargaField;
    @FXML
    private ComboBox<String> fotoBox;
    @FXML
    private Label pesanLabel;
    @FXML
    private Label jumlahLabel;
    @FXML
    private TilePane katalogPane;
    private final ProdukRepository repository = new ProdukRepository();
    private Navigator navigator;

    @FXML
    private void initialize() {
        fotoBox.getItems().addAll(GambarProduk.DAFTAR);
        fotoBox.setValue(GambarProduk.DAFTAR[0]);
        try {
            showData();
        } catch (SQLException e) {
            pesanLabel.setText("Gagal membaca database. Lihat Output.");
            e.printStackTrace();
        }
    }

    // Dipanggil Navigator setelah halaman dimuat.
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    // Menampilkan pesan dari halaman lain, misalnya "Produk berhasil diubah."
    public void setPesan(String pesan) {
        if (pesan != null && !pesan.isEmpty()) {
            pesanLabel.setText(pesan);
        }
    }

    private void showData() throws SQLException {
        ArrayList<Produk> daftar = repository.getAll();
        katalogPane.getChildren().clear();
        for (Produk produk : daftar) {
            VBox kartu = createProductCard(produk);
            katalogPane.getChildren().add(kartu);
        }
        jumlahLabel.setText("Jumlah produk: " + daftar.size());
    }

    private VBox createProductCard(Produk p) {
        StackPane bingkai = GambarProduk.buatBingkai(p.getFoto(), 180, 120);

        Label namaLabel = new Label(p.getNama());
        namaLabel.setWrapText(true);
        namaLabel.getStyleClass().add("nama-produk");

        NumberFormat rupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
        rupiah.setMaximumFractionDigits(0);
        Label hargaLabel = new Label(rupiah.format(p.getHarga()));

        hargaLabel.getStyleClass().add("harga-produk");
        Label idLabel = new Label("ID: " + p.getId());
        idLabel.getStyleClass().add("muted");

        // Satu tombol untuk membuka halaman edit produk ini.
        Button kelolaButton = new Button("Edit / Hapus");
        kelolaButton.getStyleClass().add("button-secondary");
        kelolaButton.setOnAction(e -> bukaEdit(p));

        VBox kartu = new VBox();
        kartu.setSpacing(8);
        kartu.setMinWidth(220);
        kartu.setPrefWidth(220);
        kartu.setMaxWidth(220);
        kartu.getStyleClass().add("kartu-produk");
        kartu.getChildren().add(bingkai);
        kartu.getChildren().add(namaLabel);
        kartu.getChildren().add(hargaLabel);
        kartu.getChildren().add(idLabel);
        kartu.getChildren().add(kelolaButton);
        return kartu;
    }

    private void bukaEdit(Produk p) {
        try {
            navigator.bukaEdit(p);
        } catch (IOException e) {
            pesanLabel.setText("Halaman edit gagal dibuka. Lihat Output.");
            e.printStackTrace();
        }
    }

    @FXML
    private void saveProduct() {
        Produk produk;
        try {
            String nama = namaField.getText();
            long harga = Long.parseLong(hargaField.getText().trim());
            String foto = fotoBox.getValue();
            produk = new Produk(nama, harga, foto);
        } catch (NumberFormatException e) {
            pesanLabel.setText("Harga harus angka bulat, contoh 12000.");
            return;
        } catch (IllegalArgumentException e) {
            pesanLabel.setText(e.getMessage());
            return;
        }
        try {
            repository.add(produk);
        } catch (SQLException e) {
            pesanLabel.setText("Gagal menyimpan. Lihat Output.");
            e.printStackTrace();
            return;
        }
        namaField.clear();
        hargaField.clear();
        try {
            showData();
            pesanLabel.setText("Produk berhasil disimpan.");
        } catch (SQLException e) {
            pesanLabel.setText("Data tersimpan, tetapi katalog gagal dimuat ulang.");
            e.printStackTrace();
        }
    }
}
