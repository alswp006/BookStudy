import javax.sound.midi.*;

public class MiniMiniMusicApp {
    public static void main(String[] args) {
        MiniMiniMusicApp mini = new MiniMiniMusicApp();
        int instrument = Integer.parseInt("40");
        int note = Integer.parseInt("70");
        mini.play(instrument, note);
//        if (args.length < 2){
//            System.out.println("악기와 음 높이를 지정하는 인자를 입력하세요");
//        }else{
//            int instrument = Integer.parseInt(args[0]);
//            int note = Integer.parseInt(args[1]);
//            mini.play(instrument, note);
//        }
    }
    public void play(int instrument, int note){
        try{
            Sequencer player = MidiSystem.getSequencer();
            player.open();
            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();

            MidiEvent event = null;

            ShortMessage first = new ShortMessage();
            first.setMessage(192,1,instrument,0);
            MidiEvent changeInstrument = new MidiEvent(first,1);
            track.add(changeInstrument);

            ShortMessage a = new ShortMessage();
            a.setMessage(144,1,note, 100);
            MidiEvent noteOn = new MidiEvent(a,1);
            track.add(noteOn);

            ShortMessage b = new ShortMessage();
            a.setMessage(128,1,note,100);
            MidiEvent noteOff = new MidiEvent(b, 3);
            track.add(noteOff);

            player.setSequence(seq);

            player.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
