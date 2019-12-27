package gui.game;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;

public class WordComponent extends HBox{
    String word;
    String hashWord;
    Button[] letters;

    public WordComponent(String word) {
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setMinWidth(380);
        setMaxWidth(380);
        this.word = word.trim().toUpperCase();
        hashWord = this.word.replaceAll("[A-Z]", "#");
        letters = new Button[this.word.length()];
        int i = 0;
        for(char ch: this.word.toCharArray()){
            letters[i] = new Button(" ");
            letters[i].minHeight(40);
            letters[i].minWidth(40);
            letters[i].maxHeight(40);
            letters[i].maxWidth(40);
            letters[i].setDisable(true);
            letters[i].setStyle("-fx-background-color: #090a0c,\n" +
                    "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                    "        linear-gradient(#20262b, #191d22),\n" +
                    "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                    "    -fx-background-radius: 5,4,3,5;\n" +
                    "    -fx-background-insets: 0,1,2,0;\n" +
                    "    -fx-text-fill: white;\n" +
                    "    -fx-effect: dropshadow( three-pass-box , rgba(1,1,1,0.6) , 5, 0.0 , 0 , 1 );\n" +
                    "    -fx-font-family: \"Arial\";\n" +
                    "    -fx-font-size: 12px;\n" +
                    "    -fx-padding: 10 20 10 20;");
            i++;
        }
        this.getChildren().addAll(letters);
    }

    public int guess(char ch) {
        int num = 0;
        int pos = 0;
        char c = Character.toUpperCase(ch);
        StringBuilder pass = new StringBuilder(word);
        StringBuilder hpass = new StringBuilder(hashWord);

        while(pos>=0){
            pos = word.indexOf(c, pos);
            if(pos>=0){
                letters[pos].setText(String.valueOf(ch));
                num++;
                hpass.setCharAt(pos, c);
                pass.setCharAt(pos, '#');
                pos++;
            }
        }
        hashWord = hpass.toString();
        word = pass.toString();
        return num;
    }
}
