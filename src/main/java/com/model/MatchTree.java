package com.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frank on 2016/4/13.
 * Æ¥ÅäÊ÷
 */
public class MatchTree {
    private List<MatchNode> children = new ArrayList();
    public int addNode (MatchNode child) {
        int i = contain(child.getContent());
        if (i != -1)
            return i;
        children.add(child);
        return children.size() - 1;
    }
    public boolean removeNode (MatchNode child) {
        return children.remove(child);
    }

    public int contain(Object o) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).equal(o))
                return i;
        }
        return -1;
    }

    public MatchNode getChild(int i) {
        return children.get(i);
    }

}
