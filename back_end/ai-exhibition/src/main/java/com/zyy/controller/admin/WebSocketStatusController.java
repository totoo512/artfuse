package com.zyy.controller.admin;

import com.zyy.result.Result;
import com.zyy.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * WebSocket状态监控控制器
 */
@RestController
@RequestMapping("/admin/websocket")
@Api(tags = "WebSocket状态监控")
@Slf4j
public class WebSocketStatusController {

    /**
     * 获取WebSocket连接状态
     * @return 连接状态信息
     */
    @GetMapping("/status")
    @ApiOperation("获取WebSocket连接状态")
    public Result<Map<String, Object>> getWebSocketStatus() {
        log.info("获取WebSocket连接状态");
        Map<String, Object> status = WebSocketServer.getConnectionStatus();
        return Result.success(status);
    }

    /**
     * 获取当前连接数
     * @return 连接数
     */
    @GetMapping("/count")
    @ApiOperation("获取当前连接数")
    public Result<Integer> getConnectionCount() {
        log.info("获取当前WebSocket连接数");
        int count = WebSocketServer.getConnectionCount();
        return Result.success(count);
    }
}