package com.bht.jsengine;

import java.util.concurrent.Executors;

import javax.script.ScriptException;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author binhhuynh1
 */
@Log4j2
public class JsEngineDemo {

    public static void main(String[] args) throws ScriptException {
        log.info("JsEngineDemo Init...");
        NashornSandbox sandbox = NashornSandboxes.create("--language=es6");
        sandbox.setMaxCPUTime(100);
        sandbox.setMaxMemory(100 * 1024L);
        sandbox.allowNoBraces(false);
        sandbox.setMaxPreparedStatements(30); // because preparing scripts for execution is expensive
        sandbox.setExecutor(Executors.newSingleThreadExecutor());

        String request1 = new JsonObject()
                .put("airlineCode", "vna")
                .put("departureCode", "SGN")
                .put("arrivalCode", "HAN")
                .put("listAirlinesFilter", new JsonArray().add("vietjet").add("vna").add("bamboo"))
                .put("promoCodes", new JsonObject()
                        .put("vietjet", "TEST_VJ")
                        .put("vna", "TEST_VNA")
                        .put("bamboo", "TEST_BB"))
                .toString();

        String request2 = new JsonObject()
                .put("airlineCode", "bamboo")
                .put("departureCode", "SGN")
                .put("arrivalCode", "HAN")
                .put("listAirlinesFilter", new JsonArray().add("vietjet").add("vna").add("bamboo"))
                .put("promoCodes", new JsonObject()
                        .put("vietjet", "TEST_VJ")
                        .put("vna", "TEST_VNA")
                        .put("bamboo", "TEST_BB"))
                .toString();

        log.info("Init json request: {}", request1);
        log.info("injecting variable request1...");
        sandbox.inject("request1", request1);

        log.info("Init json request: {}", request2);
        log.info("injecting variable request2...");
        sandbox.inject("request2", request2);

        String isValidRequestFunc = "function isValidRequest(requestStr) { const request = JSON.parse(requestStr); print(request.airlineCode); return request.airlineCode === 'vna'; };";
        log.info("declaring function isValidRequest... {}", isValidRequestFunc);
        sandbox.eval(isValidRequestFunc);

        log.info("Validate request1: {}", sandbox.eval("isValidRequest(request1)"));
        log.info("Validate request2: {}", sandbox.eval("isValidRequest(request2)"));
    }
}