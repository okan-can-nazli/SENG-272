package view;

import java.awt.*;
import javax.swing.*;

// this panel draws the step bar at the top (circles + connecting lines)
// active step = blue, done = green with checkmark, not yet = gray
public class StepIndicatorPanel extends JPanel {
    private static final String[] STEP_NAMES = {"Profile", "Define", "Plan", "Collect", "Analyse"};
    private static final Color ACTIVE_COLOR    = new Color(33, 150, 243);
    private static final Color COMPLETED_COLOR = new Color(67, 160, 71);
    private static final Color INACTIVE_COLOR  = new Color(190, 190, 200);
    private static final Color BG              = new Color(255, 255, 255);

    private int currentStep = 1;
    private final boolean[] completed = new boolean[6]; // 1-indexed, index 0 unused

    public StepIndicatorPanel() {
        setBackground(BG);
        setPreferredSize(new Dimension(900, 72));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 228)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        // smooth rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int n = STEP_NAMES.length;
        int sw = w / n; // width per step slot

        for (int i = 0; i < n; i++) {
            int step   = i + 1;
            int cx     = sw * i + sw / 2;
            int cy     = h / 2 - 6;
            boolean done   = completed[step];
            boolean active = (step == currentStep);
            Color col = done ? COMPLETED_COLOR : active ? ACTIVE_COLOR : INACTIVE_COLOR;

            // draw line connecting to next step
            if (i < n - 1) {
                int ncx = sw * (i + 1) + sw / 2;
                g2.setColor(done ? COMPLETED_COLOR : INACTIVE_COLOR);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(cx + 16, cy, ncx - 16, cy);
            }

            // filled circle background
            g2.setColor(col);
            g2.fillOval(cx - 14, cy - 14, 28, 28);

            // extra ring around active step
            if (active) {
                g2.setColor(ACTIVE_COLOR.darker());
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(cx - 14, cy - 14, 28, 28);
            }

            // inside circle: checkmark if done, number if not
            g2.setColor(Color.WHITE);
            if (done) {
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                String ck = "\u2713";
                g2.drawString(ck, cx - fm.stringWidth(ck) / 2, cy + fm.getAscent() / 2 - 1);
            } else {
                String num = String.valueOf(step);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(num, cx - fm.stringWidth(num) / 2, cy + fm.getAscent() / 2 - 1);
            }

            // step label below the circle
            g2.setColor(active ? ACTIVE_COLOR : done ? COMPLETED_COLOR : INACTIVE_COLOR);
            g2.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 11));
            FontMetrics fm = g2.getFontMetrics();
            String name = STEP_NAMES[i];
            g2.drawString(name, cx - fm.stringWidth(name) / 2, cy + 26);
        }
        g2.dispose();
    }

    public void setCurrentStep(int step) {
        this.currentStep = step;
        repaint();
    }

    public void markCompleted(int step) {
        if (step >= 1 && step <= 5) {
            completed[step] = true;
            repaint();
        }
    }

    // called when user starts a new session from step 5
    public void reset() {
        for (int i = 1; i <= 5; i++) completed[i] = false;
        currentStep = 1;
        repaint();
    }
}