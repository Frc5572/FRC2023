package org.frc5572.robotools;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * Annotation processor for checks. Used by VS Code.
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedOptions("frc_check.skip")
public class RobotProcessor extends AbstractProcessor {

    private void processTypeElement(TypeElement typeElement) {
        for (Element e3 : typeElement.getEnclosedElements()) {
            if (e3 instanceof TypeElement) {
                processTypeElement((TypeElement) e3);
            }
        }
        CompilationData data = new CompilationData(processingEnv, typeElement);
        Checks.process(data);
    }

    private boolean hasProcessed;

    /** Initialization function */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        System.out.println(processingEnv.getOptions().get("frc_check.skip"));
        hasProcessed = false;
    }

    /** Process all elements. */
    @Override
    public boolean process(Set<? extends TypeElement> arg0, RoundEnvironment roundEnv) {
        if (hasProcessed) {
            return false;
        }
        hasProcessed = true;
        for (ModuleElement mod : processingEnv.getElementUtils().getAllModuleElements()) {
            if (!mod.isUnnamed()) {
                continue;
            }
            for (Element element : mod.getEnclosedElements()) {
                if (element instanceof PackageElement) {
                    PackageElement packageElement = (PackageElement) element;
                    if (packageElement.getQualifiedName().toString().startsWith("frc.")) {
                        for (Element element2 : packageElement.getEnclosedElements()) {
                            if (element2 instanceof TypeElement) {
                                TypeElement typeElement = (TypeElement) element2;
                                processTypeElement(typeElement);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

}
