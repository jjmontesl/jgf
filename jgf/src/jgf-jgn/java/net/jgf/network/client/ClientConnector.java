package net.jgf.network.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.messaging.BaseJGFMessage;
import net.jgf.messaging.MessageBroker;
import net.jgf.messaging.MessageNotifications;
import net.jgf.messaging.MessagePublisher;
import net.jgf.messaging.MessageSubscriber;
import net.jgf.network.BaseConnector;
import net.jgf.network.messages.JGNChatMessage;
import net.jgf.network.messages.JGNMessage;
import net.jgf.network.translators.TranslatorMap;
import net.jgf.system.Jgf;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNClient;
import com.captiveimagination.jgn.clientserver.JGNConnection;
import com.captiveimagination.jgn.clientserver.JGNConnectionListener;
import com.captiveimagination.jgn.event.MessageListener;
import com.captiveimagination.jgn.message.Message;

/**
 * Responsible for setting up and maintaining the connection to a single server.
 * If connections to different servers are needed, more instances of this class
 * need to be added to the config.
 * @author Schrijver
 * @version 1.0
 */
@Configurable
public class ClientConnector extends BaseConnector implements MessagePublisher, MessageSubscriber, MessageListener,
        JGNConnectionListener {

    /**
     * Class logger.
     */
    private static final Logger logger              = Logger.getLogger(ClientConnector.class);

    private JGNClient           client;

    private String              bindAddressReliable = "127.0.0.1";

    private Integer             bindPortReliable    = 10000;

    private String              bindAddressFast     = "127.0.0.1";

    private Integer             bindPortFast        = 20000;

    private Integer             timeout             = 5000;

    private Integer             maxPackets          = 60;

    private MessageBroker       messageBroker;

    /**
     * Constructor.
     */
    public ClientConnector() {
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

        if (config.containsKey(configPath + "/messagebroker/@ref")) {
            String messageBrokerRef = config.getString(configPath + "/messagebroker/@ref");
            messageBroker = Jgf.getDirectory().getObjectAs(messageBrokerRef, MessageBroker.class);
            messageBroker.registerMessagePublisher(this);
            int index = 1;
            while (config.containsKey(configPath + "/messagebroker/subscription[" + index + "]/@topic")) {
                String topic = config.getString(configPath + "/messagebroker/subscription[" + index + "]/@topic");
                messageBroker.registerMessageSubscriber(this, topic);
                index++;
            }
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
        try {
            JGN.register(JGNChatMessage.class);
            // client = new JGNClient();
            client = new JGNClient(new InetSocketAddress(InetAddress.getLocalHost(), 0), new InetSocketAddress(
                    InetAddress.getLocalHost(), 0));
            client.addMessageListener(this);
            client.addServerConnectionListener(this);
            JGN.createThread(client).start();

            InetSocketAddress reliableServerAddress = null;
            InetSocketAddress fastServerAddress = null;

            // localhost needs a different approach for some reason.
            if (bindAddressReliable.equals("127.0.0.1")) {
                reliableServerAddress = new InetSocketAddress(InetAddress.getLocalHost(), bindPortReliable);
            } else {
                reliableServerAddress = new InetSocketAddress(InetAddress.getByName(bindAddressReliable),
                        bindPortReliable);
            }
            
            // localhost needs a different approach for some reason.
            if (bindAddressReliable.equals("127.0.0.1")) {
                fastServerAddress = new InetSocketAddress(InetAddress.getLocalHost(), bindPortFast);
            } else {
                fastServerAddress = new InetSocketAddress(InetAddress.getByName(bindAddressFast), bindPortFast);
            }

            client.connectAndWait(reliableServerAddress, fastServerAddress, timeout);
            System.out.println("client is connected.");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new IOException("Unable to connect, connection timed out.", e);
        }
    }

    @Override
    public void disconnect() {
        try {
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (jgfMessage != null) {
            logger.info("message received with category " + jgfMessage.getMessageCategory());
            jgfMessage.setTopic(jgfMessage.getMessageCategory() + "_received");
            messageBroker.publishMessage(jgfMessage, this.getId());
        }
    }

    @Override
    public void messageSent(Message message) {
        // TODO Auto-generated method stub

    }

    private void sendMessage(BaseJGFMessage jgfMessage) {
        JGNMessage jgnMessage = (JGNMessage) TranslatorMap.translate(jgfMessage);
        client.sendToServer(jgnMessage);
    }

    @Override
    public void receiveNotification(BaseJGFMessage message, MessageNotifications notification) {
        if (notification == MessageNotifications.NO_SUBSCRIBERS) {
            logger.info("No subscribers defined");
        }
    }

    @Override
    public void receiveMessage(BaseJGFMessage message) {
        sendMessage(message);
    }

    @Override
    public void connected(JGNConnection connection) {
        System.out.println("logged in as Player " + connection.getPlayerId());
    }

    @Override
    public void disconnected(JGNConnection connection) {
        System.out.println("logged off");
    }
}
