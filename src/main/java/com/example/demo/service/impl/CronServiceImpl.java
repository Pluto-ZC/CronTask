package com.example.demo.service.impl;

import com.example.demo.dao.CronDao;
import com.example.demo.domain.Cron;
import com.example.demo.service.CronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CronServiceImpl implements CronService {

    @Autowired
    private CronDao cronDao;

    @Override
    public List<Cron> findAll() {
        return cronDao.findAll();
    }

    @Override
    public void updateStatus(int id, int status) {
        cronDao.updateStatus(id,status);
    }
}
