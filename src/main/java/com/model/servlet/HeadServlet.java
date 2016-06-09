package com.model.servlet;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.model.persist.daoImpl.UserDaoImpl;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by frank on 2016/3/17.
 */
public class HeadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getSession().getServletContext().getRealPath("/") + "img";
        DiskFileUpload upload = new DiskFileUpload();
        upload.setSizeMax(1024 * 1024 * 100);//�����ϴ��ļ�������С
        upload.setSizeThreshold(4096);//����������ڴ��д洢������
        upload.setRepositoryPath(path);//������Threshold�Ĵ�Сʱ�����ݴ����Ӳ�̵�Ŀ¼
        ServletOutputStream writer = resp.getOutputStream();
        try {
            List list = upload.parseRequest(req);
            if (list.size() > 0) {

                FileItem item = (FileItem) list.get(0);
                if (!item.isFormField()) {
                    String filename = item.getName();
                    System.out.println(filename);
                    filename = filename.substring(filename.lastIndexOf("\\") + 1);
                    if (filename != null && !filename.trim().equals("")) {
                        File head = new File(path, filename);
                        item.write(head);
                        String PASS_INFO = "true";
                        writer.write(PASS_INFO.getBytes());
                        item = (FileItem) list.get(1);
                        ApplicationContext ctx = new ClassPathXmlApplicationContext("persist.xml");
                        UserDaoImpl userDao = (UserDaoImpl) ctx.getBean("userDao");
                        String username = item.getString();
                        username = URLDecoder.decode(username,"utf-8");
                        System.out.println(username);
                        userDao.updateUserInfo(filename, username);
                        return;
                    }
                }
            }
            //�ϴ�ʧ��
            String FAIL_INFO = "false";
            writer.write(FAIL_INFO.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
