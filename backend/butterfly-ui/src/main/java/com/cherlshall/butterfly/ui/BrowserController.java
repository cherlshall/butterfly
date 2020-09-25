package com.cherlshall.butterfly.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by htf on 2020/9/25.
 */
@Controller
public class BrowserController {

    @GetMapping("/")
    public String indexPage() {
        return "/index.html";
    }
}
