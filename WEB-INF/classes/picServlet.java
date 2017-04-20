import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*; 
import java.util.*;
import java.util.function.*;


public class picServlet extends HttpServlet {
    private    List<CustomSession> the_sessions;
    private final boolean logging = true;

    public void init() throws ServletException  {
        the_sessions = new ArrayList<CustomSession>();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        CustomSession this_session = null;
        boolean is_valid_session = false;
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
        if (!this_session.isUserAuthenticated()) {

            //see if there was a user name and password passed in
            if (req.getParameter("whoisit") != null && req.getParameter("passwd") != null) {
                String name = req.getParameter("whoisit").trim();
                String pw = req.getParameter("passwd").trim();

//                Enter code to authenticate user here
//                is_authorized_session = true;
            }
        }
        //authorized user, load upload page
        if(this_session.isUserAuthenticated()) {
            forwardTo.accept("upload.jsp");
        }
        else forwardTo.accept("search.jsp");

        return;
    }//end doPost

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Consumer <String> forwardTo =(url) ->ForwardTo(url,req,res);

        //Check for user logging out
        if(req.getParameter("logout") != null && req.getParameter("sessionID") != null){
            String ID = req.getParameter("sessionID").trim();
            for (int i = 0; i < the_sessions.size(); i++) {
                if (the_sessions.get(i).getID().equals(ID)) {  //Found an active session
                    if (logging) log("removing: " + the_sessions.get(i).getID());
                    the_sessions.remove(i);
                    break;
                }
            }
            forwardTo.accept("search.jsp");
            return;
        }
    }

    boolean tooLong(String now,String then){
        //Check amount of time that passed
        return false;
    }
    boolean checkPW(String name,String password){
        //Check password against name
        return false;
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
