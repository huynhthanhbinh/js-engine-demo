package com.bht.jsengine;

import java.util.concurrent.Executors;

import javax.script.ScriptException;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author binhhuynh1
 */
@Log4j2
public class JsEngineDemo {

    public static void main(String[] args) throws ScriptException {
        log.info("JsEngineDemo Init...");

        NashornSandbox sandbox = NashornSandboxes.create(args);
        sandbox.setMaxCPUTime(100);
        sandbox.setMaxMemory(50 * 1024L);
        sandbox.allowNoBraces(false);
        sandbox.setMaxPreparedStatements(30); // because preparing scripts for execution is expensive
        sandbox.setExecutor(Executors.newSingleThreadExecutor());

        log.info("injecting variable request1...");
        sandbox.inject("request1", 20);
        log.info("injecting variable request2...");
        sandbox.inject("request2", 21);

        log.info("declaring function checkRequest...");
        sandbox.eval("function checkRequest(request) { return 20 === request; }");

        log.info("checking request1... {}", sandbox.eval("checkRequest(request1);"));
        log.info("checking request2... {}", sandbox.eval("checkRequest(request2);"));
    }
}