package me.brentvw.controllers;

import me.brentvw.midi.MidiPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/alarm")
public class AlarmController {

    private final MidiPlayer player;

    @Autowired
    public AlarmController(MidiPlayer player) {
        this.player = player;
    }

    @PostMapping
    public ResponseEntity<Object> postAlarm(){
        new Thread(player::stop).start();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Object> getAlarm(){
        new Thread(player::play).start();
        return ResponseEntity.ok().build();
    }
}
