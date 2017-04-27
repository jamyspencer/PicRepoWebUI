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
        boolean is_adding_pic = false;
        boolean is_valid_session = false;
        Consumer <String> forwardTo =(url) ->ForwardTo(url,req,res);
        String session_id = "error";
        String tag;
        FileItem this_upload;

        if (req.getContentType() != null && req.getContentType().toLowerCase().indexOf("multipart/form-data") > -1 ) {
            is_adding_pic = true;
        }
        if (is_adding_pic){
            try {
                // Create a factory for disk-based file items
                DiskFileItemFactory factory = new DiskFileItemFactory();
                // Configure a repository (to ensure a secure temp location is used)
                ServletContext servletContext = this.getServletConfig().getServletContext();
                File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
                factory.setRepository(repository);
                // Create a new file upload handler
                ServletFileUpload upload = new ServletFileUpload(factory);
                // Parse the request
                List<FileItem> items = upload.parseRequest(req);

                // Process the uploaded items
                Iterator<FileItem> iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = iter.next();

                    if (item.isFormField()) {
                        if (item.getFieldName().equals("sessionID")) session_id = item.getString();
                        else if (item.getFieldName().equals("tag")) tag = item.getString();

                    } else {
                        String fileName = item.getName();
                        this_upload = item;
                    }
                }
            }catch(Exception ex){
                if (logging) log(ex.getMessage());
            }
        }
        else{
            if (req.getParameter("sessionID") != null){
                session_id = req.getParameter("sessionID").trim();
            }
        }
        if (logging) log ("sessionID = " + session_id);

        if (!session_id.equals("error")) {
            for (int i = 0; i < the_sessions.size(); i++) {
                if (the_sessions.get(i).isExpired()) {
                    the_sessions.remove(i);
                    if (logging) log("Session invalidated " + this_session);
                }
                else if (the_sessions.get(i).getID().equals(session_id)) {  //Found an session
                    this_session =the_sessions.get(i);
                    is_valid_session = true;
                    if (logging) log("Session validated " + this_session);
                    break;
                }
            }
        }
        else{
            if (logging) log ("sessionID is null");
            return;
        }

        //Check to see if the session needs to be authorized
        if (is_valid_session) {
            if (req.getParameter("whoisit") != null && req.getParameter("passwd") != null) {
                if (logging) log("running authenticator");
                String name = req.getParameter("whoisit").trim();
                String pw = req.getParameter("passwd").trim();
                log(this_session.tryLogin(name, pw));
            }
        }

        if (this_session.isUserAuthenticated() && is_adding_pic){
            try{
                File uploadedFile = new File(getServletContext().getRealPath("/") + "pics/" + fileName);
                item.write(uploadedFile);
                log(tryAddPic(fileName, tag));

            }catch(Exception ex){
                if (logging) log(ex.getMessage());
            }
        }


        req.setAttribute("sessionID",this_session.getID());
        if(this_session.isUserAuthenticated()) {
            forwardTo.accept("Add_Pic.jsp");
        }
        else{
            forwardTo.accept("search.jsp");
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
