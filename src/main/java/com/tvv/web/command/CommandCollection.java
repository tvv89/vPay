package com.tvv.web.command;

import java.util.Map;
import java.util.TreeMap;

import com.tvv.web.command.create.CreateAccountCommand;
import com.tvv.web.command.create.CreateCardCommand;
import com.tvv.web.command.create.CreatePaymentCommand;
import com.tvv.web.command.create.CreateUserCommand;
import com.tvv.web.command.info.InfoAccountCommand;
import com.tvv.web.command.info.InfoCardCommand;
import com.tvv.web.command.info.InfoPaymentCommand;
import com.tvv.web.command.info.InfoUserCommand;
import com.tvv.web.command.load.LoadListAccountsCommand;
import com.tvv.web.command.load.LoadListCardsCommand;
import com.tvv.web.command.load.LoadListPaymentsCommand;
import com.tvv.web.command.load.LoadListUsersCommand;
import com.tvv.web.command.status.StatusAccountsCommand;
import com.tvv.web.command.status.StatusCardsCommand;
import com.tvv.web.command.status.StatusPaymentsCommand;
import com.tvv.web.command.status.StatusUsersCommand;
import com.tvv.web.command.update.*;
import org.apache.log4j.Logger;

/**
 * Command collector. Collect all command for POST and GET request from user
 */
public class CommandCollection {
	
	private static final Logger log = Logger.getLogger(CommandCollection.class);
	
	private static Map<String, Command> commands = new TreeMap<String, Command>();
	
	static {
		/**
		 * Main authentication commands
		 */
		commands.put("login", new LoginCommand());
		commands.put("logout", new LogoutCommand());
		commands.put("registration", new RegistrationCommand());
		commands.put("createUser", new CreateUserCommand());
		/**
		 * Command for word with Users
		 */
		commands.put("listUsers", new LoadListUsersCommand());
		commands.put("updateListUser", new UpdateListUsersCommand());
		commands.put("infoUser", new InfoUserCommand());
		commands.put("statusUser", new StatusUsersCommand());
		commands.put("roleUser", new UpdateUserRoleCommand());
		/**
		 * Command for word with Accounts
		 */
		commands.put("listAccounts", new LoadListAccountsCommand());
		commands.put("updateListAccount", new UpdateListAccountsCommand());
		commands.put("infoAccount", new InfoAccountCommand());
		commands.put("statusAccount", new StatusAccountsCommand());
		commands.put("addCoin", new UpdateAccountBalanceCommand());
		commands.put("createAccount", new CreateAccountCommand());
		commands.put("infoCard", new InfoCardCommand());
		/**
		 * Command for word with Payments
		 */
		commands.put("listPayments", new LoadListPaymentsCommand());
		commands.put("updateListPayment", new UpdateListPaymentsCommand());
		commands.put("infoPayment", new InfoPaymentCommand());
		commands.put("statusPayment", new StatusPaymentsCommand());
		commands.put("createPayment", new CreatePaymentCommand());
		/**
		 * Command for word with Cards
		 */
		commands.put("listCards", new LoadListCardsCommand());
		commands.put("updateListCard", new UpdateListCardsCommand());
		commands.put("statusCard", new StatusCardsCommand());
		commands.put("createCard", new CreateCardCommand());

		commands.put("language", new LanguageCommand());
		log.debug("Command container was initialized");
		log.trace("Number of commands: " + commands.size());
	}

	/**
	 * Get command by name
	 * @param commandName String command name
	 * @return object Command
	 */
	public static Command get(String commandName) {
		if (commandName == null || !commands.containsKey(commandName)) {
			log.trace("Command not found: " + commandName);
			return commands.get("noCommand"); 
		}
		
		return commands.get(commandName);
	}
	
}