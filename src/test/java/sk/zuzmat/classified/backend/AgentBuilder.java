package sk.zuzmat.classified.backend;

/**
 * Created by zuzka on 29.03.2016.
 */
public class AgentBuilder {
    private Long id;
    private String name;
    private String coverName;
    private String favouriteWeapon;

    public AgentBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public AgentBuilder name(String name) {
        this.name = name;
        return this;
    }

    public AgentBuilder coverName(String coverName) {
        this.coverName = coverName;
        return this;
    }

    public AgentBuilder favouriteWeapon(String favouriteWeapon) {
        this.favouriteWeapon = favouriteWeapon;
        return this;
    }


    public Agent build() {
        Agent agent = new Agent();
        agent.setId(id);
        agent.setName(name);
        agent.setCoverName(coverName);
        agent.setFavouriteWeapon(favouriteWeapon);
        return agent;
    }

}
