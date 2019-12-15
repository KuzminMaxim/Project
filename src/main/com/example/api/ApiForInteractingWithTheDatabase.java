package com.example.api;

import com.example.dao.NewDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ApiForInteractingWithTheDatabase {

    @Autowired
    private NewDAO dao;

    public void save(Object object) {
        try {
            Class clazz = object.getClass();
            try {
                create(getAllAboutUsedClass(clazz, object), clazz);
            } catch (NoSuchFieldException | IllegalAccessException e){
                e.printStackTrace();
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }


    }

    public void add(Object object) {
        try {
            Class clazz = object.getClass();
            try {
                insert(getAllAboutUsedClass(clazz, object), clazz);
            } catch (NoSuchFieldException | IllegalAccessException e){
                e.printStackTrace();
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void update(Object object) {
        try {
            Class clazz = object.getClass();
            try {
                change(getAllAboutUsedClass(clazz, object), clazz);
            } catch (NoSuchFieldException | IllegalAccessException e){
                e.printStackTrace();
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void delete(Object object) {
        try {
            Class clazz = object.getClass();
            try {
                remove(getAllAboutUsedClass(clazz, object));
            } catch (NoSuchFieldException | IllegalAccessException e){
                e.printStackTrace();
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void deleteOne(Object object) {
        try {
            Class clazz = object.getClass();
            try {
                removeOne(getAllAboutUsedClass(clazz, object));
            } catch (NoSuchFieldException | IllegalAccessException e){
                e.printStackTrace();
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public <T> List <T> readAll(Class clazz) {
        return dao.getAllRecordsFromTheDatabaseRelatedToThisClassObjectType(clazz);
    }

    public <T> List <T> readAllWhereSomething(Class clazz, String name, String attribute) {
        return dao.getAllRecordsRelatedToThisClassObjectTypeAndThisNameIsAttribute(clazz, name, attribute);
    }

    public <T> T readSomethingOne(Class clazz, String name, String attribute){
        return dao.findUniqueRecordInDatabaseWhereThisNameIsThisAttributeOfParticularObjectRelatedToThisClass(clazz, name, attribute);
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

    private <T> void create(Map attributesValues, Class<T> clazz){
        String objectTypeAnnotation = clazz.getAnnotation(ObjectType.class).id();
        try {
            dao.createNewObjectInDatabase(attributesValues, objectTypeAnnotation);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private <T> void change(Map attributesValues, Class<T> clazz){
        String objectType = clazz.getAnnotation(ObjectType.class).id();
        try {
            dao.updateObjectInDatabase(attributesValues, objectType);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private <T> void insert(Map attributesValues, Class<T> clazz){
        String objectType = clazz.getAnnotation(ObjectType.class).id();
        try {
            dao.addOneNewAttributesIntoObjectInDatabase(attributesValues, objectType);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void remove(Map attributesValues){
        try {
            dao.deleteObjectFromDatabase(attributesValues);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void removeOne(Map attributesValues){
        try {
            dao.deleteOneAttributeFromObjectInDatabase(attributesValues);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

}
