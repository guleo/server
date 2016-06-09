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
        upload.setSizeMax(1024 * 1024 * 100);//设置上传文件的最大大小
        upload.setSizeThreshold(4096);//设置最多在内存中存储的数据
        upload.setRepositoryPath(path);//当超过Threshold的大小时将数据存放在硬盘的目录
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
            //上传失败
            String FAIL_INFO = "false";
            writer.write(FAIL_INFO.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
