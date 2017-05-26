package me.brentvw.midi;

import com.sun.org.apache.regexp.internal.RE;

import javax.sound.midi.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MidiPlayer {
    private MidiDevice out;
    private MidiDevice in;

    public MidiPlayer(){
        try {
            listTransmitterDevices();
            getInputDevice();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }


    private void listTransmitterDevices() throws MidiUnavailableException {
        System.out.println("out");
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
            if (device.getMaxTransmitters() != 0){
                if(device.getDeviceInfo().getName().contains("Piano")){
                   out = device;
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
                    in = device;
                    System.out.println(device.getDeviceInfo());
                }
            }
        }
    }

    public void play(){
        File midiFile = new File(getClass().getClassLoader().getResource("clairdelune.mid").getFile());

        // Play once
        try {
        //find the suitable device number here, based on some criteria
            Receiver MidiOutReceiver = in.getReceiver();
            ShortMessage myMsg = new ShortMessage();
            // Start playing the note Middle C (60),
            // moderately loud (velocity = 93).
            myMsg.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
            long timeStamp = -1;
            MidiOutReceiver.send(myMsg, timeStamp);



            Sequencer MidiOutSequencer = MidiSystem.getSequencer();
            //Add the new MIDI out device here.
            MidiOutSequencer.open();
            Receiver a = MidiOutSequencer.getTransmitter().getReceiver();
            MidiOutSequencer.getTransmitter().setReceiver(MidiOutReceiver);
            Receiver b = MidiOutSequencer.getTransmitter().getReceiver();
            MidiOutSequencer.setSequence(new FileInputStream(midiFile));

            MidiOutSequencer.start();
            while(true) {
                if(MidiOutSequencer.isRunning()) {
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
            MidiOutSequencer.stop();
            MidiOutSequencer.close();
        } catch(MidiUnavailableException mue) {
            mue.printStackTrace();
            System.out.println("Midi device unavailable!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

    }
}
