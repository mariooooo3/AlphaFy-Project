package AlphaFy;
import GUI.Interface;
import javax.swing.SwingUtilities;


public class AlphaFy {
    String userName;

    public AlphaFy(String userName) {
        this.userName = userName;
        System.out.println("Welcome back " + this.userName);
    }

    public static void main(String[] args) {
        AlphaFy alphaFy = new AlphaFy("mariooo&clap");

        SwingUtilities.invokeLater(() -> {
            Interface ui = new Interface("mariooo&clap");
            ui.setVisible(true);
        });

    }

}
