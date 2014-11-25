package com.fung.annotation.getfield;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

 
/**
 * 可以应用在从数据库导出数据，根据指定要导出的列，导出到Excel， Word等。这个只是中间的环节
 * @author 草原战狼
 *
 */
public class TestMain {
    public static void main(String[] args) {
        Student student = new Student("XiaoMing", "Male", 20, 175, 135, 2);
        int[] indexs = {34,2,5,3,0,1,2,4,6};
        System.out.println(student.getAge());
        String[] names = Student.STUDENT_COLUMN_NAMES;
        String printColumns = getColumnName(names, indexs);
        String pringStudentInfo = getStudentInfo(student, indexs);
        System.out.println(printColumns);
        System.out.println(pringStudentInfo);
    }
    /**
     * 接收一张表的实体类和外面传进来的要导出的列的索引顺序，比如要导出第2列，第4列，那么可以传入{2,4}
     * 返回获取到的指定列的值
     * @param student 表的实体类
     * @param indexs 要导出的列的索引
     * @return 指定列的值
     */
    public static String getStudentInfo(Student student, int[] indexs) {
        String studentInfo = "";
        Method[] methods = Student.class.getMethods();
        for(int index : indexs) {
            for(Method method : methods) {
                MapValue mapValue = method.getAnnotation(MapValue.class);
                if(mapValue == null){
                    continue;
                }else {
                    if(index == mapValue.index()) {
                        Object value = null;
                        try {
                            value = method.invoke(student);
                        }catch(Exception e)
                      // catch (IllegalAccessException | IllegalArgumentException  | InvocationTargetException e)
                        
                        {
                            System.out.println("parameters number is wrong.");
                            e.printStackTrace();
                        }
                        studentInfo += value.toString() + ", ";
                        break;
                    }
                }
            }
        }
        return studentInfo;
    }
    /**
     * 根据指定的索引导出列的名称
     * @param names 整张表所有列的名称
     * @param indexs 要导出的列的索引
     * @return 导出的列的名称
     */
    public static String getColumnName(String[] names, int[] indexs) {
        String result = "";
        for(int index : indexs) {
            if(index > names.length || index < 1) {
                continue;
            }
            result += names[index - 1] + ", ";
        }
        return result;
    }
}
 