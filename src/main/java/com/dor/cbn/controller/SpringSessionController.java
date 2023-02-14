package com.dor.cbn.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class SpringSessionController {

	@GetMapping("/")
    public @ResponseBody ResponseEntity<List> getSession(Model model, HttpSession session) {
        List sessionList = (List) session.getAttribute("SESSION_LIST");
        if(sessionList == null) {
        	sessionList = new ArrayList<>();
        }

        return new ResponseEntity<List>(sessionList,HttpStatus.OK);
    }

    @PostMapping("/generate/session")
    public @ResponseBody ResponseEntity<List> saveSession(@RequestBody String username, HttpServletRequest request)
    {
        List sessionList = (List) request.getSession().getAttribute("SESSION_LIST");
        if(sessionList == null) {
        	sessionList = new ArrayList<>();
            request.getSession().setAttribute("SESSION_LIST", sessionList);
        }
        sessionList.add(username);
        return new ResponseEntity<List>(sessionList,HttpStatus.OK);
    }
    
    @PostMapping("/destroy/session")
	public @ResponseBody ResponseEntity destroySession(HttpServletRequest request) {
		HttpSession session= request.getSession(false);
		String s1= session.getId();
		session.setMaxInactiveInterval(0);
    	//request.getSession(false);
		return new ResponseEntity(null,HttpStatus.OK);
	}
}

