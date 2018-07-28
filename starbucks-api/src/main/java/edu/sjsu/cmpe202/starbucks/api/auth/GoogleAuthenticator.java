package edu.sjsu.cmpe202.starbucks.api.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import edu.sjsu.cmpe202.starbucks.beans.User;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/api/v1/*"})
public class GoogleAuthenticator extends OncePerRequestFilter {

    private static GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
            .setIssuer("https://accounts.google.com")
            .build();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String auth = request.getHeader("Authorization");

        if (auth == null || auth.isEmpty()) {
            writeUnauthenticatedResponse(response);
            return;
        }

        try {
            GoogleIdToken token = verifier.verify(auth);
            if (token == null) {
                writeUnauthenticatedResponse(response);
            }

            GoogleIdToken.Payload payload = token.getPayload();

            String userId = payload.getSubject();
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            User user = new User(givenName, familyName, userId);
            request.setAttribute("user", user);
            chain.doFilter(request, response);

        } catch (Exception e) {
            writeUnauthenticatedResponse(response);
        }
    }

    private void writeUnauthenticatedResponse(HttpServletResponse response) {
        try {
            response.getWriter().append("Unauthorized! Please use a valid token and try again!");
        } catch (Exception e) {
            // Do nothing
        }

        response.setStatus(403);
    }
}
