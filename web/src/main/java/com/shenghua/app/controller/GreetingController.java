package com.shenghua.app.controller;


import com.sheng.hua.cache.api.CacheClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {
    @Autowired
    private CacheClient<String,String> cacheClient;

    @GetMapping("/getCatList")
    public String getCatList(@RequestParam(name="key", required=false, defaultValue="key") String key, Model model){
        String value = cacheClient.get(key);
        model.addAttribute("k", key);
        model.addAttribute("v", value);
        return "getCat";
    }

    @GetMapping("/addCat")
    public String addCat(@RequestParam(name="key", required=false, defaultValue="key") String key,
                         @RequestParam(name="value", required=false, defaultValue="value") String value,
                         Model model){
        boolean isSuc = cacheClient.put(key,value);
        model.addAttribute("k", key);
        model.addAttribute("v", value);
        model.addAttribute("isSuc", isSuc);
        return "addCat";
    }

}