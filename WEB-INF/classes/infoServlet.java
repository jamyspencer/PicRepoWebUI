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
    private static final String ROOT_PATH = getServletContext().getRealPath("/");
    private static final String CLASSES_PATH = ROOT_PATH + "WEB-INF/classes/";

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        if (req.getParameter("files") != null) {
            if (req.getParameter("files").trim().equals("servlet")) {
                forwardTo.accept(CLASSES_PATH + "picServlet.java");
            }
        }

        return;
    }
}