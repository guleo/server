package com.model.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by frank on 2016/3/6.
 * ����������
 */
public class Utils {

    //����������ģʽ
    public static final int QUE_SELECT_MODE = 1;
    public static final int QUE_DEL_MODE = 2;
    public static final int QUE_CHECK_MODE = 3;
    public static final int QUE_SEARCH_MODE = 4;

    //��ҳ��ҳʱһҳ��¼��
    public static final int COUNT_PER_PAGE = 10;

    //��Ϸ���
    public static final String SHOW_SUC = "YOU WIN";
    public static final String SHOW_FAIL = "YOU LOSE";
    public static final String SHOW_DRAW = "YOU DRAW";

    public static final String IMG_PATH = "img/";
    public static Object[] toObjects(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        Object[] objects = new Object[fields.length];
        for (int i = 0; i < objects.length; i++) {
            try {
                String name = fields[i].getName().substring(0, 1).toUpperCase() +
                        fields[i].getName().substring(1);
                Method m = object.getClass().getMethod("get" + name);
                objects[i] = m.invoke(object);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return objects;
    }

    public static String[] toString(Object object) {

        Field[] fields = object.getClass().getDeclaredFields();
        String[] s = new String[fields.length];
        for (int i = 0; i < s.length; i++) {
            s[i] = fields[i].getName();
        }
        return s;
    }


}
