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



public class picServlet extends HttpServlet {
    private    List<CustomSession> the_sessions;
    private final boolean logging = true;

    public void init() throws ServletException  {
        the_sessions = new ArrayList<CustomSession>();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{

        CustomSession this_session = null;
        boolean is_valid_session = false;
        PicList thesePics = null;
        String ID;
        Consumer <String> forwardTo =(url) ->ForwardTo(url,req,res);

        if(req.getParameter("logout") != null){
            if (req.getParameter("sessionID") != null) {
                ID = req.getParameter("sessionID").trim();
                for (int i = 0; i < the_sessions.size(); i++) {
                    if (the_sessions.get(i).getID().equals(ID)) {  //Found an active session
                        if (logging) log("removing: " + the_sessions.get(i).getID());
                        the_sessions.remove(i);
                        req.removeAttribute("sessionID");
                        break;
                    }
                }
            }
            req.removeAttribute("logout");
        }

        if (req.getParameter("sessionID") != null) {
            for (int i = 0; i < the_sessions.size(); i++) {
                if (the_sessions.get(i).getID().equals(req.getParameter("sessionID").trim())) {  //Found an session
                    if (the_sessions.get(i).isExpired()){
                        the_sessions.remove(i);
                    }
                    else {
                        this_session =the_sessions.get(i);
                        is_valid_session = true;
                        if (logging) log("Session validated " + this_session);
                    }
                    break;
                }
            }
        }

        if (!is_valid_session){
            this_session = new CustomSession(req.getRemoteAddr());
            the_sessions.add(this_session);
            is_valid_session = true;
        }

        if (req.getParameter("search_term") != null){
            thesePics = new PicList();
            final Object lock = this_session.getID().intern();
            synchronized (lock) {
                log(thesePics.tryGetList(req.getParameter("search_term").trim()));
            }
            if (logging) { log("Num of pics searched: " + String.valueOf(thesePics.getPicQuantity()));}

        }

        req.setAttribute("sessionID",this_session.getID());
        if (thesePics != null && thesePics.getPicQuantity() > 0){
            req.setAttribute("picHTML", thesePics.sendPics());
        }
        forwardTo.accept("search.jsp");

        return;
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        CustomSession this_session = null;
        boolean is_valid_session = false;
//        final String UPLOAD_DIR = File(getServletContext().getRealPath("/"));
                Consumer <String> forwardTo =(url) ->ForwardTo(url,req,res);

        if (req.getParameter("sessionID") != null) {
            for (int i = 0; i < the_sessions.size(); i++) {
                if (the_sessions.get(i).getID().equals(req.getParameter("sessionID").trim())) {  //Found an session
                    if (the_sessions.get(i).isExpired()){
                        the_sessions.remove(i);
                    }
                    else {
                        this_session =the_sessions.get(i);
                        is_valid_session = true;
                        if (logging) log("Session validated " + this_session);
                    }
                    break;
                }
            }
        }

        if (!is_valid_session){
            this_session = new CustomSession(req.getRemoteAddr());
            the_sessions.add(this_session);
            is_valid_session = true;
        }
        //Check to see if the session needs to be authorized
        if (req.getParameter("whoisit") != null && req.getParameter("passwd") != null) {
            if (logging) log ("running authenticator");
            String name = req.getParameter("whoisit").trim();
            String pw = req.getParameter("passwd").trim();
            log(this_session.tryLogin(name, pw));
        }

        //Check for incoming pic upload
        if (req.getParameter("filename") != null && req.getParameter("tag") != null){
            String fileName;
            try {
                List<FileItem> multiparts = new ServletFileUpload(
                new DiskFileItemFactory()).parseRequest(req);
                final Part filePart = req.getPart("file");
                final String fileName = getFileName(filePart);

                OutputStream out = null;
                InputStream filecontent = null;
                final PrintWriter writer = res.getWriter();


                out = new FileOutputStream(new File(path + File.separator + fileName));
                filecontent = filePart.getInputStream();

                int read = 0;
                final byte[] bytes = new byte[1024];

                while ((read = filecontent.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }

                //File uploaded successfully
                req.setAttribute("message", "File Uploaded Successfully");
            } catch (Exception ex) {
                request.setAttribute("message", "File Upload Failed");
                log(ex.getMessage());
            }

            log (PicList.tryAddPic(fileName, req.getParameter("tag").trim()));
        }

        if(this_session.isUserAuthenticated()) {
            forwardTo.accept("Add_Pic.jsp");
        }
    }

    public void log(String s){

        try {
            File outFile = new File(getServletContext().getRealPath("/") + "my_log");
            outFile.createNewFile();
            PrintWriter fileWriter = new PrintWriter(new FileOutputStream(outFile,true));
            fileWriter.println(s+" at: " + new Date(System.currentTimeMillis()).toString());
            fileWriter.close();
        } catch (IOException ex) {

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
    void RedirectTo(String url,HttpServletRequest req, HttpServletResponse res) {
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

    public void destroy()
    {
        log("The instance was destroyed");
    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

}


/*
    final Object lock = req.getSession().getId().intern();
            req.setAttribute("thesessioncount",the_sessions.size());
synchronized(lock) {
        if (req.getParameter("task") != null) {
        NotesBean thesenotes = new NotesBean();
        if (!req.getParameter("task").trim().equals("0")) {
        thesenotes.setAll(req.getParameter("java_source"), Integer.parseInt(req.getParameter("version")));
        if (req.getParameter("task").trim().equals("2")) {
        thesenotes.setNotes(req.getParameter("notes").trim(), req.getParameter("java_source"), Integer.parseInt(req.getParameter("version")));
        }
        }
        req.setAttribute("theBean", thesenotes);
        }
        }
        req.setAttribute("sessionID",this_session);
        req.setAttribute("thesessioncount",the_sessions.size());
        forwardTo.accept("getNotes.jsp")
*/
