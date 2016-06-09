package com.model.servlet;

import com.model.persist.daoImpl.QuestionDaoImpl;
import com.model.persist.daoImpl.ShareDaoImpl;
import com.model.persist.daoImpl.UserDaoImpl;
import com.model.persist.domain.Question;
import com.model.util.Utils;
import org.json.JSONStringer;
import org.json.JSONWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by frank on 2016/5/22.
 * 处理管理员审核题库请求
 */
@Controller
public class AdminShareServlet {
    @RequestMapping(value = "/Share", method = RequestMethod.GET, produces = {"text/json;charset=utf-8"})
    @ResponseBody
    String SearchResultSe(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int mode = Integer.parseInt(req.getParameter("mode"));
        ApplicationContext ctx = new ClassPathXmlApplicationContext("persist.xml");
        JSONStringer stringer = new JSONStringer();

        switch (mode) {
            case Utils.QUE_SELECT_MODE:
                stringer.array();
                int curPage = Integer.parseInt(req.getParameter("curPage"));
                UserDaoImpl userDao = (UserDaoImpl) ctx.getBean("userDao");
                ShareDaoImpl shareDao = (ShareDaoImpl) ctx.getBean("shareDao");
                QuestionDaoImpl questionDao = (QuestionDaoImpl) ctx.getBean("questionDao");
                List<Question> list = shareDao.getQueShare();

                if (list != null) {
                    int tmp = list.size() / Utils.COUNT_PER_PAGE;
                    int totalPage = (list.size() % Utils.COUNT_PER_PAGE == 0) ? tmp : tmp + 1;
                    int fromIndex = (curPage - 1) * Utils.COUNT_PER_PAGE;
                    int toIndex = (curPage != totalPage) ? curPage * Utils.COUNT_PER_PAGE : list.size();
                    if (fromIndex == toIndex) {
                        Question temp = list.get(fromIndex);
                        list.clear();
                        list.add(temp);
                    } else list = list.subList(fromIndex, toIndex);
                    System.out.println("from,to" + fromIndex + toIndex);
                    for (Question que : list) {
                        JSONWriter writer = stringer.object();
                        int firstClass = que.getFirstClass();
                        int subClass = que.getSubClass();
                        int commit = que.getCommit_id();
                        String username = userDao.getUserBaseById(commit).getUsername();
                        String first = questionDao.getFirstClassNameById(firstClass);
                        String sub = questionDao.getSubClassNameById(subClass);
                        writer.key("Id").value(que.getId());
                        writer.key("First").value(first);
                        writer.key("Sub").value(sub);
                        writer.key("Commit").value(username);
                        writer.key("Text").value(que.getText());
                        writer.key("Right").value(que.getRight());
                        writer.key("Wrong1").value(que.getWrong1());
                        writer.key("Wrong2").value(que.getWrong2());
                        writer.key("Wrong3").value(que.getWrong3());
                        writer.key("Time").value(que.getTime());
                        writer.endObject();
                    }
                    JSONWriter writer = stringer.object();
                    writer.key("TotalPage").value(totalPage);
                    writer.key("CurPage").value(curPage);
                    writer.endObject();
                }
                stringer.endArray();
                break;
            case Utils.QUE_DEL_MODE:
                JSONWriter writer = stringer.object();
                ShareDaoImpl shareDao1 = (ShareDaoImpl) ctx.getBean("shareDao");
                int id = Integer.parseInt(req.getParameter("id"));
                Boolean b = shareDao1.delQueShareById(id);
                if (b)
                    writer.key("result").value("success");
                else
                    writer.key("result").value("fail");
                stringer.endObject();
                break;
            case Utils.QUE_CHECK_MODE:
                JSONWriter writer1 = stringer.object();
                ShareDaoImpl shareDao2 = (ShareDaoImpl) ctx.getBean("shareDao");
                int id1 = Integer.parseInt(req.getParameter("id"));

                QuestionDaoImpl questionDao1 = (QuestionDaoImpl) ctx.getBean("questionDao");
                Question share = shareDao2.getQueShareById(id1);
                int firstClass = share.getFirstClass();
                int subClass = share.getSubClass();
                String text = share.getText();
                String right = share.getRight();
                String wrong1 = share.getWrong1();
                String wrong2 = share.getWrong2();
                String wrong3 = share.getWrong3();

                Boolean b1 = questionDao1.insertQuestion(new Object[]{firstClass, subClass, text, right, wrong1, wrong2, wrong3});
                if (b1)
                    b1 = b1 && shareDao2.delQueShareById(id1);
                if (b1)
                    writer1.key("result").value("success");
                else
                    writer1.key("result").value("fail");
                stringer.endObject();
                break;
            case Utils.QUE_SEARCH_MODE:
                stringer.array();
                questionDao = (QuestionDaoImpl) ctx.getBean("questionDao");
                String text1 = new String(req.getParameter("text").getBytes("iso-8859-1"), "utf-8");
                System.out.println(text1);
                curPage = Integer.parseInt(req.getParameter("curPage"));
                int type = Integer.parseInt(req.getParameter("type"));
                if (type == 0)
                    id = questionDao.selectFirstClassByName(text1).get(0).getClassId();
                else
                    id = questionDao.selectSubClassByName(text1).get(0).getClassId();
                shareDao = (ShareDaoImpl) ctx.getBean("shareDao");
                list = shareDao.getQueShareByClass(id, type);
                if (list != null && list.size() > 0) {
                    int tmp = list.size() / Utils.COUNT_PER_PAGE;
                    int totalPage = (list.size() % Utils.COUNT_PER_PAGE == 0) ? tmp : tmp + 1;
                    int fromIndex = (curPage - 1) * Utils.COUNT_PER_PAGE;
                    int toIndex = (curPage != totalPage) ? curPage * Utils.COUNT_PER_PAGE : list.size();
                    if (fromIndex == toIndex) {
                        Question temp = list.get(fromIndex);
                        list.clear();
                        list.add(temp);
                    } else list = list.subList(fromIndex, toIndex);

                    for (Question que : list) {
                        writer = stringer.object();
                        firstClass = que.getFirstClass();
                        subClass = que.getSubClass();
                        int commit = que.getCommit_id();
                        userDao = (UserDaoImpl) ctx.getBean("userDao");
                        String username = userDao.getUserBaseById(commit).getUsername();
                        String first = questionDao.getFirstClassNameById(firstClass);
                        String sub = questionDao.getSubClassNameById(subClass);
                        writer.key("Id").value(que.getId());
                        writer.key("First").value(first);
                        writer.key("Sub").value(sub);
                        writer.key("Commit").value(username);
                        writer.key("Text").value(que.getText());
                        writer.key("Right").value(que.getRight());
                        writer.key("Wrong1").value(que.getWrong1());
                        writer.key("Wrong2").value(que.getWrong2());
                        writer.key("Wrong3").value(que.getWrong3());
                        writer.key("Time").value(que.getTime());
                        writer.endObject();
                    }
                    writer = stringer.object();
                    writer.key("TotalPage").value(totalPage);
                    writer.key("CurPage").value(curPage);
                    writer.endObject();
                }
                stringer.endArray();
                break;

        }
        return stringer.toString();
    }
}
