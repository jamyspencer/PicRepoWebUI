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

        Consumer <String> forwardTo =(url) ->ForwardTo(url,req,res);
        private final String ROOT_PATH = getServletContext().getRealPath("/");
        private final String CLASSES_PATH = ROOT_PATH + "WEB-INF/classes/";

        if (req.getParameter("files") != null) {
            if (req.getParameter("files").trim().equals("servlet")) {
                forwardTo.accept(CLASSES_PATH + "picServlet.java");
            }
        }

        return;
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