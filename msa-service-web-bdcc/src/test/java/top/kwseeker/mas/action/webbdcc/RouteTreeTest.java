package top.kwseeker.mas.action.webbdcc;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class RouteTreeTest {

    /**
     * 路由树
     * ROOT -> A -> a -> b     /A/a/b
     *           -> c -> d
     *      -> B -> x -> y
     *           -> m -> n
     */
    @Test
    public void test() {
        Node root = new RootNode();
        Node A = new Node("A");
        Node Aa = new Node("a");
        Node Aab = new Node("b", true);
        Node Ac = new Node("c");
        Node Acd = new Node("d", true);
        Node B = new Node("B");
        Node Bx = new Node("x");
        Node Bxy = new Node("y", true);
        Node Bm = new Node("m");
        Node Bmn = new Node("n", true);
        root.addChild(A);
        root.addChild(B);
        A.addChild(Aa);
        A.addChild(Ac);
        Aa.addChild(Aab);
        Ac.addChild(Acd);
        B.addChild(Bx);
        B.addChild(Bm);
        Bx.addChild(Bxy);
        Bm.addChild(Bmn);
    }

    @Data
    static class Node {
        // 路由节点路径
        private String path;
        // 是否降级
        private boolean degrade;
        private boolean isLeaf;
        // 子路由节点
        private Map<String, Node> children;

        public Node(String path) {
            this(path, false);
        }

        public Node(String path, boolean isLeaf) {
            this.path = path;
            this.isLeaf = isLeaf;
            if (!isLeaf) {
                children = new HashMap<>();
            }
        }

        public void addChild(Node node) {
            children.put(node.path, node);
        }
    }

    static class RootNode extends Node {
        public RootNode() {
            super("ROOT");
        }
    }
}
