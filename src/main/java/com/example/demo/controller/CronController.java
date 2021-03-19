package com.example.demo.controller;

import com.example.demo.domain.Cron;
import com.example.demo.service.CronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cron")
public class CronController {
    @Autowired
    private CronService cronService;

    @PostMapping(value = "/cronList")
    public void List(){
        List<Cron> all = cronService.findAll();
        for (Cron cron : all) {
            System.out.println(cron.getName());
        }

        cronService.updateStatus(1, 5);
    }

}
