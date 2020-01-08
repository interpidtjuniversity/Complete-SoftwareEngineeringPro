package com.landlords.controller;


import com.landlords.entity.Friend;
import com.landlords.entity.Player;
import com.landlords.entity.Task;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class TaskController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ResponseBody
    @PostMapping(value = "/getTasklist", produces = "application/json;charset=UTF-8")
    public JSONObject getTasklist(@RequestBody Map<String, String> httpRequest){
        String playerid = httpRequest.get("id");
        String sql = "select current_progress from task where id = ?";
        RowMapper<Task> rowMapper = new BeanPropertyRowMapper<>(Task.class);
        Task task = jdbcTemplate.queryForObject(sql,rowMapper,playerid);

        JSONObject temp = new JSONObject();
        temp.put("current_progress",task.getCurrent_progress());

        return temp;
    }

    @PostMapping(value = "/resetTask", produces = "application/json;charset=UTF-8")
    public void resetTask(@RequestBody Map<String, String> httpRequest){
        String playerid = httpRequest.get("id");
        String sql = "update task set current_progress=0 where id =" + playerid;
        jdbcTemplate.update(sql);
    }

    @PostMapping(value = "/updateTask", produces = "application/json;charset=UTF-8")
    public void updateTask(@RequestBody Map<String, String> httpRequest){
        String playerid = httpRequest.get("id");
        String sql = "select current_progress from task where id = ?";
        RowMapper<Task> rowMapper = new BeanPropertyRowMapper<>(Task.class);
        Task task = jdbcTemplate.queryForObject(sql,rowMapper,playerid);
        if(task.getCurrent_progress()<10) {
            sql = "update task set current_progress=current_progress + 1 where id =" + playerid;
            jdbcTemplate.update(sql);
        }
    }
}
