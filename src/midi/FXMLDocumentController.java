/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midi;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

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
        Synthesizer siSynthesizer = MidiSystem.getSynthesizer();
        siSynthesizer.open();
        MidiChannel[] midiChanels = siSynthesizer.getChannels();
        
        Instrument[] instruments = siSynthesizer.getDefaultSoundbank().getInstruments();
        siSynthesizer.loadInstrument(instruments[0]);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
               
        
    }    
    
}
