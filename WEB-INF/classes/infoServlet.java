import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.function.*;
import mybeans.UserAuthenticator;
import mybeans.PicList;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;



public class infoServlet extends HttpServlet {

    public void init() throws ServletException  {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Consumer<String> forwardTo = (url) -> ForwardTo(url, req, res);
        final String ROOT_PATH = "/usr/share/tomcat/webapps/j-spencer/";
        final String CLASSES_PATH = "WEB-INF/classes/";
        ServletContext context = getServletContext();
        String filename =  "error";
        res.setContentType("text/html");


        if (req.getParameter("files") != null) {
            if (req.getParameter("files").trim().equals("servlet")) {
                filename = ROOT_PATH + CLASSES_PATH + "picServlet.java";
            }
            else if (req.getParameter("files").trim().equals("Pic.java")) {
                filename = ROOT_PATH + CLASSES_PATH + "mybeans/Pic.java";
            }
            else if (req.getParameter("files").trim().equals("PicList.java")) {
                filename = ROOT_PATH + CLASSES_PATH + "mybeans/PicList.java";
            }
            else if (req.getParameter("files").trim().equals("CustomSession.java")) {
                filename = ROOT_PATH + CLASSES_PATH + "CustomSession.java";
            }
            else if (req.getParameter("files").trim().equals("search.jsp")) {
                filename = ROOT_PATH + "search.jsp";
            }
            else if (req.getParameter("files").trim().equals("Add_Pic.jsp")) {
                filename = ROOT_PATH + "Add_Pic.jsp";
            }
            else if (req.getParameter("files").trim().equals("web.xml")) {
                filename = ROOT_PATH + "WEB-INF/web.xml";
            }
        }


        PrintWriter pw = res.getWriter();
        FileReader fr = new FileReader(filename);

        int k;
        char ch;

        while( (k = fr.read()) != -1 )
        {
            ch = (char) k;

            if(k == 13 || k == 10) 		 // 13 is ASCII value of new line character
                pw.print("<BR>");
            else if(ch == '<')
                pw.print("&lt;");
            else if(ch == '>')
                pw.print("&gt;");
            else if(ch == ' ')
                pw.print("&nbsp");
            else
                pw.print(ch);
        }

        fr.close();
        pw.close();
    }

    void ForwardTo(String url,HttpServletRequest req, HttpServletResponse res) {
        RequestDispatcher dispatcher= req.getRequestDispatcher(url);
        try {
            dispatcher.forward(req, res);
        } catch (IOException|ServletException is) {
            log(" req from " + url + " not forwarded at ");
            try {
                throw is;
            } catch (Exception e) {
            }
        }
    }
}