package com.seguridad.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    @RequestMapping(value = { "/", "/inicio", "/dashboard", "/pages/**", "/auth/**" })
    public String forward() {
        return "forward:/index.html";
    }
}
