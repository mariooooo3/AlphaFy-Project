package AlphaFy;

import GUI.Interface;

import javax.swing.SwingUtilities;

public class AlphaFy {

    private final String userName;

    public AlphaFy(String userName) {
        this.userName = userName;
    }

    public void start() {
        Interface ui = new Interface(this.userName);
        ui.setLocationRelativeTo(null);
        ui.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AlphaFy app = new AlphaFy("mariooo&clap");
            app.start();
        });
    }
}

