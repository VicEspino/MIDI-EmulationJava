/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midi;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

/**
 *
 * @author VAPESIN
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws MidiUnavailableException {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
//        Synthesizer siSynthesizer = MidiSystem.getSynthesizer();
//        siSynthesizer.open();
//        MidiChannel[] midiChanels = siSynthesizer.getChannels();
//        
//        Instrument[] instruments = siSynthesizer.getDefaultSoundbank().getInstruments();
//        siSynthesizer.loadInstrument(instruments[0]);
    
        Receiver rcv= MidiSystem.getReceiver();
//        Transmitter transmitter = MidiSystem.getTransmitter();
//        transmitter.setReceiver(rcv);
        
        MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
        
        for(MidiDevice.Info inforact:midiDeviceInfo){

            System.out.println(inforact.toString());
            System.out.println(inforact.getDescription()+"\n");

        }        
        
        MidiDevice midiDevice = MidiSystem.getMidiDevice(midiDeviceInfo[4]);
       // midiDevice.getReceivers().add(rcv);
    
        if(midiDevice.isOpen()){
                  
        }else{
            midiDevice.open();
            
        }
        
        
        ShortMessage msMessage = new ShortMessage();
        try {
            msMessage.setMessage(ShortMessage.NOTE_ON, 0, 20, 93);
            rcv.send(msMessage, -1);
            
            System.out.println("Channel-> "+msMessage.getChannel());

 

            
            
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
       
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
               
        
    }    
    
}
