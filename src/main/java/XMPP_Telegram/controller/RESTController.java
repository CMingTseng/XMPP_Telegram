package XMPP_Telegram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {

    @Autowired
    private XMPPController xmppController;

    @RequestMapping("/")
    public void test() {}

    @RequestMapping("/secured")
    public String secured(){
        System.out.println("Inside secured()");
        return "Hello user !!!";
    }

    @RequestMapping("/stop")
    public String stop(){
        xmppController.stop();
        return "XMPP server stoped";
    }
    @RequestMapping("/start")
    public String start(){
        xmppController.start();
        return "XMPP server started";
    }

    public void setXmppController(XMPPController xmppController) {
        this.xmppController = xmppController;
    }
}
