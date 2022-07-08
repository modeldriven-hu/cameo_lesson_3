package hu.modeldriven.cameo.action;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.magicdraw.uml.Finder;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Type;

public class ModelStructureGenerator {

    private final Package parentPackage;

    public ModelStructureGenerator(Package parentPackage) {
        this.parentPackage = parentPackage;
    }

    public void execute() {
        var project = Application.getInstance().getProject();

        try {
            SessionManager.getInstance().createSession(project, "Creating model elements from lesson 3");
            createModelElements(project);
        } catch (Exception e) {
            Application.getInstance().getGUILog().showMessage("Exception occured: " + e.getMessage());
        } finally {
            SessionManager.getInstance().closeSession(project);
        }
    }

    private void createModelElements(Project project) throws ReadOnlyElementException {

        var factory = project.getElementsFactory();

        // create a new class and add it to the package

        var newClass = factory.createClassInstance();
        newClass.setName("First Class");
        ModelElementsManager.getInstance().addElement(newClass, parentPackage);

        // find the type string

        var stringType = (Type)Finder.byQualifiedName()
                .find(project,"UML Standard Profile::UML2 Metamodel::PrimitiveTypes::String");

        // create a property add add it to newClass

        var property = factory.createPropertyInstance();
        property.setName("myProperty");
        property.setType(stringType);

        ModelElementsManager.getInstance().addElement(property, newClass);

        // [X] create a class
        // [X] add a field with type
        // [ ] add an operation
        // [ ] create another class
        // [ ] create a relationship between them
        // [ ] assign a stereotype with tag
    }


}
