package cn.topideal.com.multithread.cas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CAS单一共享变量操作的问题
 */
public class CasSingleReferenceQue {

    public static void main(String[] args) {
        User uA = new User("liuyou", 27, "湖南衡阳");
        AtomicReference<User> atomicReference = new AtomicReference();

        System.out.println(atomicReference.compareAndSet(null, uA));
        System.out.println(atomicReference.get().toString());

        User uB = new User("moqingqing", 26, "湖南邵阳");
        System.out.println(atomicReference.compareAndSet(uA, uB));

        System.out.println(atomicReference.get().toString());


    }
}

@Data
@AllArgsConstructor
@ToString
class User {
    String name;
    int age;
    String address;
}
