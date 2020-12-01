package course.spring.web.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import course.spring.exceptions.AlreadyExistingEntityException;
import course.spring.exceptions.ForbiddenAccessEntityException;
import course.spring.exceptions.InvalidEntityDataException;
import course.spring.exceptions.NonExistingEntityException;
import course.spring.models.responses.ErrorResponse;
import course.spring.web.ErrorHandlerControllerAdvice;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class FilterChainExceptionHandlerFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private ErrorHandlerControllerAdvice controllerAdvice;

    @Autowired
    public FilterChainExceptionHandlerFilter(ErrorHandlerControllerAdvice controllerAdvice) {
        this.controllerAdvice = controllerAdvice;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException | AuthenticationException e) {
            log.error("Spring Security Filter Chain Exception:", e);
            ResponseEntity<ErrorResponse> responseEntity = controllerAdvice.handleAuthenticationException(e);
            getException(response, responseEntity);
        } catch (InvalidEntityDataException e) {
            ResponseEntity<ErrorResponse> responseEntity = controllerAdvice.handleInvalidEntityData(e);
            getException(response, responseEntity);
        } catch (NonExistingEntityException e) {
            ResponseEntity<ErrorResponse> responseEntity = controllerAdvice.handleNonExistingEntity(e);
            getException(response, responseEntity);
        } catch (AlreadyExistingEntityException e) {
            ResponseEntity<ErrorResponse> responseEntity = controllerAdvice.handleAlreadyExistingEntity(e);
            getException(response, responseEntity);
        } catch (ForbiddenAccessEntityException e) {
            ResponseEntity<ErrorResponse> responseEntity = controllerAdvice.handleForbiddenAccessEntity(e);
            getException(response, responseEntity);
        }
    }

    private void getException(HttpServletResponse response, ResponseEntity<ErrorResponse> responseEntity) throws IOException {
        response.setStatus(responseEntity.getStatusCodeValue());
        PrintWriter out = response.getWriter();
        new ObjectMapper().writeValue(out, responseEntity.getBody());
    }
}
