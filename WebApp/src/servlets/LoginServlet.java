package servlets;
import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shticell.sheets.manager.MultiSheetEngineManager;
import shticell.users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import static constant.Constants.USERNAME;

@WebServlet(name = "LoginServlet",urlPatterns={"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        System.out.println("Servlet Checking if user exists: " + usernameFromSession);

        if (usernameFromSession == null) { //user is not logged in yet

            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //no username in session and no username in parameter - not standard situation. it's a conflict

                // stands for conflict in server state
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();

                /*
                One can ask why not enclose all the synchronizations inside the userManager object ?
                Well, the atomic action we need to perform here includes both the question (isUserExists) and (potentially) the insertion
                of a new user (addUser). These two actions needs to be considered atomic, and synchronizing only each one of them, solely, is not enough.
                (of course there are other more sophisticated and performable means for that (atomic objects etc) but these are not in our scope)

                The synchronized is on this instance (the servlet).
                As the servlet is singleton - it is promised that all threads will be synchronized on the very same instance (crucial here)

                A better code would be to perform only as little and as necessary things we need here inside the synchronized block and avoid
                do here other not related actions (such as response setup. this is shown here in that manner just to stress this issue
                 */
                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        //response.getOutputStream().print(errorMessage);
                        response.getWriter().println(errorMessage);


                    }
                    else {
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter);
                        //set the username in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet
                        //create a new one
                        request.getSession(true).setAttribute(USERNAME, usernameFromParameter);


                        //redirect the request to the chat room - in order to actually change the URL
                        System.out.println("On login, request URI is: " + request.getRequestURI());
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            //TODO
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

}