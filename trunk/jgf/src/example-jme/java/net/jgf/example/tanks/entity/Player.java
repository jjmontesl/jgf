
package net.jgf.example.tanks.entity;

import com.jme3.network.connection.Client;

import net.jgf.config.Configurable;
import net.jgf.entity.BaseEntity;


/**
 */
@Configurable
public class Player extends BaseEntity {

    private String name;
    
    private int score;
    
    private Client client;
    
    private Tank tank;
    
    
	@Override
	public void doUpdate(float tpf) {
	    
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Tank getTank() {
        return tank;
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }
    
}
