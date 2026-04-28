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

// step 3 - read only view, shows dimensions and metrics of the selected scenario
// user cant edit anything here, just review before moving to data collection
public class Step3PlanPanel extends JPanel {
    private final AppSession session;
    private final MainFrame  mainFrame;
    private JPanel contentArea;

    public Step3PlanPanel(AppSession session, MainFrame mainFrame) {
        this.session   = session;
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setBackground(new Color(245, 245, 248));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(25, 60, 25, 60));

        JLabel title = makeTitle("Step 3: Plan Measurement");
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
        JButton next = makePrimary("Next \u2192");
        back.addActionListener(e -> mainFrame.goBack(3));
        next.addActionListener(e -> mainFrame.goToStep(4));
        btnRow.add(back);
        btnRow.add(next);
        add(btnRow, BorderLayout.SOUTH);
    }

    // called every time user navigates to this step, rebuilds the table
    public void refresh() {
        contentArea.removeAll();
        Scenario scenario = session.getSelectedScenario();
        if (scenario == null) return;

        addInfoLine("Scenario: " + scenario.getName());
        addInfoLine("Quality Type: " + session.getQualityType() + "   |   Mode: " + session.getMode());
        contentArea.add(Box.createVerticalStrut(10));

        for (Dimension dim : scenario.getDimensions()) {
            // blue header bar for each dimension
            JLabel header = new JLabel("  " + dim.getName() + "  \u2014  Coefficient: " + dim.getCoefficient());
            header.setFont(new Font("SansSerif", Font.BOLD, 13));
            header.setForeground(Color.WHITE);
            header.setOpaque(true);
            header.setBackground(new Color(33, 150, 243));
            header.setBorder(new EmptyBorder(6, 12, 6, 12));
            header.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 34));
            header.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentArea.add(header);

            // metrics table for this dimension
            String[] cols = {"Metric", "Coefficient", "Direction", "Range", "Unit"};
            DefaultTableModel model = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            for (Metric m : dim.getMetrics()) {
                model.addRow(new Object[]{
                    m.getName(), m.getCoefficient(), m.getDirectionLabel(), m.getRangeLabel(), m.getUnit()
                });
            }

            JTable table = makeTable(model, new Color(235, 243, 254), new Color(45, 65, 105));
            int rows = dim.getMetrics().size();
            addTableToContent(table, rows);
            contentArea.add(Box.createVerticalStrut(12));
        }

        contentArea.revalidate();
        contentArea.repaint();
    }

    private void addInfoLine(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.ITALIC, 12));
        l.setForeground(new Color(100, 105, 135));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentArea.add(l);
        contentArea.add(Box.createVerticalStrut(3));
    }

    private JTable makeTable(DefaultTableModel model, Color headerBg, Color headerFg) {
        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(27);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(headerBg);
        table.getTableHeader().setForeground(headerFg);
        table.setGridColor(new Color(220, 225, 235));
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(true);
        // alternating row colors
        table.setDefaultRenderer(Object.class, new TableCellRenderer() {
            private final DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                Component c = r.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 254));
                ((JLabel) c).setBorder(new EmptyBorder(0, 9, 0, 9));
                return c;
            }
        });
        return table;
    }

    private void addTableToContent(JTable table, int rows) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(210, 218, 230)));
        int h = table.getRowHeight() * rows + 25;
        sp.setPreferredSize(new java.awt.Dimension(760, h));
        sp.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, h));
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentArea.add(sp);
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
        b.setPreferredSize(new java.awt.Dimension(130, 40));
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