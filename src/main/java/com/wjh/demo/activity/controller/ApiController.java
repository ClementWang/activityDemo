package com.wjh.demo.activity.controller;

import javax.annotation.security.RolesAllowed;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.StartProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.ClaimTaskPayloadBuilder;
import org.activiti.api.task.model.builders.CompleteTaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wjh.demo.activity.common.UserUtil;

@RolesAllowed("ROLE_ACTIVITI_USER")
@RestController
@RequestMapping("/api")
public class ApiController {
    private Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private TaskRuntime taskRuntime;

    @GetMapping("/processDefines")
    public Page<ProcessDefinition> processDefines() {
        return processRuntime.processDefinitions(Pageable.of(0, 10));
    }

    @PostMapping("/startProcess/{key}")
    public void startProcess(@PathVariable("key") String key) {
        String currentUser = UserUtil.currentUserName();
        ProcessInstance instance = processRuntime.start(new StartProcessPayloadBuilder()
                .withVariable("currentLoginUser", currentUser)
                .withProcessDefinitionKey(key)
                .build());
        logger.debug(instance.toString());
    }

    @GetMapping("/tasks")
    public Page<Task> tasks() {
        return taskRuntime.tasks(Pageable.of(0, 10));
    }

    @PostMapping("/task/{id}/claim")
    public void claimTask(@PathVariable("id") String taskId) {
        Task task = taskRuntime.claim(new ClaimTaskPayloadBuilder().withTaskId(taskId).build());
        logger.debug(task.toString());
    }

    @PostMapping("/task/{id}/complete")
    public void completeTask(@PathVariable("id") String taskId) {
        Task task = taskRuntime.complete(new CompleteTaskPayloadBuilder().withTaskId(taskId).build());
        logger.debug(task.toString());
    }
}
