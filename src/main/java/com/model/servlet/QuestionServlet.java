package com.model.servlet;

import org.json.JSONStringer;
import org.json.JSONWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.model.persist.dao.QuestionDao;
import com.model.persist.daoImpl.QuestionDaoImpl;
import com.model.persist.domain.Question;
import com.model.persist.domain.QuestionClass;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by frank on 2016/3/28.
 * 知识问答题目servlet
 */
public class QuestionServlet extends HttpServlet {
    private static final int GET_CLASS = 0;
    private static final int GET_QUESTION = 1;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    @ResponseBody
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int type = Integer.parseInt(req.getParameter("type"));
        resp.setContentType("text/json;charset=utf-8");
        JSONStringer stringer = new JSONStringer();
        ApplicationContext ctx = new ClassPathXmlApplicationContext("persist.xml");
        QuestionDaoImpl questionDao = (QuestionDaoImpl) ctx.getBean("questionDao");
        if (type == GET_CLASS) {
            stringer.array();
            List<QuestionClass> firstClassList = questionDao.selectFirstClass();
            for (QuestionClass firstClass : firstClassList) {

                List<String> subList = new ArrayList();
                List<QuestionClass> data = questionDao.findSubClassById(firstClass.getClassId());
                if (data == null || data.size() == 0) {
                    continue;
                }
                for (QuestionClass d : data) {
                    subList.add(d.getClassName());
                }

                JSONWriter writer = stringer.object();
                JSONStringer s1 = new JSONStringer();
                writer.key("first").value(firstClass.getClassName());
                JSONWriter writer1 = s1.array();
                for (String sub : subList) {
                    JSONWriter w = writer1.object();
                    w.key("sub").value(sub);
                    writer1.endObject();
                }
                s1.endArray();
                writer.key("name").value(s1);
                stringer.endObject();
            }
            stringer.endArray();
        }
        if (type == GET_QUESTION) {
            String subClass = new String(req.getParameter("subClass").getBytes("iso-8859-1"), "utf-8");
            int num = Integer.parseInt(req.getParameter("num"));
            QuestionDao queDao = (QuestionDao) ctx.getBean("questionDao");
            List<Question> questions = queDao.selectQuestion(subClass);
            List<Integer> queId = getQuestionList(num);
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
            JSONWriter object = stringer.object();
            object.key("questions").value(stringer1.toString());
            object.endObject();
        }
        resp.getWriter().write(stringer.toString());
    }

    private List<Integer> getQuestionList(int num) {
        List<Integer> queId = new ArrayList();
        Random r = new Random();
        int i = 0;
        while (i < num) {
            int id = r.nextInt(num);
            if (!queId.contains(id)) {
                queId.add(id);
                i++;
            }
        }
        return queId;
    }

}
