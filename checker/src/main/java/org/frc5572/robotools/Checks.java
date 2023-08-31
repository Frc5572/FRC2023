package org.frc5572.robotools;

import org.frc5572.robotools.checks.*;

public class Checks {
    
    private static final Check[] CHECKS = new Check[] {
        new ExampleCheck(),
        new IOCheck(),
    };

    public static void process(CompilationData data) {
        for(Check c : CHECKS) {
            c.check(data);
        }
    }

}
