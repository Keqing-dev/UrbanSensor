package dev.keqing.urbansensor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class DocsController {
    @RequestMapping("/")
    void redirectDocs(HttpServletResponse response) throws IOException {
        response.sendRedirect("/docs.html");
    }
}
