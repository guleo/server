package com.model.servlet.listener;

import com.model.MatchNode;
import com.model.MatchTree;
import org.json.JSONStringer;
import org.json.JSONWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.model.persist.dao.QuestionDao;
import com.model.persist.domain.Question;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by frank on 2016/4/3.
 * 监听用户匹配请求
 */
public class MatchListener implements ServletRequestListener {

    private static MatchTree matchTree = new MatchTree();
    private static List<MatchPair> pairs = new ArrayList();
    private static List<MatchQuestion> matchQuestions = new ArrayList();

    private String searchQue(String user) {
        for (MatchQuestion question : matchQuestions) {
            if (question.getRival1().equals(user) || question.getRival2().equals(user))
                return question.getQuestions();
        }
        return null;
    }

    private boolean removeQue(String user) {
        for (MatchQuestion question : matchQuestions) {
            if (question.getRival1().equals(user) || question.getRival2().equals(user))
                return matchQuestions.remove(question);
        }
        return false;
    }

    private boolean removePair(String user) {
        for (MatchPair pair : pairs) {
            if (pair.getRival1().equals(user) || pair.getRival2().equals(user))
                return pairs.remove(pair);
        }
        return false;
    }

    private MatchPair searchPair(String s) {
        for (MatchPair pair : pairs) {
            if (pair.getRival1().equals(s) || pair.getRival2().equals(s))
                return pair;
        }
        return null;
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {


        try {
            ServletRequest request = servletRequestEvent.getServletRequest();
            String type = request.getParameter("type");

            if (type != null && type.equals("matchServlet")) {
                String username = new String(request.getParameter("username").
                        getBytes("iso-8859-1"), "utf-8");
                String firstClass = new String(request.getParameter("firstClass").getBytes("iso-8859-1"), "utf-8");
                String subClass = new String(request.getParameter("subClass").getBytes("iso-8859-1"), "utf-8");
                int first = matchTree.contain(firstClass);
                if (first != -1) {
                    MatchNode node = matchTree.getChild(first);
                    node = node.getChild(subClass);
                    System.out.println("node:" + username);
                    node = node.getChild(username);

                    node.reset();
                    node = node.getParent();
                    boolean is = node.removeChild(username);
                    if (is) {
                        System.out.println(removePair(username));
                        System.out.print(removeQue(username));
                    }
                    System.out.println(is);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//            if (request.getServerName().equals("matchServlet")) {
//                String username = new String(request.getParameter("username").
//                    getBytes("iso-8859-1"), "utf-8");
//                MatchPair pair = (MatchPair) request.getAttribute("pair");
//                MatchNode node = pair.getNode();
//                node.removeChild(username);
//                Object rival = pair.getRival2();
//                node.removeChild(rival);
//                pairs.remove(pair);
//            }
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        try {
            ServletRequest request = servletRequestEvent.getServletRequest();
            String type = request.getParameter("type");
            if (type != null && type.equals("matchServlet")) {
                int num = Integer.parseInt(request.getParameter("num"));
                String username = new String(request.getParameter("username").
                        getBytes("iso-8859-1"), "utf-8");
                String firstClass = new String(request.getParameter("firstClass").getBytes("iso-8859-1"), "utf-8");
                String subClass = new String(request.getParameter("subClass").getBytes("iso-8859-1"), "utf-8");
                String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/";

                int first, sub;
                MatchNode node = new MatchNode(firstClass);
                first = matchTree.addNode(node);
                node = matchTree.getChild(first);
                sub = node.addChild(subClass);
                node = node.getChild(sub);

                MatchPair pair = searchPair(username);
                MatchNode m;
                if (pair == null) {
                    node.addChild(username);
                    System.out.println("match:" + username);
                    pair = generatePair(node, username, request.getRemoteAddr());
                    if (pair == null) {
                        m = node.getChild(username);
                        System.out.print(m.isMatchPaired());
                        String url_user = request.getRemoteAddr();
                        m.setUrl(url_user);
                        request.setAttribute("username", username);
                        request.setAttribute("url", url);
                        request.setAttribute("node", m);
                        return;
                    }
                    System.out.println("paired:true");
                    m = node.getChild(username);
                    m.setUrl(request.getRemoteAddr());
                    m.setPaired(true);
                    pairs.add(pair);
                }
                System.out.println("pair:" + pair.getRival1() + " " + pair.getRival2());

                System.out.println("username:" + username);
                String rival, rivalUrl,userUrl;
                if (pair.getRival1().equals(username)) {
                    rival = pair.getRival2();
                    rivalUrl = pair.getUrl2();
                    userUrl = pair.getUrl1();
                } else {
                    rival = pair.getRival1();
                    rivalUrl = pair.getUrl1();
                    userUrl = pair.getUrl2();
                }

                String questions = searchQue(rival);
                if (questions == null) {
                    MatchQuestion question = generateQue(username, rival, subClass, num);
                    matchQuestions.add(question);
                    questions = question.getQuestions();
                }
                m = node.getChild(username);
                m.setMatchPair(rival, questions,rivalUrl);
                request.setAttribute("username", username);
                request.setAttribute("url", url);
                request.setAttribute("node", m);

                m = node.getChild(rival);
                m.setMatchPair(username, questions,userUrl);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private MatchPair generatePair(MatchNode node, String user, String url) {

        for (int i = 0; i < node.getChildCount(); i++)
            if (!node.getChild(i).isPaired() && !node.getChild(i).getContent().equals(user)) {
                //在此添加匹配机制

                MatchNode m = node.getChild(i);
                m.setPaired(true);
                String rival = (String) m.getContent();
                MatchPair pair = new MatchPair(user, rival, node);
                pair.setUrl1(url);
                pair.setUrl2(m.getUrl());
                return pair;
            }
        return null;
    }

    private MatchQuestion generateQue(String rival1, String rival2, String subClass, int num) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("persist.xml");
        QuestionDao queDao = (QuestionDao) ctx.getBean("questionDao");
        List<Question> questions = queDao.selectQuestion(subClass);System.out.println(questions);
        List<Integer> queId = getQuestionList(num,questions.size());
        JSONStringer stringer1 = new JSONStringer();
        stringer1.array();

        for (int id : queId) {
            if (questions != null) {

                JSONWriter object1 = stringer1.object();
                //writer.write(que.getBytes());
                Question question = questions.get(id);
                String text = question.getText();
                String right = question.getRight();
                String wrong1 = question.getWrong1();
                String wrong2 = question.getWrong2();
                String wrong3 = question.getWrong3();
                object1.key("text").value(text);
                object1.key("right").value(right);
                object1.key("wrong1").value(wrong1);
                object1.key("wrong2").value(wrong2);
                object1.key("wrong3").value(wrong3);
                stringer1.endObject();
            } else break;
        }
        stringer1.endArray();
        return new MatchQuestion(stringer1.toString(), rival1, rival2);
    }


    public class MatchPair {

        private String rival1;
        private String rival2;
        private String url1;
        private String url2;
        //子类的节点
        private MatchNode node;

        public MatchPair(String rival1, String rival2, MatchNode node) {
            this.rival1 = rival1;
            this.rival2 = rival2;
            this.node = node;
        }

        public String getRival1() {
            return rival1;
        }

        public String getRival2() {
            return rival2;
        }

        public MatchNode getNode() {
            return node;
        }

        public String getUrl1() {
            return url1;
        }

        public void setUrl1(String url1) {
            this.url1 = url1;
        }

        public String getUrl2() {
            return url2;
        }

        public void setUrl2(String url2) {
            this.url2 = url2;
        }
    }

    public class MatchQuestion {
        private String questions;
        private String rival1;
        private String rival2;

        public MatchQuestion(String questions, String rival1, String rival2) {
            this.questions = questions;
            this.rival1 = rival1;
            this.rival2 = rival2;
        }

        public String getQuestions() {
            return questions;
        }

        public String getRival1() {
            return rival1;
        }

        public String getRival2() {
            return rival2;
        }
    }

    /**
     * 产生随机数
     *
     * @param num 随机选出的问题总数
     * @return 含不同随机数的列表
     */
    private List<Integer> getQuestionList(int num,int size) {
        List<Integer> queId = new ArrayList();
        Random r = new Random();
        int i = 0;
        while (i < num) {
            int id = r.nextInt(size-1);
            if (!queId.contains(id)) {
                queId.add(id);
                i++;
            }
        }
        return queId;
    }
}
