package tp.pr5;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.UIManager;
//import tp.pr3.gui.Ventana;
import tp.pr5.cityLoader.CityLoaderFromTxtFile;
import tp.pr5.citySolver.AutoPlayer;
import tp.pr5.console.Console;
import tp.pr5.console.WindowedConsole;
import tp.pr5.gui.MainWindow;
import tp.pr5.lang.ErrorMessages;
import tp.pr5.sound.SoundModule;
import tp.pr5.util.ArgumentParser;

public class Main {
	/**
	 * Inicializa los lugares y calles así como los parámetros para arrancar el robots
	 * @param args
	 * @throws IOException 
	 */
	
	
	public static void main (String args[]) {
        String laf = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(laf);
        } catch (Exception ex) {
            return;
        }
        
		String mainHelp = "Execute this assignment with these parameters:\nusage: tp.pr5.Main";
		ArgumentParser argPar = new ArgumentParser(mainHelp);
		boolean swing = false, sound = false, console = false, debug = false, autoPlay = false;;
		
		//Initialize variables and configure the argument parser
		argPar.addCommand(argPar.new ArgumentParserOption(
				"h", new String[]{"help"}, false, false, false, 	"Shows this help message", null, false));
		argPar.addCommand(argPar.new ArgumentParserOption(
				"i", new String[]{"interface"}, true, true, true, 	"The type of interface: console or swing or both", "type", false));
		argPar.addCommand(argPar.new ArgumentParserOption (
				"m", new String[]{"map"}, true, true, true, 		"File with the description of the city", "mapfile", false));
		argPar.addCommand(argPar.new ArgumentParserOption (
				"s", new String[]{"sound"}, false, false, false, null, null, true));
		argPar.addCommand(argPar.new ArgumentParserOption (
				"d", new String[]{"debug"}, false, false, false, null, null, true));
		argPar.addCommand(argPar.new ArgumentParserOption (
				"a", new String[]{"autoplay"}, false, true, false, null, null, true));


		try {
			argPar.processArguments(args);
		} catch (IllegalArgumentException e) {}		
		
		//han pedido ayuda?
		if (argPar.hasCommandAppeared("h")) 
			Main.exitWithMessage(System.out, argPar.getHelp(), 0);
		//han puesto el mapa?
		if (!argPar.hasCommandAppeared("m")) 
			Main.exitWithMessage(System.err, "Map file not specified", 1);
		//han mandado la interfaz?
		if (!argPar.hasCommandAppeared("i"))
			Main.exitWithMessage(System.err, "Interface not specified", 1);
		//han pedido sonido?
		if (argPar.hasCommandAppeared("s"))
			sound = true;
		//modo debug?
		if (argPar.hasCommandAppeared("d"))
			debug = true;
		//autoplay?
		if (argPar.hasCommandAppeared("a"))
			autoPlay = true;
		
		//cargamos la interfaz
		if (argPar.hasCommandAppeared("i"))
			switch(argPar.getArg("i")) {
				case "swing":  swing = true; break;
				case "console": console = true; break;
				case "both": swing = true; console = true; break;
				case "none": break;
				default: Main.exitWithMessage(System.err, "Wrong type of interface", 3);
			}

		//cargamos el mapa
		City _places = null;		
		CityLoaderFromTxtFile loader = new CityLoaderFromTxtFile();
		String path = argPar.getArg("m");
		FileInputStream iFS;
		try {
			iFS = new FileInputStream(path);
			try {
				_places = loader.loadCity(iFS);
			} catch (IOException e) {
				Main.exitWithMessage(System.err, ErrorMessages.MAP_FILE_READING + argPar.getArg("m") + ErrorMessages.MAP_FILE_WRONG_SYNTAX, 2);
			}
		} catch (FileNotFoundException e1) {
			Main.exitWithMessage(System.err, ErrorMessages.MAP_FILE_READING + argPar.getArg("m") + ErrorMessages.MAP_FILE_NO_EXIST, 2);
		}
		
		//start the robot in the auto or normal mode
		RobotEngine robotEngine;
		if (autoPlay)
			robotEngine = 	new AutoPlayer( _places, loader.getInitialPlace(), Direction.NORTH, 
							argPar.getArg("a") != null ? Integer.parseInt(argPar.getArg("a")) : Integer.MAX_VALUE);
		else
			robotEngine = new RobotEngine( _places, loader.getInitialPlace(), Direction.NORTH);
		//start the controller pointing to the robot
		Controller controller = new Controller(robotEngine);
		//start all interface needed. They will register themselves as observers
		if (swing) 	//set location to not overlap the debug window if existant
			new MainWindow(controller, !autoPlay).setLocation(new Point(debug?300:0,0));
		if (sound)
			new SoundModule(controller);
		if (console) //si hay console y swing no acepta entrada
			new Console(controller, System.out,null, !(console && swing) && !autoPlay ? System.in : null);
		if (debug)
			new WindowedConsole(controller);

		
		
		//start the engine calling update methods on the observers and such
		robotEngine.requestStart();
	}
	
	/**
	 * Exits the application with the specified code, printing the message to the specified output, 
	 * @param output Where to print errMessage
	 * @param errMessage message to print
	 * @param errCode exit code of the application
	 */
	private static void exitWithMessage(PrintStream output, String errMessage, int errCode) {
		if (output != null && errMessage != null)
			output.println(errMessage);
		System.exit(errCode);
	}
}
