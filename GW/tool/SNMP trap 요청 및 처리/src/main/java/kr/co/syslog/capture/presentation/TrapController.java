package kr.co.syslog.capture.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TrapController {

    private List<String> traps = new ArrayList<>();

    @GetMapping("/traps")
    public List<String> getTraps() {
        return traps;
    }

    public void addTrap(String trap) {
        traps.add(trap);
    }
}