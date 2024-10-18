package top.kwseeker.msa.action.webha.trigger.http;

import lombok.extern.slf4j.Slf4j;
import top.kwseeker.msa.action.framework.common.model.Response;

import static top.kwseeker.msa.action.framework.common.exception.GlobalErrorCodes.REQUEST_LIMITED_ERROR;

@Slf4j
public class HelloBlockedController {

    public Response<String> testFlow1(String name) {
        log.info("testFlow1: {} been blocked", name);
        return Response.fail(REQUEST_LIMITED_ERROR);
    }
}
