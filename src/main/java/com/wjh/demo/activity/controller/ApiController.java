package com.wjh.demo.activity.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RolesAllowed("ROLE_ACTIVITI_USER")
@RestController
public class ApiController {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @GetMapping("/processDefines")
    public String getProcessDefines() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        JSONArray proDefines = new JSONArray();
        list.forEach(item -> {
            JSONObject proDefine = new JSONObject();
            proDefine.put("id", item.getId());
            proDefine.put("name", item.getName());
            proDefine.put("key", item.getKey());
            proDefine.put("description", item.getDescription());
            proDefine.put("version", item.getVersion());
            proDefines.put(proDefine);
        });
        return proDefines.toString();
    }

    @GetMapping("/startProcess/{key}")
    public void createProcess(@PathVariable("key") String key) {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(key);
        System.out.println(instance);
    }

    @GetMapping("/tasks/unAssignee")
    public String getTasksUnAssignee() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        List<String> groups = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());
        System.out.println(groups);
        List<Task> tasks = taskService.createTaskQuery().taskInvolvedGroupsIn(groups).list();
        JSONArray taskArray = new JSONArray();
        tasks.forEach(item -> {
            JSONObject taskJson = new JSONObject();
            taskJson.put("id", item.getId());
            taskJson.put("name", item.getName());
            taskJson.put("description", item.getDescription());
            taskJson.put("owner", item.getOwner());
            taskJson.put("assignee", item.getAssignee());
            taskJson.put("ClaimTime", item.getClaimTime());
            taskArray.put(taskJson);
        });
        return taskArray.toString();
    }

    @GetMapping("/task/{id}/claim")
    public void claimTask(@PathVariable("id") String taskId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        taskService.claim(taskId, userDetails.getUsername());
    }

    @GetMapping("/tasks")
    public String getTasks() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(userName).list();
        JSONArray taskArray = new JSONArray();
        tasks.forEach(item -> {
            JSONObject taskJson = new JSONObject();
            taskJson.put("id", item.getId());
            taskJson.put("name", item.getName());
            taskJson.put("description", item.getDescription());
            taskJson.put("owner", item.getOwner());
            taskJson.put("assignee", item.getAssignee());
            taskJson.put("ClaimTime", item.getClaimTime());
            taskArray.put(taskJson);
        });
        return taskArray.toString();
    }

    @GetMapping("/task/{id}/complete")
    public void completeTask(@PathVariable("id") String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.addComment(taskId, task.getProcessInstanceId(), "批准");
        taskService.complete(taskId);
    }
}
