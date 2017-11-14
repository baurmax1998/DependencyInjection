package de.cherry.myLib;


import de.cherry.myLib.annotations.Inject;
import de.cherry.myLib.interfaces.Main;
import de.cherry.myLib.internal.AdminUi;
import de.cherry.myLib.internal.ClassFinder;
import de.cherry.myLib.internal.Instanter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mbaaxur on 10.11.17.
 */
public class App {

    private String packageName;
    private HashMap<Class<?>, List<Class<?>>> classes;
    private Instanter instanter = new Instanter();


    public App(String packageName) throws ClassNotFoundException {
        this.packageName = packageName;

    }

    public void init() throws ClassNotFoundException {
        try {
            this.classes = ClassFinder.getClassesForPackageAsTree(packageName);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("The Application hasn't found Classes in the Package: " + packageName);
        }
    }

    public void start(boolean withAdminTool) throws ClassNotFoundException {
        List<Class<?>> entrys = this.classes.get(Main.class);
        if (entrys.size() == 0)
            throw new ClassNotFoundException(
                    "None of the classes has implemented the Main interface");
        for (Class<?> entry : entrys) {
            final Main main = (Main) instantiateAndFillClass(entry);
            Runnable myRunnable = new Runnable() {
                public void run() {
                    System.out.println("Runnable running: " + main.getClass().getName());
                    main.main();
                }
            };
            Thread thread = new Thread(myRunnable);
            thread.start();
        }
        if (withAdminTool)
            startAdminUi();
    }

    private void startAdminUi() {
        final Main main = (Main) instantiateAndFillClass(AdminUi.class);
        Runnable myRunnable = new Runnable() {
            public void run() {
                System.out.println("Runnable running: " + main.getClass().getName());
                main.main();
            }
        };

        Thread thread = new Thread(myRunnable);
        thread.start();
    }


    private Object instantiateAndFillClass(Class<?> entry) {
        Object aObject = instantiateClass(entry);
        try {
            fillClass(aObject);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("The Class " + aObject.getClass().getName() + " couldn't filled.", e);
        }
        return aObject;
    }


    public Object instantiateClass(Class classForObject) {
        Object object = null;
        if (classForObject.isInterface()) {
            classForObject = classes.get(classForObject).get(0);
        }
        try {
            object = classForObject.newInstance();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("The Class "
                    + classForObject.getName()
                    + " couldn't be instantiated because a public constructor with no parameter."
                    , e);
        } catch (InstantiationException e) {
            throw new RuntimeException("The Class "
                    + classForObject.getName()
                    + " couldn't be instantiated."
                    , e);
        }
        return object;
    }


    private void fillClass(Object object) throws IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (hasAnnotation(field, Inject.class)) {
                field.setAccessible(true);
                field.set(object, instantiateAndFillClass(field.getType()));
            }
        }
    }


    private boolean hasAnnotation(Field field, Class annotationClass) {
        for (Annotation oneAnnotation : field.getDeclaredAnnotations()) {
            if (oneAnnotation.annotationType().equals(annotationClass))
                return true;
        }
        return false;
    }

}
