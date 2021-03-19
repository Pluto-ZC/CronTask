package com.example.demo.dao;

import com.example.demo.domain.Cron;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CronDao {

    @Select("select * from cron")
    List<Cron> findAll();

    @Update("update cron set status = #{status} where id = #{id}")
    void updateStatus(int id, int status);
}
