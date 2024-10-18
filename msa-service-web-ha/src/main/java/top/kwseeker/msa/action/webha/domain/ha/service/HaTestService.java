package top.kwseeker.msa.action.webha.domain.ha.service;

import org.springframework.stereotype.Service;

@Service
public class HaTestService {

    public String testFlow(String name) {
        return "Hello " + name;
    }

    public String testFuse(String name) {
        return "Hello " + name;
    }
}
