package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class OKButton extends Button {
    protected String top;

    public OKButton() {
        super("OK");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Koko");
        alert.setContentText("Spoko");
        this.setOnAction(e->{
                alert.setTitle(top);
                alert.show();}
                );
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }
}
