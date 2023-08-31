package org.frc5572.robotools;

import org.frc5572.robotools.checks.Check;
import org.frc5572.robotools.checks.IOCheck;

public class Checks {

    private static final Check[] CHECKS = new Check[] {new IOCheck(),};

    /** Run through all checks */
    public static void process(CompilationData data) {
        for (Check c : CHECKS) {
            c.check(data);
        }
    }

}
