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
                filename = CLASSES_PATH + "picServlet.java";
            }
            else if (req.getParameter("files").trim().equals("search.jsp")) {
                filename = "search.jsp";
            }
            else if (req.getParameter("files").trim().equals("Add_Pic.jsp")) {
                filename = "Add_Pic.jsp";
            }
        }


        InputStream is = context.getResourceAsStream(filename);
        if (is != null) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            PrintWriter writer = res.getWriter();
            String text;

            // We read the file line by line and later will be displayed on the
            // browser page.
            while ((text = reader.readLine()) != null) {
                writer.println(text + "</br>");
            }
        }
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