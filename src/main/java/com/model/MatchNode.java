package com.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by frank on 2016/4/13.
 * 匹配结点,至于匹配标志以后再改
 */
public class MatchNode {
    private List<MatchNode> children;
    private Object content;
    private MatchNode parent;
    private MatchPair pair;
    private String url;
    private boolean isPaired = false;

    public MatchNode(Object content) {
        this.content = content;
        children = new ArrayList();
    }

    public void setParent(MatchNode o) {
        this.parent = o;
    }

    public MatchNode getParent() {
        return parent;
    }

    public int addChild(Object o) {
        MatchNode node = new MatchNode(o);
        node.setParent(this);
        int i = contain(o);
        if (i == -1) {
            children.add(node);
            return children.size() - 1;
        }
        return i;
    }

    public Object getContent() {
        return this.content;
    }

    public boolean removeChild(MatchNode node) {
        return children.remove(node);
    }

    public boolean removeChild(Object o) {
        System.out.println("bef:" + children.size());
        Iterator i = children.iterator();
        while (i.hasNext()) {
            MatchNode node = (MatchNode) i.next();
            if (node.getContent().equals(o)) {
                i.remove();
                System.out.println("aft:" + children.size());
                return true;
            }

        }
        return false;
    }

    public int contain(Object s) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getContent().toString().equals(s))
                return i;
        }
        return -1;
    }

    public boolean equal(Object o) {
        return (content.toString().equals(o));
    }

    public MatchNode getChild(int i) {
        return children.get(i);
    }

    public MatchNode getChild(Object o) {
        int index = contain(o);
        return children.get(index);
    }

    public int getChildCount() {
        return children.size();
    }

    public boolean isPaired() {
        return isPaired;
    }

    public void setPaired(boolean paired) {
        isPaired = paired;
    }

    public void setMatchPair(String rival, String questions, String url) {
        pair = new MatchPair(rival, questions,url);
    }

    public boolean isMatchPaired() {
        return pair != null;
    }

    public String getRival() {
        return pair.getRival();
    }

    public String getQuestions() {
        return pair.getQuestions();
    }

    private class MatchPair {
        private String rival;
        private String questions;
        private String url_rival;

        public MatchPair(String rival, String questions,String url) {
            this.rival = rival;
            this.questions = questions;
            url_rival = url;
        }

        public void setUrl(String url_rival) {
            this.url_rival = url_rival;
        }

        public String getUrl() {
            return url_rival;
        }
        public String getRival() {
            return rival;
        }

        public String getQuestions() {
            return questions;
        }

        public void setQuestions(String questions) {
            this.questions = questions;
        }

        public void reset() {
            this.questions = null;
            this.rival = null;
        }
    }

    public void reset() {
        if (pair != null)
            pair.reset();
        pair = null;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getRivalUrl() {
        return pair.getUrl();
    }

    public void setRivalUrl(String url) {
        pair.setUrl(url);
    }

}
