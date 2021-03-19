package com.example.demo.service;

import com.example.demo.domain.Cron;
import com.example.demo.service.impl.CronServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CronService {
    List<Cron> findAll();
    void updateStatus(int id,int status);
}

