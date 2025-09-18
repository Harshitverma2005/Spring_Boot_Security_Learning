package com.example.spring_boot_security.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class StudentController {

    @GetMapping("/hello")
    public String grett(HttpSession session)

    {
        Integer count = (Integer) session.getAttribute("count");
        if (count == null)
        {
            count =0;
        }
        count ++;
        session.setAttribute("count",count);
        return "Shree Radha "+session.getId()+" and no. of counts is "+count;
    }


}
