package cn.topideal.com.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;

public class ZookeeperClientDemo implements Watcher {

    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("10.10.0.233:2181,10.10.0.225:2181", 50000, new ZookeeperClientDemo());

            String path = "/dubbo";

            if (zooKeeper.exists(path, true) == null) {
                zooKeeper.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            } else
                System.out.println("节点创建失败，该节点已存在");
            System.out.println("节点创建成功");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    private static void test1() {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1", 2181, new ZookeeperClientDemo());

            String path = "/dubbo";

            if (zooKeeper.exists(path, true) == null) {
                zooKeeper.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            } else
                System.out.println("节点创建失败，该节点已存在");
            System.out.println("节点创建成功");

        } catch (IOException | KeeperException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("触发了事件监听机制");
    }
}
