package com.example.api;

import com.example.dao.NewDAO;
import com.example.model.UserInfo;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    public <T> List readOne(String id) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

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
    }


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

    private <T> void getAllAboutNewClass(Class<T> clazz){

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
