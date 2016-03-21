/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classified;

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

}
 