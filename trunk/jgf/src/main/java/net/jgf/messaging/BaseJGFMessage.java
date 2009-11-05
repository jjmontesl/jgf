package net.jgf.messaging;


/**
 * Base JGF message class. All Messages in the JGF are derived from this one.
 * @author Schrijver
 * @version 1.0
 */
public abstract class BaseJGFMessage {
    
    private long id;
    
    private String topic;
    
    /**
     * @return the id
     */
    public final long getId() {
        return id;
    }

    
    /**
     * @param id the id to set
     */
    public final void setId(long id) {
        this.id = id;
    }


    /**
     * @param topic the topic to set
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }


    /**
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }
    
}
