package com.wjh.demo.activity.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    
    @GetMapping("/processDefines")
    @RolesAllowed("ROLE_ACTIVITI_ADMIN")
    public List<String> getProcessDefines() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        List<String> names = new ArrayList<String>();
        list.forEach(processDefine -> {
            names.add(processDefine.getName());
        });
        return names;
    }

    @PostMapping("/startProcess/:id")
    public void createProcess(@PathVariable("id") String id) {
        runtimeService.startProcessInstanceById(id);
    }
}
