package org.frc5572.robotools;

import com.sun.source.util.TreeScanner;
import com.sun.source.tree.IdentifierTree;

public class GetIdentifier extends TreeScanner<String, Void> {

    @Override
    public String visitIdentifier(IdentifierTree node, Void p) {
        return node.getName().toString();
    }
    
}
