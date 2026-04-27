package GUI;

import Playback.CompositePlayable;
import Playback.Playable;
import Media.Album;
import Media.Folder;
import Media.PlayList;
import Media.Song;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.sound.sampled.*;

public class Interface extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Color CYAN     = new Color(0, 200, 255);
    private static final Color CYAN_SEL = new Color(0, 200, 255, 40);
    private static final Color GREEN    = new Color(29, 185, 84);
    private static final Color WHITE    = Color.WHITE;
    private static final Color MUTED    = new Color(130, 185, 215);
    private static final Color TRANSP   = new Color(0, 0, 0, 0);
    private static final Color PANEL_BG = new Color(3, 9, 26, 185);
    private static final Color BAR_BG   = new Color(0, 0, 10, 220);

    private JTextField nowPlaying;
    private JTextField welcome;
    private JTextField artistField;
    private JTextField songField;
    private JProgressBar progressBar;
    private javax.swing.Timer progressTimer;
    private static Clip currentClip;
    public String userName;

    public Interface(String userName) {
        this.userName = userName;
        initUI();
        welcome.setText("Welcome back, " + userName);
    }

    public static void PlayMusic(java.net.URL musicURL) {
        try {
            if (currentClip != null && currentClip.isRunning()) {
                currentClip.stop();
                currentClip.close();
            }
            AudioInputStream ai = AudioSystem.getAudioInputStream(musicURL);
            currentClip = AudioSystem.getClip();
            currentClip.open(ai);
            currentClip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void playSongFromTree(String songName) {
        String duration = songName.substring(songName.length() - 5);
        String name     = songName.substring(0, songName.length() - 5);
        java.net.URL url = Interface.class.getResource("/" + name.replaceAll("\\s+", "") + ".wav");
        if (url == null) { System.out.println("Missing: " + name); return; }
        PlayMusic(url);
        startProgressBar();
        nowPlaying.setText(name + duration);
    }

    private void addCompositeToTree(DefaultMutableTreeNode parent, CompositePlayable composite) {
        for (Playable p : composite.getChildren()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(p);
            parent.add(node);
            if (p instanceof CompositePlayable) addCompositeToTree(node, (CompositePlayable) p);
        }
    }

    private void startProgressBar() {
        if (progressTimer != null) progressTimer.stop();
        progressTimer = new javax.swing.Timer(150, e -> {
            if (currentClip != null && currentClip.isOpen()) {
                long cur = currentClip.getMicrosecondPosition();
                long tot = currentClip.getMicrosecondLength();
                if (tot > 0) progressBar.setValue((int)(100 * cur / tot));
                if (!currentClip.isRunning()) progressTimer.stop();
            }
        });
        progressTimer.start();
    }

    private JButton makeBtn(String text, boolean accent) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hov = getModel().isRollover(), prs = getModel().isPressed();
                Color fill = accent
                        ? (prs ? GREEN.darker() : hov ? GREEN.brighter() : GREEN)
                        : (prs ? new Color(0,50,80,220) : hov ? new Color(0,60,95,230) : new Color(5,18,45,210));
                g2.setColor(fill);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(hov ? CYAN : new Color(0, 160, 200, 180));
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(0.6f, 0.6f, getWidth()-1.2f, getHeight()-1.2f, 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        b.setOpaque(false); b.setContentAreaFilled(false);
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JTextField ro(Font f, Color c) {
        JTextField tf = new JTextField();
        tf.setFont(f); tf.setForeground(c);
        tf.setEditable(false); tf.setOpaque(false);
        tf.setBackground(TRANSP); tf.setBorder(null);
        return tf;
    }

    private JLabel lbl(String t, int style, int size, Color c) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(c);
        return l;
    }

    private JPanel darkPanel() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PANEL_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
    }

    private void initUI() {
        setTitle("AlphaFy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 819, 577);
        setResizable(false);

        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(5, 5, 20));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JPanel bg = new JPanel() {
            private final Image img = new ImageIcon(
                    Interface.class.getResource("/background.png")).getImage();
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        bg.setLayout(null);
        bg.setBounds(10, 10, 800, 533);
        contentPane.add(bg);

        final int SX  = 250;
        final int SY  = 143;
        final int SW  = 303;
        final int SH  = 268;
        final int GAP = 6;

        JPanel left = darkPanel();
        left.setOpaque(false);
        left.setLayout(null);
        left.setBounds(GAP, SY + GAP, SX - GAP * 2, SH - GAP * 2);
        bg.add(left);

        JLabel capNP = lbl("NOW PLAYING", Font.BOLD, 9, MUTED);
        capNP.setBounds(12, 12, 160, 12);
        left.add(capNP);

        JSeparator hr1 = new JSeparator();
        hr1.setForeground(new Color(0, 180, 220, 50));
        hr1.setBounds(8, 27, left.getWidth() - 16, 1);
        left.add(hr1);

        nowPlaying = ro(new Font("Segoe UI", Font.BOLD, 12), WHITE);
        nowPlaying.setBounds(10, 34, left.getWidth() - 20, 40);
        left.add(nowPlaying);

        JSeparator hr2 = new JSeparator();
        hr2.setForeground(new Color(0, 180, 220, 50));
        hr2.setBounds(8, 82, left.getWidth() - 16, 1);
        left.add(hr2);

        JLabel capWelcome = lbl("LOGGED IN AS", Font.BOLD, 9, MUTED);
        capWelcome.setBounds(12, 90, 160, 12);
        left.add(capWelcome);

        welcome = ro(new Font("Segoe UI", Font.PLAIN, 11), new Color(160, 210, 230));
        welcome.setBounds(10, 106, left.getWidth() - 20, 40);
        left.add(welcome);

        JPanel screen = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 200, 255, 12));
                g2.fill(new RoundRectangle2D.Float(-4, -4, getWidth()+8, getHeight()+8, 20, 20));
                g2.setColor(PANEL_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        screen.setOpaque(false);
        screen.setLayout(null);
        screen.setBounds(SX, SY, SW, SH);
        bg.add(screen);

        JLabel libLbl = lbl("Your Library", Font.BOLD, 12, CYAN);
        libLbl.setBounds(12, 8, SW - 24, 20);
        screen.add(libLbl);

        JPanel hr3 = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 180, 220, 55));
                g.fillRect(0, 0, getWidth(), 1);
            }
        };
        hr3.setOpaque(false);
        hr3.setBounds(8, 30, SW - 16, 1);
        screen.add(hr3);

        JTree tree = buildTree();
        JScrollPane scroll = new JScrollPane(tree,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setBackground(TRANSP);
        scroll.getVerticalScrollBar().setOpaque(false);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(4, 0));
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = new Color(0, 180, 220, 120);
                trackColor = TRANSP;
            }
            @Override protected JButton createDecreaseButton(int o) { return tiny(); }
            @Override protected JButton createIncreaseButton(int o) { return tiny(); }
            private JButton tiny() {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b;
            }
        });
        scroll.setBounds(6, 34, SW - 12, SH - 40);
        screen.add(scroll);

        int rx = SX + SW + GAP;
        int rw = 800 - rx - GAP;

        JPanel right = darkPanel();
        right.setOpaque(false);
        right.setLayout(null);
        right.setBounds(rx, SY + GAP, rw, SH - GAP * 2);
        bg.add(right);

        JLabel capStats = lbl("STATISTICS", Font.BOLD, 9, MUTED);
        capStats.setBounds(12, 12, rw, 12);
        right.add(capStats);

        JSeparator hr4 = new JSeparator();
        hr4.setForeground(new Color(0, 180, 220, 50));
        hr4.setBounds(8, 27, rw - 16, 1);
        right.add(hr4);

        JLabel capA = lbl("TOP ARTIST", Font.BOLD, 9, MUTED);
        capA.setBounds(12, 36, rw, 12);
        right.add(capA);

        JButton btnArtist = makeBtn("Most Streamed Artist", false);
        btnArtist.setBounds(8, 51, rw - 16, 28);
        right.add(btnArtist);

        artistField = ro(new Font("Segoe UI", Font.BOLD, 11), CYAN);
        artistField.setBounds(10, 83, rw - 16, 30);
        right.add(artistField);

        JSeparator hr5 = new JSeparator();
        hr5.setForeground(new Color(0, 180, 220, 50));
        hr5.setBounds(8, 120, rw - 16, 1);
        right.add(hr5);

        JLabel capS = lbl("TOP SONG", Font.BOLD, 9, MUTED);
        capS.setBounds(12, 130, rw, 12);
        right.add(capS);

        JButton btnSong = makeBtn("Most Played Song", false);
        btnSong.setBounds(8, 145, rw - 16, 28);
        right.add(btnSong);

        songField = ro(new Font("Segoe UI", Font.BOLD, 11), CYAN);
        songField.setBounds(10, 177, rw - 16, 40);
        right.add(songField);

        int barY = SY + SH + 8;
        int barH = 533 - barY;

        JPanel bar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 200, 255, 70));
                g2.fillRect(0, 0, getWidth(), 1);
                g2.setColor(BAR_BG);
                g2.fillRect(0, 1, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setLayout(null);
        bar.setBounds(0, barY, 800, barH);
        bg.add(bar);

        int btnW = 105, btnH = 32, btnGap = 10;
        int totalW = btnW * 2 + btnGap;
        int startX = (800 - totalW) / 2;
        int btnY   = (barH - btnH) / 2 - 10;

        JButton btnPause = makeBtn("⏸  Pause", false);
        btnPause.setBounds(startX, btnY, btnW, btnH);
        bar.add(btnPause);

        JButton btnPlay = makeBtn("▶  Play", true);
        btnPlay.setBounds(startX + btnW + btnGap, btnY, btnW, btnH);
        bar.add(btnPlay);

        int progW = totalW + 40;
        int progX = (800 - progW) / 2;
        int progY = btnY + btnH + 10;

        JPanel track = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.dispose();
            }
        };
        track.setOpaque(false);
        track.setLayout(null);
        track.setBounds(progX, progY, progW, 6);
        bar.add(track);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setOpaque(false);
        progressBar.setBorder(null);
        progressBar.setBackground(TRANSP);
        progressBar.setForeground(CYAN);
        progressBar.setBounds(0, 0, progW, 6);
        progressBar.setUI(new BasicProgressBarUI() {
            @Override public void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int filled = (int)(getWidth() * progressBar.getValue() / 100.0);
                if (filled > 0) {
                    g2.setColor(CYAN);
                    g2.fill(new RoundRectangle2D.Float(0, 0, filled, 6, 6, 6));
                }
                g2.dispose();
            }
        });
        track.add(progressBar);

        Folder rootFolder = buildLibrary();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootFolder);
        addCompositeToTree(rootNode, rootFolder);
        tree.setModel(new DefaultTreeModel(rootNode));

        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) return;
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (node == null || !node.isLeaf()) return;
                playSongFromTree(node.toString());
                ((Song) node.getUserObject()).play();
            }
        });

        btnPause.addActionListener(e -> {
            if (currentClip != null && currentClip.isRunning()) {
                currentClip.stop();
                String t = nowPlaying.getText();
                if (!t.startsWith("Paused: ")) nowPlaying.setText("Paused: " + t);
            }
        });

        btnPlay.addActionListener(e -> {
            if (currentClip != null && !currentClip.isRunning()) {
                currentClip.start();
                startProgressBar();
                if (nowPlaying.getText().startsWith("Paused: "))
                    nowPlaying.setText(nowPlaying.getText().substring(8));
            }
        });

        btnArtist.addActionListener(e -> {
            Song max = rootFolder.mostPlayedSong();
            artistField.setText(max != null ? max.artistName : "—");
        });

        btnSong.addActionListener(e -> {
            Song max = rootFolder.mostPlayedSong();
            songField.setText(max != null
                    ? max.songName + " · " + max.getCount() + " plays" : "—");
        });
    }

    private JTree buildTree() {
        JTree tree = new JTree();
        tree.setOpaque(false);
        tree.setBackground(TRANSP);
        tree.setForeground(WHITE);
        tree.setBorder(new EmptyBorder(2, 6, 2, 4));
        tree.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tree.setRowHeight(17);
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree t, Object val,
                    boolean sel, boolean exp, boolean leaf, int row, boolean focus) {
                Component c = super.getTreeCellRendererComponent(
                        t, val, sel, exp, leaf, row, focus);
                c.setForeground(sel ? CYAN : leaf ? WHITE : new Color(170, 215, 235));
                setFont(new Font("Segoe UI", leaf ? Font.PLAIN : Font.BOLD, 11));
                setBackgroundNonSelectionColor(TRANSP);
                setBackgroundSelectionColor(CYAN_SEL);
                setBorderSelectionColor(TRANSP);
                setLeafIcon(null); setOpenIcon(null); setClosedIcon(null);
                return c;
            }
        });
        return tree;
    }

    private Folder buildLibrary() {
        Folder root = new Folder("AlphaFy Music");
        PlayList favourites = new PlayList("Favourites");
        PlayList onRepeat   = new PlayList("On Repeat");

        Album sremmLife = new Album("SremmLife");
        sremmLife.addMedia(new Song("No Typee",          "Rae Sremmurd", 3.20));
        sremmLife.addMedia(new Song("Black Beatles",     "Rae Sremmurd", 4.51));
        sremmLife.addMedia(new Song("Swang",             "Rae Sremmurd", 3.28));
        sremmLife.addMedia(new Song("Come Get Her",      "Rae Sremmurd", 3.46));
        sremmLife.addMedia(new Song("Not So Bad",        "Rae Sremmurd", 3.18));

        Album bully = new Album("BULLY");
        bully.addMedia(new Song("BULLY",                 "Ye", 2.03));
        bully.addMedia(new Song("PREACHER MAN",          "Ye", 3.02));
        bully.addMedia(new Song("BEAUTY AND THE BEAST",  "Ye", 1.46));
        bully.addMedia(new Song("LAST BREATH",           "Ye", 2.22));
        bully.addMedia(new Song("LOSING YOUR MIND",      "Ye", 3.24));

        Album loveSick = new Album("Love Sick");
        loveSick.addMedia(new Song("No Pole",            "Don Toliver", 3.07));
        loveSick.addMedia(new Song("Embarrassed",        "Don Toliver", 3.12));

        favourites.addMedia(bully);
        favourites.addMedia(loveSick);
        favourites.addMedia(new Song("Tiramisu",         "Don Toliver", 2.19));

        Album mrMoraleBig = new Album("Mr. Morale & the Big Steppers");
        Album mrMorale = new Album("Mr. Morale");
        mrMorale.addMedia(new Song("N95",                "Kendrick Lamar", 3.15));
        mrMorale.addMedia(new Song("Die Hard",           "Kendrick Lamar", 3.59));
        mrMoraleBig.addMedia(mrMorale);
        mrMoraleBig.addMedia(new Song("Count Me Out",    "Kendrick Lamar", 4.43));
        mrMoraleBig.addMedia(new Song("Savior",          "Kendrick Lamar", 3.44));

        onRepeat.addMedia(mrMoraleBig);
        onRepeat.addMedia(new Song("Bodies",             "Offset",          3.33));
        onRepeat.addMedia(new Song("luther",             "Kendrick Lamar",  2.57));

        root.addMedia(favourites);
        root.addMedia(onRepeat);
        root.addMedia(sremmLife);
        root.addMedia(new Song("Money Trees",            "Kendrick Lamar",  6.26));
        return root;
    }
}
