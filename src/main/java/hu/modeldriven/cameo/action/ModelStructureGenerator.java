package hu.modeldriven.cameo.action;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.magicdraw.uml.Finder;
import com.nomagic.uml2.ext.jmi.helpers.CoreHelper;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Type;
import com.nomagic.uml2.impl.ElementsFactory;

public class ModelStructureGenerator {

    private final Project project;
    private final ElementsFactory factory;
    private final ModelElementsManager manager;

    public ModelStructureGenerator() {
        this.project = Application.getInstance().getProject();
        this.factory = project.getElementsFactory();
        this.manager = ModelElementsManager.getInstance();
    }

    public void execute(Package parentPackage) {
        try {
            SessionManager.getInstance().createSession(project, "Creating model elements from lesson 3");
            createModelElements(parentPackage);
        } catch (Exception e) {
            Application.getInstance().getGUILog().showMessage("Exception occured: " + e.getMessage());
        } finally {
            SessionManager.getInstance().closeSession(project);
        }
    }

    private void createModelElements(Package parentPackage) throws ReadOnlyElementException {
        var firstClass = createClass(parentPackage, "First class");

        addProperty(firstClass);
        addOperation(firstClass);

        var secondClass = createClass(parentPackage, "Second class");
        addStereotype(secondClass);

        createRelation(parentPackage, firstClass, secondClass);
    }

    private Class createClass(Package parentPackage, String name) throws ReadOnlyElementException {
        var mdClass = factory.createClassInstance();
        mdClass.setName(name);
        manager.addElement(mdClass, parentPackage);

        return mdClass;
    }

    private void addProperty(Class mdClass) throws ReadOnlyElementException {
        var stringType = (Type) Finder.byQualifiedName()
                .find(project, "UML Standard Profile::UML2 Metamodel::PrimitiveTypes::String");

        var property = factory.createPropertyInstance();
        property.setName("myProperty");
        property.setType(stringType);
        CoreHelper.setMultiplicity(0, 1, property);

        manager.addElement(property, mdClass);
    }

    private void addOperation(Class mdClass) throws ReadOnlyElementException {
        var operation = factory.createOperationInstance();
        operation.setName("myOperation");
        manager.addElement(operation, mdClass);
    }

    private void addStereotype(Class mdClass) throws ReadOnlyElementException {
        var profile = StereotypesHelper.getProfile(project, "My profile");
        var stereotype = StereotypesHelper.getStereotype(project, "myStereotype", profile);

        StereotypesHelper.addStereotype(mdClass, stereotype);
        StereotypesHelper.setStereotypePropertyValue(mdClass, stereotype, "name", "hello");
    }

    private void createRelation(Package parentPackage, Class firstClass, Class secondClass) throws ReadOnlyElementException {
        var dependency = factory.createDependencyInstance();
        CoreHelper.setSupplierElement(dependency, firstClass);
        CoreHelper.setClientElement(dependency, secondClass);
        dependency.setOwner(parentPackage);
    }

}
