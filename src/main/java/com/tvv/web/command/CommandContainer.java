package com.tvv.web.command;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class CommandContainer {
	
	private static final Logger log = Logger.getLogger(CommandContainer.class);
	
	private static Map<String, Command> commands = new TreeMap<String, Command>();
	
	static {

		commands.put("login", new LoginCommand());
		commands.put("logout", new LogoutCommand());

		// client commands

		// admin commands
		commands.put("listUsers", new ListUsersCommand());
		commands.put("listAccounts", new ListAccountsCommand());
		commands.put("listPayments", new ListPaymentsCommand());
		commands.put("listCards", new ListCardsCommand());
		commands.put("registration", new FormRegistration());

		commands.put("createUser", new CreateUserCommand());
		
		log.debug("Command container was successfully initialized");
		log.trace("Number of commands --> " + commands.size());
	}

	public static Command get(String commandName) {
		if (commandName == null || !commands.containsKey(commandName)) {
			log.trace("Command not found, name --> " + commandName);
			return commands.get("noCommand"); 
		}
		
		return commands.get(commandName);
	}
	
}