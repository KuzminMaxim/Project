package com.example.api;

import com.example.dao.NewDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
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

    public <T> List <T> readAll(Class clazz) {
        try {
            return dao.selectListOfSomething(clazz);
        } catch (IllegalAccessException | InstantiationException e){
            e.printStackTrace();
        }
        return null;
    }

    public <T> List <T> readAllWhereSomething(Class clazz, String name, String attribute) {
        try {
            return dao.selectListOfSomethingWhereSomething(clazz, name, attribute);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public <T> T readSomethingOne(Class clazz, String name, String attribute){
        try {
            return dao.selectSomethingOne(clazz, name, attribute);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

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
