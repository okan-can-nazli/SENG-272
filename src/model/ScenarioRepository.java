package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// hard coded scenario data (I used aı as a workhorse right there)


/**
 * Contains 2 modes (Health, Education) with 2 scenarios each.
 * All data is defined as Java constants — no file I/O required.
 */


public class ScenarioRepository {

    /** Map: mode name -> list of scenarios for that mode */
    private static final Map<String, List<Scenario>> scenarioMap = new HashMap<>();

    static {
        initHealthScenarios();
        initEducationScenarios();
    }

    // -------------------------------------------------------------------------
    // Health Scenarios
    // -------------------------------------------------------------------------
    private static void initHealthScenarios() {
        List<Scenario> list = new ArrayList<>();

        // --- Scenario A: Hospital Management System ---
        Scenario a = new Scenario("Scenario A \u2014 Hospital Management System");

        Dimension relA = new Dimension("Reliability", 30);
        relA.addMetric(new Metric("System uptime",         60, true,  95,  100, "%",        98.5));
        relA.addMetric(new Metric("MTTR",                  40, false,  0,  120, "min",       15.0));

        Dimension secA = new Dimension("Security", 25);
        secA.addMetric(new Metric("Auth failure rate",     50, false,  0,   10, "%",          0.5));
        secA.addMetric(new Metric("Data breach incidents", 50, false,  0,    5, "incidents",  0.0));

        Dimension perfA = new Dimension("Performance", 25);
        perfA.addMetric(new Metric("Response time",        50, false,  0,    2, "sec",        0.3));
        perfA.addMetric(new Metric("Throughput",           50, true,   0, 1000, "req/s",    750.0));

        Dimension usaA = new Dimension("Usability", 20);
        usaA.addMetric(new Metric("SUS score",             60, true,   0,  100, "points",    78.0));
        usaA.addMetric(new Metric("User error rate",       40, false,  0,   20, "%",          3.0));

        a.addDimension(relA);
        a.addDimension(secA);
        a.addDimension(perfA);
        a.addDimension(usaA);
        list.add(a);

        // --- Scenario B: Pharmacy Management System ---
        Scenario b = new Scenario("Scenario B \u2014 Pharmacy Management System");

        Dimension relB = new Dimension("Reliability", 30);
        relB.addMetric(new Metric("System uptime",         60, true,  95,  100, "%",        97.2));
        relB.addMetric(new Metric("MTTR",                  40, false,  0,  120, "min",       30.0));

        Dimension secB = new Dimension("Security", 25);
        secB.addMetric(new Metric("Auth failure rate",     50, false,  0,   10, "%",          1.5));
        secB.addMetric(new Metric("Encryption coverage",  50, true,   0,  100, "%",         95.0));

        Dimension perfB = new Dimension("Performance", 25);
        perfB.addMetric(new Metric("Response time",        50, false,  0,    2, "sec",        0.8));
        perfB.addMetric(new Metric("Throughput",           50, true,   0, 1000, "req/s",    500.0));

        Dimension usaB = new Dimension("Usability", 20);
        usaB.addMetric(new Metric("SUS score",             60, true,   0,  100, "points",    72.0));
        usaB.addMetric(new Metric("Task completion rate",  40, true,   0,  100, "%",         85.0));

        b.addDimension(relB);
        b.addDimension(secB);
        b.addDimension(perfB);
        b.addDimension(usaB);
        list.add(b);

        scenarioMap.put("Health", list);
    }

    // -------------------------------------------------------------------------
    // Education Scenarios
    // -------------------------------------------------------------------------
    private static void initEducationScenarios() {
        List<Scenario> list = new ArrayList<>();

        // --- Scenario C: Team Alpha (matches PDF sample dataset) ---
        Scenario c = new Scenario("Scenario C \u2014 Team Alpha");

        Dimension usaC = new Dimension("Usability", 25);
        usaC.addMetric(new Metric("SUS score",             50, true,   0,  100, "points",   89.0));
        usaC.addMetric(new Metric("Onboarding time",       50, false,  0,   60, "min",       5.0));

        Dimension perfC = new Dimension("Performance Efficiency", 20);
        perfC.addMetric(new Metric("Video start time",     50, false,  0,   15, "sec",       2.0));
        perfC.addMetric(new Metric("Concurrent exams",     50, true,   0,  600, "users",   550.0));

        Dimension accC = new Dimension("Accessibility", 20);
        accC.addMetric(new Metric("WCAG compliance",       50, true,   0,  100, "%",        92.0));
        accC.addMetric(new Metric("Screen reader score",   50, true,   0,  100, "%",        85.0));

        Dimension relC = new Dimension("Reliability", 20);
        relC.addMetric(new Metric("Uptime",                50, true,  95,  100, "%",        99.1));
        relC.addMetric(new Metric("MTTR",                  50, false,  0,  120, "min",      10.0));

        Dimension funC = new Dimension("Functional Suitability", 15);
        funC.addMetric(new Metric("Feature completion",    50, true,   0,  100, "%",        95.0));
        funC.addMetric(new Metric("Assignment submit rate",50, true,   0,  100, "%",        98.0));

        c.addDimension(usaC);
        c.addDimension(perfC);
        c.addDimension(accC);
        c.addDimension(relC);
        c.addDimension(funC);
        list.add(c);

        // --- Scenario D: Team Beta ---
        Scenario d = new Scenario("Scenario D \u2014 Team Beta");

        Dimension usaD = new Dimension("Usability", 25);
        usaD.addMetric(new Metric("SUS score",             50, true,   0,  100, "points",   74.0));
        usaD.addMetric(new Metric("Onboarding time",       50, false,  0,   60, "min",      18.0));

        Dimension perfD = new Dimension("Performance Efficiency", 20);
        perfD.addMetric(new Metric("Video start time",     50, false,  0,   15, "sec",       6.0));
        perfD.addMetric(new Metric("Concurrent exams",     50, true,   0,  600, "users",   350.0));

        Dimension accD = new Dimension("Accessibility", 20);
        accD.addMetric(new Metric("WCAG compliance",       50, true,   0,  100, "%",        70.0));
        accD.addMetric(new Metric("Screen reader score",   50, true,   0,  100, "%",        65.0));

        Dimension relD = new Dimension("Reliability", 20);
        relD.addMetric(new Metric("Uptime",                50, true,  95,  100, "%",        97.5));
        relD.addMetric(new Metric("MTTR",                  50, false,  0,  120, "min",      45.0));

        Dimension funD = new Dimension("Functional Suitability", 15);
        funD.addMetric(new Metric("Feature completion",    50, true,   0,  100, "%",        80.0));
        funD.addMetric(new Metric("Assignment submit rate",50, true,   0,  100, "%",        88.0));

        d.addDimension(usaD);
        d.addDimension(perfD);
        d.addDimension(accD);
        d.addDimension(relD);
        d.addDimension(funD);
        list.add(d);

        scenarioMap.put("Education", list);
    }

    /**
     * Returns the list of scenarios for the given mode name.
     * @param mode "Health" or "Education"
     */
    public static List<Scenario> getScenariosForMode(String mode) {
        return scenarioMap.getOrDefault(mode, new ArrayList<>());
    }
}
