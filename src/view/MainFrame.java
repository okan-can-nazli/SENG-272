package view;

import java.awt.*;
import javax.swing.*;
import model.AppSession;

// main app window screen

/*
 * Uses CardLayout to implement the 5-step wizard flow.
 * Coordinates navigation between steps and tracks completion status.
 */



public class MainFrame extends JFrame {
    private static final Color BG = new Color(245, 245, 248);

    private final AppSession session;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    private final StepIndicatorPanel stepIndicator;
    private final Step1ProfilePanel  step1;
    private final Step2DefinePanel   step2;
    private final Step3PlanPanel     step3;
    private final Step4CollectPanel  step4;
    private final Step5AnalysePanel  step5;

    private int currentStep = 1;

    public MainFrame() {
        session    = new AppSession();
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);

        setTitle("ISO/IEC 15939 Measurement Process Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 660);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(820, 600));
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        stepIndicator = new StepIndicatorPanel();
        add(stepIndicator, BorderLayout.NORTH);

        step1 = new Step1ProfilePanel(session, this);
        step2 = new Step2DefinePanel(session, this);
        step3 = new Step3PlanPanel(session, this);
        step4 = new Step4CollectPanel(session, this);
        step5 = new Step5AnalysePanel(session, this);

        cardPanel.setBackground(BG);
        cardPanel.add(step1, "step1");
        cardPanel.add(step2, "step2");
        cardPanel.add(step3, "step3");
        cardPanel.add(step4, "step4");
        cardPanel.add(step5, "step5");
        add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, "step1");
        stepIndicator.setCurrentStep(1);
    }

    // step tracker
    public void goToStep(int target) {
        if (target == 1 && currentStep != 1) {
            // Full reset for new session
            session.reset();
            stepIndicator.reset();
            step2.resetSelections();
            currentStep = 1;
            cardLayout.show(cardPanel, "step1");
            return;
        }

        if (target > currentStep) {
            stepIndicator.markCompleted(currentStep);
        }
        currentStep = target;
        stepIndicator.setCurrentStep(target);

        // refresh panels before showing(ai adviced)
        switch (target) {
            case 3 -> step3.refresh();
            case 4 -> step4.refresh();
            case 5 -> step5.refresh();
        }

        cardLayout.show(cardPanel, "step" + target);
    }

    // navigate back one step without marking anything as completed
    public void goBack(int fromStep) {
        int target = fromStep-1;
        if (target>=1){
            currentStep = target;
            stepIndicator.setCurrentStep(target);
            cardLayout.show(cardPanel,"step"+target);
        }
        
    }
}
