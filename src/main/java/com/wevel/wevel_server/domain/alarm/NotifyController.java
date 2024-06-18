package com.wevel.wevel_server.domain.alarm;

import com.wevel.wevel_server.domain.alarm.entity.Notify;
import com.wevel.wevel_server.domain.alarm.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifies")
public class NotifyController {

    private final NotifyService notifyService;

    @Autowired
    public NotifyController(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    @GetMapping("/{userId}")
    public List<Notify> getNotificationsByUserId(@PathVariable Long userId) {
        return notifyService.getNotificationsByUserId(userId);
    }
}