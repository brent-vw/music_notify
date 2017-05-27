package me.brentvw.controllers;

import me.brentvw.midi.MidiPlayer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/alarm")
public class AlarmController {

    @PostMapping
    public ResponseEntity<Object> postAlarm(){
        new Thread(()->new MidiPlayer().play()).start();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Object> getAlarm(){
        new Thread(()->new MidiPlayer().play()).start();
        return ResponseEntity.ok().build();
    }
}
