package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.AppSession;
import model.Profile;

/**
  Step 1: Profile
  get username, school, and session name
  validates all fields before allowing navigation to Step 2
 */
public class Step1ProfilePanel extends JPanel {
    private final AppSession session;
    private final MainFrame  mainFrame;

    private JTextField usernameField;
    private JTextField schoolField;
    private JTextField sessionNameField;

    public Step1ProfilePanel(AppSession session, MainFrame mainFrame) {
        this.session   = session;
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setBackground(new Color(245, 245, 248));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 80, 30, 80));

        // Title
        JLabel title = makeTitle("Step 1: Profile");
        JLabel sub = new JLabel("Enter your information to begin the measurement session.");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(new Color(110, 110, 140));

        JPanel header = new JPanel();
        header.setBackground(new Color(245, 245, 248));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(sub);
        add(header, BorderLayout.NORTH);

        // Card
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 228), 1),
            new EmptyBorder(35, 50, 35, 50)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(10, 8, 10, 8);
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        usernameField    = makeField("e.g. john_doe");
        schoolField      = makeField("e.g. Cankaya University");
        sessionNameField = makeField("e.g. Q1 Evaluation");

        addRow(card, gbc, 0, "Username",      usernameField);
        addRow(card, gbc, 1, "School",         schoolField);
        addRow(card, gbc, 2, "Session Name",   sessionNameField);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(245, 245, 248));
        wrapper.setBorder(new EmptyBorder(20, 0, 0, 0));
        wrapper.add(card, BorderLayout.CENTER);
        add(wrapper, BorderLayout.CENTER);

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnRow.setBackground(new Color(245, 245, 248));
        btnRow.setBorder(new EmptyBorder(16, 0, 0, 0));
        JButton next = makePrimary("Next \u2192");
        next.addActionListener(e -> onNext());
        btnRow.add(next);
        add(btnRow, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(new Color(60, 60, 85));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.75;
        panel.add(field, gbc);
    }

    private void onNext() {
        String username    = usernameField.getText().trim();
        String school      = schoolField.getText().trim();
        String sessionName = sessionNameField.getText().trim();

        if (username.isEmpty()) {
            warn("Please enter your username to continue.");
            usernameField.requestFocus();
            return;
        }
        if (school.isEmpty()) {
            warn("Please enter your school name to continue.");
            schoolField.requestFocus();
            return;
        }
        if (sessionName.isEmpty()) {
            warn("Please enter a session name to continue.");
            sessionNameField.requestFocus();
            return;
        }

        Profile p = session.getProfile();
        p.setUsername(username);
        p.setSchool(school);
        p.setSessionName(sessionName);

        mainFrame.goToStep(2);
    }








    //! Helpers
    private JLabel makeTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 22));
        l.setForeground(new Color(35, 35, 60));
        return l;
    }

    private JTextField makeField(String placeholder) {
        JTextField f = new JTextField(28) {
            @Override public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(new Color(190, 190, 205));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    Insets ins = getInsets();
                    g2.drawString(placeholder, ins.left, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
                }
            }
        };
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(205, 205, 215), 1),
            new EmptyBorder(7, 11, 7, 11)
        ));
        f.setPreferredSize(new Dimension(300, 38));
        return f;
    }

    private JButton makePrimary(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBackground(new Color(33, 150, 243));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(140, 40));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Missing Information", JOptionPane.WARNING_MESSAGE);
    }
}
