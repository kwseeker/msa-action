package top.kwseeker.mas.action.webbdcc;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 很久没用怎么用的不记得了，写个测试回忆下Curator使用方法
 */
@Slf4j
public class CuratorFrameworkTest {

    private static final String ZK_CONNECT_STRING = "localhost:22181";
    private static final int SESSION_TIMEOUT_MS = 5000;
    private static final int CONNECTION_TIMEOUT_MS = 3000;
    private static final int RETRY_BASE_SLEEP_TIME_MS = 3000;
    private static final int RETRY_MAX_RETRIES = 3;

    private final CuratorFramework zkClient = CuratorFrameworkFactory.builder()
            .connectString(ZK_CONNECT_STRING)
            .sessionTimeoutMs(SESSION_TIMEOUT_MS)
            .connectionTimeoutMs(CONNECTION_TIMEOUT_MS)
            .retryPolicy(new ExponentialBackoffRetry(RETRY_BASE_SLEEP_TIME_MS , RETRY_MAX_RETRIES))
            .build();

    {
        zkClient.start();
        try {
            zkClient.blockUntilConnected();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCuratorFramework() throws Exception {
        // 创建节点并写入数据
        createNode(zkClient, CreateMode.EPHEMERAL, "/msa-test-node/config/test1/desc", "Just test for curator!");
        createNode(zkClient, CreateMode.EPHEMERAL, "/msa-test-node/config/test1/start-time", "2020-01-01 12:00:00");
        createNode(zkClient, CreateMode.EPHEMERAL, "/msa-test-node/config/test1/end-time", "2020-01-05 12:00:00");

        readData(zkClient, "/msa-test-node/config/test1/end-time");
        setData(zkClient, "/msa-test-node/config/test1/end-time", "2020-01-03 12:00:00");
        setDataAsync(zkClient, "/msa-test-node/config/test1/start-time", "2020-01-02 12:00:00");

        readData(zkClient, "/msa-test-node/config/test1/start-time");
        readData(zkClient, "/msa-test-node/config/test1/end-time");

        zkClient.getChildren().forPath("/msa-test-node/config/test1").forEach(childNodePath -> {
            log.info("Child node: " + childNodePath);
        });

        Thread.sleep(5);
    }

    @Test
    public void testReadNotExistNode() throws Exception {
        assertThrows(Exception.class, () -> readData(zkClient, "/not-exist-node"));
    }

    @Test
    public void testCheckExist() throws Exception {
        String node = "/msa-test-node/not-exist-node";
        Stat stat = zkClient.checkExists().forPath(node);
        // 不存在的节点 checkExists 返回 null
        assertNull(stat);
    }

    @Test
    public void testReadExistButNoDataNode() throws Exception {
        String node = "/msa-test-node/exist-but-no-data-node";
        if (null != zkClient.checkExists().forPath(node)) {
            zkClient.delete().forPath(node);
        }
        zkClient.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(node);
        byte[] data = zkClient.getData().forPath(node);
        // 默认会返回一个IP地址？127.0.1.1
        System.out.println("Data from " + node + ": " + new String(data));

        zkClient.setData().forPath(node, "".getBytes());
        byte[] data2 = zkClient.getData().forPath(node);
        assertEquals(data2.length, 0);
    }

    /**
     * 只能监听一次，后面两种监听实现可以一直监听
     */
    @Test
    public void testWatcher() throws Exception {
        String node = "/msa-test-node/test-watcher-node";
        zkClient.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(node);
        // 注册监听
        byte[] bytes = zkClient.getData()
                .usingWatcher((Watcher) watchedEvent -> {
                    System.out.println("watchedEvent: " + watchedEvent);
                })
                .forPath(node);
        System.out.println("Data from " + node + ": " + new String(bytes));

        // 修改值
        zkClient.setData().forPath(node, "1".getBytes());
        zkClient.setData().forPath(node, "2".getBytes());

        Thread.sleep(5000);
    }

    /**
     * NodeCache PathChildrenCache TreeCache 均被 CuratorCache 替代了
     */
    @Test
    public void testNodeCache() throws Exception {
        String node = "/msa-test-node/test-nodecache-node";
        zkClient.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(node);
        // 不过此接口已经被废弃了
        NodeCache nodeCache = new NodeCache(zkClient, node, false);
        // 注册监听
        NodeCacheListener l = new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                ChildData childData = nodeCache.getCurrentData();
                log.info("ZNode节点状态改变, path={}", childData.getPath());
                log.info("ZNode节点状态改变, data={}", new String(childData.getData(), "Utf-8"));
                log.info("ZNode节点状态改变, stat={}", childData.getStat());
            }
        };
        nodeCache.getListenable().addListener(l);
        nodeCache.start();

        // 修改值
        zkClient.setData().forPath(node, "1".getBytes());
        zkClient.setData().forPath(node, "2".getBytes());

        Thread.sleep(5000);
    }

    @Test
    public void testCuratorCache() throws Exception {
        String node = "/msa-test-node/test-curatorcache-node";
        if (null != zkClient.checkExists().forPath(node)) {
            zkClient.delete().forPath(node);
        }
        zkClient.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(node);

        CuratorCache curatorCache = CuratorCache.build(zkClient, node);
        curatorCache.start();
        // 注册监听，内部也可以监听子节点变化
        curatorCache.listenable().addListener((type, oldData, data) -> {    //CuratorCacheListener
            switch (type) {
                case NODE_CREATED:
                    log.info("ZNode节点创建, oldData={}, data={}", oldData, data);
                    break;
                case NODE_CHANGED:
                    log.info("ZNode节点状态改变, oldData={}", oldData);
                    log.info("ZNode节点状态改变, data={}", data);
                    break;
                case NODE_DELETED:
                    log.info("ZNode节点被删除, oldData={}, data={}", oldData, data);
                default:
                    break;
            }
        });

        // 修改值
        zkClient.setData().forPath(node, "1".getBytes());
        zkClient.setData().forPath(node, "2".getBytes());
        zkClient.setData().forPath(node, "3".getBytes());

        // 测试监听子节点变化
        String childNode = node + "/child";
        zkClient.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(childNode, "1".getBytes());
        zkClient.setData().forPath(childNode, "2".getBytes());
        zkClient.setData().forPath(childNode, "3".getBytes());

        zkClient.delete().forPath(childNode);

        Thread.sleep(5000);
    }

    private static void createNode(CuratorFramework curator, CreateMode createMode, String path, String initialData)
            throws Exception {
        curator.create()
                .creatingParentsIfNeeded()
                .withMode(createMode)
                .forPath(path, initialData.getBytes());
        System.out.println("Node created at " + path);
    }

    private static void readData(CuratorFramework curator, String path) throws Exception {
        byte[] data = curator.getData().forPath(path);
        System.out.println("Data from " + path + ": " + new String(data));
    }

    private static void setData(CuratorFramework curator, String path, String newData) throws Exception {
        Stat res = curator.setData().forPath(path, newData.getBytes());
        System.out.println("Data to " + path + "result: " + res);
    }

    private static void setDataAsync(CuratorFramework curator, String path, String newData) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        curator.setData()
                .inBackground((client, event) -> {
                    System.out.println("Node value set event: " + event);
                    latch.countDown();
                }, path)
                .forPath(path, newData.getBytes());

        // 等待异步设值和回调完成
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for completion: " + e.getMessage());
        }
    }
}
