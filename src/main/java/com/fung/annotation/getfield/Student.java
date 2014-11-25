package com.fung.annotation.getfield;

 

/**
 * Student 相当于一个表的实体类，我们可以假想它是从数据库读出来映射出来的一张表信息
 * STUDENT_COLUMN_NAMES： 这张表的类名
 * @author 草原战狼
 *
 */
public class Student {
    public static String[] STUDENT_COLUMN_NAMES = {"Name", "Sex", "Age", "Height", "Weight", "Grade"};
    private String name;
    private String sex;
    private int age;
    private int height;
    private int weigth;
    private int grade;
    
    public Student(String name, String sex, int age, int height, int weight, int grade) {
        this.name = name;
        this.sex = sex;
        this.height = height;
        this.weigth = weight;
        this.age = age;
        this.grade = grade;
    }
    public Student() {
        
    }
    
    @MapValue(index = 1)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @MapValue(index = 2)
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    @MapValue(index = 3)
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    @MapValue(index = 4)
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    @MapValue(index = 5)
    public int getWeigth() {
        return weigth;
    }
    public void setWeigth(int weigth) {
        this.weigth = weigth;
    }
    @MapValue(index = 6)
    public int getGrade() {
        return grade;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }
}

 