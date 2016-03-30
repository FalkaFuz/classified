package sk.zuzmat.classified.backend;

/**
 * Created by zuzka on 29.03.2016.
 */
public class MissionBuilder {
    private Long id;
    private String location;
    private String codeName;

    public MissionBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public MissionBuilder location(String location) {
        this.location = location;
        return this;
    }

    public MissionBuilder codeName(String codeName) {
        this.codeName = codeName;
        return this;
    }



    public Mission build() {
        Mission mission = new Mission();
        mission.setId(id);
        mission.setLocation(location);
        mission.setCodeName(codeName);
        return mission;
    }

}
