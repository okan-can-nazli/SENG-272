package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.AppSession;
import model.Dimension;
import model.Metric;
import model.Scenario;

// step 4 - shows raw metric values and their calculated scores (1.0 to 5.0)
// scores are color coded: green = good, yellow = ok, red = bad
public class Step4CollectPanel extends JPanel {
    private final AppSession session;
    private final MainFrame  mainFrame;
    private JPanel contentArea;

    public Step4CollectPanel(AppSession session, MainFrame mainFrame) {
        this.session   = session;
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setBackground(new Color(245, 245, 248));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(25, 60, 25, 60));

        JLabel title = makeTitle("Step 4: Collect Data");
        add(title, BorderLayout.NORTH);

        contentArea = new JPanel();
        contentArea.setBackground(new Color(245, 245, 248));
        contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(contentArea);
        scroll.setBorder(new EmptyBorder(14, 0, 0, 0));
        scroll.getViewport().setBackground(new Color(245, 245, 248));
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        add(scroll, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(new Color(245, 245, 248));
        JButton back = makeSecondary("\u2190 Back");
        JButton next = makePrimary("Analyse \u2192");
        back.addActionListener(e -> mainFrame.goBack(4));
        next.addActionListener(e -> mainFrame.goToStep(5));
        btnRow.add(back);
        btnRow.add(next);
        add(btnRow, BorderLayout.SOUTH);
    }

    // rebuilds the table every time user arrives at this step
    public void refresh() {
        contentArea.removeAll();
        Scenario scenario = session.getSelectedScenario();
        if (scenario == null) return;

        // color legend at the top
        JPanel legend = buildLegend();
        legend.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentArea.add(legend);
        contentArea.add(Box.createVerticalStrut(10));

        for (Dimension dim : scenario.getDimensions()) {
            // green header per dimension
            JLabel header = new JLabel("  " + dim.getName());
            header.setFont(new Font("SansSerif", Font.BOLD, 13));
            header.setForeground(Color.WHITE);
            header.setOpaque(true);
            header.setBackground(new Color(67, 160, 71));
            header.setBorder(new EmptyBorder(6, 12, 6, 12));
            header.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 34));
            header.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentArea.add(header);

            String[] cols = {"Metric", "Direction", "Range", "Raw Value", "Score (1\u20135)", "Coeff / Unit"};
            DefaultTableModel model = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            for (Metric m : dim.getMetrics()) {
                double score = m.calculateScore();
                model.addRow(new Object[]{
                    m.getName(),
                    m.getDirectionLabel(),
                    m.getRangeLabel(),
                    formatValue(m.getRawValue()),
                    String.format("%.1f", score),
                    m.getCoefficient() + " / " + m.getUnit()
                });
            }

            JTable table = makeScoreTable(model);
            int h = table.getRowHeight() * dim.getMetrics().size() + 25;
            JScrollPane sp = new JScrollPane(table);
            sp.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(200, 220, 205)));
            sp.setPreferredSize(new java.awt.Dimension(760, h));
            sp.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, h));
            sp.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentArea.add(sp);
            contentArea.add(Box.createVerticalStrut(12));
        }

        contentArea.revalidate();
        contentArea.repaint();
    }

    // removes decimal if value is a whole number e.g 89.0 -> 89
    private String formatValue(double v) {
        return (v == (long) v) ? String.valueOf((long) v) : String.valueOf(v);
    }

    private JPanel buildLegend() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        p.setBackground(new Color(245, 245, 248));
        p.add(legendDot(new Color(200, 240, 200), "4.5 \u2013 5.0  Excellent"));
        p.add(legendDot(new Color(220, 245, 180), "3.5 \u2013 4.0  Good"));
        p.add(legendDot(new Color(255, 244, 190), "2.5 \u2013 3.0  Fair"));
        p.add(legendDot(new Color(255, 215, 200), "1.0 \u2013 2.0  Poor"));
        return p;
    }

    private JLabel legendDot(Color bg, String text) {
        JLabel l = new JLabel("  " + text + "  ");
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        l.setOpaque(true);
        l.setBackground(bg);
        l.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210), 1));
        return l;
    }

    private JTable makeScoreTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(27);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(230, 248, 232));
        table.getTableHeader().setForeground(new Color(30, 80, 40));
        table.setGridColor(new Color(215, 228, 218));
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(true);

        // score column (index 4) gets color based on value
        table.setDefaultRenderer(Object.class, new TableCellRenderer() {
            private final DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                Component c = r.getTableCellRendererComponent(t, v, sel, foc, row, col);
                ((JLabel) c).setBorder(new EmptyBorder(0, 9, 0, 9));
                if (!sel) {
                    if (col == 4) {
                        try {
                            double s = Double.parseDouble(v.toString());
                            if      (s >= 4.5) c.setBackground(new Color(200, 240, 200));
                            else if (s >= 3.5) c.setBackground(new Color(220, 245, 180));
                            else if (s >= 2.5) c.setBackground(new Color(255, 244, 190));
                            else               c.setBackground(new Color(255, 215, 200));
                            ((JLabel) c).setFont(((JLabel) c).getFont().deriveFont(Font.BOLD));
                        } catch (NumberFormatException ex) {
                            c.setBackground(Color.WHITE);
                        }
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(246, 251, 247));
                    }
                }
                return c;
            }
        });
        return table;
    }

    private JLabel makeTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 22));
        l.setForeground(new Color(35, 35, 60));
        return l;
    }

    private JButton makePrimary(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBackground(new Color(33, 150, 243));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setPreferredSize(new java.awt.Dimension(140, 40));
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
        b.setPreferredSize(new java.awt.Dimension(110, 40));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}