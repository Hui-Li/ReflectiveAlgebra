package annotation;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(value={"annotation.Refl"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ReflProcessor extends AbstractProcessor {
    private Filer filer;
    
    @Override
    public void init(ProcessingEnvironment env){
        filer = env.getFiler();
    }
    
 
    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment env) {
        String folder = null;
        String classContent = null;
        String algName;
        JavaFileObject jfo = null;
        for (Element element: env.getElementsAnnotatedWith(Refl.class)) {            
            
            // Avoid infinite loop.
            if (element.getSimpleName().toString().startsWith("G_")) {
                continue;
            }

            // Initialization.
            TypeMirror tm = element.asType();
            String typeArgs = tm.accept(new DeclaredTypeVisitor(), element);
            String[] lTypeArgs = toList(typeArgs);
            algName = element.getSimpleName().toString();
            
            // Create query classes "AlgNameQuery".
            // One issue here. Using "java.util.List" instead of "List".
            folder = "refl";
            classContent = createQueryClass(folder, element, lTypeArgs, typeArgs);
            jfo = null;
            try{
                jfo = filer.createSourceFile(folder + "/" + "Refl" + algName, element);
                jfo.openWriter().append(classContent).close();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
            
        }
        return true;        
    }
    
    String createQueryClass(String folder, Element element, String[] lTypeArgs, String typeArgs) {
        String algName = element.getSimpleName().toString();
        String classContent = "package " + folder + ";\n\n"
                + "import java.util.List;\n"
                + "import java.util.ArrayList;\n"
                + "import library.ReflAlg;\n"
                + "import " + getPackage(element) + "." + element.getSimpleName() + ";\n\n" 
                + "public abstract class " + "Refl" + algName + "<E> implements " + algName + "<E>";
        classContent += " {\n\n" + 
                "\tabstract ReflAlg<E> alg();\n\n";
        List<? extends Element> le = element.getEnclosedElements();
        for (Element e: le){
            String methodName = e.getSimpleName().toString();
            String[] args = {methodName, typeArgs};
            classContent += e.asType().accept(new ReflTypeVisitor(), args);
        }
        classContent += "}";
        return classContent;
    }
    
    private String[] toList(String message) {
        return message.split(",");
    }

    @Override
    public SourceVersion getSupportedSourceVersion(){
        return SourceVersion.latestSupported();
    }
    
    private String getPackage(Element element) {
        return ((PackageElement)element.getEnclosingElement()).getQualifiedName().toString();
    }
}
