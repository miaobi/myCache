package note;

/**
 * 单例模式
 */
public class Singleton {
    //自己创建一个静态实例
    private  static Singleton instance = new Singleton();
    //将构造函数设为私有
    private Singleton(){}
    //提供一个获取静态实例的静态方法
    public static Singleton getSingleton(){
        return instance;
    }
}

//public class Singleton1 {
//    private static volatile Singleton1 sngleton1;
//    private Singleton1(){}
//    public static Singleton1 getSingleton1() {
//        if(sngleton1==null){
//            synchronized (Singleton.class) {
//                if (sngleton1 == null) {
//                    sngleton1 = new Singleton1();
//                }
//            }
//        }
//        return sngleton1;
//    }
//
//}
