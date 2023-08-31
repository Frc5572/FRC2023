package org.frc5572.robotools;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.Trees;

public class CompilationData {

    public Types types;
    private Trees trees;
    private SourcePositions positions;
    private CompilationUnitTree compilationUnitTree;
    public TypeElement element;
    private Messager messager;

    public CompilationData(Types types, Trees trees, SourcePositions positions, CompilationUnitTree compilationUnitTree,
            TypeElement element, Messager messager) {
        this.types = types;
        this.trees = trees;
        this.positions = positions;
        this.compilationUnitTree = compilationUnitTree;
        this.element = element;
        this.messager = messager;
    }

    public CompilationData(JavacTask task, TaskEvent event) {
        this(task.getTypes(), Trees.instance(task), Trees.instance(task).getSourcePositions(), event.getCompilationUnit(), event.getTypeElement(), null);
    }

    public CompilationData(ProcessingEnvironment processingEnv, TypeElement element) {
        this(processingEnv.getTypeUtils(), null, null, null, element, processingEnv.getMessager());
    }

    public void error(Element element, Object object) {
        echo(element, object.toString(), Diagnostic.Kind.ERROR, "error", "Error");
    }

    public void warn(Element element, Object object) {
        echo(element, object.toString(), Diagnostic.Kind.MANDATORY_WARNING, "warning", "Warning");
    }

    public void note(Element element, Object object) {
        echo(element, object.toString(), Diagnostic.Kind.NOTE, "notice", "Note");
    }

    private void echo(Element element, String errString, Diagnostic.Kind kind, String ghString, String humanString) {
        if (compilationUnitTree == null) {
            messager.printMessage(kind, errString, element);
        } else {
            Tree tree = trees.getTree(element);
            LineMap linemap = compilationUnitTree.getLineMap();
            long pos = positions.getStartPosition(compilationUnitTree, tree);
            long row = linemap.getLineNumber(pos);
            String name = compilationUnitTree.getSourceFile().toUri().toString().split("/src/")[1];
            System.out.println("::" + ghString + " file=src/" + name + ",line=" + row + "::" + errString);
        }
    }

    private boolean _implements(String qualifiedName, TypeElement elem) {
        if(elem.getQualifiedName().toString().equals(qualifiedName)) {
            return true;
        }
        Element superClass = types.asElement(elem.getSuperclass());
        if(superClass instanceof TypeElement) {
            if(_implements(qualifiedName, (TypeElement) superClass)) {
                return true;
            }
        }
        for(var iface : elem.getInterfaces()) {
            Element interface_ = types.asElement(iface);
            if(interface_ instanceof TypeElement) {
                if(_implements(qualifiedName, (TypeElement) interface_)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean _extends(String qualifiedName, TypeElement elem) {
        if(elem.getQualifiedName().toString().equals(qualifiedName)) {
            return true;
        }
        Element superClass = types.asElement(elem.getSuperclass());
        if(superClass instanceof TypeElement) {
            if(_extends(qualifiedName, (TypeElement) superClass)) {
                return true;
            }
        }
        return false;
    }

    public boolean implementsInterface(String qualifiedName) {
        return _implements(qualifiedName, this.element);
    }

    public boolean implementsInterface(TypeElement e, String qualifiedName) {
        return _implements(qualifiedName, e);
    }

    public boolean extendsClass(String qualifiedName) {
        return _extends(qualifiedName, this.element);
    }

    public boolean extendsClass(TypeElement e, String qualifiedName) {
        return _extends(qualifiedName, e);
    }

}
