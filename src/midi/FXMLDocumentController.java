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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
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
    private Button vicBtn;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws MidiUnavailableException, InvalidMidiDataException {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
        

        Synthesizer  synth;
    
        Sequencer    seq;
    
        MidiDevice   inputPort;
        MidiDevice   inputPort2;
        // [obtain and open the three devices...]
        MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
        
        for(MidiDevice.Info infoactu:midiDeviceInfo){
            System.out.println(infoactu.getName());
            System.out.println(infoactu.getVendor());
            System.out.println(infoactu.getDescription());
            System.out.println(infoactu.getVersion()+"\n");
        }
        
        inputPort = MidiSystem.getMidiDevice(midiDeviceInfo[1]);
        inputPort = MidiSystem.getMidiDevice(midiDeviceInfo[6]);
   
        inputPort.open();
        
        synth = MidiSystem.getSynthesizer();
        
        //synth = (Synthesizer) MidiSystem.getMidiDevice(midiDeviceInfo[3]);
        //seq = MidiSystem.getSequencer();
        seq = (Sequencer) MidiSystem.getMidiDevice(midiDeviceInfo[7]);
        Transmitter   inPortTrans1;
        Transmitter   inPortTrans2;
        Receiver      synthRcvr;
        Receiver      seqRcvr;
        synth.open();
        inPortTrans1 = inputPort.getTransmitter();
        //synthRcvr = synth.getReceiver();
        synthRcvr = MidiSystem.getReceiver();
        
        synthRcvr.send(new ShortMessage(ShortMessage.NOTE_ON, 0, 53, 100), -1);
        inPortTrans1.setReceiver(synthRcvr);
        
        inPortTrans2 = inputPort.getTransmitter();
        seqRcvr = seq.getReceiver(); 
        inPortTrans2.setReceiver(seqRcvr);
  
    
    
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
               new Thread(()->{
               
               
                   try {
                       Thread.sleep(5000);
                   } catch (InterruptedException ex) {
                       Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                   }
                   Platform.runLater(()->{
               
                                      vicBtn.fire();
               });
               }).start();
        
    }    
    
}
