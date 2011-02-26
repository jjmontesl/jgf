
package net.jgf.example.tanks.entity;

import net.jgf.config.Configurable;
import net.jgf.entity.BaseEntity;


/**
 */
@Configurable
public class Client extends BaseEntity {

    private String name;
    
    private int score;
    
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

	

}
