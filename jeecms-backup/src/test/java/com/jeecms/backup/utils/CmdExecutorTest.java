package com.jeecms.backup.utils;

import org.junit.Test;

public class CmdExecutorTest {

    @Test
    public void executeCommand() {
        String cmd = "ping 127.0.0.1";
        CmdExecutor.executeCommand(cmd);

    }
}