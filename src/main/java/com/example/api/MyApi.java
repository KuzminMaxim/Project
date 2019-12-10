package com.example.api;

import com.example.dao.NewDAO;
import com.example.model.UserInfo;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;

@Repository
public class MyApi {

    @Autowired
    private NewDAO dao;

    public void save(Object object) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = object.getClass();
        create(getAllAboutUsedClass(clazz, object), clazz);
    }

    public void add(Object object) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = object.getClass();
        insert(getAllAboutUsedClass(clazz, object), clazz);
    }

    public void update(Object object) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = object.getClass();
        change(getAllAboutUsedClass(clazz, object), clazz);
    }

    public void delete(Object object) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = object.getClass();
        remove(getAllAboutUsedClass(clazz, object));
    }

    public void deleteOne(Object object) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = object.getClass();
        removeOne(getAllAboutUsedClass(clazz, object));
    }

    public <T> List <T> readAll(Object object) {
        try {
            return dao.selectListOfSomething(object);
        } catch (IllegalAccessException | SQLException | InstantiationException e){
            e.printStackTrace();
        }
        return null;
    }

    public <T> List <T> readAllWhereSomething(Object object, String name, String attribute) {
        try {
            return dao.selectListOfSomethingWhereSomething(object, name, attribute);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public <T> T readSomethingOne(Object object, String name, String attribute){
        try {
            return dao.selectSomethingOne(object, name, attribute);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*public <T> List readOne(String id) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        List list = new ArrayList();
        Map<String, String> myMap = new HashMap<>();

        /////Specify in which package to search for annotations/////
        Reflections reflections = new Reflections(UserInfo.class.getPackage().getName());
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ObjectType.class);

        for (Class clazz : classes){

            ObjectType objectType = (ObjectType) clazz.getAnnotation(ObjectType.class);
            //System.out.println("Object type: " + objectType.id());
            //System.out.println("ID: " + id);

            if (id.equals(objectType.id())){

                Constructor constructor = clazz.getConstructor();
                Object object = constructor.newInstance();

                Field[] field = clazz.getDeclaredFields();

                String[] annotations = new String[field.length];
                String[] names = new String[field.length];

                for (int i = 0; i < field.length; i++){
                    names[i] = field[i].getName();
                    try {
                        annotations[i] = clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();
                    } catch (NullPointerException ignored){}
                    if (annotations[i] != null){
                        myMap.put(annotations[i], names[i]);
                    }

                }
                return dao.selectSomething(id, myMap);
            }

            //System.out.println("Attributes: "+Arrays.toString(annotations));

        }
        return list;
    }*/


    private <T> Map<String, String> getAllAboutUsedClass(Class<T> clazz, Object object) throws IllegalAccessException, NoSuchFieldException {
        Map<String, String> attributesValues = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        String[] names = new String[fields.length];
        String[] annotation = new String[fields.length];
        String[] values = new String[annotation.length];

        for (int i = 0; i<fields.length; i++){
            fields[i].setAccessible(true);
            names[i] = fields[i].getName();
            try {
                annotation[i] = clazz.getDeclaredField(names[i]).getAnnotation(Attribute.class).id();
            } catch (NullPointerException ignored){}

        }

        for (int i = 0; i<annotation.length; i++){
            if (annotation[i] != null){
                Field field = clazz.getDeclaredField(names[i]);
                field.setAccessible(true);
                values[i] = (String) field.get(object);
                attributesValues.put(annotation[i], values[i]);
            }
        }
        return attributesValues;
    }

    private <T> void getAllAboutNewClass(Class<T> clazz){

    }

    private <T> void create(Map attributesValues, Class<T> clazz){
        String objectTypeAnnotation = clazz.getAnnotation(ObjectType.class).id();
        try {
            dao.createSomething(attributesValues, objectTypeAnnotation);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private <T> void change(Map attributesValues, Class<T> clazz){
        String objectType = clazz.getAnnotation(ObjectType.class).id();
        try {
            dao.updateSomething(attributesValues, objectType);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private <T> void insert(Map attributesValues, Class<T> clazz){
        String objectType = clazz.getAnnotation(ObjectType.class).id();
        try {
            dao.addSomething(attributesValues, objectType);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void remove(Map attributesValues){
        try {
            dao.deleteSomething(attributesValues);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void removeOne(Map attributesValues){
        try {
            dao.deleteSomeoneFromSomething(attributesValues);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

}
