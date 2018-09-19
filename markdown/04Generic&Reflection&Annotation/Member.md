Menber是成员的顶层接口，定义了成员常用的功能。
```java
public interface Member {
    
    //标识类或接口的所有公共成员的集合，包括继承的成员。
    public static final int PUBLIC = 0;
    
    //标识类或接口的已声明成员集。
    public static final int DECLARED = 1;
    
    //返回成员所属的类
    public Class<?> getDeclaringClass();
    
    //返回成员名称
    public String getName();
    
    //返回成员的修饰符
    public int getModifiers();
    
    //如果编译器引入了该成员，则返回true;否则返回false
    public boolean isSynthetic();
    
 }
```

AnnotatedElement，用于操作注解
```java

```


AccessibleObject是成员的基类，维护了一个成员变量override(可访问标志)，用于表示成员是否可访问。
AccessibleObject实现了AnnotatedElement接口，可以操作成员上的注解。
```java
public class AccessibleObject implements AnnotatedElement {
    
    //可访问标志
    boolean override;
    
    //将此对象的可访问标志设置为指示的布尔值
    public void setAccessible(boolean flag) throws SecurityException {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) sm.checkPermission(ACCESS_PERMISSION);
            setAccessible0(this, flag);
    }
    
    //获取此成员的可访问标志的值
    public boolean isAccessible() {
            return override;
    }
    
    //批量将此数组中的成员的可访问标志设置为指示的布尔值，为了提高效率
    public static void setAccessible(AccessibleObject[] array, boolean flag)
        throws SecurityException {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) sm.checkPermission(ACCESS_PERMISSION);
        for (int i = 0; i < array.length; i++) {
            setAccessible0(array[i], flag);
        }
    }
}
```
Executable是一个抽象类，实现了Member接口，是一个成员；实现了GenericDeclaration接口，可以操作参数上的注解；继承自AccessibleObject，支持可访问控制。Executable是成员方法（构造方法和方法）的基类。
Executable主要是增加了针对方法参数、异常和返回值的功能。
```java
public abstract class Executable extends AccessibleObject
    implements Member, GenericDeclaration {
    
    //返回参数的类型数组
    public abstract Class<?>[] getParameterTypes();
    
    //返回参数的泛型类型数组
    public Type[] getGenericParameterTypes() {
        if (hasGenericInformation())//参数中包含泛型信息
            return getGenericInfo().getParameterTypes();
        else//参数中不包含泛型信息，返回原始类型
            return getParameterTypes();
    }
    
    //获取参数的注解类型数组
    public AnnotatedType[] getAnnotatedParameterTypes() {
        return TypeAnnotationParser.buildAnnotatedTypes(getTypeAnnotationBytes0(),
                sun.misc.SharedSecrets.getJavaLangAccess().
                        getConstantPool(getDeclaringClass()),
                this,
                getDeclaringClass(),
                getParameterTypes(),
                TypeAnnotation.TypeAnnotationTarget.METHOD_FORMAL_PARAMETER);
    }
    
    //获取异常的原始类型数组
    public abstract Class<?>[] getExceptionTypes();
    
    //获取异常的泛型类型数组
    public Type[] getGenericExceptionTypes() {
        Type[] result;
        if (hasGenericInformation() &&
            ((result = getGenericInfo().getExceptionTypes()).length > 0))
            return result;
        else
            return getExceptionTypes();
    }
    

    //获取异常的注解类型数组
    public AnnotatedType[] getAnnotatedExceptionTypes() {
        return TypeAnnotationParser.buildAnnotatedTypes(getTypeAnnotationBytes0(),
                sun.misc.SharedSecrets.getJavaLangAccess().
                        getConstantPool(getDeclaringClass()),
                this,
                getDeclaringClass(),
                getGenericExceptionTypes(),
                TypeAnnotation.TypeAnnotationTarget.THROWS);
    }
    
    //获取参数数组
    public Parameter[] getParameters() {
        return privateGetParameters().clone();
    }
    
    //获取参数个数
    public int getParameterCount() {
        throw new AbstractMethodError();
    }
    
    //获取参数的注解列表
    public abstract Annotation[][] getParameterAnnotations();
    
    //是否采用可变数量的参数
    public boolean isVarArgs()  {
        return (getModifiers() & Modifier.VARARGS) != 0;
    }
    
    //获取返回值的注解类型
    public abstract AnnotatedType getAnnotatedReturnType();
    
    //返回描述此成员的字符串，包括任何类型参数
    public abstract String toGenericString();
    
}
```

Constructor
Constructor继承自Executable
```java
public final class Constructor<T> extends Executable {
    private Class<T>            clazz;//成员所属的Class类
    private int                 slot;
    private Class<?>[]          parameterTypes;//参数类型数组
    private Class<?>[]          exceptionTypes;//异常类型数组
    private int                 modifiers;//修饰符
    // Generics and annotations support
    private transient String    signature;//泛型和注解支持
    // generic info repository; lazily initialized
    private transient ConstructorRepository genericInfo;//泛型信息
    private byte[]              annotations;//方法的注解字节数组
    private byte[]              parameterAnnotations;//参数的注解字节数组
    
    private volatile ConstructorAccessor constructorAccessor;
    private Constructor<T>      root;
    
    //实例化对象
    @CallerSensitive
    public T newInstance(Object ... initargs)
        throws InstantiationException, IllegalAccessException,
               IllegalArgumentException, InvocationTargetException
    {
        if (!override) {
            if (!Reflection.quickCheckMemberAccess(clazz, modifiers)) {
                Class<?> caller = Reflection.getCallerClass();
                checkAccess(caller, clazz, null, modifiers);
            }
        }
        if ((clazz.getModifiers() & Modifier.ENUM) != 0)
            throw new IllegalArgumentException("Cannot reflectively create enum objects");
        ConstructorAccessor ca = constructorAccessor;   // read volatile
        if (ca == null) {
            ca = acquireConstructorAccessor();
        }
        T inst = (T) ca.newInstance(initargs);
        return inst;
    }
}
```
Method
```java
public final class Method extends Executable {
    private Class<?>            clazz;//成员所属的Class类
    private int                 slot;
    // This is guaranteed to be interned by the VM in the 1.4
    // reflection implementation
    private String              name;//方法名称
    private Class<?>            returnType;//返回值类型
    private Class<?>[]          parameterTypes;//方法类型数组
    private Class<?>[]          exceptionTypes;//异常类型数组
    private int                 modifiers;//修饰符
    // Generics and annotations support
    private transient String              signature;
    // generic info repository; lazily initialized
    private transient MethodRepository genericInfo;
    private byte[]              annotations;
    private byte[]              parameterAnnotations;
    private byte[]              annotationDefault;
    private volatile MethodAccessor methodAccessor;
    // For sharing of MethodAccessors. This branching structure is
    // currently only two levels deep (i.e., one root Method and
    // potentially many Method objects pointing to it.)
    private Method              root;
    
    //获取返回值原始类型
    public Class<?> getReturnType() {
        return returnType;
    }
    
    //获取返回值泛型类型
    public Type getGenericReturnType() {
      if (getGenericSignature() != null) {
        return getGenericInfo().getReturnType();
      } else { return getReturnType();}
    }
    
    //执行方法
    @CallerSensitive
    public Object invoke(Object obj, Object... args)
        throws IllegalAccessException, IllegalArgumentException,
           InvocationTargetException
    {
        if (!override) {
            if (!Reflection.quickCheckMemberAccess(clazz, modifiers)) {
                Class<?> caller = Reflection.getCallerClass();
                checkAccess(caller, clazz, obj, modifiers);
            }
        }
        MethodAccessor ma = methodAccessor;             // read volatile
        if (ma == null) {
            ma = acquireMethodAccessor();
        }
        return ma.invoke(obj, args);
    }
}
```

Field继承自AccessibleObject，实现了Member
```java
public final
class Field extends AccessibleObject implements Member {
        private Class<?>            clazz;//成员所属class
        private int                 slot;
        // This is guaranteed to be interned by the VM in the 1.4
        // reflection implementation
        private String              name;//名称
        private Class<?>            type;//类型
        private int                 modifiers;//修饰符
        // Generics and annotations support
        private transient String    signature;
        // generic info repository; lazily initialized
        private transient FieldRepository genericInfo;
        private byte[]              annotations;//注解信息
        // Cached field accessor created without override
        private FieldAccessor fieldAccessor;
        // Cached field accessor created with override
        private FieldAccessor overrideFieldAccessor;
        // For sharing of FieldAccessors. This branching structure is
        // currently only two levels deep (i.e., one root Field and
        // potentially many Field objects pointing to it.)
        private Field               root;
        
        //是否枚举值
        public boolean isEnumConstant() {
            return (getModifiers() & Modifier.ENUM) != 0;
        }
        //属性原始类型
        public Class<?> getType() {
            return type;
        }
        //属性泛型类型
        public Type getGenericType() {
            if (getGenericSignature() != null)
                return getGenericInfo().getGenericType();
            else
                return getType();
        }
        
        public String toGenericString() {
            int mod = getModifiers();
            Type fieldType = getGenericType();
            return (((mod == 0) ? "" : (Modifier.toString(mod) + " "))
                + fieldType.getTypeName() + " "
                + getDeclaringClass().getTypeName() + "."
                + getName());
        }
        
        //获取注解类型
        public AnnotatedType getAnnotatedType() {
            return TypeAnnotationParser.buildAnnotatedType(getTypeAnnotationBytes0(),
                                                               sun.misc.SharedSecrets.getJavaLangAccess().
                                                                   getConstantPool(getDeclaringClass()),
                                                               this,
                                                               getDeclaringClass(),
                                                               getGenericType(),
                                                               TypeAnnotation.TypeAnnotationTarget.FIELD);
        }
}
```