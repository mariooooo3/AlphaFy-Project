package GUI;

import Playback.CompositePlayable;
import Playback.Playable;
import Media.Album;
import Media.Folder;
import Media.PlayList;
import Media.Song;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Font;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Interface extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField2;
    private JTextField nowPlaying;
    private JTextField welcome;


    public String userName;

    public Interface(String userName) {
        this.userName = userName;
        initUI();
        welcome.setText("Welcome back " + this.userName);


    }

    private static Clip currentClip;

    public static void PlayMusic(java.net.URL musicURL) {
        try {
            if (currentClip != null && currentClip.isRunning()) {
                currentClip.stop();
                currentClip.close();
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicURL);
            currentClip = AudioSystem.getClip();
            currentClip.open(audioInput);
            currentClip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private JTextField textField;

    private void playSongFromTree(String songName) {
        String duration = songName.substring(songName.length() - 5);
        String songName1 = songName.substring(0, songName.length() - 5);
        String fileName = songName1.replaceAll("\\s+", "");

        java.net.URL url = Interface.class.getResource("/" + fileName + ".wav");

        if (url == null) {
            System.out.println("Fisierul nu exista: " + fileName);
            return;
        }

        PlayMusic(url);
        nowPlaying.setText(songName1 + duration);
    }

    private void addCompositeToTree(DefaultMutableTreeNode parent, CompositePlayable composite) {
        for (Playable p : composite.getChildren()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(p);
            parent.add(node);

            if (p instanceof CompositePlayable) {
                addCompositeToTree(node, (CompositePlayable) p);
            }
        }
    }

    private void initUI() {
        setBackground(new Color(226, 226, 254));
        setTitle("AlphaFy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 819, 577);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(226, 226, 254));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel() {
            private Image backgroundImage = new ImageIcon(
                    Interface.class.getResource("/background.png")).getImage();


            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);
        panel.setBounds(10, 10, 800, 533);
        contentPane.add(panel);

        JButton pause = new JButton("Pause");
        pause.setForeground(new Color(255, 255, 255));
        pause.setBackground(new Color(255, 255, 255));
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentClip != null && currentClip.isRunning()) {
                    currentClip.stop();
                    String temp = nowPlaying.getText();
                    nowPlaying.setText("Paused:" + temp);
                }

            }
        });
        pause.setFont(new Font("Tahoma", Font.BOLD, 12));
        pause.setBounds(251, 377, 140, 29);
        panel.add(pause);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(264, 151, 278, 209);
        panel.add(scrollPane);

        JTree tree = new JTree();
        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                    if (node == null)
                        return;

                    if (node.isLeaf()) {
                        String songName = node.toString();
                        playSongFromTree(songName);
                        Song song = (Song) node.getUserObject();
                        song.play();
                    }
                }
            }
        });

        scrollPane.setViewportView(tree);

        Folder rootFolder = new Folder("AlphaFy Music");

        PlayList favourites = new PlayList("Favourites");
        PlayList onRepeat = new PlayList("On Repeat");

        Album sremmLife = new Album("SremmLife");
        sremmLife.addMedia(new Song("No Typee", "Rae Sremmurd", 3.20));
        sremmLife.addMedia(new Song("Black Beatles", "Rae Sremmurd", 4.51));
        sremmLife.addMedia(new Song("Swang", "Rae Sremmurd", 3.28));
        sremmLife.addMedia(new Song("Come Get Her", "Rae Sremmurd", 3.46));
        sremmLife.addMedia(new Song("Not So Bad", "Rae Sremmurd", 3.18));

        Album bully = new Album("BULLY");
        bully.addMedia(new Song("BULLY", "Ye", 2.03));
        bully.addMedia(new Song("PREACHER MAN", "Ye", 3.02));
        bully.addMedia(new Song("BEAUTY AND THE BEAST", "Ye", 1.46));
        bully.addMedia(new Song("LAST BREATH", "Ye", 2.22));
        bully.addMedia(new Song("LOSING YOUR MIND", "Ye", 3.24));

        Album loveSick = new Album("Love Sick");
        loveSick.addMedia(new Song("No Pole", "Don Toliver", 3.07));
        loveSick.addMedia(new Song("Embarrassed", "Don Toliver", 3.12));

        favourites.addMedia(bully);
        favourites.addMedia(loveSick);
        favourites.addMedia(new Song("Tiramisu", "Don Toliver", 2.19));

        Album mrMoraleBig = new Album("Mr. Morale & the Big Steppers");
        Album mrMorale = new Album("Mr. Morale");
        mrMorale.addMedia(new Song("N95", "Kendrick Lamar", 3.15));
        mrMorale.addMedia(new Song("Die Hard", "Kendrick Lamar", 3.59));
        mrMoraleBig.addMedia(mrMorale);
        mrMoraleBig.addMedia(new Song("Count Me Out", "Kendrick Lamar", 4.43));
        mrMoraleBig.addMedia(new Song("Savior", "Kendrick Lamar", 3.44));

        onRepeat.addMedia(mrMoraleBig);
        onRepeat.addMedia(new Song("Bodies", "Offset", 3.33));
        onRepeat.addMedia(new Song("luther", "Kendrick Lamar", 2.57));

        rootFolder.addMedia(favourites);
        rootFolder.addMedia(onRepeat);
        rootFolder.addMedia(sremmLife);
        rootFolder.addMedia(new Song("Money Trees", "Kendrick Lamar", 6.26));

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootFolder);

        addCompositeToTree(rootNode, rootFolder);

        tree.setModel(new DefaultTreeModel(rootNode));

        JButton play = new JButton("Play");
        play.setForeground(new Color(255, 255, 255));
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentClip != null && !currentClip.isRunning()) {
                    currentClip.start();
                    if (nowPlaying.getText().startsWith("Paused:")) {
                        nowPlaying.setText(nowPlaying.getText().substring(7));
                    }
                }
            }
        });
        play.setFont(new Font("Tahoma", Font.BOLD, 12));
        play.setBounds(402, 377, 140, 29);
        panel.add(play);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setBounds(322, 361, 145, 18);
        panel.add(progressBar);

        textField2 = new JTextField();
        textField2.setFont(new Font("Tahoma", Font.BOLD, 12));
        textField2.setForeground(new Color(255, 255, 255));
        textField2.setEditable(false);
        textField2.setBounds(576, 175, 214, 18);
        panel.add(textField2);
        textField2.setColumns(10);

        nowPlaying = new JTextField();
        nowPlaying.setForeground(new Color(255, 255, 255));
        nowPlaying.setFont(new Font("Tahoma", Font.BOLD, 12));
        nowPlaying.setEditable(false);
        nowPlaying.setColumns(10);
        nowPlaying.setBounds(10, 151, 244, 29);
        panel.add(nowPlaying);

        welcome = new JTextField();
        welcome.setForeground(new Color(255, 255, 255));
        welcome.setFont(new Font("Tahoma", Font.BOLD, 12));
        welcome.setEditable(false);
        welcome.setColumns(10);
        welcome.setBounds(306, 428, 220, 29);
        panel.add(welcome);

        JLabel lblNowPlaying = new JLabel("Now Playing");
        lblNowPlaying.setForeground(new Color(255, 255, 255));
        lblNowPlaying.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNowPlaying.setBounds(10, 112, 98, 29);
        panel.add(lblNowPlaying);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.BOLD, 12));
        textField.setForeground(new Color(255, 255, 255));
        textField.setEditable(false);
        textField.setColumns(10);
        textField.setBounds(576, 242, 214, 18);
        panel.add(textField);

        JButton mostPlayedSong_1 = new JButton("MostPlayedSong");
        mostPlayedSong_1.setForeground(new Color(255, 255, 255));
        mostPlayedSong_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Song songMax = rootFolder.mostPlayedSong();
                if (songMax != null)
                    textField.setText(songMax.songName + ":" + songMax.getCount() + " ascultari");
                else
                    textField.setText("Nicio melodie redata");

            }
        });
        mostPlayedSong_1.setFont(new Font("Tahoma", Font.BOLD, 12));
        mostPlayedSong_1.setBounds(576, 203, 164, 29);
        panel.add(mostPlayedSong_1);

        JButton mostStreamedArtist = new JButton("MostStreamedArtist");
        mostStreamedArtist.setForeground(new Color(255, 255, 255));
        mostStreamedArtist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Song max = rootFolder.mostPlayedSong();
                if (max != null)
                    textField2.setText(max.artistName);
                else
                    textField2.setText("Niciun artist preferat");

            }
        });
        mostStreamedArtist.setFont(new Font("Tahoma", Font.BOLD, 12));

        mostStreamedArtist.setBounds(576, 145, 164, 29);
        panel.add(mostStreamedArtist);

        tree.setOpaque(false);
        tree.setBackground(new Color(0, 0, 0, 0));

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JButton[] buttons = {pause, play, mostPlayedSong_1, mostStreamedArtist};
        for (JButton b : buttons) {
            b.setOpaque(false);
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
        }

        JTextField[] textFields = {textField, textField2, nowPlaying, welcome};
        for (JTextField tf : textFields) {
            tf.setOpaque(false);
            tf.setBackground(new Color(0, 0, 0, 0));
            tf.setBorder(null);
        }

        progressBar.setOpaque(false);
        progressBar.setBackground(new Color(0, 0, 0, 0));
        progressBar.setForeground(new Color(255, 255, 255, 200));

        tree.setBorder(null);

        scrollPane.setBorder(null);

        scrollPane.getViewport().setBorder(null);

        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public java.awt.Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
                                                                   boolean expanded, boolean leaf, int row, boolean hasFocus) {

                java.awt.Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
                        hasFocus);

                c.setForeground(Color.WHITE);

                setBackgroundNonSelectionColor(new Color(0, 0, 0, 0));
                setBackgroundSelectionColor(new Color(255, 255, 255, 50));
                setBorderSelectionColor(new Color(0, 0, 0, 0));

                return c;
            }
        });

        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(255, 255, 255, 50);
                this.trackColor = new Color(0, 0, 0, 0);
            }
        });

    }
}
