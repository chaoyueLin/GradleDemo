package com.example

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.internal.reflect.Instantiator


class Student{
    String name
    int age
    boolean isMale
}



//父类
class Animal {

    String name
    int legs

    Animal(String name) {
        this.name = name
    }

    void setLegs(int c) {
        legs = c
    }

    String toString() {
        return "This animal is $name, it has ${legs} legs."
    }
}

//子类
class Pig extends Animal {

    int age
    String owner

    Pig(int age, String owner) {
        super("Pig")
        this.age = age
        this.owner = owner
    }

    String toString() {
        return super.toString() + " Its age is $age, its owner is $owner."
    }

}

class OuterExt {

    String outerName
    String msg
    InnerExt innerExt = new InnerExt()

    void outerName(String name) {
        outerName = name
    }

    void msg(String msg) {
        this.msg = msg
    }

    //创建内部Extension，名称为方法名 inner
    void inner(Action<InnerExt> action) {
        action.execute(inner)
    }

    //创建内部Extension，名称为方法名 inner
    void inner(Closure c) {
        org.gradle.util.ConfigureUtil.configure(c, innerExt)
    }

    String toString() {
        return "OuterExt[ name = ${outerName}, msg = ${msg}] " + innerExt
    }

}


class InnerExt {

    String innerName
    String msg

    void innerName(String name) {
        innerName = name
    }

    void msg(String msg) {
        this.msg = msg
    }

    String toString() {
        return "InnerExt[ name = ${innerName}, msg = ${msg}]"
    }

}

public class MyPlugin implements  Plugin<Project>{

    @Override
    void apply(Project project) {
        project.extensions.create("student",Student.class)
        Student studen = project.extensions.getByType(Student.class)

        //创建的Extension是 Animal 类型
        Animal aAnimal = project.extensions.create(Animal.class, "animal", Pig.class, 3, "hjy")
        //创建的Extension是 Pig 类型
        Pig aPig = project.extensions.create("pig", Pig.class, 5, "kobe")

        def outExt = project.extensions.create("outer", OuterExt)

        project.task('testdd'){
            doLast{
                println studen.name
                println aAnimal
                println aPig
                //验证 aPig 对象是 ExtensionAware 类型的
                println "aPig is a instance of ExtensionAware : ${aPig instanceof ExtensionAware}"
                println outExt
            }
        }

    }
}