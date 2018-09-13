# Class 的获取
- 通过 Object.getClass()
- 通过 .class 标识
- 通过 Class.forName() 方法

```java
Class<ArrayListTest> cl = ArrayListTest.class;
Class<? extends ArrayListTest> c = new ArrayListTest().getClass();
Class<?> clazz =  Class.forName("collection.List.ArrayListTest");
```


# 获取Class的名字
```java
cl.getName() //collection.List.ArrayListTest 全限定类名
cl.getSimpleName() //ArrayListTest 
cl.getCanonicalName() //collection.List.ArrayListTest 全限定类名
cl.getTypeName() //collection.List.ArrayListTest

```

# 获取Class的修饰符
分为4类
- 用来限制作用域，如 public、protected、priviate。
- 用来提示子类复写，abstract。
- 用来标记为静态类 static。

```java
int modi = cl.getModifiers();
Modifier.isPublic(modi)

```

# 获取 Class 的成员

## 获取 Filed
```java
//获取本类和父类的所有public方法
Field[] fields = cl.getFields();
//获取指定名称的public方法
Field field = cl.getField("value");
//获取本类的所有方法
Field[] fields2 = cl.getDeclaredFields();
//获取指定名称的本类方法
Field field2 = cl.getDeclaredField("value");
```
