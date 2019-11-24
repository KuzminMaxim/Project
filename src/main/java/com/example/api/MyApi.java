package com.example.api;

import com.example.dao.NewDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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

    public void readOne(){}

    public void readAll(){}


    private <T> Map<String, String> getAllAboutUsedClass(Class<T> clazz, Object object) throws IllegalAccessException, NoSuchFieldException {
        Map<String, String> myMap = new HashMap<>();
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
                myMap.put(annotation[i], values[i]);
            }
        }
        return myMap;
    }

    private <T> void create(Map map, Class<T> clazz){
        String objectTypeAnnotation = clazz.getAnnotation(ObjectType.class).id();
        try {
            dao.createSomething(map, objectTypeAnnotation);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private <T> void change(Map map, Class<T> clazz){
        String objectType = clazz.getAnnotation(ObjectType.class).id();
        try {
            dao.updateSomething(map, objectType);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private <T> void insert(Map map, Class<T> clazz){
        String objectType = clazz.getAnnotation(ObjectType.class).id();
        try {
            dao.addSomething(map, objectType);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void remove(Map map){
        try {
            dao.deleteSomething(map);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

}
