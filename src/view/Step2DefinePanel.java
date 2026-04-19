package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import model.AppSession;
import model.Scenario;
import model.ScenarioRepository;

// Step 2 UI panel. 
// We need three sets of radio buttons here. 
// Important: The scenarios list changes depending on whether they pick Health or Education.
public class Step2DefinePanel extends JPanel {
    private final AppSession session;
    private final MainFrame  mainFrame;

    // Quality Type (Product vs Process)
    private JRadioButton productBtn, processBtn;
    private ButtonGroup typeGroup;

    // Mode (Health vs Education)
    private JRadioButton healthBtn, educationBtn;
    private ButtonGroup modeGroup;

    // Scenarios (this gets wiped and redrawn when the mode changes)
    private ButtonGroup scenarioGroup;
    private JPanel scenarioInner;

    public Step2DefinePanel(AppSession session, MainFrame mainFrame) {
        this.session   = session;
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setBackground(new Color(245, 245, 248));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(25, 60, 25, 60));

        JLabel title = makeTitle("Step 2: Define Quality Dimensions");
        add(title, BorderLayout.NORTH);

        JPanel cols = new JPanel(new GridLayout(1, 3, 14, 0));
        cols.setBackground(new Color(245, 245, 248));
        cols.setBorder(new EmptyBorder(18, 0, 0, 0));

        // Column 1: Quality Type
        JPanel typeCard = makeCard("2a. Quality Type");
        typeGroup  = new ButtonGroup();
        productBtn = makeRadio("Product Quality");
        processBtn = makeRadio("Process Quality");
        
        // group them so you can only pick one
        typeGroup.add(productBtn);
        typeGroup.add(processBtn);
        productBtn.setSelected(true); // default
        
        typeCard.add(productBtn);
        addHint(typeCard, "Performance, security, usability...");
        typeCard.add(Box.createVerticalStrut(8));
        typeCard.add(processBtn);
        addHint(typeCard, "Sprint efficiency, code quality...");
        typeCard.add(Box.createVerticalGlue());
        cols.add(typeCard);

        // Column 2: Mode selection
        JPanel modeCard = makeCard("2b. Mode");
        modeGroup    = new ButtonGroup();
        healthBtn    = makeRadio("Health");
        educationBtn = makeRadio("Education");
        
        modeGroup.add(healthBtn);
        modeGroup.add(educationBtn);
        healthBtn.setSelected(true);

        // trigger a refresh of the 3rd column if they change the mode here
        ActionListener modeChanged = e -> updateScenarios();
        healthBtn.addActionListener(modeChanged);
        educationBtn.addActionListener(modeChanged);

        modeCard.add(healthBtn);
        addHint(modeCard, "Health management system scenarios");
        modeCard.add(Box.createVerticalStrut(8));
        modeCard.add(educationBtn);
        addHint(modeCard, "Education LMS system scenarios");
        modeCard.add(Box.createVerticalGlue());
        cols.add(modeCard);

        // Column 3: Scenarios
        JPanel scenarioCard = makeCard("2c. Scenario");
        scenarioInner = new JPanel();
        scenarioInner.setBackground(Color.WHITE);
        scenarioInner.setLayout(new BoxLayout(scenarioInner, BoxLayout.Y_AXIS));
        scenarioCard.add(scenarioInner);
        scenarioCard.add(Box.createVerticalGlue());
        cols.add(scenarioCard);

        add(cols, BorderLayout.CENTER);

        // Bottom nav buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(new Color(245, 245, 248));
        JButton back = makeSecondary("\u2190 Back");
        JButton next = makePrimary("Next \u2192");
        
        back.addActionListener(e -> mainFrame.goBack(2));
        next.addActionListener(e -> onNext());
        
        btnRow.add(back);
        btnRow.add(next);
        add(btnRow, BorderLayout.SOUTH);

        // populate the scenarios for the first time based on the default mode
        updateScenarios();
    }

    // Wipes the scenario panel and repopulates it based on whatever mode is currently checked.
    private void updateScenarios() {
        String mode = healthBtn.isSelected() ? "Health" : "Education";
        List<Scenario> scenarios = ScenarioRepository.getScenariosForMode(mode);

        // clear out the old radio buttons
        scenarioInner.removeAll();
        scenarioGroup = new ButtonGroup();

        for (int i = 0; i < scenarios.size(); i++) {
            JRadioButton rb = makeRadio(scenarios.get(i).getName());
            // stash the index so we can easily grab the right scenario later
            rb.setActionCommand(String.valueOf(i));
            scenarioGroup.add(rb);
            
            // default to the first one in the list
            if (i == 0) rb.setSelected(true);
            
            scenarioInner.add(rb);
            scenarioInner.add(Box.createVerticalStrut(8));
        }
        
        // force swing to redraw the panel
        scenarioInner.revalidate();
        scenarioInner.repaint();
    }

    private void onNext() {
        // grab whatever they selected
        String qualityType = productBtn.isSelected() ? "Product Quality" : "Process Quality";
        String mode        = healthBtn.isSelected()  ? "Health"          : "Education";

        int idx = 0; // fallback just in case nothing is selected
        if (scenarioGroup.getSelection() != null) {
            idx = Integer.parseInt(scenarioGroup.getSelection().getActionCommand());
        }
        Scenario selected = ScenarioRepository.getScenariosForMode(mode).get(idx);

        // save it all to the session state
        session.setQualityType(qualityType);
        session.setMode(mode);
        session.setSelectedScenario(selected);

        // move to the next screen
        mainFrame.goToStep(3);
    }

    // Called when the user restarts the app to put everything back to default
    public void resetSelections() {
        productBtn.setSelected(true);
        healthBtn.setSelected(true);
        updateScenarios();
    }

    // --- UI Helper Methods ---
    
    private JLabel makeTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 22));
        l.setForeground(new Color(35, 35, 60));
        return l;
    }

    private JPanel makeCard(String title) {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 228), 1),
                title, TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                new Color(33, 150, 243)
            ),
            new EmptyBorder(6, 8, 8, 8)
        ));
        return p;
    }

    private JRadioButton makeRadio(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        rb.setBackground(Color.WHITE);
        rb.setFocusPainted(false); // gets rid of the ugly dotted line when clicked
        return rb;
    }

    private void addHint(JPanel parent, String hint) {
        JLabel l = new JLabel(hint);
        l.setFont(new Font("SansSerif", Font.ITALIC, 11));
        l.setForeground(new Color(150, 150, 170));
        l.setBorder(new EmptyBorder(0, 22, 0, 0)); // indent it a bit so it aligns under the radio button
        parent.add(l);
    }

    private JButton makePrimary(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBackground(new Color(33, 150, 243));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(130, 40));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton makeSecondary(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.PLAIN, 14));
        b.setBackground(new Color(238, 238, 245));
        b.setForeground(new Color(80, 80, 100));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(110, 40));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}