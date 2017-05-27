package me.brentvw.midi;

import org.springframework.stereotype.Component;

import javax.sound.midi.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class MidiPlayer {
    private Sequencer sequencer;
    public MidiPlayer(){
        /*try {
            listTransmitterDevices();
            getInputDevice();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }*/
    }


    private void listTransmitterDevices() throws MidiUnavailableException {
        System.out.println("out");
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
            if (device.getMaxTransmitters() != 0){
                if(device.getDeviceInfo().getName().contains("Piano")){
                   //out = device;
                    System.out.println(device.getDeviceInfo());
                }
            }
        }
    }

    // should get me my USB MIDI Interface. There are two of them but only one
    // has Transmitters so the if statement should get me the one i want
    private void getInputDevice() throws MidiUnavailableException {
        System.out.println("in");
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
            if (device.getMaxReceivers() != 0) {
                if(device.getDeviceInfo().getName().contains("Piano")) {
                    //in = device;
                    System.out.println(device.getDeviceInfo());
                }
            }
        }
    }

    public void play(){
        if(isPlaying()){
            return;
        }
        File midiFile = new File(getClass().getClassLoader().getResource("clairdelune.mid").getFile());

        // Play once
        try {
        //find the suitable device number here, based on some criteria
            //Receiver MidiOutReceiver = in.getReceiver();
            this.sequencer = MidiSystem.getSequencer();
            //Add the new MIDI out device here.
            sequencer.open();
            //sequencer.getTransmitter().setReceiver(MidiOutReceiver);
            sequencer.setSequence(new FileInputStream(midiFile));

            sequencer.start();
            while(true) {
                if(sequencer.isRunning()) {
                    try {
                        Thread.sleep(1000); // Check every second
                    } catch(InterruptedException ignore) {
                        break;
                    }
                } else {
                    break;
                }
            }
            // Close the MidiDevice & free resources
            sequencer.stop();
            sequencer.close();
        } catch(MidiUnavailableException mue) {
            mue.printStackTrace();
            System.out.println("Midi device unavailable!");
        } catch (IOException | InvalidMidiDataException e) {
            e.printStackTrace();
        }

    }

    public void stop(){
        if(sequencer !=null){
            if(sequencer.isRunning()){
                sequencer.stop();
            }
        }
    }

    private boolean isPlaying() {
        return sequencer != null && sequencer.isRunning();
    }

    public Sequencer getSequencer() {
        return sequencer;
    }

    public void setSequencer(Sequencer sequencer) {
        this.sequencer = sequencer;
    }
}
