package com.software.pro.landlordsserver.api;

import org.nico.noson.Noson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/landlord_servlet")
public class Landlordsapi {

    @RequestMapping(value = "/room_servlet")
    @ResponseBody
    public String getRoom_Info(HttpServletRequest request, @RequestParam(name = "room_id") int room_id){
        return null;
    }

    @RequestMapping(value = "/roomlist_servlet")
    @ResponseBody
    public String getRoomList_Info(HttpServletRequest request){
        return null;
    }
}
