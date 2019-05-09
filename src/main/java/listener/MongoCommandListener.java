/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;

/**
 *
 * @author thomasmalvoisin
 */
public class MongoCommandListener implements CommandListener {

    @Override
    public void commandStarted(final CommandStartedEvent event) {
//        System.out.println(String.format("Sent command '%s:%s' with id %s to database '%s' "
//                + "on connection '%s' to server '%s'",
//                event.getCommandName(),
//                event.getCommand().get(event.getCommandName()),
//                event.getRequestId(),
//                event.getDatabaseName(),
//                event.getConnectionDescription()
//                        .getConnectionId(),
//                event.getConnectionDescription().getServerAddress()));
        //System.out.println(String.format("comand sent : %s", event.getCommand().toString()));
    }

    @Override
    public void commandSucceeded(final CommandSucceededEvent event) {
//        System.out.println(String.format("Successfully executed command '%s' with id %s "
//                + "on connection '%s' to server '%s'",
//                event.getCommandName(),
//                event.getRequestId(),
//                event.getConnectionDescription()
//                        .getConnectionId(),
//                event.getConnectionDescription().getServerAddress()));
    }

    @Override
    public void commandFailed(final CommandFailedEvent event) {
//        System.out.println(String.format("Failed execution of command '%s' with id %s "
//                + "on connection '%s' to server '%s' with exception '%s'",
//                event.getCommandName(),
//                event.getRequestId(),
//                event.getConnectionDescription()
//                        .getConnectionId(),
//                event.getConnectionDescription().getServerAddress(),
//                event.getThrowable()));
    }
}
