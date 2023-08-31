package org.frc5572.robotools.checks;

import org.frc5572.robotools.CompilationData;

@FunctionalInterface
public interface Check {
    
    public boolean check(CompilationData data);

}
