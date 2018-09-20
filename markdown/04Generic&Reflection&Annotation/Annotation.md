使用@Interface定义

元注解：
@Retention：生命周期
- 源码阶段：RetentionPolicy.SOURCE
- 字节码阶段：RetentionPolicy.CLASS
- 运行阶段：RetentionPolicy.RUNTIME

@Target：目标
- 包：ElementType.PACKAGE
- 类/接口：ElementType.TYPE
- 属性：ElementType.FIELD
- 方法：ElementType.METHOD
- 参数：ElementType.PARAMETER
- 构造器：ElementType.Constructor
- 局部变量：ElementType.LOCAL_VARIABLE
- 注解：ElementType.ANNOTATION_TYPE
- 类型参数：ElementType.TYPE_PARAMETER
- 任意地方：ElementType.TYPE_USE

@Inherited：是否继承

@Documented：javadoc中显示

@Repeatable：可重复标签
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(RepeatablesAnnotation.class)
public @interface RepeatableAnnotation {
    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RepeatableAnnotations{
    RepeatableAnnotation[] value();
}
```

Java内置注解与其它元注解

- @Override：用于标明此方法覆盖了父类的方法
- @Deprecated：用于标明已经过时的方法或类
- @SuppressWarnnings:用于有选择的关闭编译器对类、方法、成员变量、变量初始化的警告