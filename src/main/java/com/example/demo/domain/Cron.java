package com.example.demo.domain;

import lombok.Data;

@Data
public class Cron {

    private Integer id;
    private String name;
    private String cron;
    private String task;
    private Integer status;
}
