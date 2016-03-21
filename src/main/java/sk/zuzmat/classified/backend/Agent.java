/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.zuzmat.classified.backend;

/**
 *
 * @author xschwar2
 */
public class Agent {
    
    private Long id;
    private String name;
    private String coverName;
    private String favouriteWeapon;
    
   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getCoverName() {
        return coverName;
    }

    public void setCoverName(String coverName) {
        this.coverName = coverName;
    }

    public String getFavouriteWeapon() {
        return favouriteWeapon;
    }

    public void setFavouriteWeapon(String favouriteWeapon) {
        this.favouriteWeapon = favouriteWeapon;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coverName='" + coverName + '\'' +
                ", favouriteWeapon='" + favouriteWeapon + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agent agent = (Agent) o;

        return id != null ? id.equals(agent.id) : agent.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
 