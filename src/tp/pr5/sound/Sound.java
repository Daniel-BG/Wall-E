package tp.pr5.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

/**
 * 
 * @original_author Markus Persson
 *
 */
public class Sound {
	public static final int LOOP_CONTINUOUSLY = 0xffffffff; 
	public static final int LOOP_ONCE = 0x00000000;
	public static final int LOOP_TWICE = 0x00000001;
	
	public static final Sound playerHurt = new Sound("/sound/playerhurt.wav");
	public static final Sound playerDeath = new Sound("/sound/death.wav");
	public static final Sound monsterHurt = new Sound("/sound/monsterhurt.wav");
	public static final Sound test = new Sound("/sound/test.wav");
	public static final Sound foundspaceship = new Sound("/sound/foundspaceship.wav");
	public static final Sound pickup = new Sound("/sound/pickup.wav");
	public static final Sound bossdeath = new Sound("/sound/bossdeath.wav");
	public static final Sound craft = new Sound("/sound/craft.wav");
	public static final Sound useditem = new Sound("/sound/useditem.wav");
	public static final Sound gameOver = new Sound("/sound/gameover.wav");
	public static final Sound gameMusic = new Sound("/sound/gamemusicloop.wav");
	public static final Sound gameMusicIntro = new Sound("/sound/gamemusicintro.wav");
	public static final Sound gameMusicLoop = new Sound("/sound/gamemusicloop.wav");

	//rellenar esto a medida que se añadan sonidos. Es para pararlos con mute al instante en vez de esperar a que acaben
	private static final Sound[] soundArray = {playerHurt,playerDeath,monsterHurt,test,foundspaceship,pickup,
			bossdeath,craft,useditem,gameOver,gameMusic,gameMusicIntro,gameMusicLoop};
	
	private Clip clip;
	private static boolean mute = false;
	private boolean playable = true;
	private AudioListener listener;
	private boolean stopOnNext = false;
	
	/**
	 * Constructor for Sound, specifying the file where it is located
	 * @param name file where the sound is located
	 */
	private Sound(String name) {
		try {
			AudioInputStream AIS;
			AIS = AudioSystem.getAudioInputStream(Sound.class.getResource(name));
			clip = AudioSystem.getClip();
			clip.open(AIS);
			listener = new AudioListener();
			clip.addLineListener(listener);
		} catch (Throwable e) {
			playable = false; //ha fallado la apertura y no se podrá reproducir (para validador y cosas)
		}
	}
	/**
	 * Mutes all sounds coming from this class.
	 */
	public static void mute() {
		Sound.mute = true;
		for (int i = 0; i < Sound.soundArray.length; i++) 
			Sound.soundArray[i].stop();
	}
	/**
	 * Allows for sound to be played again
	 */
	public static void unMute() {
		Sound.mute = false;
	}
	/**
	 * Sets the gain for all the sounds. Note that previous gains will not be overwritten, but added instead
	 * @param vol
	 */
	public static void setMasterVolumeGain(float vol) {
		for (int i = 0; i < Sound.soundArray.length; i++) 
			Sound.soundArray[i].setVolumeGain(vol);
	}
	/**
	 * Sets the gain for this specific sound. Note that previous gains will not be overwritten, but added instead
	 * @param vol
	 */
	public void setVolumeGain(float vol) {
		if (!playable)
			return;
		FloatControl volume = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
		volume.setValue(vol);
	}

	/**
	 * Plays the sounds in the array in the order they are arranged, the number of times loopTimes provides. <br>
	 * Note that due to the Clip class implementation, the number of times a sound is played will be that specified by loopTimes[i] PLUS 1 <br>
	 * Use the Sound class constants to avoid confusion
	 * @param sounds
	 * @param loopTimes
	 */
	public static void playSounds(final Sound[] sounds, final int[] loopTimes) {
		if (sounds == null || loopTimes == null || sounds.length != loopTimes.length || Sound.mute)
			return;
		
		try {
			new Thread() { //aunque los sonidos individuales se inician en un nuevo hilo, esto crea una espera que hay que mandar a otro hilo para no taponar el principal
				public void run() {
					for (int i = 0; i < sounds.length; i++) {
						if (!sounds[i].playable)
							continue;
						sounds[i].playClip(loopTimes[i]);
						try {
							sounds[i].listener.waitUntilDone();
						} catch (InterruptedException e) {
							
						}
					}	
				}
			}.start();
		} catch (Throwable e) {}
	}
	/**
	 * Plays the sound once
	 */
	public void play() {
		if (mute || !playable)
			return;
		playClip(Sound.LOOP_ONCE);
	}
	/**
	 * Plays the sound indefinetely until the thread is forced to stop or the .stop() method is called
	 */
	public void loop() {
		if (mute || !playable)
			return;
		playClip(Sound.LOOP_CONTINUOUSLY);
	}
	/**
	 * Play the clip the specified number of times
	 * @param times
	 */
	private void playClip(final int times) {
		if (Sound.mute || !playable)
			return;
		if (this.stopOnNext) {
			this.stopOnNext = false;
			return;
		}
		clip.setFramePosition(0);
		clip.loop(times);
	}
	/**
	 * Stop the current playing of the sound
	 */
	public void stop() {
		if (playable)
			clip.stop();
	}
	/**
	 * Stops the playback or sets a flag for stopping the next playback
	 * @param waitForNext
	 */
	public void stop(boolean waitForNext) {
		if (!waitForNext || !playable) {
			this.stop();
			return;
		}
		if (!clip.isActive())
			this.stopOnNext = true;
		else
			clip.stop();
	}
	
	
	/**
	 * Internal class used to listen to audio ending events
	 */
	private class AudioListener implements LineListener {
		private boolean done = false;
		@Override public synchronized void update(LineEvent event) {
			Type eventType = event.getType();
			if (eventType == Type.STOP || eventType == Type.CLOSE) {
				done = true;
				notifyAll();
			}
		}
		/**
		 * Waits until the song has ended
		 * @throws InterruptedException
		 */
		public synchronized void waitUntilDone() throws InterruptedException {
			while (!done) {wait(); }
		}
	}
	
}