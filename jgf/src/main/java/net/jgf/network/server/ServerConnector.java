package net.jgf.network.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.component.BaseComponent;
import net.jgf.messages.BaseJGFMessage;
import net.jgf.messaging.MESSAGE_EVENT_TYPE;
import net.jgf.messaging.MessageEvent;
import net.jgf.messaging.MessageEventObserver;
import net.jgf.messaging.PostOffice;
import net.jgf.network.messages.JGNMessage;
import net.jgf.translators.TranslatorMap;

import org.apache.commons.lang.StringUtils;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNClient;
import com.captiveimagination.jgn.event.DebugListener;
import com.captiveimagination.jgn.event.MessageListener;
import com.captiveimagination.jgn.message.Message;
import com.captiveimagination.jgn.test.chat.NamedChatMessage;

/**
 * Responsible for setting up and maintaining the connection to a single server.
 * If connections to different servers are needed, more instances of this class
 * need to be added to the config.
 * @author Schrijver
 * @version 1.0
 */
@Configurable
public class ServerConnector extends BaseConnector implements MessageEventObserver, MessageListener {

    private JGNClient client;

    private String    bindAddressReliable = "127.0.0.1";

    private Integer   bindPortReliable    = 10000;

    private String    bindAddressFast     = "127.0.0.1";

    private Integer   bindPortFast        = 20000;

    private Integer   timeout             = 5000;

    private Integer   maxPackets          = 60;
    
    /**
     * Constructor.
     */
    public ServerConnector() {
    }

    @Override
    public void readConfig(Config config, String configPath) {

        super.readConfig(config, configPath);

        String baReliable = config.getString(configPath + "/bindaddress[@type='reliable']");
        if (StringUtils.isNotEmpty(baReliable)) {
            bindAddressReliable = baReliable;
        }
        String bpReliable = config.getString(configPath + "/bindport[@type='reliable']");
        if (StringUtils.isNotEmpty(bpReliable)) {
            bindPortReliable = new Integer(bpReliable);
        }
        String baFast = config.getString(configPath + "/bindaddress[@type='fast']");
        if (StringUtils.isNotEmpty(baFast)) {
            bindAddressFast = baFast;
        }
        String bpFast = config.getString(configPath + "/bindport[@type='fast']");
        if (StringUtils.isNotEmpty(bpFast)) {
            bindPortFast = new Integer(bpFast);
        }
        String to = config.getString(configPath + "/timeout");
        if (StringUtils.isNotEmpty(to)) {
            timeout = new Integer(to);
        }
        String mPackets = config.getString(configPath + "/maxpackets");
        if (StringUtils.isNotEmpty(mPackets)) {
            maxPackets = new Integer(mPackets);
        }
    }

    /**
     * @return the bindAddressReliable
     */
    public final String getBindAddressReliable() {
        return bindAddressReliable;
    }

    /**
     * @param bindAddressReliable the bindAddressReliable to set
     */
    public final void setBindAddressReliable(String bindAddressReliable) {
        this.bindAddressReliable = bindAddressReliable;
    }

    /**
     * @return the bindPortReliable
     */
    public final Integer getBindPortReliable() {
        return bindPortReliable;
    }

    /**
     * @param bindPortReliable the bindPortReliable to set
     */
    public final void setBindPortReliable(Integer bindPortReliable) {
        this.bindPortReliable = bindPortReliable;
    }

    /**
     * @return the bindAddressFast
     */
    public final String getBindAddressFast() {
        return bindAddressFast;
    }

    /**
     * @param bindAddressFast the bindAddressFast to set
     */
    public final void setBindAddressFast(String bindAddressFast) {
        this.bindAddressFast = bindAddressFast;
    }

    /**
     * @return the bindPortFast
     */
    public final Integer getBindPortFast() {
        return bindPortFast;
    }

    /**
     * @param bindPortFast the bindPortFast to set
     */
    public final void setBindPortFast(Integer bindPortFast) {
        this.bindPortFast = bindPortFast;
    }

    /**
     * @return the timeout
     */
    public final Integer getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public final void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the maxPackets
     */
    public final Integer getMaxPackets() {
        return maxPackets;
    }

    /**
     * @param maxPackets the maxPackets to set
     */
    public final void setMaxPackets(Integer maxPackets) {
        this.maxPackets = maxPackets;
    }

    @Override
    public void connect() throws IOException {
        JGN.register(NamedChatMessage.class);

        client = new JGNClient();
        client.addMessageListener(this);
        client.addMessageListener(new DebugListener("ChatClient>"));
        JGN.createThread(client).start();

        InetSocketAddress reliableServerAddress = new InetSocketAddress(bindAddressReliable, bindPortReliable);
        InetSocketAddress fastServerAddress = new InetSocketAddress(bindAddressFast, bindPortFast);

        try {
            client.connectAndWait(reliableServerAddress, fastServerAddress, timeout);
        } catch (InterruptedException e) {
            throw new IOException("Unable to connect, connection timed out.", e);
        }
    }

    @Override
    public void disconnect() {
        // TODO Auto-generated method stub

    }

    @Override
    public void messageCertified(Message message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void messageFailed(Message message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void messageReceived(Message message) {
        BaseJGFMessage jgfMessage = (BaseJGFMessage) TranslatorMap.translate(message);
        PostOffice.fireMessageEvent(null);
    }

    @Override
    public void messageSent(Message message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleMessageEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessageEventKey().getMessageEventType() == MESSAGE_EVENT_TYPE.SEND) {
            sendMessage((BaseJGFMessage) messageEvent.getMessageEventPayload());
        }
    }
    
    private void sendMessage(BaseJGFMessage jgfMessage) {
        JGNMessage jgnMessage = (JGNMessage) TranslatorMap.translate(jgfMessage);
        switch (jgfMessage.getCategory()) {
            case BROADCAST: 
                client.broadcast(jgnMessage);
                break;
            case P2P: 
                client.sendToPlayer(jgnMessage, jgfMessage.getPlayerId());
                break;
            case SERVER: 
                client.sendToServer(jgnMessage);
                break;
            default: 
                break;
        }
    }
}
