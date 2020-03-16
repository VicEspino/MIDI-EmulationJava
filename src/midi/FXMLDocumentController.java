/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midi;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

/**
 *
 * @author VAPESIN
 */
public class FXMLDocumentController implements Initializable,EventHandler<ActionEvent> {
    
    @FXML
    private Label label;
    @FXML
    private Button vicBtn;
    @FXML
    private JFXComboBox<String> combo_devices;
    @FXML
    private Button vicBtn1;
    @FXML
    private Button vicBtn11;
    

    MidiDevice.Info[] midiDeviceInfo;
     MidiDevice   inputPort;
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//               new Thread(()->{
//               
//               
//                   try {
//                       Thread.sleep(5000);
//                   } catch (InterruptedException ex) {
//                       Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//                   }
//                   Platform.runLater(()->{
//               
//                                      vicBtn.fire();
//               });
//               }).start();
        ObservableList lista = FXCollections.observableArrayList();
                // [obtain and open the three devices...]
        midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
        for(MidiDevice.Info infoactu:midiDeviceInfo){
            System.out.println(infoactu.getName());
            System.out.println(infoactu.getVendor());
            System.out.println(infoactu.getDescription());
            System.out.println(infoactu.getVersion()+"\n");
            lista.add((infoactu.getName() + " " + infoactu.getDescription()));
        }
        
       
        this.combo_devices.setItems(lista);
        
        this.combo_devices.setOnAction(this);
     }    
    int selectedIndex =-1;
    @Override
    public void handle(ActionEvent t) {


        selectedIndex = this.combo_devices.getSelectionModel().getSelectedIndex();
        try {
            this.connectPortMIDI();
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void connectPortMIDI() throws MidiUnavailableException{
        
        inputPort = MidiSystem.getMidiDevice( this.midiDeviceInfo[this.selectedIndex] );

        inputPort.open();
    }
    
     public static void playSong(int tempo) {
        try {
 
            //Open a MIDI sequencer
            Sequencer player = MidiSystem.getSequencer();
            //Set the sequence's beats per minute
            player.setTempoInBPM(tempo);
            //get the sequencers transmitting object to send to synth
            Transmitter sendData = player.getTransmitter();
            //create and initialize the synthesizer + optional methods
            Synthesizer instrument = MidiSystem.getSynthesizer();
            //MidiChannel[] channels = s.getChannels();
            //Instrument[] instruments = s.getAvailableInstruments();
            //load the default soundbank into the synthesizer
            instrument.loadAllInstruments(instrument.getDefaultSoundbank());
            //get the synths receiving MIDI input port
            Receiver receiveData = instrument.getReceiver();
            //tie the sequencer object and synthesizer together
            sendData.setReceiver(receiveData);
            //open the sequencer's connection and the synthesizer's connection
            player.open();
            instrument.open();
            //safe to send MIDI data post-this-point
            //create a new sequence using a PPQ timebase (1 tick / quarter note)
            //we can change the number of ticks if we want smaller sub-divisions
            Sequence composition = new Sequence(Sequence.PPQ, 1);
            //create a track to sequence notes on
            //can create more if composition is multitrack
            composition.createTrack();
            //get the tracks optional, for sequencing multiple tracks
            Track[] tracks = composition.getTracks();
            //add messages to track[0], channel 5 is default, 90 is default
            //note velocity
            //keep track of the starting time (0) and the ending time
            int timestamp = 1;
            //the MIDI root note code for octave 5 is 60.
            int rootNote = 60;
            //default velocity
            int defaultVelo = 90;
            //for every note in our song
            
                //add a new 'note-on' event on channel 5
                //translate the note name to a MIDI value
                //get the beginning timestamp (playhead)
//                tracks[0].add(new MidiEvent(
//                        new ShortMessage(ShortMessage.NOTE_ON, 0,
//                            getMIDIValue(rootNote, n), defaultVelo), timestamp));
//                //increase the timestamp by the number of song beats in a note
//                timestamp += n.getBeats();
                //add a 'note-off' event at the ending timestamp
                tracks[0].add(new MidiEvent(
                        new ShortMessage(ShortMessage.NOTE_OFF, 0,
                            53, defaultVelo), timestamp));
            
            //set up our sequencer to play the sequence we created
            player.setSequence(composition);
            //start our sequence
            player.start();
            //this loop will quit once the sequence is done playing
            System.out.println("Started playing...");
            //beat is a quarter note
            //fraction of minute the song plays is number of beats over the bpm
//            double songTimeMultiplier = ((double)getCurrentNumberOfBeats(noteList) / (double)tempo);
            //number of milliseconds in a minute is 60000
            int millisInMinute = 60000;
            //sleep for the fraction of a minute that is the multiplier
            try
            {
                //make the console sleep for a little
                Thread.sleep(100);
            }
            catch(InterruptedException e)
            {
                //we couldn't sleep (should never happen)
                System.out.println("Failed to sleep. Ending playback.");
            }
            //display done, and close out all other midi devices
            System.out.println("Finished playing...");
            //stop sequence once done playing
            player.stop();
            //free up the track memory
            composition.deleteTrack(tracks[0]);
            //close the sequencer and synthesizer objects to prevent hang
            //transmitter and receiver objects also freed.
            player.close();
            instrument.close();
            receiveData.close();
            sendData.close();
            //MIDI unavailable or invalid data are the caught exceptions
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            System.out.println("An error occurred with one "
                + "of the following tasks" + e.getMessage());
        }
    }
  
    @FXML
    private void handleButtonAction(ActionEvent event) throws MidiUnavailableException, InvalidMidiDataException {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
        

//        Synthesizer  synth;    
//        Sequencer    seq;    

//        Transmitter transmitter = inputPort.getTransmitter();
//        Receiver receiver = MidiSystem.getReceiver();
//        transmitter.setReceiver(receiver);
//        receiver.send(new ShortMessage(ShortMessage.NOTE_ON, 0, 53, 100),10);
        

        Receiver inReceiver = inputPort.getReceiver();
        inReceiver.send(new ShortMessage(ShortMessage.NOTE_ON, 0, 45, 5),-1);
        //inputPort.getTransmitter().setReceiver(inReceiver);

        
    }
    @FXML
    private void handleButtonAction1(ActionEvent event) throws InvalidMidiDataException, MidiUnavailableException {
        
        Receiver inReceiver = inputPort.getReceiver();
        inReceiver.send(new ShortMessage(ShortMessage.NOTE_ON, 0,45, 75),-1);
        
    }

    @FXML
    private void handleButtonAction11(ActionEvent event) throws InvalidMidiDataException, MidiUnavailableException {
        
          Receiver inReceiver = inputPort.getReceiver();
        inReceiver.send(new ShortMessage(ShortMessage.NOTE_ON, 0, 45, 127),-1);
        
    }

            

  
    

    
}
