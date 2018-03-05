package XMPP_Telegram.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {

    @RequestMapping("/")
    public void test() {}
}
