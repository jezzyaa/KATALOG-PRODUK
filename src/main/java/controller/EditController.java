package controller;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import data.ProdukRepository;
import main.Navigator;
import model.Produk;
import util.GambarProduk;

public class EditController {

    @FXML
    private Label idLabel;
    @FXML
    private StackPane previewPane;
    @FXML
    private TextField namaField;
    @FXML
    private TextField hargaField;
    @FXML
    private ComboBox<String> fotoBox;
    @FXML
    private Label pesanLabel;

    private final ProdukRepository repository = new ProdukRepository();
    private Navigator navigator;
    private Produk produk;   // produk yang sedang diedit

    @FXML
    private void initialize() {
        fotoBox.getItems().addAll(GambarProduk.DAFTAR);
    }

    // Dipanggil Navigator setelah halaman dimuat: menerima produk yang dipilih di katalog.
    public void setData(Navigator navigator, Produk produk) {
        this.navigator = navigator;
        this.produk = produk;
        idLabel.setText("ID produk: " + produk.getId());
        namaField.setText(produk.getNama());
        hargaField.setText(String.valueOf(produk.getHarga()));
        fotoBox.setValue(produk.getFoto());
        tampilkanPreview(produk.getFoto());
    }

    private void tampilkanPreview(String foto) {
        if (foto == null) {
            return;
        }
        previewPane.getChildren().clear();
        previewPane.getChildren().add(GambarProduk.buatBingkai(foto, 260, 170));
    }

    // Dipanggil setiap pilihan foto pada ComboBox berubah.
    @FXML
    private void fotoDipilih() {
        tampilkanPreview(fotoBox.getValue());
    }

    // ===================== UPDATE =====================
    @FXML
    private void simpanPerubahan() {
        Produk baru;
        try {
            String nama = namaField.getText();
            long harga = Long.parseLong(hargaField.getText().trim());
            String foto = fotoBox.getValue();
            baru = new Produk(produk.getId(), nama, harga, foto);
        } catch (NumberFormatException e) {
            pesanLabel.setText("Harga harus angka bulat, contoh 12000.");
            return;
        } catch (IllegalArgumentException e) {
            pesanLabel.setText(e.getMessage());
            return;
        }
        try {
            boolean berhasil = repository.update(baru);
            if (!berhasil) {
                pesanLabel.setText("Produk tidak ditemukan di database.");
                return;
            }
        } catch (SQLException e) {
            pesanLabel.setText("Gagal mengubah. Lihat Output.");
            e.printStackTrace();
            return;
        }
        kembaliKeKatalog("Produk berhasil diubah.");
    }

    // ===================== DELETE =====================
    @FXML
    private void hapusProduk() {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Hapus produk \"" + produk.getNama() + "\"?",
                ButtonType.YES, ButtonType.NO);
        dialog.setTitle("Konfirmasi Hapus");
        dialog.setHeaderText(null);
        dialog.initOwner(namaField.getScene().getWindow());
        dialog.getDialogPane().getStylesheets().add(
                getClass().getResource("/view/styles/app.css").toExternalForm());
        ButtonType jawaban = dialog.showAndWait().orElse(ButtonType.NO);
        if (jawaban != ButtonType.YES) {
            return;
        }
        try {
            boolean berhasil = repository.delete(produk);
            if (!berhasil) {
                pesanLabel.setText("Produk tidak ditemukan di database.");
                return;
            }
        } catch (SQLException e) {
            pesanLabel.setText("Gagal menghapus. Lihat Output.");
            e.printStackTrace();
            return;
        }
        kembaliKeKatalog("Produk \"" + produk.getNama() + "\" berhasil dihapus.");
    }

    @FXML
    private void kembali() {
        kembaliKeKatalog("");
    }

    private void kembaliKeKatalog(String pesan) {
        try {
            navigator.bukaKatalog(pesan);
        } catch (IOException e) {
            pesanLabel.setText("Gagal membuka katalog. Lihat Output.");
            e.printStackTrace();
        }
    }
}
