import java.util.Random;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/*
 * Links you may need
 * https://discordapp.com/oauth2/authorize?client_id=REPLACEME&scope=bot&permissions=8
 * https://discordapp.com/developers/applications/
 * 
 */

public class APP_Discord_Driver extends ListenerAdapter {

	public static void main(String[] args) {

		try {
			new APP_Discord_Driver();
		} catch (LoginException e) {
			System.out.println("Failed to login. Maybe your token has expired?");
		}
		
	}
	
	public APP_Discord_Driver() throws LoginException {
		
		//This launches the bot. You'll notice it's online while your program is running.
		JDA bot = new JDABuilder()
				.setToken(Settings.token)
				.addEventListener(this)
				.build();
		
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		super.onMessageReceived(event);
		
		//I don't want to reply to myself or other bots
		if (!event.getAuthor().isBot()) {
			
			MessageChannel chn = event.getChannel();
			Message msg = event.getMessage();
			User usr = event.getAuthor();
			Member m = event.getMember();
			
			boolean isCommand = msg.getContentRaw().charAt(0) == '!';
			if (isCommand) {
				String[] cmd = msg.getContentRaw().substring(1).split(" ");
				if (cmd.length > 0) {
					
					Message response = null;
					
					if (cmd[0].equals("r")) {
						
						Random r = new Random();
						if (cmd.length == 1) {
							//0 or 1
							int max = 1;
							response = new MessageBuilder()
									.setContent(String.valueOf(r.nextInt(max + 1)))
									.build();
						} else if (cmd.length == 2) {
							//0 through x
							int max = Integer.valueOf(cmd[1]);
							response = new MessageBuilder()
									.setContent(String.valueOf(r.nextInt(max + 1)))
									.build();
						}
					} else if (cmd[0].equals("source")) {
						//reply with github source
						response = new MessageBuilder()
								.setContent("https://github.com/neonchickens/APP_Discord_Bot")
								.build();
					}
					
					if (response != null) {
						chn.sendMessage(response).complete();
					}
				}
			}
		}
	}
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		super.onGuildMemberJoin(event);

		Member m = event.getMember();
		Guild g = event.getGuild();

		//Builds message response
		Message response = new MessageBuilder()
				.setContent("Welcome to the APP discord " + m.getAsMention() + "!")
				.build();
		
		//Welcomes and assigns new members
		g.getDefaultChannel().sendMessage(response).complete();
		g.getController().addRolesToMember(m, g.getRolesByName("Members", true).get(0)).complete();
	}
}
