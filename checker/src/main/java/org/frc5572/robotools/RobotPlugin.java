package org.frc5572.robotools;

import javax.lang.model.util.Types;

import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.source.util.Trees;

public class RobotPlugin implements Plugin {

    @Override
    public String getName() {
        return "rchk";
    }

    @Override
    public void init(JavacTask task, String... arg1) {
        Types types = task.getTypes();
        Trees trees = Trees.instance(task);
        SourcePositions positions = trees.getSourcePositions();
        task.addTaskListener(new TaskListener() {
            @Override
            public void finished(TaskEvent e) {
                if (e.getKind() == TaskEvent.Kind.ANALYZE) {
                    CompilationData data = new CompilationData(types, trees, positions, e.getCompilationUnit(), e.getTypeElement(), null);
                    Checks.process(data);
                }
            }
        });
    }

}
