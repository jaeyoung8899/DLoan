package dloan.common.handler;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class DLoanEnvServlet extends HttpServlet {

    @Autowired
    private DLoanEnvService dLoanEnvService;

    public void init() throws ServletException{
        System.out.println("-------------");
        dLoanEnvService.makeEnv();
    }
}

