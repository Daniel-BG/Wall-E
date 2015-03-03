package tp.pr5.sound;

import java.util.List;

import tp.pr5.sound.Sound;
import tp.pr5.Controller;
import tp.pr5.Direction;
import tp.pr5.PlaceInfo;
import tp.pr5.items.Item;
import tp.pr5.observers.ItemContainerObserver;
import tp.pr5.observers.NavigationModuleObserver;
import tp.pr5.observers.RobotObserver;

public class SoundModule implements ItemContainerObserver, NavigationModuleObserver, RobotObserver{

	/**
	 * Constructor for soundModule. <br>
	 * This class will register itself as an observer
	 * @param c Controller used to register this class as Engine, Item and Navigation observer
	 */
	public SoundModule (Controller c) {
		Sound.gameMusicIntro.setVolumeGain(-6);
		Sound.gameMusicLoop.setVolumeGain(-6);
		c.addEngineObserver(this);
		c.addItemContainerObserver(this);
		c.addNavigationModuleObserver(this);
	}
	
	//ROBOTOBSERVER
	@Override
	public void communicationCompleted() {}
	@Override
	public void communicationHelp(String help) {}
	@Override
	public void engineOff(boolean atSpaceShip) {
		if (!atSpaceShip)
			Sound.playerDeath.play();
	}
	@Override
	public void raiseError(String message) {
		Sound.playerHurt.play();
	}
	@Override
	public void robotSays(String message) {}
	@Override
	public void robotUpdate(int newFuel, int newPoints) {}
	
	//NAVMODOBSERVER
	@Override
	public void headingChanged(Direction newDirection) {
		Sound.monsterHurt.play();
	}
	@Override
	public void initNavigationModule(PlaceInfo place, Direction heading) {
		//vamos a reproducir el de inicio una vez y el principal indefinidamente
		Sound[] ss = {Sound.gameMusicIntro, Sound.gameMusicLoop};
		//lo indicamos aquí
		int[] st = {Sound.LOOP_ONCE,Sound.LOOP_CONTINUOUSLY};
		//y a bailar!
		Sound.playSounds(ss, st);
	}
	@Override
	public void placeHasChanged(PlaceInfo place) {}
	@Override
	public void placeScanned(PlaceInfo place) {}
	@Override
	public void robotArrivesAtPlace(Direction heading, PlaceInfo place) {
		if (place.isSpaceship())
			Sound.foundspaceship.play();
		else
			Sound.craft.play();
	}
	@Override
	public void robotCrashes(Direction heading, int status) {
		Sound.playerHurt.play();
	}
	@Override
	public void doorToggled(Direction dir, boolean isOpen) {}
	
	
	//INVOBSERVER
	@Override
	public void inventoryChange(List<Item> inventory) {}
	@Override
	public void inventoryScanned(String inventoryDescription) {}
	@Override
	public void itemEmpty(String itemName) {}
	@Override
	public void itemScanned(String description) {}
	@Override
	public void itemAdded(String itemName) {
		Sound.pickup.play();
	}
	@Override
	public void itemRemoved(String itemName) {
		Sound.monsterHurt.play();
	}
}
